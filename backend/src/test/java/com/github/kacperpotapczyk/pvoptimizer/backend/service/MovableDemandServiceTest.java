package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand.MovableDemand;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand.MovableDemandRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand.MovableDemandStart;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand.MovableDemandValue;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MovableDemandServiceTest {

    private final MovableDemandService movableDemandService;

    @Autowired
    MovableDemandServiceTest(MovableDemandService movableDemandService) {
        this.movableDemandService = movableDemandService;
    }

    @Test
    public void testMovableDemandAddingAndQuery() {

        String baseName = "base 1";
        LocalDateTime start1 = LocalDateTime.parse("2023-10-05T11:00:00");
        LocalDateTime start2 = LocalDateTime.parse("2023-10-05T11:15:00");
        long duration = 15;
        double value = 10;

        Set<MovableDemandRevision> revisionSet = new HashSet<>();
        revisionSet.add(getMovableDemandRevision(value, duration, start1, start2));

        MovableDemand movableDemand = new MovableDemand();
        movableDemand.setName(baseName);
        movableDemand.setRevisions(revisionSet);

        MovableDemand addedMovableDemand = movableDemandService.newBaseObject(movableDemand);
        assertEquals(baseName, addedMovableDemand.getName());
        assertEquals(1, addedMovableDemand.getRevisions().size());

        MovableDemand dbMovableDemand = movableDemandService.findByName(baseName);
        assertTrue(movableDemandService.existsById(dbMovableDemand.getId()));
        assertEquals(1, dbMovableDemand.getRevisions().size());
        assertEquals(1L, dbMovableDemand.getLastRevisionNumber().orElseThrow());

        MovableDemandRevision dbRevision = dbMovableDemand.getRevision(1L).orElseThrow();
        assertEquals(1, dbRevision.getMovableDemandValues().size());
        assertEquals(2, dbRevision.getMovableDemandStarts().size());

        MovableDemandValue movableDemandValue = dbRevision.getMovableDemandValues().get(0);
        assertEquals(duration, movableDemandValue.getDurationMinutes());
        assertEquals(value, movableDemandValue.getValue());

        MovableDemandStart movableDemandStart1 = dbRevision.getMovableDemandStarts().stream()
                .min(MovableDemandStart::compareTo)
                .orElseThrow();
        assertEquals(start1, movableDemandStart1.getStart());

        MovableDemandStart movableDemandStart2 = dbRevision.getMovableDemandStarts().stream()
                .max(MovableDemandStart::compareTo)
                .orElseThrow();
        assertEquals(start2, movableDemandStart2.getStart());
    }

    @Test
    public void addMovableDemandRevision() {

        String baseName = "addRevisionTest";
        LocalDateTime start1 = LocalDateTime.parse("2023-10-05T11:30:00");
        LocalDateTime start2 = LocalDateTime.parse("2023-10-05T12:00:00");
        long duration = 10;
        double value = 21.3;

        MovableDemandRevision movableDemandRevision = (getMovableDemandRevision(value, duration, start1, start2));

        MovableDemand movableDemand = movableDemandService.addRevision(baseName, movableDemandRevision);
        assertEquals(2, movableDemand.getRevisions().size());

        MovableDemandRevision dbRevision = movableDemand.getRevision(2).orElseThrow();
        assertEquals(1, dbRevision.getMovableDemandValues().size());
        assertEquals(2, dbRevision.getMovableDemandStarts().size());

        MovableDemandValue movableDemandValue = dbRevision.getMovableDemandValues().get(0);
        assertEquals(duration, movableDemandValue.getDurationMinutes());
        assertEquals(value, movableDemandValue.getValue());

        MovableDemandStart movableDemandStart1 = dbRevision.getMovableDemandStarts().stream()
                .min(MovableDemandStart::compareTo)
                .orElseThrow();
        assertEquals(start1, movableDemandStart1.getStart());

        MovableDemandStart movableDemandStart2 = dbRevision.getMovableDemandStarts().stream()
                .max(MovableDemandStart::compareTo)
                .orElseThrow();
        assertEquals(start2, movableDemandStart2.getStart());
    }

    @Test
    public void getMovableDemandWithRevision() {

        String baseName = "queryOnly";
        long revisionNumber = 2L;

        MovableDemand movableDemand = movableDemandService.getBaseObjectWithRevision(baseName, revisionNumber);

        assertEquals(baseName, movableDemand.getName());
        assertEquals(1, movableDemand.getRevisions().size());

        MovableDemandRevision movableDemandRevision = movableDemand.getRevision(revisionNumber).orElseThrow();
        assertEquals(2, movableDemandRevision.getMovableDemandStarts().size());
        assertEquals(2, movableDemandRevision.getMovableDemandValues().size());

        List<MovableDemandStart> movableDemandStartList = movableDemandRevision.getMovableDemandStarts().stream()
                .sorted().toList();
        assertEquals(LocalDateTime.parse("2023-01-01T09:55:00"), movableDemandStartList.get(0).getStart());
        assertEquals(LocalDateTime.parse("2023-01-01T10:10:00"), movableDemandStartList.get(1).getStart());

        List<MovableDemandValue> movableDemandValueList = movableDemandRevision.getMovableDemandValues().stream()
                .sorted().toList();
        assertEquals(1, movableDemandValueList.get(0).getOrder());
        assertEquals(10, movableDemandValueList.get(0).getDurationMinutes());
        assertEquals(10.0, movableDemandValueList.get(0).getValue());
        assertEquals(2, movableDemandValueList.get(1).getOrder());
        assertEquals(15, movableDemandValueList.get(1).getDurationMinutes());
        assertEquals(5.0, movableDemandValueList.get(1).getValue());
    }

    @Test
    public void deleteMovableDemandRevision() {

        String baseName = "revisionToBeDeleted";
        LocalDateTime start1 = LocalDateTime.parse("2023-10-05T11:30:00");
        LocalDateTime start2 = LocalDateTime.parse("2023-10-05T12:00:00");
        long duration = 10;
        double value = 21.3;

        assertThrows(IllegalStateException.class, () -> movableDemandService.deleteBaseObjectRevision(baseName, 1L));

        MovableDemandRevision movableDemandRevision = getMovableDemandRevision(value, duration, start1, start2);
        movableDemandService.addRevision(baseName, movableDemandRevision);

        MovableDemand movableDemand = movableDemandService.findByName(baseName);
        assertEquals(2, movableDemand.getRevisions().size());

        movableDemandService.deleteBaseObjectRevision(baseName, 1L);
        assertEquals(1, movableDemand.getRevisions().size());
        assertEquals(2L, movableDemand.getRevision(2L).orElseThrow().getRevisionNumber());
    }

    @Test
    public void deleteMovableDemand() {

        String baseName = "toBeDeleted";
        assertTrue(movableDemandService.existsByName(baseName));

        movableDemandService.deleteBaseObject(baseName);
        assertFalse(movableDemandService.existsByName(baseName));
    }

    private MovableDemandRevision getMovableDemandRevision(double value, long duration, LocalDateTime start1, LocalDateTime start2) {

        List<MovableDemandValue> valueList = new ArrayList<>();
        valueList.add(new MovableDemandValue(1L, value, duration));

        Set<MovableDemandStart> startSet = new HashSet<>();
        startSet.add(new MovableDemandStart(start1));
        startSet.add(new MovableDemandStart(start2));

        return new MovableDemandRevision(
                0L,
                startSet,
                valueList
        );
    }
}