package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.Contract;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractConstraint;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.repository.ContractRepository;
import com.github.kacperpotapczyk.pvoptimizer.backend.repository.ContractRevisionRepository;
import jakarta.transaction.Transactional;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ContractServiceImp implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractRevisionRepository contractRevisionRepository;

    @Autowired
    public ContractServiceImp(ContractRepository contractRepository, ContractRevisionRepository contractRevisionRepository) {
        this.contractRepository = contractRepository;
        this.contractRevisionRepository = contractRevisionRepository;
    }

    @Override
    public boolean existsById(long id) {
        return contractRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return contractRepository.existsByName(name);
    }

    @Override
    public Contract findByName(String name) {

        return contractRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Contract", (Object) name));
    }

    @Override
    public Contract newBaseObject(Contract contract) {

        if (!existsByName(contract.getName()) && contract.getRevisions().size()==1) {
            contract.setId(0L);
            ContractRevision contractRevision = contract.getRevisions().stream().findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Base object must have one revision"));

            if (isRevisionValid(contractRevision)) {
                contractRevision.setRevisionNumber(1L);
                contractRevision.setContract(contract);

                contractRevision.getContractMinPowerConstraints().forEach(constraint -> constraint.setContractRevision(contractRevision));
                contractRevision.getContractMaxPowerConstraints().forEach(constraint -> constraint.setContractRevision(contractRevision));
                contractRevision.getContractMinEnergyConstraints().forEach(constraint -> constraint.setContractRevision(contractRevision));
                contractRevision.getContractMaxEnergyConstraints().forEach(constraint -> constraint.setContractRevision(contractRevision));

                contract.setRevisions(Set.of(contractRevision));

                return contractRepository.save(contract);
            }
            else {
                throw new IllegalArgumentException("Revision has invalid constraints");
            }
        }
        else {
            throw new IllegalArgumentException("Invalid contract data");
        }
    }

    @Override
    public Contract addRevision(String name, ContractRevision revision) {

        if (isRevisionValid(revision)) {
            Contract contract = findByName(name);

            Long lastRevisionNumber = contract.getLastRevisionNumber().orElse(0L);
            revision.setRevisionNumber(lastRevisionNumber + 1L);
            revision.setContract(contract);

            contract.addRevision(revision);

            return contractRepository.save(contract);
        }
        else {
            throw new IllegalArgumentException("Revision has invalid constraints");
        }
    }

    @Override
    public Page<Contract> getBaseObjects(Pageable pageable) {

        return contractRepository.findAllNotDeleted(pageable);
    }

    @Override
    public Contract getBaseObjectWithRevision(String name, long revisionNumber) {

        Contract contract = contractRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Contract", (Object) name));

        ContractRevision contractRevision = contractRevisionRepository.findByBase_NameAndRevisionNumber(name, revisionNumber)
                .orElseThrow(() -> new ObjectNotFoundException("ContractRevision", revisionNumber));

        contract.setRevisions(Set.of(contractRevision));
        return contract;
    }

    @Override
    public ContractRevision getBaseObjectRevision(String name, long revisionNumber) {

        return contractRevisionRepository.findByBase_NameAndRevisionNumber(name, revisionNumber)
                .orElseThrow(() -> new ObjectNotFoundException("ContractRevision", revisionNumber));
    }

    @Override
    @Transactional
    public void deleteBaseObject(String name) {

        Contract contract = contractRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Contract", (Object) name));

        contract.softDelete();
    }

    @Override
    public Contract deleteBaseObjectRevision(String name, long revisionNumber) {

        Contract contract = contractRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Contract", (Object) name));

        ContractRevision contractRevision = contractRevisionRepository.findByBase_NameAndRevisionNumber(name, revisionNumber)
                .orElseThrow(() -> new ObjectNotFoundException("ContractRevision", revisionNumber));

        if (contract.getRevisions().size() <= 1) {
            throw new IllegalStateException("Cannot delete last revision of Contract#" + name);
        }

        contractRevision.softDelete();

        return contract;
    }

    private boolean isRevisionValid(ContractRevision contractRevision) {

        if (contractRevision.getContractMinPowerConstraints() != null) {
            if (!contractRevision.getContractMinPowerConstraints().stream().allMatch(this::isConstraintValid)) {
                return false;
            }
        }
        if (contractRevision.getContractMaxPowerConstraints() != null) {
            if (!contractRevision.getContractMaxPowerConstraints().stream().allMatch(this::isConstraintValid)) {
                return false;
            }
        }
        if (contractRevision.getContractMinEnergyConstraints() != null) {
            if (!contractRevision.getContractMinEnergyConstraints().stream().allMatch(this::isConstraintValid)) {
                return false;
            }
        }
        if (contractRevision.getContractMaxEnergyConstraints() != null) {
            return contractRevision.getContractMaxEnergyConstraints().stream().allMatch(this::isConstraintValid);
        }
        return true;
    }

    private boolean isConstraintValid(ContractConstraint constraint) {

        return constraint.getDateTimeStart().isBefore(constraint.getDateTimeEnd()) && constraint.getConstraintValue() >= 0.0;
    }
}
