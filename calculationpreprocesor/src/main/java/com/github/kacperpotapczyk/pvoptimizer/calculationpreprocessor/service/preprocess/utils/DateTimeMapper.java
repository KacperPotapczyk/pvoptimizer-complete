package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeMapper {

    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ISO_DATE_TIME;
    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ISO_LOCAL_TIME;

    public LocalDateTime mapToLocalDateTime(CharSequence dateTime) {
        return LocalDateTime.from(dateTimeFormat.parse(dateTime));
    }

    public CharSequence mapDateTimeToCharSequence(LocalDateTime localDateTime) {
        return localDateTime.format(dateTimeFormat);
    }

    public LocalTime mapToLocalTime(CharSequence time) {
        return LocalTime.from(timeFormat.parse(time));
    }
}
