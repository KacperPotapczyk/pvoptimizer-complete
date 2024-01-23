package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess.utils;

import java.util.List;
import java.util.Optional;

public interface IntervalValueAverageService {

    double weighted(List<IntervalValue> intervalValues);
    Optional<Double> mvpBreak(List<OptionalIntervalValue> optionalIntervalValues);
    double mvpContinue(List<OptionalIntervalValue> optionalIntervalValues);
    double mvpSubstituteZero(List<OptionalIntervalValue> optionalIntervalValues);
}
