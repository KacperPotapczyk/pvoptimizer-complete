package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess.utils;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IntervalValueAverageServiceImpl implements IntervalValueAverageService {

    @Override
    public double weighted(List<IntervalValue> intervalValues) {

        double weightedSum = 0;
        double sumOfWeights = 0;

        for (IntervalValue intervalValue : intervalValues) {
            weightedSum += intervalValue.value() * intervalValue.length().toMinutes();
            sumOfWeights += intervalValue.length().toMinutes();
        }
        return weightedSum / sumOfWeights;
    }

    @Override
    public Optional<Double> mvpBreak(List<OptionalIntervalValue> optionalIntervalValues) {

        double weightedSum = 0;
        double sumOfWeights = 0;

        for (OptionalIntervalValue optionalIntervalValue : optionalIntervalValues) {

            if (optionalIntervalValue.value().isPresent()) {
                weightedSum += optionalIntervalValue.value().get() * optionalIntervalValue.length().toMinutes();
                sumOfWeights += optionalIntervalValue.length().toMinutes();
            }
            else {
                return Optional.empty();
            }
        }
        return Optional.of(weightedSum / sumOfWeights);
    }

    @Override
    public double mvpContinue(List<OptionalIntervalValue> optionalIntervalValues) {

        double weightedSum = 0;
        double sumOfWeights = 0;

        for (OptionalIntervalValue optionalIntervalValue : optionalIntervalValues) {

            if (optionalIntervalValue.value().isPresent()) {
                weightedSum += optionalIntervalValue.value().get() * optionalIntervalValue.length().toMinutes();
                sumOfWeights += optionalIntervalValue.length().toMinutes();
            }
        }
        return weightedSum / sumOfWeights;
    }

    @Override
    public double mvpSubstituteZero(List<OptionalIntervalValue> optionalIntervalValues) {

        double weightedSum = 0;
        double sumOfWeights = 0;

        for (OptionalIntervalValue optionalIntervalValue : optionalIntervalValues) {

            weightedSum += optionalIntervalValue.value().orElse(0.0) * optionalIntervalValue.length().toMinutes();
            sumOfWeights += optionalIntervalValue.length().toMinutes();
        }
        return weightedSum / sumOfWeights;
    }
}
