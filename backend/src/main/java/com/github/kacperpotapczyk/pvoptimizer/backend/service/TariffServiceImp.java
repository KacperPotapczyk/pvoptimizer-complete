package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.cyclicalvalue.CyclicalDailyValue;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.Tariff;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.TariffRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.repository.TariffRepository;
import com.github.kacperpotapczyk.pvoptimizer.backend.repository.TariffRevisionRepository;
import jakarta.transaction.Transactional;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TariffServiceImp implements TariffService {

    private final TariffRepository tariffRepository;

    private final TariffRevisionRepository tariffRevisionRepository;

    @Autowired
    public TariffServiceImp(TariffRepository tariffRepository, TariffRevisionRepository tariffRevisionRepository) {
        this.tariffRepository = tariffRepository;
        this.tariffRevisionRepository = tariffRevisionRepository;
    }

    @Override
    public boolean existsById(long id) {
        return tariffRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return tariffRepository.existsByName(name);
    }

    @Override
    public Tariff findByName(String name) {
        return tariffRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Tariff", (Object) name));
    }

    @Override
    @Transactional
    public Tariff newBaseObject(Tariff tariff) {

        if (!existsByName(tariff.getName()) && tariff.getRevisions().size()==1) {
            tariff.setId(0L);
            TariffRevision tariffRevision = tariff.getRevisions().stream().findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Base object must have one revision"));
            tariffRevision.setRevisionNumber(1L);
            tariffRevision.setTariff(tariff);
            addCyclicalDailyValuesToTariffRevision(tariffRevision);

            tariff.setRevisions(Set.of(tariffRevision));

            return tariffRepository.save(tariff);
        }
        else {
            throw new IllegalArgumentException("Tariff with name: " + tariff.getName() + " already exists in DB");
        }
    }

    @Override
    @Transactional
    public Tariff addRevision(String name, TariffRevision revision) {

        Tariff tariff = findByName(name);

        Long lastRevisionNumber = tariff.getLastRevisionNumber().orElse(0L);
        revision.setRevisionNumber(lastRevisionNumber + 1L);
        revision.setTariff(tariff);
        addCyclicalDailyValuesToTariffRevision(revision);

        tariff.addRevision(revision);

        return tariffRepository.save(tariff);
    }

    @Override
    public Page<Tariff> getBaseObjects(Pageable pageable) {
        return tariffRepository.findAllNotDeleted(pageable);
    }

    @Override
    public Tariff getBaseObjectWithRevision(String name, long revisionNumber) {

        Tariff tariff = tariffRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Tariff", (Object) name));

        TariffRevision tariffRevision = tariffRevisionRepository.findByBase_NameAndRevisionNumber(name, revisionNumber)
                .orElseThrow(() -> new ObjectNotFoundException("TariffRevision", revisionNumber));

        tariff.setRevisions(Set.of(tariffRevision));
        return tariff;
    }

    @Override
    public TariffRevision getBaseObjectRevision(String name, long revisionNumber) {

        return tariffRevisionRepository.findByBase_NameAndRevisionNumber(name, revisionNumber)
                .orElseThrow(() -> new ObjectNotFoundException("TariffRevision", revisionNumber));
    }

    @Override
    @Transactional
    public void deleteBaseObject(String name) {

        Tariff tariff = tariffRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Tariff", (Object) name));
        tariff.softDelete();
    }

    @Override
    public Tariff deleteBaseObjectRevision(String name, long revisionNumber) {

        Tariff tariff = tariffRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Tariff", (Object) name));

        TariffRevision tariffRevision = tariffRevisionRepository.findByBase_NameAndRevisionNumber(name, revisionNumber)
                .orElseThrow(() -> new ObjectNotFoundException("TariffRevision", revisionNumber));

        if (tariff.getRevisions().size() <= 1) {
            throw new IllegalStateException("Cannot delete last revision of Tariff#" + name);
        }

        tariffRevision.softDelete();

        return tariff;
    }

    private void addCyclicalDailyValuesToTariffRevision(TariffRevision tariffRevision) {

        if (tariffRevision.getCyclicalDailyValues() != null) {
            tariffRevision.getCyclicalDailyValues().forEach(cyclicalDailyValue -> {
                cyclicalDailyValue.setTariffRevision(tariffRevision);
                addDailyTimeValueToCyclicalDailyValue(cyclicalDailyValue);
            });

        }
    }

    private void addDailyTimeValueToCyclicalDailyValue(CyclicalDailyValue cyclicalDailyValue) {

        if (cyclicalDailyValue.getDailyTimeValues() != null) {
            cyclicalDailyValue.getDailyTimeValues().forEach(dailyTimeValue -> dailyTimeValue.setCyclicalDailyValues(cyclicalDailyValue));
        } else {
            throw new IllegalArgumentException("CyclicalDailyValue must have at least one dailyTimeValue");
        }
    }
}
