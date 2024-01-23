package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class IntervalValueAverageServiceTest {

    @Autowired
    private IntervalValueAverageService averageService;

    @Test
    void weighted() {

        List<IntervalValue> intervalValues = getIntervalValueList();

        assertEquals(7.728275862, averageService.weighted(intervalValues), 1e-9);
    }

    @Test
    void mvpBreak() {

        List<OptionalIntervalValue> optionalIntervalValues = getOptionalIntervalValueListWithAllValuesPresent();
        Optional<Double> avg = averageService.mvpBreak(optionalIntervalValues);

        assertTrue(avg.isPresent());
        assertEquals(7.728275862, avg.get(), 1e-9);
    }

    @Test
    void mvpBreakEmpty() {

        List<OptionalIntervalValue> optionalIntervalValues = getOptionalIntervalValueListWithMissingValue();
        Optional<Double> avg = averageService.mvpBreak(optionalIntervalValues);

        assertTrue(avg.isEmpty());
    }

    @Test
    void mvpContinue() {

        List<OptionalIntervalValue> optionalIntervalValues = getOptionalIntervalValueListWithMissingValue();

        assertEquals(7.728275862, averageService.mvpContinue(optionalIntervalValues), 1e-9);
    }

    @Test
    void mvpSubstituteZero() {

        List<OptionalIntervalValue> optionalIntervalValues = getOptionalIntervalValueListWithMissingValue();

        assertEquals(6.225555556, averageService.mvpSubstituteZero(optionalIntervalValues), 1e-9);
    }

    private List<IntervalValue> getIntervalValueList() {
        List<IntervalValue> intervalValues = new ArrayList<>(4);
        intervalValues.add(new IntervalValue(
                LocalDateTime.of(2023, 1,1,10,0,0),
                LocalDateTime.of(2023, 1,1,10,15,0),
                10
        ));
        intervalValues.add(new IntervalValue(
                LocalDateTime.of(2023, 1,1,10,15,0),
                LocalDateTime.of(2023, 1,1,10,19,0),
                13.3
        ));
        intervalValues.add(new IntervalValue(
                LocalDateTime.of(2023, 1,1,10,20,0),
                LocalDateTime.of(2023, 1,1,10,24,0),
                -2.45
        ));
        intervalValues.add(new IntervalValue(
                LocalDateTime.of(2023, 1,1,10,24,0),
                LocalDateTime.of(2023, 1,1,10,30,0),
                5.12
        ));
        return intervalValues;
    }

    private List<OptionalIntervalValue> getOptionalIntervalValueListWithAllValuesPresent() {

        List<OptionalIntervalValue> optionalIntervalValues = new ArrayList<>(5);
        optionalIntervalValues.add(new OptionalIntervalValue(
                LocalDateTime.of(2023, 1,1,10,0,0),
                LocalDateTime.of(2023, 1,1,10,15,0),
                Optional.of(10.0)
        ));
        optionalIntervalValues.add(new OptionalIntervalValue(
                LocalDateTime.of(2023, 1,1,10,15,0),
                LocalDateTime.of(2023, 1,1,10,19,0),
                Optional.of(13.3)
        ));
        optionalIntervalValues.add(new OptionalIntervalValue(
                LocalDateTime.of(2023, 1,1,10,20,0),
                LocalDateTime.of(2023, 1,1,10,24,0),
                Optional.of(-2.45)
        ));
        optionalIntervalValues.add(new OptionalIntervalValue(
                LocalDateTime.of(2023, 1,1,10,24,0),
                LocalDateTime.of(2023, 1,1,10,30,0),
                Optional.of(5.12)
        ));

        return optionalIntervalValues;
    }

    private List<OptionalIntervalValue> getOptionalIntervalValueListWithMissingValue() {

        List<OptionalIntervalValue> optionalIntervalValues = getOptionalIntervalValueListWithAllValuesPresent();
        optionalIntervalValues.add(new OptionalIntervalValue(
                LocalDateTime.of(2023, 1,1,10,55,0),
                LocalDateTime.of(2023, 1,1,11,2,0),
                Optional.empty()
        ));

        return optionalIntervalValues;
    }
}