package com.github.kacperpotapczyk.pvoptimizer.backend.mapper;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeMapper {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_TIME;

    public CharSequence dateTimeAsCharSequence(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(dateTimeFormatter) : null;
    }

    public LocalDateTime dateTimeAsLocalDateTime(CharSequence charSequence) {
        return charSequence != null ? LocalDateTime.parse(charSequence, dateTimeFormatter) : null;
    }

    public CharSequence timeAsCharSequence(LocalTime localTime) {
        return localTime != null ? localTime.format(timeFormatter) : null;
    }

    public LocalTime timeAsLocalTime(CharSequence charSequence) {
        return charSequence != null ? LocalTime.parse(charSequence, timeFormatter) : null;
    }
}
