package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.constraint.TimeWindowConstraint;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage.Storage;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage.StorageRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.repository.StorageRepository;
import com.github.kacperpotapczyk.pvoptimizer.backend.repository.StorageRevisionRepository;
import jakarta.transaction.Transactional;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class StorageServiceImpl implements StorageService {

    private final StorageRepository storageRepository;
    private final StorageRevisionRepository storageRevisionRepository;

    @Autowired
    public StorageServiceImpl(StorageRepository storageRepository, StorageRevisionRepository storageRevisionRepository) {
        this.storageRepository = storageRepository;
        this.storageRevisionRepository = storageRevisionRepository;
    }

    @Override
    public boolean existsById(long id) {

        return storageRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {

        return storageRepository.existsByName(name);
    }

    @Override
    public Storage findByName(String name) {

        return storageRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Storage", (Object) name));
    }

    @Override
    @Transactional
    public Storage newBaseObject(Storage storage) {

        if (!existsByName(storage.getName()) && storage.getRevisions().size()==1) {
            storage.setId(0L);

            StorageRevision storageRevision = storage.getRevisions().stream().findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Base object must have one revision"));

            if (isRevisionValid(storageRevision)) {
                storageRevision.setRevisionNumber(1L);
                addConstraintsToRevision(storageRevision);
                storage.addRevision(storageRevision);
                return storageRepository.save(storage);
            }
            else {
                throw new IllegalArgumentException("Revision has invalid constraints");
            }
        }
        else {
            throw new IllegalArgumentException("Invalid storage data");
        }
    }

    @Override
    public Storage addRevision(String name, StorageRevision storageRevision) {

        if (isRevisionValid(storageRevision)) {
            Storage storage = findByName(name);

            Long lastRevisionNumber = storage.getLastRevisionNumber().orElse(0L);
            storageRevision.setRevisionNumber(lastRevisionNumber + 1L);
            addConstraintsToRevision(storageRevision);
            storage.addRevision(storageRevision);
            return storageRepository.save(storage);
        }
        else {
            throw new IllegalArgumentException("Revision has invalid constraints");
        }
    }

    @Override
    public Page<Storage> getBaseObjects(Pageable pageable) {
        return storageRepository.findAllNotDeleted(pageable);
    }

    @Override
    public Storage getBaseObjectWithRevision(String name, long revisionNumber) {

        Storage storage = findByName(name);
        storage.setRevisions(Set.of(getBaseObjectRevision(name, revisionNumber)));
        return storage;
    }

    @Override
    public StorageRevision getBaseObjectRevision(String name, long revisionNumber) {
        return storageRevisionRepository.findByBase_NameAndRevisionNumber(name, revisionNumber)
                .orElseThrow(() -> new ObjectNotFoundException("StorageRevision", revisionNumber));
    }

    @Override
    @Transactional
    public void deleteBaseObject(String name) {

        Storage storage = findByName(name);
        storage.softDelete();
        storageRepository.save(storage);
    }

    @Override
    @Transactional
    public Storage deleteBaseObjectRevision(String name, long revisionNumber) {

        Storage storage = findByName(name);
        StorageRevision storageRevision = getBaseObjectRevision(name, revisionNumber);

        if (storage.getRevisions().size() <= 1) {
            throw new IllegalStateException("Cannot delete last revision of Storage#" + name);
        }

        storageRevision.softDelete();
        storageRevisionRepository.save(storageRevision);

        return storage;
    }

    private boolean isRevisionValid(StorageRevision storageRevision) {

        if (storageRevision.getStorageMinChargeConstraints() != null) {
            if (!storageRevision.getStorageMinChargeConstraints().stream().allMatch(this::isConstraintValid)) {
                return false;
            }
        }
        if (storageRevision.getStorageMaxChargeConstraints() != null) {
            if (!storageRevision.getStorageMaxChargeConstraints().stream().allMatch(this::isConstraintValid)) {
                return false;
            }
        }
        if (storageRevision.getStorageMinDischargeConstraints() != null) {
            if (!storageRevision.getStorageMinDischargeConstraints().stream().allMatch(this::isConstraintValid)) {
                return false;
            }
        }
        if (storageRevision.getStorageMaxDischargeConstraints() != null) {
            if (!storageRevision.getStorageMaxDischargeConstraints().stream().allMatch(this::isConstraintValid)) {
                return false;
            }
        }
        if (storageRevision.getStorageMinEnergyConstraints() != null) {
            if (!storageRevision.getStorageMinEnergyConstraints().stream().allMatch(this::isConstraintValid)) {
                return false;
            }
        }
        if (storageRevision.getStorageMaxEnergyConstraints() != null) {
            return storageRevision.getStorageMaxEnergyConstraints().stream().allMatch(this::isConstraintValid);
        }
        return true;
    }

    private boolean isConstraintValid(TimeWindowConstraint constraint) {

        return constraint.getDateTimeStart().isBefore(constraint.getDateTimeEnd()) && constraint.getConstraintValue() >= 0.0;
    }

    private void addConstraintsToRevision(StorageRevision storageRevision) {

        if (storageRevision.getStorageMinChargeConstraints() != null) {
            storageRevision.getStorageMinChargeConstraints().forEach(constraint -> constraint.setStorageRevision(storageRevision));
        }
        if (storageRevision.getStorageMaxChargeConstraints() != null) {
            storageRevision.getStorageMaxChargeConstraints().forEach(constraint -> constraint.setStorageRevision(storageRevision));
        }
        if (storageRevision.getStorageMinDischargeConstraints() != null) {
            storageRevision.getStorageMinDischargeConstraints().forEach(constraint -> constraint.setStorageRevision(storageRevision));
        }
        if (storageRevision.getStorageMaxDischargeConstraints() != null) {
            storageRevision.getStorageMaxDischargeConstraints().forEach(constraint -> constraint.setStorageRevision(storageRevision));
        }
        if (storageRevision.getStorageMinEnergyConstraints() != null) {
            storageRevision.getStorageMinEnergyConstraints().forEach(constraint -> constraint.setStorageRevision(storageRevision));
        }
        if (storageRevision.getStorageMaxEnergyConstraints() != null) {
            storageRevision.getStorageMaxEnergyConstraints().forEach(constraint -> constraint.setStorageRevision(storageRevision));
        }
    }
}
