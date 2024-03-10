package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess;

import com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.IntervalDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.TaskPostProcessDataDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.*;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess.utils.*;
import com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class TaskPreProcessImp implements TaskPreProcess {

    private final DateTimeMapper dateTimeMapper;
    private final IntervalValueAverageService averageService;
    // TODO add as task parameter not fixed value
    private static final int intervalLengthMinutes = 15;

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
        TaskPostProcessDataDto taskPostProcessDataDto = generateTaskPostProcessData(taskDto, taskCalculationDto, intervals);

        return new PreProcessResult(taskDto, taskPostProcessDataDto);
    }

    private TaskPostProcessDataDto generateTaskPostProcessData(TaskDto taskDto, TaskCalculationDto taskCalculationDto, List<Interval> intervals) {

        log.debug("Creating data for post-processing of task with id: {}", taskCalculationDto.getId());
        TaskPostProcessDataDto.Builder builder = TaskPostProcessDataDto.newBuilder()
                .setId(taskCalculationDto.getId())
                .setDateTimeStart(taskCalculationDto.getDateTimeStart())
                .setDateTimeEnd(taskCalculationDto.getDateTimeEnd());

        builder.setIntervals(
                intervals.stream()
                        .map(interval -> new IntervalDto(
                                dateTimeMapper.mapDateTimeToCharSequence(interval.dateTimeStart()),
                                dateTimeMapper.mapDateTimeToCharSequence(interval.dateTimeEnd())
                        ))
                        .toList()
        );

        builder.setMovableDemands(
                taskDto.getMovableDemands().stream()
                        .map(this::mapMovableDemandToMovableDemandTaskPostProcessDto)
                        .toList()
        );

        return builder.build();
    }

    private MovableDemandPostProcessDto mapMovableDemandToMovableDemandTaskPostProcessDto(MovableDemandDto movableDemandDto) {

        return new MovableDemandPostProcessDto(
                movableDemandDto.getId(),
                movableDemandDto.getName(),
                movableDemandDto.getProfile(),
                movableDemandDto.getProfile().stream()
                        .map(value -> value * (intervalLengthMinutes / 60.0))
                        .toList()
        );
    }

    private TaskDto mapToTaskDto(TaskCalculationDto taskCalculationDto, List<Interval> intervals) {

        log.debug("Building solver input data from task with id: {}", taskCalculationDto.getId());
        TaskDto.Builder taskDtoBuilder = TaskDto.newBuilder()
                .setId(taskCalculationDto.getId())
                .setRelativeGap(1e-10)
                .setTimeoutSeconds(100L);

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

        taskDtoBuilder.setStorages(
                taskCalculationDto.getStorages().stream()
                        .map(taskStorageDto -> mapTaskStorageDtoToStorageDto(taskStorageDto, intervals))
                        .toList()
        );

        taskDtoBuilder.setDemand(
                mapTaskDemandDtoListToDemandDto(taskCalculationDto.getDemands(), intervals)
        );

        taskDtoBuilder.setProduction(
                mapTaskProductionDtoListToProductionDto(taskCalculationDto.getProductions(), intervals)
        );

        taskDtoBuilder.setMovableDemands(
                taskCalculationDto.getMovableDemands().stream()
                        .map(taskMovableDemandDto -> mapTaskMovableDemandDtoToMovableDemandDto(taskMovableDemandDto, intervals))
                        .toList()
        );

        return taskDtoBuilder.build();
    }

    private List<Interval> buildIntervalList(LocalDateTime taskStart, LocalDateTime taskEnd) {

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
                        .map(interval -> calculateUnitPriceForInterval(interval, contractTariff))
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

    private StorageDto mapTaskStorageDtoToStorageDto(TaskStorageDto taskStorageDto, List<Interval> intervals) {

        StorageDto.Builder storageBuilder = StorageDto.newBuilder();

        storageBuilder
                .setId(taskStorageDto.getId())
                .setName(taskStorageDto.getName())
                .setMaxCapacity(taskStorageDto.getCapacity())
                .setMaxCharge(taskStorageDto.getMaxCharge())
                .setMaxDischarge(taskStorageDto.getMaxDischarge())
                .setInitialEnergy(taskStorageDto.getInitialEnergy());

        storageBuilder
                .setMinChargeConstraints(mapMinStorageConstraints(intervals, taskStorageDto.getMinChargeConstraints()))
                .setMaxChargeConstraints(mapMaxStorageConstraints(intervals, taskStorageDto.getMaxChargeConstraints()))
                .setMinDischargeConstraints(mapMinStorageConstraints(intervals, taskStorageDto.getMinDischargeConstraints()))
                .setMaxDischargeConstraints(mapMaxStorageConstraints(intervals, taskStorageDto.getMaxDischargeConstraints()))
                .setMinEnergyConstraints(mapMinStorageConstraints(intervals, taskStorageDto.getMinEnergyConstraints()))
                .setMaxEnergyConstraints(mapMaxStorageConstraints(intervals, taskStorageDto.getMaxEnergyConstraints()));

        return storageBuilder.build();
    }

    private Map<CharSequence, Double> mapMinStorageConstraints(List<Interval> intervals, List<TaskStorageConstraintDto> constraintDtoList) {

        Map<CharSequence, Double> constraintMap = new HashMap<>();
        List<IntervalValue> constraints = mapStorageConstraintsToIntervalValue(constraintDtoList);

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

    private Map<CharSequence, Double> mapMaxStorageConstraints(List<Interval> intervals, List<TaskStorageConstraintDto> constraintDtoList) {

        Map<CharSequence, Double> constraintMap = new HashMap<>();
        List<IntervalValue> constraints = mapStorageConstraintsToIntervalValue(constraintDtoList);

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

    private List<IntervalValue> mapStorageConstraintsToIntervalValue(List<TaskStorageConstraintDto> constraints) {

        return constraints.stream()
                .map(constraint -> new IntervalValue(
                        dateTimeMapper.mapToLocalDateTime(constraint.getDateTimeStart()),
                        dateTimeMapper.mapToLocalDateTime(constraint.getDateTimeEnd()),
                        constraint.getConstraintValue()
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

    private MovableDemandDto mapTaskMovableDemandDtoToMovableDemandDto(TaskMovableDemandDto taskMovableDemandDto, List<Interval> intervals) {

        return new MovableDemandDto(
                taskMovableDemandDto.getId(),
                taskMovableDemandDto.getName(),
                mapMovableDemandValuesToProfile(taskMovableDemandDto.getMovableDemandValues()),
                mapMovableDemandStartsToIntervals(taskMovableDemandDto.getMovableDemandStarts(), intervals)
        );
    }

    private List<Integer> mapMovableDemandStartsToIntervals(List<TaskMovableDemandStartDto> movableDemandStarts, List<Interval> intervals) {

        Set<Integer> uniqueStartIntervals = new HashSet<>();

        for (TaskMovableDemandStartDto movableDemandStart: movableDemandStarts) {
            for (Interval interval : intervals) {
                if (isBetweenLeftClosedRange(
                        dateTimeMapper.mapToLocalDateTime(movableDemandStart.getStart()),
                        interval.dateTimeStart(),
                        interval.dateTimeEnd()
                )) {
                    uniqueStartIntervals.add(interval.index());
                    break;
                }
            }
        }

        return uniqueStartIntervals.stream()
                .sorted(Integer::compareTo)
                .toList();
    }

    private List<Double> mapMovableDemandValuesToProfile(List<TaskMovableDemandValueDto> movableDemandValues) {

        long profileLengthInMinutes = movableDemandValues.stream()
                .map(TaskMovableDemandValueDto::getDurationMinutes)
                .reduce(0L, Long::sum);

        int numberOfIntervals = (int) Math.ceil((double) profileLengthInMinutes / intervalLengthMinutes);
        List<Double> result = new ArrayList<>(numberOfIntervals);

        int valueIndex = 0;
        long currentValueDurationUsed = 0;

        for (int i=0; i<numberOfIntervals; i++) {
            long intervalTimeLeft = intervalLengthMinutes;
            boolean intervalFilled = false;

            List<Double> intervalValues = new ArrayList<>();
            List<Long> intervalValueDuration = new ArrayList<>();

            if (movableDemandValues.get(valueIndex).getDurationMinutes() - currentValueDurationUsed > intervalLengthMinutes) {
                intervalValues.add(movableDemandValues.get(valueIndex).getValue());
                intervalValueDuration.add((long) intervalLengthMinutes);
                currentValueDurationUsed += intervalLengthMinutes;
                intervalFilled = true;
            }

            while (!intervalFilled) {

                long currentValueDuration = movableDemandValues.get(valueIndex).getDurationMinutes();
                intervalValues.add(movableDemandValues.get(valueIndex).getValue());

                if (currentValueDuration - currentValueDurationUsed > intervalTimeLeft) {
                    intervalValueDuration.add(intervalTimeLeft);
                    currentValueDurationUsed += intervalTimeLeft;
                    intervalFilled = true;
                } else {
                    intervalValueDuration.add(currentValueDuration - currentValueDurationUsed);
                    intervalTimeLeft -= currentValueDuration - currentValueDurationUsed;
                    currentValueDurationUsed = 0;
                    valueIndex += 1;
                }

                if (valueIndex >= movableDemandValues.size()) {
                    intervalValues.add(0.0);
                    intervalValueDuration.add(intervalTimeLeft);
                    intervalFilled = true;
                }
            }

            result.add(weightedAverage(intervalValues, intervalValueDuration));
        }

        return result;
    }

    private double weightedAverage(List<Double> values, List<Long> weights) {

        double weightedSum = 0;
        double sumOfWeights = 0;

        for (int i=0; i<values.size(); i++) {
            weightedSum += values.get(i) * weights.get(i);
            sumOfWeights += weights.get(i);
        }

        return weightedSum / sumOfWeights;
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

    private double calculateUnitPriceForInterval(Interval interval, TaskTariffDto contractTariff) {

        List<IntervalValue> unitPriceValues = new ArrayList<>();

        if (interval.dateTimeStart().toLocalDate().isEqual(interval.dateTimeEnd().toLocalDate())) {
            unitPriceValues.addAll(findActiveDailyValuesForInterval(interval, contractTariff));
        }
        else {
            long numberOfDaysInInterval = interval.dateTimeStart().until(interval.dateTimeEnd(), ChronoUnit.DAYS);
            Interval firstDay = new Interval(
                    interval.index(),
                    interval.dateTimeStart(),
                    interval.dateTimeStart().toLocalDate().plusDays(1).atStartOfDay()
            );
            unitPriceValues.addAll(findActiveDailyValuesForInterval(firstDay, contractTariff));

            for (long i=1; i<numberOfDaysInInterval; i++) {
                Interval middleInterval = new Interval(
                        interval.index(),
                        interval.dateTimeStart().toLocalDate().plusDays(i).atStartOfDay(),
                        interval.dateTimeStart().toLocalDate().plusDays(i+1).atStartOfDay()
                );
                unitPriceValues.addAll(findActiveDailyValuesForInterval(middleInterval, contractTariff));
            }

            Interval secondDay = new Interval(
                    interval.index(),
                    interval.dateTimeStart().toLocalDate().plusDays(1).atStartOfDay(),
                    interval.dateTimeEnd()
            );
            unitPriceValues.addAll(findActiveDailyValuesForInterval(secondDay, contractTariff));
        }

        return averageService.weighted(unitPriceValues);
    }

    private List<IntervalValue> findActiveDailyValuesForInterval(Interval interval, TaskTariffDto contractTariff) {

        Optional<CyclicalDailyValueDto> cyclicalDailyValuesInInterval = contractTariff.getCyclicalDailyValues().stream()
                .filter(dailyValueDto -> matchingIntervalDayOfWeek(interval.dateTimeStart().getDayOfWeek(), dailyValueDto.getDayOfTheWeek()))
                .findFirst();
        if (cyclicalDailyValuesInInterval.isPresent()) {
            return getTariffPriceSubValuesForInterval(interval, cyclicalDailyValuesInInterval.get());
        }

        Optional<CyclicalDailyValueDto> cyclicalDailyValuesInRange = contractTariff.getCyclicalDailyValues().stream()
                .filter(dailyValueDto -> matchingIntervalWithRange(interval.dateTimeStart().getDayOfWeek(), dailyValueDto.getDayOfTheWeek()))
                .findFirst();
        if (cyclicalDailyValuesInRange.isPresent()) {
            return getTariffPriceSubValuesForInterval(interval, cyclicalDailyValuesInRange.get());
        }

        Optional<CyclicalDailyValueDto> cyclicalDailyValuesInAll = contractTariff.getCyclicalDailyValues().stream()
                .filter(dailyValueDto -> dailyValueDto.getDayOfTheWeek() == WeekdaysDto.ALL)
                .findFirst();
        if (cyclicalDailyValuesInAll.isPresent()) {
            return getTariffPriceSubValuesForInterval(interval, cyclicalDailyValuesInAll.get());
        }

        List<IntervalValue> intervalValues = new ArrayList<>();
        intervalValues.add(new IntervalValue(interval, contractTariff.getDefaultPrice()));
        return intervalValues;
    }

    private List<IntervalValue> getTariffPriceSubValuesForInterval(Interval interval, CyclicalDailyValueDto cyclicalDailyValuesInInterval) {

        List<IntervalValue> subValues = new ArrayList<>();
        List<DailyTimeValueDto> dailyTimeValues = cyclicalDailyValuesInInterval.getDailyTimeValues();

        double startValue = dailyTimeValues.stream()
                .filter(value -> isBeforeOrEqual(dateTimeMapper.mapToLocalTime(value.getStartTime()), interval.dateTimeStart().toLocalTime()))
                .max((v1, v2) -> dateTimeMapper.mapToLocalTime(v1.getStartTime()).compareTo(dateTimeMapper.mapToLocalTime(v2.getStartTime())))
                .map(DailyTimeValueDto::getCurrentValue)
                .orElse(dailyTimeValues.get(dailyTimeValues.size() - 1).getCurrentValue());

        List<DailyTimeValueDto> intervalBreaks = dailyTimeValues.stream()
                .filter(dailyTimeValueDto -> dateTimeMapper.mapToLocalTime(dailyTimeValueDto.getStartTime()).isAfter(interval.dateTimeStart().toLocalTime()) &&
                        dateTimeMapper.mapToLocalTime(dailyTimeValueDto.getStartTime()).isBefore(interval.dateTimeEnd().toLocalTime()))
                .toList();

        if (intervalBreaks.isEmpty()) {
            subValues.add(new IntervalValue(interval, startValue));
        } else {

            LocalDate currentDate = interval.dateTimeStart().toLocalDate();
            LocalDateTime firstEndDateTime = LocalDateTime.of(
                    currentDate,
                    dateTimeMapper.mapToLocalTime(intervalBreaks.get(0).getStartTime())
            );
            subValues.add(new IntervalValue(interval.dateTimeStart(), firstEndDateTime, startValue));

            for (int i=1; i<intervalBreaks.size()-1; i++) {

                LocalDateTime startDateTime = LocalDateTime.of(
                        currentDate,
                        dateTimeMapper.mapToLocalTime(intervalBreaks.get(i).getStartTime())
                );
                LocalDateTime endDateTime = LocalDateTime.of(
                        currentDate,
                        dateTimeMapper.mapToLocalTime(intervalBreaks.get(i+1).getStartTime())
                );
                subValues.add(new IntervalValue(startDateTime, endDateTime, intervalBreaks.get(i).getCurrentValue()));
            }

            LocalDateTime lastStartDateTime = LocalDateTime.of(
                    currentDate,
                    dateTimeMapper.mapToLocalTime(intervalBreaks.get(intervalBreaks.size()-1).getStartTime())
            );
            subValues.add(new IntervalValue(lastStartDateTime, interval.dateTimeEnd(), intervalBreaks.get(intervalBreaks.size()-1).getCurrentValue()));
        }

        return subValues;
    }

    private boolean matchingIntervalDayOfWeek(DayOfWeek dayOfWeek, WeekdaysDto weekdaysDto) {

        return dayOfWeek.getValue() == weekdaysDto.ordinal()+1;
    }

    private boolean matchingIntervalWithRange(DayOfWeek dayOfWeek, WeekdaysDto weekdaysDto) {

        if (dayOfWeek.getValue() <= 5 && weekdaysDto == WeekdaysDto.MONDAY_TO_FRIDAY) {
            return true;
        } else return dayOfWeek.getValue() > 5 && weekdaysDto == WeekdaysDto.WEEKEND;
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

    private boolean isBeforeOrEqual(LocalTime dt1, LocalTime dt2) {

        return dt1.isBefore(dt2) || dt1.equals(dt2);
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
