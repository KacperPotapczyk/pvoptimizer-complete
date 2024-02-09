package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage.Storage;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage.StorageMaxChargeConstraint;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage.StorageRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.utils.StorageBuilder;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class StorageServiceTest {

    private final StorageService storageService;

    @Autowired
    StorageServiceTest(StorageService storageService) {
        this.storageService = storageService;
    }

    @Test
    public void testStorageAddingAndQuery() {

        String baseName = "base 1";
        double capacity = 100;
        double maxCharge = 10;
        double maxDischarge = 20;
        double initialEnergy = 40;
        double maxChargeConstraint = 5;

        StorageBuilder storageBuilder = new StorageBuilder(
                baseName,
                capacity,
                maxCharge,
                maxDischarge,
                initialEnergy
        ).addMaxChargeConstraint(
                LocalDateTime.parse("2023-10-05T11:00:00"),
                LocalDateTime.parse("2023-10-05T11:15:00"),
                maxChargeConstraint
        );

        Storage addedStorage = storageService.newBaseObject(storageBuilder.build());

        assertEquals(baseName, addedStorage.getName());
        assertEquals(1, addedStorage.getRevisions().size());

        Storage dbStorage = storageService.findByName(baseName);
        assertTrue(storageService.existsById(dbStorage.getId()));
        assertEquals(capacity, dbStorage.getCapacity());
        assertEquals(maxCharge, dbStorage.getMaxCharge());
        assertEquals(maxDischarge, dbStorage.getMaxDischarge());

        assertEquals(1, dbStorage.getRevisions().size());
        StorageRevision storageRevision = dbStorage.getRevisions().stream().findFirst().orElseThrow();

        assertEquals(1L, storageRevision.getRevisionNumber());
        assertEquals(initialEnergy, storageRevision.getInitialEnergy());
        assertEquals(0, storageRevision.getStorageMinChargeConstraints().size());
        assertEquals(1, storageRevision.getStorageMaxChargeConstraints().size());
        assertEquals(0, storageRevision.getStorageMinDischargeConstraints().size());
        assertEquals(0, storageRevision.getStorageMaxDischargeConstraints().size());
        assertEquals(0, storageRevision.getStorageMinEnergyConstraints().size());
        assertEquals(0, storageRevision.getStorageMaxEnergyConstraints().size());

        StorageMaxChargeConstraint storageMaxChargeConstraint = storageRevision.getStorageMaxChargeConstraints().get(0);
        assertEquals(maxChargeConstraint, storageMaxChargeConstraint.getConstraintValue());
    }

    @Test
    public void addStorageRevision() {

        String baseName = "addRevisionTest";
        double initialEnergy = 30.0;

        StorageRevision storageRevision = new StorageRevision();
        storageRevision.setInitialEnergy(initialEnergy);

        Storage storage = storageService.addRevision(baseName, storageRevision);

        assertEquals(2, storage.getRevisions().size());
        StorageRevision dbRevision = storage.getRevision(2).orElseThrow();

        assertEquals(initialEnergy, dbRevision.getInitialEnergy());
    }

    @Test
    public void getStorageWithRevision() {

        String baseName = "queryOnly";
        long revisionNumber = 2L;

        Storage storage = storageService.getBaseObjectWithRevision(baseName, revisionNumber);

        assertEquals(baseName, storage.getName());
        assertEquals(1, storage.getRevisions().size());

        StorageRevision storageRevision = storage.getRevision(revisionNumber).orElseThrow();
        assertEquals(revisionNumber, storageRevision.getRevisionNumber());
        assertEquals(0, storageRevision.getStorageMinChargeConstraints().size());
        assertEquals(1, storageRevision.getStorageMaxChargeConstraints().size());
        assertEquals(0, storageRevision.getStorageMinDischargeConstraints().size());
        assertEquals(1, storageRevision.getStorageMaxDischargeConstraints().size());
        assertEquals(1, storageRevision.getStorageMinEnergyConstraints().size());
        assertEquals(0, storageRevision.getStorageMaxEnergyConstraints().size());
    }

    @Test
    public void deleteStorageRevision() {

        String baseName = "revisionToBeDeleted";

        assertThrows(IllegalStateException.class, () -> storageService.deleteBaseObjectRevision(baseName, 1L));

        StorageRevision storageRevision = new StorageRevision();
        storageRevision.setInitialEnergy(20.0);

        storageService.addRevision(baseName, storageRevision);

        Storage storage = storageService.findByName(baseName);
        assertEquals(2, storage.getRevisions().size());

        storageService.deleteBaseObjectRevision(baseName, 1L);
        assertEquals(1, storage.getRevisions().size());
        assertEquals(2L, storage.getRevision(2L).orElseThrow().getRevisionNumber());
    }

    @Test
    public void deleteStorage() {

        String baseName = "toBeDeleted";
        assertTrue(storageService.existsByName(baseName));

        storageService.deleteBaseObject(baseName);
        assertFalse(storageService.existsByName(baseName));
    }
}