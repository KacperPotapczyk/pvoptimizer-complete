package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public record Interval(
        int index,
        LocalDateTime dateTimeStart,
        LocalDateTime dateTimeEnd,
        Duration length
) {
    public Interval(int index, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd) {

        this(index, dateTimeStart, dateTimeEnd, Duration.between(dateTimeStart, dateTimeEnd));
    }
}
