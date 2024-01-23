package com.github.kacperpotapczyk.pvoptimizer.backend.mapper;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public CharSequence asCharSequence(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(formatter) : null;
    }

    public LocalDateTime asLocalDateTime(CharSequence charSequence) {
        return charSequence != null ? LocalDateTime.parse(charSequence, formatter) : null;
    }
}
