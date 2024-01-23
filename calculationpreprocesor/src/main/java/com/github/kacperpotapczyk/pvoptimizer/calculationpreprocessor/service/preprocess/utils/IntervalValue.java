package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public record IntervalValue(
        long index,
        LocalDateTime dateTimeStart,
        LocalDateTime dateTimeEnd,
        Duration length,
        double value
) {
    public IntervalValue(Interval interval, double value) {
        this(interval.index(), interval.dateTimeStart(), interval.dateTimeEnd(), interval.length(), value);
    }

    public IntervalValue(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd, double value) {
        this(0, dateTimeStart, dateTimeEnd, Duration.between(dateTimeStart, dateTimeEnd), value);
    }
}
