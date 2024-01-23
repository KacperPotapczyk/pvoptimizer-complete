package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public record OptionalIntervalValue(
        long index,
        LocalDateTime dateTimeStart,
        LocalDateTime dateTimeEnd,
        Duration length,
        Optional<Double> value
) {
    public OptionalIntervalValue(Interval interval, Optional<Double> value) {
        this(interval.index(), interval.dateTimeStart(), interval.dateTimeEnd(), interval.length(), value);
    }

    public OptionalIntervalValue(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd, Optional<Double> value) {
        this(0, dateTimeStart, dateTimeEnd, Duration.between(dateTimeStart, dateTimeEnd), value);
    }
}
