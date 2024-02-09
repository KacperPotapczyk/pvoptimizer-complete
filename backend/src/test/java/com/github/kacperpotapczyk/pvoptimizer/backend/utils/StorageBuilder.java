package com.github.kacperpotapczyk.pvoptimizer.backend.utils;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StorageBuilder {

    private final String name;
    private final double capacity;
    private final double maxCharge;
    private final double maxDischarge;
    private final double initialEnergy;

    private final List<StorageMinChargeConstraint> storageMinChargeConstraints;
    private final List<StorageMaxChargeConstraint> storageMaxChargeConstraints;
    private final List<StorageMinDischargeConstraint> storageMinDischargeConstraints;
    private final List<StorageMaxDischargeConstraint> storageMaxDischargeConstraints;
    private final List<StorageMinEnergyConstraint> storageMinEnergyConstraints;
    private final List<StorageMaxEnergyConstraint> storageMaxEnergyConstraints;

    public StorageBuilder(String name, double capacity, double maxCharge, double maxDischarge, double initialEnergy) {
        this.name = name;
        this.capacity = capacity;
        this.maxCharge = maxCharge;
        this.maxDischarge = maxDischarge;
        this.initialEnergy = initialEnergy;

        this.storageMinChargeConstraints = new ArrayList<>();
        this.storageMaxChargeConstraints = new ArrayList<>();
        this.storageMinDischargeConstraints = new ArrayList<>();
        this.storageMaxDischargeConstraints = new ArrayList<>();
        this.storageMinEnergyConstraints = new ArrayList<>();
        this.storageMaxEnergyConstraints = new ArrayList<>();
    }

    public StorageBuilder addMinChargeConstraint(LocalDateTime startDate, LocalDateTime endDate, double value) {

        StorageMinChargeConstraint storageMinChargeConstraint = new StorageMinChargeConstraint();
        storageMinChargeConstraint.setDateTimeStart(startDate);
        storageMinChargeConstraint.setDateTimeEnd(endDate);
        storageMinChargeConstraint.setConstraintValue(value);

        this.storageMinChargeConstraints.add(storageMinChargeConstraint);

        return this;
    }

    public StorageBuilder addMaxChargeConstraint(LocalDateTime startDate, LocalDateTime endDate, double value) {

        StorageMaxChargeConstraint storageMaxChargeConstraint = new StorageMaxChargeConstraint();
        storageMaxChargeConstraint.setDateTimeStart(startDate);
        storageMaxChargeConstraint.setDateTimeEnd(endDate);
        storageMaxChargeConstraint.setConstraintValue(value);

        this.storageMaxChargeConstraints.add(storageMaxChargeConstraint);

        return this;
    }

    public StorageBuilder addMinDischargeConstraint(LocalDateTime startDate, LocalDateTime endDate, double value) {

        StorageMinDischargeConstraint storageMinDischargeConstraint = new StorageMinDischargeConstraint();
        storageMinDischargeConstraint.setDateTimeStart(startDate);
        storageMinDischargeConstraint.setDateTimeEnd(endDate);
        storageMinDischargeConstraint.setConstraintValue(value);

        this.storageMinDischargeConstraints.add(storageMinDischargeConstraint);

        return this;
    }

    public StorageBuilder addMaxDischargeConstraint(LocalDateTime startDate, LocalDateTime endDate, double value) {

        StorageMaxDischargeConstraint storageMaxDischargeConstraint = new StorageMaxDischargeConstraint();
        storageMaxDischargeConstraint.setDateTimeStart(startDate);
        storageMaxDischargeConstraint.setDateTimeEnd(endDate);
        storageMaxDischargeConstraint.setConstraintValue(value);

        this.storageMaxDischargeConstraints.add(storageMaxDischargeConstraint);

        return this;
    }

    public StorageBuilder addMinEnergyConstraint(LocalDateTime startDate, LocalDateTime endDate, double value) {

        StorageMinEnergyConstraint storageMinEnergyConstraint = new StorageMinEnergyConstraint();
        storageMinEnergyConstraint.setDateTimeStart(startDate);
        storageMinEnergyConstraint.setDateTimeEnd(endDate);
        storageMinEnergyConstraint.setConstraintValue(value);

        this.storageMinEnergyConstraints.add(storageMinEnergyConstraint);

        return this;
    }

    public StorageBuilder addMaxEnergyConstraint(LocalDateTime startDate, LocalDateTime endDate, double value) {

        StorageMaxEnergyConstraint storageMaxEnergyConstraint = new StorageMaxEnergyConstraint();
        storageMaxEnergyConstraint.setDateTimeStart(startDate);
        storageMaxEnergyConstraint.setDateTimeEnd(endDate);
        storageMaxEnergyConstraint.setConstraintValue(value);

        this.storageMaxEnergyConstraints.add(storageMaxEnergyConstraint);

        return this;
    }

    public Storage build() {

        Storage storage = new Storage();
        storage.setName(this.name);
        storage.setCapacity(this.capacity);
        storage.setMaxCharge(this.maxCharge);
        storage.setMaxDischarge(this.maxDischarge);

        StorageRevision storageRevision = new StorageRevision(
                storage,
                this.initialEnergy,
                this.storageMinChargeConstraints,
                this.storageMaxChargeConstraints,
                this.storageMinDischargeConstraints,
                this.storageMaxDischargeConstraints,
                this.storageMinEnergyConstraints,
                this.storageMaxEnergyConstraints
        );

        storage.addRevision(storageRevision);

        return storage;
    }
}
