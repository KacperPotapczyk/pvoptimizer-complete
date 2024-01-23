package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeMapper {

    private static final DateTimeFormatter format = DateTimeFormatter.ISO_DATE_TIME;

    public LocalDateTime mapToLocalDateTime(CharSequence dateTime) {
        return LocalDateTime.from(format.parse(dateTime));
    }

    public CharSequence mapToCharSequence(LocalDateTime localDateTime) {
        return localDateTime.format(format);
    }
}
