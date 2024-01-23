package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.Demand;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.DemandValue;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.DemandRevision;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class DemandServiceTest {

    private final DemandService demandService;

    @Autowired
    public DemandServiceTest(DemandService demandService) {
        this.demandService = demandService;
    }

    @Test
    public void testDemandAddingAndQuery() {
        
        String baseName = "base 1";

        Demand addedDemand = demandService.newBaseObject(getTestDemand(baseName));

        assertEquals(baseName, addedDemand.getName());
        assertEquals(1, addedDemand.getRevisions().size());
        assertTrue(demandService.existsByName(baseName));

        Demand dbDemand = demandService.findByName(baseName);
        assertTrue(demandService.existsById(dbDemand.getId()));

        assertEquals(1, dbDemand.getRevisions().size());
        DemandRevision dbRevision = dbDemand.getRevisions().stream().findAny().orElseThrow();

        assertEquals(1L, dbRevision.getRevisionNumber());
        assertEquals(1, dbRevision.getDemandValues().size());

        Set<DemandValue> demandValueSet = dbRevision.getDemandValues();
        assertEquals(30, demandValueSet.stream()
                .min(Comparator.comparing(DemandValue::getDateTime))
                .orElseThrow()
                .getValue()
        );
    }

    @Test
    public void addDemandRevision() {

        Demand demand = demandService.findByName("addRevisionTest");

        DemandRevision demandRevision = new DemandRevision();
        demandRevision.setRevisionNumber(2L);
        Set<DemandValue> demandValues = demandRevision.getDemandValues();
        demandValues.add(new DemandValue(20.0, LocalDateTime.parse("2023-10-05T12:00:00")));
        demandRevision.setDemandValues(demandValues);

        demandService.addRevision(demand.getName(), demandRevision);

        assertEquals(2, demand.getRevisions().size());
        DemandRevision dbVariant = demand.getRevision(2).orElseThrow();

        assertEquals(1, dbVariant.getDemandValues().size());

        assertEquals(20, dbVariant.getDemandValues().stream()
                .findAny()
                .orElseThrow()
                .getValue()
        );
    }

    @Test
    public void getDemandWithRevision() {

        String demandName = "queryOnly";
        Long revisionNumber = 2L;

        Demand demand = demandService.getBaseObjectWithRevision(demandName, revisionNumber);

        assertEquals(demandName, demand.getName());
        assertEquals(1, demand.getRevisions().size());

        DemandRevision demandRevision = demand.getRevision(revisionNumber).orElseThrow();
        assertEquals(revisionNumber, demandRevision.getRevisionNumber());
        assertEquals(4, demandRevision.getDemandValues().size());
    }

    @Test
    public void deleteDemandRevision() {

        String baseName = "revisionToBeDeleted";

        assertThrows(IllegalStateException.class, () -> demandService.deleteBaseObjectRevision(baseName, 1L));

        DemandRevision demandRevision = new DemandRevision();
        demandRevision.setRevisionNumber(2L);
        Set<DemandValue> demandValues = demandRevision.getDemandValues();
        demandValues.add(new DemandValue(20.0, LocalDateTime.parse("2023-10-05T12:00:00")));
        demandRevision.setDemandValues(demandValues);

        demandService.addRevision(baseName, demandRevision);

        Demand demand = demandService.findByName(baseName);

        assertEquals(2, demand.getRevisions().size());
        demandService.deleteBaseObjectRevision(baseName, 1L);

        assertEquals(1, demand.getRevisions().size());
        assertEquals(2L, demand.getRevision(2L).orElseThrow().getRevisionNumber());
    }

    @Test
    public void deleteDemand() {

        String baseName = "toBeDeleted";
        assertTrue(demandService.existsByName(baseName));

        demandService.deleteBaseObject(baseName);
        assertFalse(demandService.existsByName(baseName));
    }

    private Demand getTestDemand(String name) {

        DemandValue demandValue1 = new DemandValue();
        demandValue1.setValue(30.0);
        demandValue1.setDateTime(LocalDateTime.parse("2023-10-05T11:00:00"));

        Set<DemandValue> demandValueSet = new HashSet<>();
        demandValueSet.add(demandValue1);

        DemandRevision demandRevision = new DemandRevision(1L, demandValueSet);

        Set<DemandRevision> demandRevisionSet = new HashSet<>();
        demandRevisionSet.add(demandRevision);

        Demand demand = new Demand();
        demand.setName(name);
        demand.setRevisions(demandRevisionSet);

        return demand;
    }
}
