package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.production.Production;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.production.ProductionRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.production.ProductionValue;
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
class ProductionServiceTest {

    private final ProductionService productionService;

    @Autowired
    ProductionServiceTest(ProductionService productionService) {
        this.productionService = productionService;
    }

    @Test
    public void testProductionAddingAndQuery() {

        String baseName = "base 1";

        Production addedProduction = productionService.newBaseObject(getTestProduction(baseName));

        assertEquals(baseName, addedProduction.getName());
        assertEquals(1, addedProduction.getRevisions().size());
        assertTrue(productionService.existsByName(baseName));

        Production dbProduction = productionService.findByName(baseName);
        assertTrue(productionService.existsById(dbProduction.getId()));

        assertEquals(1, dbProduction.getRevisions().size());
        ProductionRevision productionRevision = dbProduction.getRevisions().stream().findAny().orElseThrow();

        assertEquals(1L, productionRevision.getRevisionNumber());
        assertEquals(1, productionRevision.getProductionValues().size());

        Set<ProductionValue> productionValueSet = productionRevision.getProductionValues();
        assertEquals(30, productionValueSet.stream()
                .min(Comparator.comparing(ProductionValue::getDateTime))
                .orElseThrow()
                .getValue()
        );
    }

    @Test
    public void addProductionRevision() {

        Production production = productionService.findByName("addRevisionTest");

        ProductionRevision productionRevision = new ProductionRevision();
        productionRevision.setRevisionNumber(2L);
        Set<ProductionValue> productionValues = productionRevision.getProductionValues();
        productionValues.add(new ProductionValue(20.0, LocalDateTime.parse("2023-10-05T12:00:00")));
        productionRevision.setProductionValues(productionValues);

        productionService.addRevision(production.getName(), productionRevision);

        assertEquals(2, production.getRevisions().size());
        ProductionRevision dbVariant = production.getRevision(2).orElseThrow();

        assertEquals(1, dbVariant.getProductionValues().size());

        assertEquals(20, dbVariant.getProductionValues().stream()
                .findAny()
                .orElseThrow()
                .getValue()
        );
    }

    @Test
    public void getProductionWithRevision() {

        String baseName = "queryOnly";
        Long revisionNumber = 2L;

        Production production = productionService.getBaseObjectWithRevision(baseName, revisionNumber);

        assertEquals(baseName, production.getName());
        assertEquals(1, production.getRevisions().size());

        ProductionRevision productionRevision = production.getRevision(revisionNumber).orElseThrow();
        assertEquals(revisionNumber, productionRevision.getRevisionNumber());
        assertEquals(4, productionRevision.getProductionValues().size());
    }

    @Test
    public void deleteProductionRevision() {

        String baseName = "revisionToBeDeleted";

        assertThrows(IllegalStateException.class, () -> productionService.deleteBaseObjectRevision(baseName, 1L));

        ProductionRevision productionRevision = new ProductionRevision();
        productionRevision.setRevisionNumber(2L);
        Set<ProductionValue> productionValues = productionRevision.getProductionValues();
        productionValues.add(new ProductionValue(20.0, LocalDateTime.parse("2023-10-05T12:00:00")));
        productionRevision.setProductionValues(productionValues);

        productionService.addRevision(baseName, productionRevision);

        Production production = productionService.findByName(baseName);

        assertEquals(2, production.getRevisions().size());
        productionService.deleteBaseObjectRevision(baseName, 1L);

        assertEquals(1, production.getRevisions().size());
        assertEquals(2L, production.getRevision(2L).orElseThrow().getRevisionNumber());
    }

    @Test
    public void deleteProduction() {

        String baseName = "toBeDeleted";
        assertTrue(productionService.existsByName(baseName));

        productionService.deleteBaseObject(baseName);
        assertFalse(productionService.existsByName(baseName));
    }

    private Production getTestProduction(String name) {

        ProductionValue productionValue1 = new ProductionValue();
        productionValue1.setValue(30.0);
        productionValue1.setDateTime(LocalDateTime.parse("2023-10-05T11:00:00"));

        Set<ProductionValue> productionValueSet = new HashSet<>();
        productionValueSet.add(productionValue1);

        ProductionRevision productionRevision = new ProductionRevision();
        productionRevision.setRevisionNumber(1L);
        productionRevision.setProductionValues(productionValueSet);

        Set<ProductionRevision> productionRevisionSet = new HashSet<>();
        productionRevisionSet.add(productionRevision);

        Production production = new Production();
        production.setName(name);
        production.setRevisions(productionRevisionSet);

        return production;
    }
}