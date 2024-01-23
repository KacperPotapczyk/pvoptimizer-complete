package com.github.kacperpotapczyk.pvoptimizer.resultpostprocessor.util;

import java.time.Duration;
import java.time.LocalDateTime;

public record Interval(
//        int index,
        LocalDateTime dateTimeStart,
        LocalDateTime dateTimeEnd,
        Duration length
) {
    public Interval(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd) {

        this(dateTimeStart, dateTimeEnd, Duration.between(dateTimeStart, dateTimeEnd));
    }
}
