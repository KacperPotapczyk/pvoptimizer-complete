package com.github.kacperpotapczyk.pvoptimizer.resultpostprocessor.service;

import com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.*;
import com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.TaskPostProcessDataDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.*;
import com.github.kacperpotapczyk.pvoptimizer.resultpostprocessor.mapper.DateTimeMapper;
import com.github.kacperpotapczyk.pvoptimizer.resultpostprocessor.util.Interval;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ResultPostProcessorImpl implements ResultPostProcessor {

    private final DateTimeMapper dateTimeMapper;

    @Autowired
    public ResultPostProcessorImpl(DateTimeMapper dateTimeMapper) {
        this.dateTimeMapper = dateTimeMapper;
    }

    @Override
    public TaskCalculationResultDto postProcess(TaskPostProcessDataDto taskPostProcessDataDto, ResultDto resultDto) {

        log.debug("Post processing result for task with id: {}", taskPostProcessDataDto.getId());
        TaskCalculationResultDto.Builder builder = TaskCalculationResultDto.newBuilder();

        builder.setId(taskPostProcessDataDto.getId())
                .setDateTimeStart(taskPostProcessDataDto.getDateTimeStart())
                .setDateTimeEnd(taskPostProcessDataDto.getDateTimeEnd())
                .setResultStatus(mapOptimizationStatusDtoToTaskCalculationResultStatusDto(resultDto.getOptimizationStatus()))
                .setOptimizerMessage(resultDto.getErrorMessage());

        if (builder.getResultStatus() == TaskCalculationResultStatusDto.SOLUTION_FOUND) {

            builder.setObjectiveFunctionValue(resultDto.getObjectiveFunctionValue())
                    .setElapsedTime(resultDto.getElapsedTime())
                    .setRelativeGap(resultDto.getRelativeGap());

            List<Interval> intervals = taskPostProcessDataDto.getIntervals().stream()
                    .map(intervalDto -> new Interval(
                            dateTimeMapper.mapCharSequenceToLocalDateTime(intervalDto.getDateTimeStart()),
                            dateTimeMapper.mapCharSequenceToLocalDateTime(intervalDto.getDateTimeEnd())
                    ))
                    .toList();

            builder.setContractResults(
                            resultDto.getContractResults().stream()
                                    .map(contractResultDto -> mapContractResult(contractResultDto, intervals))
                                    .toList())
                    .setStorageResults(
                            resultDto.getStorageResults().stream()
                                    .map(storageResultDto -> mapStorageResult(storageResultDto, intervals))
                                    .toList())
                    .setMovableDemandResults(
                            resultDto.getMovableDemandResults().stream()
                                    .map(movableDemandResultDto -> mapMovableDemandResult(taskPostProcessDataDto, movableDemandResultDto, intervals))
                                    .toList());
        }

        return builder.build();
    }

    private TaskCalculationResultStatusDto mapOptimizationStatusDtoToTaskCalculationResultStatusDto(OptimizationStatusDto optimizationStatusDto) {

        return switch (optimizationStatusDto) {
            case SOLUTION_FOUND -> TaskCalculationResultStatusDto.SOLUTION_FOUND;
            case SOLUTION_NOT_FOUND -> TaskCalculationResultStatusDto.SOLUTION_NOT_FOUND;
        };
    }

    private TaskCalculationContractResultDto mapContractResult(ContractResultDto contractResultDto, List<Interval> intervals) {

        TaskCalculationContractResultDto.Builder builder = TaskCalculationContractResultDto.newBuilder()
                .setId(contractResultDto.getId());

        int numberOfResults = contractResultDto.getPower().size();
        List<TaskCalculationContractResultValueDto> taskCalculationContractResultValueDtoList = new ArrayList<>(numberOfResults);
        for (int i=0; i<numberOfResults; i++) {

            taskCalculationContractResultValueDtoList.add(new TaskCalculationContractResultValueDto(
                    dateTimeMapper.mapLocalDateTimeToCharSequence(intervals.get(i).dateTimeStart()),
                    dateTimeMapper.mapLocalDateTimeToCharSequence(intervals.get(i).dateTimeEnd()),
                    contractResultDto.getPower().get(i),
                    contractResultDto.getEnergy().get(i),
                    contractResultDto.getCost().get(i)
            ));
        }

        builder.setContractResultValues(taskCalculationContractResultValueDtoList);

        return builder.build();
    }

    private TaskCalculationStorageResultDto mapStorageResult(StorageResultDto storageResultDto, List<Interval> intervals) {

        TaskCalculationStorageResultDto.Builder builder = TaskCalculationStorageResultDto.newBuilder()
                .setId(storageResultDto.getId());

        int numberOfValues = storageResultDto.getStorageMode().size();
        List<TaskCalculationStorageResultValueDto> taskCalculationStorageResultValueDtoList = new ArrayList<>(numberOfValues);

        for (int i=0; i<numberOfValues; i++) {

            taskCalculationStorageResultValueDtoList.add(new TaskCalculationStorageResultValueDto(
                    dateTimeMapper.mapLocalDateTimeToCharSequence(intervals.get(i).dateTimeStart()),
                    dateTimeMapper.mapLocalDateTimeToCharSequence(intervals.get(i).dateTimeEnd()),
                    storageResultDto.getCharge().get(i),
                    storageResultDto.getDischarge().get(i),
                    storageResultDto.getEnergy().get(i),
                    mapStorageMode(storageResultDto.getStorageMode().get(i))
            ));
        }
        builder.setStorageResultValues(taskCalculationStorageResultValueDtoList);

        return builder.build();
    }

    private TaskCalculationStorageModeDto mapStorageMode(StorageModeDto storageModeDto) {

        return switch (storageModeDto) {
            case DISABLED -> TaskCalculationStorageModeDto.DISABLED;
            case CHARGING -> TaskCalculationStorageModeDto.CHARGING;
            case DISCHARGING -> TaskCalculationStorageModeDto.DISCHARGING;
        };
    }

    private TaskCalculationMovableDemandResultDto mapMovableDemandResult(
            TaskPostProcessDataDto taskPostProcessDataDto,
            MovableDemandResultDto movableDemandResultDto,
            List<Interval> intervals
    ) {

        MovableDemandPostProcessDto movableDemandPostProcessData = taskPostProcessDataDto.getMovableDemands().stream()
                .filter(movableDemandPostProcessDto -> movableDemandPostProcessDto.getId() == movableDemandResultDto.getId())
                .findFirst()
                .orElseThrow();

        List<TaskCalculationMovableDemandResultValueDto> movableDemandResultValueDtoList = new ArrayList<>();
        int startInterval = movableDemandResultDto.getStartInterval();

        for (int i=0; i<movableDemandPostProcessData.getDemandPowerProfile().size(); i++) {
            movableDemandResultValueDtoList.add(new TaskCalculationMovableDemandResultValueDto(
                    dateTimeMapper.mapLocalDateTimeToCharSequence(intervals.get(startInterval + i).dateTimeStart()),
                    dateTimeMapper.mapLocalDateTimeToCharSequence(intervals.get(startInterval + i).dateTimeEnd()),
                    movableDemandPostProcessData.getDemandPowerProfile().get(i),
                    movableDemandPostProcessData.getDemandEnergyProfile().get(i)
            ));
        }

        return new TaskCalculationMovableDemandResultDto(
                movableDemandResultDto.getId(),
                movableDemandResultValueDtoList
        );
    }
}
