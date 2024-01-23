package com.github.kacperpotapczyk.pvoptimizer.backend.utils;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.*;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.Tariff;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ContractBuilder {

    private final String name;
    private final Tariff tariff;
    private final ContractType contractType;
    private List<ContractMinPowerConstraint> contractMinPowerConstraints;
    private List<ContractMaxPowerConstraint> contractMaxPowerConstraints;
    private List<ContractMinEnergyConstraint> contractMinEnergyConstraints;
    private List<ContractMaxEnergyConstraint> contractMaxEnergyConstraints;

    public ContractBuilder(String name, Tariff tariff, ContractType contractType) {
        this.name = name;
        this.tariff = tariff;
        this.contractType = contractType;

        this.contractMinPowerConstraints = new ArrayList<>();
        this.contractMaxPowerConstraints = new ArrayList<>();
        this.contractMinEnergyConstraints = new ArrayList<>();
        this.contractMaxEnergyConstraints = new ArrayList<>();
    }

    public ContractBuilder addContractMinPowerConstraint(LocalDateTime startDate, LocalDateTime endDate, double value) {

        if (this.contractMinPowerConstraints == null) {
            this.contractMinPowerConstraints = new ArrayList<>();
        }

        ContractMinPowerConstraint contractMinPowerConstraint = new ContractMinPowerConstraint();
        contractMinPowerConstraint.setDateTimeStart(startDate);
        contractMinPowerConstraint.setDateTimeEnd(endDate);
        contractMinPowerConstraint.setConstraintValue(value);

        this.contractMinPowerConstraints.add(contractMinPowerConstraint);

        return this;
    }

    public ContractBuilder addContractMaxPowerConstraint(LocalDateTime startDate, LocalDateTime endDate, double value) {

        if (this.contractMaxPowerConstraints == null) {
            this.contractMaxPowerConstraints = new ArrayList<>();
        }

        ContractMaxPowerConstraint contractMaxPowerConstraint = new ContractMaxPowerConstraint();
        contractMaxPowerConstraint.setDateTimeStart(startDate);
        contractMaxPowerConstraint.setDateTimeEnd(endDate);
        contractMaxPowerConstraint.setConstraintValue(value);

        this.contractMaxPowerConstraints.add(contractMaxPowerConstraint);

        return this;
    }

    public ContractBuilder addContractMinEnergyConstraint(LocalDateTime startDate, LocalDateTime endDate, double value) {

        if (this.contractMinEnergyConstraints == null) {
            this.contractMinEnergyConstraints = new ArrayList<>();
        }

        ContractMinEnergyConstraint contractMinEnergyConstraint = new ContractMinEnergyConstraint();
        contractMinEnergyConstraint.setDateTimeStart(startDate);
        contractMinEnergyConstraint.setDateTimeEnd(endDate);
        contractMinEnergyConstraint.setConstraintValue(value);

        this.contractMinEnergyConstraints.add(contractMinEnergyConstraint);

        return this;
    }

    public ContractBuilder addContractMaxEnergyConstraint(LocalDateTime startDate, LocalDateTime endDate, double value) {

        if (this.contractMaxEnergyConstraints == null) {
            this.contractMaxEnergyConstraints = new ArrayList<>();
        }

        ContractMaxEnergyConstraint contractMaxEnergyConstraint = new ContractMaxEnergyConstraint();
        contractMaxEnergyConstraint.setDateTimeStart(startDate);
        contractMaxEnergyConstraint.setDateTimeEnd(endDate);
        contractMaxEnergyConstraint.setConstraintValue(value);

        this.contractMaxEnergyConstraints.add(contractMaxEnergyConstraint);

        return this;
    }

    public Contract build() {

        Contract contract = new Contract();
        contract.setName(this.name);
        contract.setTariff(this.tariff);
        contract.setContractType(this.contractType);

        ContractRevision contractRevision = new ContractRevision(
                contract,
                this.contractMinPowerConstraints,
                this.contractMaxPowerConstraints,
                this.contractMinEnergyConstraints,
                this.contractMaxEnergyConstraints
        );

        contract.addRevision(contractRevision);

        return contract;
    }

}
