package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.Tariff;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.TariffRevision;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class TariffServiceTest {

    private final TariffService tariffService;

    @Autowired
    public TariffServiceTest(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @Test
    public void testTariffAddingAndQuery() {

        String baseName = "base tariff 1";
        double defaultPrice = 3.24;

        Tariff addedTariff = tariffService.newBaseObject(getTestTariff(baseName, defaultPrice));

        assertEquals(baseName, addedTariff.getName());
        assertEquals(1, addedTariff.getRevisions().size());
        assertTrue(tariffService.existsByName(baseName));

        Tariff dbTariff = tariffService.findByName(baseName);
        assertTrue(tariffService.existsById(dbTariff.getId()));

        assertEquals(1, dbTariff.getRevisions().size());
        TariffRevision dbRevision = dbTariff.getRevisions().stream().findAny().orElseThrow();

        assertEquals(1L, dbRevision.getRevisionNumber());
        assertEquals(defaultPrice, dbRevision.getDefaultPrice());
    }

    @Test
    public void addTariffRevision() {

        double defaultValue = 13.2;
        Tariff tariff = tariffService.findByName("addRevisionTest");

        TariffRevision tariffRevision = new TariffRevision();
        tariffRevision.setDefaultPrice(defaultValue);

        tariffService.addRevision(tariff.getName(), tariffRevision);

        assertEquals(2, tariff.getRevisions().size());
        TariffRevision dbTariffRevision = tariff.getRevision(2L).orElseThrow();

        assertEquals(defaultValue, dbTariffRevision.getDefaultPrice());
    }

    @Test
    public void getTariffWithRevision() {

        String tariffName = "queryOnly";
        long revisionNumber = 2L;

        Tariff tariff = tariffService.getBaseObjectWithRevision(tariffName, revisionNumber);

        assertEquals(tariffName, tariff.getName());
        assertEquals(1, tariff.getRevisions().size());

        TariffRevision tariffRevision = tariff.getRevision(revisionNumber).orElseThrow();
        assertEquals(revisionNumber, tariffRevision.getRevisionNumber());
        assertEquals(0.056, tariffRevision.getDefaultPrice());
    }

    @Test
    public void deleteTariffRevision() {

        String baseName = "revisionToBeDeleted";

        assertThrows(IllegalStateException.class, () -> tariffService.deleteBaseObjectRevision(baseName, 1L));

        TariffRevision tariffRevision = new TariffRevision();
        tariffRevision.setDefaultPrice(1.2);

        tariffService.addRevision(baseName, tariffRevision);

        Tariff tariff = tariffService.findByName(baseName);
        assertEquals(2, tariff.getRevisions().size());

        tariffService.deleteBaseObjectRevision(baseName, 1L);
        assertEquals(1, tariff.getRevisions().size());
        assertEquals(2L, tariff.getRevision(2L).orElseThrow().getRevisionNumber());
    }

    @Test
    public void deleteTariff() {

        String baseName = "toBeDeleted";
        assertTrue(tariffService.existsByName(baseName));

        tariffService.deleteBaseObject(baseName);
        assertFalse(tariffService.existsByName(baseName));
    }

    private Tariff getTestTariff(String name, double defaultPrice) {

        TariffRevision tariffRevision = new TariffRevision(1L, defaultPrice);

        Set<TariffRevision> tariffRevisionSet = new HashSet<>();
        tariffRevisionSet.add(tariffRevision);

        Tariff tariff = new Tariff();
        tariff.setName(name);
        tariff.setRevisions(tariffRevisionSet);

        return tariff;
    }
}
