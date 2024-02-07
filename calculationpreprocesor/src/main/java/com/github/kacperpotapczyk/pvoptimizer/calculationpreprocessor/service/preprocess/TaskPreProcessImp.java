package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess;

import com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.IntervalDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.TaskPostProcessDataDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.*;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess.utils.*;
import com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class TaskPreProcessImp implements TaskPreProcess {

    private final DateTimeMapper dateTimeMapper;
    private final IntervalValueAverageService averageService;

    @Autowired
    public TaskPreProcessImp(DateTimeMapper dateTimeMapper, IntervalValueAverageService averageService) {
        this.dateTimeMapper = dateTimeMapper;
        this.averageService = averageService;
    }

    @Override
    public PreProcessResult preProcess(TaskCalculationDto taskCalculationDto) {

        log.debug("Pre-processing task with id: {}", taskCalculationDto.getId());
        List<Interval> intervals = buildIntervalList(
                dateTimeMapper.mapToLocalDateTime(taskCalculationDto.getDateTimeStart()),
                dateTimeMapper.mapToLocalDateTime(taskCalculationDto.getDateTimeEnd())
        );

        TaskDto taskDto = mapToTaskDto(taskCalculationDto, intervals);
        TaskPostProcessDataDto taskPostProcessDataDto = generateTaskPostProcessData(taskCalculationDto, intervals);

        return new PreProcessResult(taskDto, taskPostProcessDataDto);
    }

    private TaskPostProcessDataDto generateTaskPostProcessData(TaskCalculationDto taskCalculationDto, List<Interval> intervals) {

        log.debug("Creating data for post-processing of task with id: {}", taskCalculationDto.getId());
        TaskPostProcessDataDto.Builder builder = TaskPostProcessDataDto.newBuilder();

        builder.setId(taskCalculationDto.getId());
        builder.setDateTimeStart(taskCalculationDto.getDateTimeStart());
        builder.setDateTimeEnd(taskCalculationDto.getDateTimeEnd());

        builder.setIntervals(
                intervals.stream()
                        .map(interval -> new IntervalDto(
                                dateTimeMapper.mapToCharSequence(interval.dateTimeStart()),
                                dateTimeMapper.mapToCharSequence(interval.dateTimeEnd())
                        ))
                        .toList()
        );

        return builder.build();
    }

    private TaskDto mapToTaskDto(TaskCalculationDto taskCalculationDto, List<Interval> intervals) {

        log.debug("Building solver input data from task with id: {}", taskCalculationDto.getId());
        TaskDto.Builder taskDtoBuilder = TaskDto.newBuilder();

        taskDtoBuilder.setId(taskCalculationDto.getId());
        taskDtoBuilder.setRelativeGap(1e-10);
        taskDtoBuilder.setTimeoutSeconds(100L);

        taskDtoBuilder.setIntervals(
                intervals.stream()
                        .map(interval -> interval.length().toMinutes() / 60.0)
                        .toList()
        );

        taskDtoBuilder.setContracts(
                taskCalculationDto.getContracts().stream()
                        .map(taskContractDto -> mapTaskContractDtoToContractDto(taskContractDto, intervals, taskCalculationDto.getTariffs()))
                        .toList()
        );

        taskDtoBuilder.setDemand(
                mapTaskDemandDtoListToDemandDto(taskCalculationDto.getDemands(), intervals)
        );

        taskDtoBuilder.setProduction(
                mapTaskProductionDtoListToProductionDto(taskCalculationDto.getProductions(), intervals)
        );

        return taskDtoBuilder.build();
    }

    private List<Interval> buildIntervalList(LocalDateTime taskStart, LocalDateTime taskEnd) {

        int intervalLengthMinutes = 15;
        List<Interval> intervals = new ArrayList<>();

        int index = 0;
        LocalDateTime currentDateTimeStart = taskStart;
        LocalDateTime currentDateTimeEnd = taskStart.plusMinutes(intervalLengthMinutes);

        while (currentDateTimeEnd.isBefore(taskEnd)) {
            intervals.add(new Interval(
                    index,
                    currentDateTimeStart,
                    currentDateTimeEnd,
                    Duration.of(intervalLengthMinutes, ChronoUnit.MINUTES)
            ));
            index++;
            currentDateTimeStart = currentDateTimeEnd;
            currentDateTimeEnd = currentDateTimeEnd.plusMinutes(intervalLengthMinutes);
        }

        if (currentDateTimeStart.isBefore(taskEnd)) {
            intervals.add(new Interval(
                    index,
                    currentDateTimeStart,
                    taskEnd,
                    Duration.between(currentDateTimeStart, taskEnd)
            ));
        }

        return intervals;
    }

    private ContractDto mapTaskContractDtoToContractDto(TaskContractDto taskContractDto, List<Interval> intervals, List<TaskTariffDto> tariffs) {

        TaskTariffDto contractTariff = tariffs.stream()
                .filter(tariff -> tariff.getName().equals(taskContractDto.getTariffName()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Contract with id: " + taskContractDto.getId() + " does not have relevant tariff"));

        ContractDto.Builder contractBuilder = ContractDto.newBuilder();

        contractBuilder.setId(taskContractDto.getId());
        contractBuilder.setName(taskContractDto.getName());
        contractBuilder.setStartInterval(0);

        switch (taskContractDto.getContractType()) {
            case SELL -> contractBuilder.setContractDirection(ContractDirectionDto.SELL);
            case PURCHASE -> contractBuilder.setContractDirection(ContractDirectionDto.PURCHASE);
            default -> throw new IllegalArgumentException("Invalid contract type");
        }

        contractBuilder.setMinPower(mapMinPowerIntervalsConstraints(intervals, taskContractDto.getMinPowerConstraints()));
        contractBuilder.setMaxPower(mapMaxPowerIntervalsConstraints(intervals, taskContractDto.getMaxPowerConstraints()));

        contractBuilder.setMinEnergy(mapEnergyConstraints(intervals, taskContractDto.getMinEnergyConstraints()));
        contractBuilder.setMaxEnergy(mapEnergyConstraints(intervals, taskContractDto.getMaxEnergyConstraints()));

        contractBuilder.setUnitPrice(
                intervals.stream()
                        .map(dummy -> contractTariff.getDefaultPrice())
                        .toList()
        );

        return contractBuilder.build();
    }

    private Map<CharSequence, Double> mapMinPowerIntervalsConstraints(List<Interval> intervals, List<TaskContractConstraintDto> minPowerConstraints) {

        Map<CharSequence, Double> constraintMap = new HashMap<>();
        List<IntervalValue> constraints = mapConstraintsToIntervalValue(minPowerConstraints);

        for (Interval interval : intervals) {

            List<IntervalValue> intervalConstraints = findActiveIntervalValuesAtInterval(constraints, interval);

            if (!intervalConstraints.isEmpty()) {

                List<Interval> subIntervals = splitIntervalOnIntervalValues(interval, intervalConstraints);
                List<OptionalIntervalValue> subIntervalsValue = findMaxValuesOnSubIntervals(intervalConstraints, subIntervals);
                constraintMap.put(Long.toString(interval.index()), averageService.mvpContinue(subIntervalsValue));
            }
        }
        return constraintMap;
    }

    private Map<CharSequence, Double> mapMaxPowerIntervalsConstraints(List<Interval> intervals, List<TaskContractConstraintDto> maxPowerConstraints) {

        Map<CharSequence, Double> constraintMap = new HashMap<>();
        List<IntervalValue> constraints = mapConstraintsToIntervalValue(maxPowerConstraints);

        for (Interval interval : intervals) {

            List<IntervalValue> intervalConstraints = findActiveIntervalValuesAtInterval(constraints, interval);

            if (!intervalConstraints.isEmpty()) {

                List<Interval> subIntervals = splitIntervalOnIntervalValues(interval, intervalConstraints);
                List<OptionalIntervalValue> subIntervalsValue = findMinValuesOnSubIntervals(intervalConstraints, subIntervals);

                Optional<Double> average = averageService.mvpBreak(subIntervalsValue);
                average.ifPresent(value -> constraintMap.put(Long.toString(interval.index()), value));
            }
        }
        return constraintMap;
    }

    private List<SumConstraintDto> mapEnergyConstraints(List<Interval> intervals, List<TaskContractConstraintDto> energyConstraints) {

        return energyConstraints.stream()
                .map(constraint -> {
                    List<Interval> activeIntervals = findIntervalsInConstraintRange(intervals, constraint);
                    int firstInterval = activeIntervals.stream().min(Comparator.comparingInt(Interval::index)).map(Interval::index).orElseThrow();
                    int lastInterval = activeIntervals.stream().max(Comparator.comparingInt(Interval::index)).map(Interval::index).orElseThrow();
                    return new SumConstraintDto(
                            firstInterval,
                            lastInterval,
                            constraint.getConstraintValue()
                    );
                })
                .toList();
    }

    private List<Interval> findIntervalsInConstraintRange(List<Interval> intervals, TaskContractConstraintDto constraint) {

        return intervals.stream()
                .filter(interval -> dateTimeRangeInInterval(
                        interval,
                        dateTimeMapper.mapToLocalDateTime(constraint.getDateTimeStart()),
                        dateTimeMapper.mapToLocalDateTime(constraint.getDateTimeEnd())))
                .toList();
    }

    private List<IntervalValue> mapConstraintsToIntervalValue(List<TaskContractConstraintDto> constraints) {

        return constraints.stream()
                .map(constraint -> new IntervalValue(
                        dateTimeMapper.mapToLocalDateTime(constraint.getDateTimeStart()),
                        dateTimeMapper.mapToLocalDateTime(constraint.getDateTimeEnd()),
                        constraint.getConstraintValue()
                ))
                .toList();
    }

    private List<IntervalValue> findActiveIntervalValuesAtInterval(List<IntervalValue> intervalValues, Interval interval) {

        return intervalValues.stream()
                .filter(intervalValue -> dateTimeRangeInInterval(
                        interval,
                        intervalValue.dateTimeStart(),
                        intervalValue.dateTimeEnd()
                ))
                .toList();
    }

    private DemandDto mapTaskDemandDtoListToDemandDto(List<TaskDemandDto> demands, List<Interval> intervals) {

        List<Double> demandProfile = new ArrayList<>(intervals.size());

        for (Interval interval : intervals) {

            List<IntervalValue> demandIntervalValues = buildDemandIntervalValuesAtInterval(demands, interval);
            List<Interval> subIntervals = splitIntervalOnIntervalValues(interval, demandIntervalValues);
            List<IntervalValue> subIntervalsValue = findSumOnSubIntervals(subIntervals, demandIntervalValues);

            demandProfile.add(averageService.weighted(subIntervalsValue));
        }

        return new DemandDto(
                1L,
                "Demand",
                demandProfile
        );
    }

    private List<IntervalValue> buildDemandIntervalValuesAtInterval(List<TaskDemandDto> demands, Interval interval) {

        List<IntervalValue> demandIntervalValues = new ArrayList<>();
        for (TaskDemandDto demand : demands) {

            List<TaskDemandValueDto> demandValues = demand.getDemandValues();

            List<TaskDemandValueDto> demandValuesActiveInInterval = new ArrayList<>(
                    demandValues.stream()
                            .filter(demandValue -> isBetweenLeftClosedRange(
                                    dateTimeMapper.mapToLocalDateTime(demandValue.getDateTime()),
                                    interval.dateTimeStart(),
                                    interval.dateTimeEnd()))
                            .toList()
            );

            boolean startDateTimeCovered = demandValuesActiveInInterval.stream()
                    .anyMatch(demandValue -> dateTimeMapper.mapToLocalDateTime(demandValue.getDateTime()).isEqual(interval.dateTimeStart()));

            if (!startDateTimeCovered) {
                demandValuesActiveInInterval.add(demandValues.stream()
                        .filter(demandValue -> dateTimeMapper.mapToLocalDateTime(demandValue.getDateTime()).isBefore(interval.dateTimeStart()))
                        .max((dv1, dv2) -> dateTimeMapper.mapToLocalDateTime(dv1.getDateTime()).compareTo(dateTimeMapper.mapToLocalDateTime(dv2.getDateTime())))
                        .orElse(new TaskDemandValueDto(0.0, interval.dateTimeStart().format(DateTimeFormatter.ISO_DATE_TIME)))
                );
            }

            List<TaskDemandValueDto> demandValuesActiveInIntervalSorted = demandValuesActiveInInterval.stream()
                    .sorted((dv1, dv2) -> dateTimeMapper.mapToLocalDateTime(dv1.getDateTime()).compareTo(dateTimeMapper.mapToLocalDateTime(dv2.getDateTime())))
                    .toList();

            for (int i=0; i<demandValuesActiveInIntervalSorted.size()-1; i++) {
                demandIntervalValues.add(new IntervalValue(
                        i,
                        dateTimeMapper.mapToLocalDateTime(demandValuesActiveInIntervalSorted.get(i).getDateTime()),
                        dateTimeMapper.mapToLocalDateTime(demandValuesActiveInIntervalSorted.get(i+1).getDateTime()),
                        Duration.between(
                                dateTimeMapper.mapToLocalDateTime(demandValuesActiveInIntervalSorted.get(i).getDateTime()),
                                dateTimeMapper.mapToLocalDateTime(demandValuesActiveInIntervalSorted.get(i+1).getDateTime())
                        ),
                        demandValuesActiveInIntervalSorted.get(i).getValue()
                ));
            }

            int lastIndex = demandValuesActiveInIntervalSorted.size()-1;
            demandIntervalValues.add(new IntervalValue(
                    lastIndex,
                    dateTimeMapper.mapToLocalDateTime(demandValuesActiveInIntervalSorted.get(lastIndex).getDateTime()),
                    interval.dateTimeEnd(),
                    Duration.between(
                            dateTimeMapper.mapToLocalDateTime(demandValuesActiveInIntervalSorted.get(lastIndex).getDateTime()),
                            interval.dateTimeEnd()
                    ),
                    demandValuesActiveInIntervalSorted.get(lastIndex).getValue()
            ));
        }
        return demandIntervalValues;
    }

    private ProductionDto mapTaskProductionDtoListToProductionDto(List<TaskProductionDto> productions, List<Interval> intervals) {

        List<Double> productionProfile = new ArrayList<>(intervals.size());

        for (Interval interval : intervals) {

            List<IntervalValue> productionIntervalValues = buildProductionIntervalValuesAtInterval(productions, interval);
            List<Interval> subIntervals = splitIntervalOnIntervalValues(interval, productionIntervalValues);
            List<IntervalValue> subIntervalsValue = findSumOnSubIntervals(subIntervals, productionIntervalValues);

            productionProfile.add(averageService.weighted(subIntervalsValue));
        }

        return new ProductionDto(
                1L,
                "Production",
                productionProfile
        );
    }

    private List<IntervalValue> buildProductionIntervalValuesAtInterval(List<TaskProductionDto> productions, Interval interval) {

        List<IntervalValue> productionIntervalValues = new ArrayList<>();
        for (TaskProductionDto production : productions) {

            List<TaskProductionValueDto> demandValues = production.getProductionsValues();

            List<TaskProductionValueDto> productionValuesActiveInInterval = new ArrayList<>(
                    demandValues.stream()
                            .filter(demandValue -> isBetweenLeftClosedRange(
                                    dateTimeMapper.mapToLocalDateTime(demandValue.getDateTime()),
                                    interval.dateTimeStart(),
                                    interval.dateTimeEnd()))
                            .toList()
            );

            boolean startDateTimeCovered = productionValuesActiveInInterval.stream()
                    .anyMatch(productionValue -> dateTimeMapper.mapToLocalDateTime(productionValue.getDateTime()).isEqual(interval.dateTimeStart()));

            if (!startDateTimeCovered) {
                productionValuesActiveInInterval.add(demandValues.stream()
                        .filter(productionValue -> dateTimeMapper.mapToLocalDateTime(productionValue.getDateTime()).isBefore(interval.dateTimeStart()))
                        .max((pv1, pv2) -> dateTimeMapper.mapToLocalDateTime(pv1.getDateTime()).compareTo(dateTimeMapper.mapToLocalDateTime(pv2.getDateTime())))
                        .orElse(new TaskProductionValueDto(0.0, interval.dateTimeStart().format(DateTimeFormatter.ISO_DATE_TIME)))
                );
            }

            List<TaskProductionValueDto> productionValuesActiveInIntervalSorted = productionValuesActiveInInterval.stream()
                    .sorted((pv1, pv2) -> dateTimeMapper.mapToLocalDateTime(pv1.getDateTime()).compareTo(dateTimeMapper.mapToLocalDateTime(pv2.getDateTime())))
                    .toList();

            for (int i=0; i<productionValuesActiveInIntervalSorted.size()-1; i++) {
                productionIntervalValues.add(new IntervalValue(
                        i,
                        dateTimeMapper.mapToLocalDateTime(productionValuesActiveInIntervalSorted.get(i).getDateTime()),
                        dateTimeMapper.mapToLocalDateTime(productionValuesActiveInIntervalSorted.get(i+1).getDateTime()),
                        Duration.between(
                                dateTimeMapper.mapToLocalDateTime(productionValuesActiveInIntervalSorted.get(i).getDateTime()),
                                dateTimeMapper.mapToLocalDateTime(productionValuesActiveInIntervalSorted.get(i+1).getDateTime())
                        ),
                        productionValuesActiveInIntervalSorted.get(i).getValue()
                ));
            }

            int lastIndex = productionValuesActiveInIntervalSorted.size()-1;
            productionIntervalValues.add(new IntervalValue(
                    lastIndex,
                    dateTimeMapper.mapToLocalDateTime(productionValuesActiveInIntervalSorted.get(lastIndex).getDateTime()),
                    interval.dateTimeEnd(),
                    Duration.between(
                            dateTimeMapper.mapToLocalDateTime(productionValuesActiveInIntervalSorted.get(lastIndex).getDateTime()),
                            interval.dateTimeEnd()
                    ),
                    productionValuesActiveInIntervalSorted.get(lastIndex).getValue()
            ));
        }
        return productionIntervalValues;
    }

    private List<Interval> splitIntervalOnIntervalValues(Interval interval, List<IntervalValue> demandIntervalValues) {

        Set<LocalDateTime> subIntervalsDivisionDateTime = new HashSet<>();
        subIntervalsDivisionDateTime.add(interval.dateTimeStart());
        subIntervalsDivisionDateTime.add(interval.dateTimeEnd());

        for (IntervalValue intervalValue : demandIntervalValues) {

            if (isBetweenLeftClosedRange(intervalValue.dateTimeStart(), interval.dateTimeStart(), interval.dateTimeEnd())) {
                subIntervalsDivisionDateTime.add(intervalValue.dateTimeStart());
            }
            if (isBetweenLeftClosedRange(intervalValue.dateTimeEnd(), interval.dateTimeStart(), interval.dateTimeEnd())) {
                subIntervalsDivisionDateTime.add(intervalValue.dateTimeEnd());
            }
        }

        List<LocalDateTime> orderedSubIntervalsDivisionDateTime = subIntervalsDivisionDateTime.stream()
                .sorted()
                .toList();

        List<Interval> subIntervals = new ArrayList<>();
        for (int j=0; j<orderedSubIntervalsDivisionDateTime.size() - 1; j++) {
            subIntervals.add(new Interval(
                    j,
                    orderedSubIntervalsDivisionDateTime.get(j),
                    orderedSubIntervalsDivisionDateTime.get(j+1)
            ));
        }
        return subIntervals;
    }

    private List<IntervalValue> findSumOnSubIntervals(List<Interval> subIntervals, List<IntervalValue> demandIntervalValues) {
        List<IntervalValue> subIntervalsValue = new ArrayList<>(subIntervals.size());
        for (Interval subInterval : subIntervals) {

            double demandSum = demandIntervalValues.stream()
                    .filter(demandIntervalValue -> isIntervalFullyCovered(
                            subInterval,
                            demandIntervalValue.dateTimeStart(),
                            demandIntervalValue.dateTimeEnd()
                    ))
                    .map(IntervalValue::value)
                    .reduce(0.0, Double::sum);

            subIntervalsValue.add(new IntervalValue(subInterval, demandSum));
        }
        return subIntervalsValue;
    }

    private List<OptionalIntervalValue> findMaxValuesOnSubIntervals(List<IntervalValue> intervalValues, List<Interval> subIntervals) {

        List<OptionalIntervalValue> subIntervalsValue = new ArrayList<>(subIntervals.size());
        for (Interval subInterval : subIntervals) {

            Optional<Double> maxValue = intervalValues.stream()
                    .filter(intervalValue -> dateTimeRangeInInterval(
                            subInterval,
                            intervalValue.dateTimeStart(),
                            intervalValue.dateTimeEnd()))
                    .map(IntervalValue::value)
                    .max(Comparator.comparing(Double::valueOf));

            subIntervalsValue.add(new OptionalIntervalValue(subInterval, maxValue));
        }
        return subIntervalsValue;
    }

    private List<OptionalIntervalValue> findMinValuesOnSubIntervals(List<IntervalValue> intervalValues, List<Interval> subIntervals) {

        List<OptionalIntervalValue> subIntervalsValue = new ArrayList<>(subIntervals.size());
        for (Interval subInterval : subIntervals) {

            Optional<Double> minValue = intervalValues.stream()
                    .filter(intervalValue -> dateTimeRangeInInterval(
                            subInterval,
                            intervalValue.dateTimeStart(),
                            intervalValue.dateTimeEnd()))
                    .map(IntervalValue::value)
                    .min(Comparator.comparing(Double::valueOf));

            subIntervalsValue.add(new OptionalIntervalValue(subInterval, minValue));
        }
        return subIntervalsValue;
    }

    private boolean dateTimeRangeInInterval(Interval interval, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd) {

        if (isIntervalFullyCovered(interval, dateTimeStart, dateTimeEnd)) {
            return true;
        }
        return isBetweenLeftClosedRange(dateTimeStart, interval.dateTimeStart(), interval.dateTimeEnd())
                || isBetweenRightClosedRange(dateTimeEnd, interval.dateTimeStart(), interval.dateTimeEnd());
    }

    private boolean isIntervalFullyCovered(Interval interval, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd) {

        return isBeforeOrEqual(dateTimeStart, interval.dateTimeStart()) && isAfterOrEqual(dateTimeEnd, interval.dateTimeEnd());
    }

    private boolean isBeforeOrEqual(LocalDateTime dt1, LocalDateTime dt2) {

        return dt1.isBefore(dt2) || dt1.isEqual(dt2);
    }

    private boolean isAfterOrEqual(LocalDateTime dt1, LocalDateTime dt2) {

        return dt1.isAfter(dt2) || dt1.isEqual(dt2);
    }

    private boolean isBetweenLeftClosedRange(LocalDateTime dateTime, LocalDateTime windowStart, LocalDateTime windowEnd) {

        return (windowStart.isBefore(dateTime) && dateTime.isBefore(windowEnd)) || dateTime.isEqual(windowStart);
    }

    private boolean isBetweenRightClosedRange(LocalDateTime dateTime, LocalDateTime windowStart, LocalDateTime windowEnd) {

        return (windowStart.isBefore(dateTime) && dateTime.isBefore(windowEnd)) || dateTime.isEqual(windowEnd);
    }
}
