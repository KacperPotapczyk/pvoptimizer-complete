package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.Contract;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractMinPowerConstraint;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractType;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.Tariff;
import com.github.kacperpotapczyk.pvoptimizer.backend.utils.ContractBuilder;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ContractServiceTest {

    private final ContractService contractService;
    private final TariffService tariffService;

    @Autowired
    public ContractServiceTest(ContractService contractService, TariffService tariffService) {
        this.contractService = contractService;
        this.tariffService = tariffService;
    }

    @Test
    public void testContractAddingAndQuery() {

        String baseName = "base contract 1";
        Tariff tariff = tariffService.findByName("queryOnly");
        LocalDateTime startDate = LocalDateTime.parse("2023-10-05T12:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-11-05T12:00:00");
        double minPowerValue = 3.1;

        ContractBuilder contractBuilder = new ContractBuilder(baseName, tariff, ContractType.PURCHASE);
        Contract contract = contractBuilder
                .addContractMinPowerConstraint(startDate, endDate, minPowerValue)
                .build();

        Contract addedContract = contractService.newBaseObject(contract);

        assertEquals(baseName, addedContract.getName());
        assertEquals(1, addedContract.getRevisions().size());
        assertEquals(ContractType.PURCHASE, addedContract.getContractType());
        assertTrue(contractService.existsByName(baseName));

        ContractRevision contractRevision = addedContract.getRevision(1).orElseThrow();
        assertEquals(1, contractRevision.getContractMinPowerConstraints().size());
        assertEquals(minPowerValue, contractRevision.getContractMinPowerConstraints().get(0).getConstraintValue());
    }

    @Test
    public void addContractRevision() {

        Contract contract = contractService.findByName("addRevisionTest");

        assertEquals(1, contract.getRevisions().size());

        ContractRevision contractRevision = new ContractRevision(2L);
        contractService.addRevision(contract.getName(), contractRevision);

        assertEquals(2, contract.getRevisions().size());
    }

    @Test
    public void addInvalidContractRevision() {

        Contract contract = contractService.findByName("addRevisionTest");

        assertEquals(1, contract.getRevisions().size());

        ContractRevision contractRevision = new ContractRevision(2L);

        ContractMinPowerConstraint contractMinPowerConstraint = new ContractMinPowerConstraint();
        contractMinPowerConstraint.setConstraintValue(-1.0);
        contractMinPowerConstraint.setDateTimeStart(LocalDateTime.parse("2023-10-05T12:00:00"));
        contractMinPowerConstraint.setDateTimeEnd(LocalDateTime.parse("2023-01-05T12:00:00"));

        List<ContractMinPowerConstraint> contractMinPowerConstraintList = new ArrayList<>();
        contractMinPowerConstraintList.add(contractMinPowerConstraint);

        contractRevision.setContractMinPowerConstraints(contractMinPowerConstraintList);

        assertThrows(IllegalArgumentException.class, () -> contractService.addRevision(contract.getName(), contractRevision));
        assertEquals(1, contract.getRevisions().size());
    }

    @Test
    public void getContractWithRevision() {

        String contractName = "queryOnly";
        long revisionNumber = 2L;

        Contract contract = contractService.getBaseObjectWithRevision(contractName, revisionNumber);

        assertEquals(contractName, contract.getName());
        assertEquals(1, contract.getRevisions().size());

        ContractRevision contractRevision = contract.getRevision(revisionNumber).orElseThrow();
        assertEquals(revisionNumber, contractRevision.getRevisionNumber());
        assertEquals(2, contractRevision.getContractMinPowerConstraints().size());
        assertEquals(1, contractRevision.getContractMaxPowerConstraints().size());
        assertEquals(1, contractRevision.getContractMinEnergyConstraints().size());
        assertEquals(1, contractRevision.getContractMaxEnergyConstraints().size());
    }

    @Test
    public void deleteContractRevision() {

        String contractName = "revisionToBeDeleted";

        assertThrows(IllegalStateException.class, () -> contractService.deleteBaseObjectRevision(contractName, 1L));

        ContractRevision contractRevision = new ContractRevision(2L);

        Contract contract = contractService.addRevision(contractName, contractRevision);
        assertEquals(2, contract.getRevisions().size());

        contractService.deleteBaseObjectRevision(contractName, 1L);
        assertEquals(1, contract.getRevisions().size());
        assertEquals(2L, contract.getRevision(2L).orElseThrow().getRevisionNumber());
    }

    @Test
    public void deleteContract() {

        String contractName = "toBeDeleted";
        assertTrue(contractService.existsByName(contractName));

        contractService.deleteBaseObject(contractName);
        assertFalse(contractService.existsByName(contractName));
    }
}
