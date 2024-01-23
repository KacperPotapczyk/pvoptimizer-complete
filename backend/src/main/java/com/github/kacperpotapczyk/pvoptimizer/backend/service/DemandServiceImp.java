package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.Demand;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.DemandRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.repository.DemandRepository;
import com.github.kacperpotapczyk.pvoptimizer.backend.repository.DemandRevisionRepository;
import jakarta.transaction.Transactional;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DemandServiceImp implements DemandService {

    private final DemandRepository demandRepository;
    private final DemandRevisionRepository demandRevisionRepository;

    @Autowired
    public DemandServiceImp(DemandRepository demandRepository, DemandRevisionRepository demandRevisionRepository) {
        this.demandRepository = demandRepository;
        this.demandRevisionRepository = demandRevisionRepository;
    }

    @Override
    public boolean existsById(long id) {

        return demandRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return demandRepository.existsByName(name);
    }

    @Override
    public Demand findByName(String name) {
        return demandRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Demand", (Object) name));
    }

    @Override
    @Transactional
    public Demand newBaseObject(Demand demand) {

        if (!existsByName(demand.getName()) && demand.getRevisions().size()==1) {
            demand.setId(0L);
            DemandRevision demandRevision = demand.getRevisions().stream().findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Base object must have one revision"));
            demandRevision.setRevisionNumber(1L);
            demandRevision.setDemand(demand);
            demandRevision.getDemandValues().forEach(demandValue -> demandValue.setDemandRevision(demandRevision));

            demand.setRevisions(Set.of(demandRevision));

            return demandRepository.save(demand);
        }
        else {
            throw new IllegalArgumentException("Demand with name: " + demand.getName() + " already exists in DB");
        }
    }

    @Override
    @Transactional
    public Demand addRevision(String name, DemandRevision revision) {

        Demand demand = findByName(name);
        Long lastRevisionNumber = demand.getLastRevisionNumber().orElse(0L);
        revision.setRevisionNumber(lastRevisionNumber + 1L);
        revision.setDemand(demand);
        revision.getDemandValues().forEach(demandValue -> demandValue.setDemandRevision(revision));

        demand.addRevision(revision);

        return demandRepository.save(demand);
    }

    @Override
    public Page<Demand> getBaseObjects(Pageable pageable) {

        return demandRepository.findAllNotDeleted(pageable);
    }

    @Override
    public Demand getBaseObjectWithRevision(String name, long revisionNumber) {

        Demand demand = demandRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Demand", (Object) name));

        DemandRevision demandRevision = demandRevisionRepository.findByBase_NameAndRevisionNumber(name, revisionNumber)
                .orElseThrow(() -> new ObjectNotFoundException("DemandRevision", revisionNumber));

        demand.setRevisions(Set.of(demandRevision));
        return demand;
    }

    @Override
    public DemandRevision getBaseObjectRevision(String name, long revisionNumber) {

        return demandRevisionRepository.findByBase_NameAndRevisionNumber(name, revisionNumber)
                .orElseThrow(() -> new ObjectNotFoundException("DemandRevision", revisionNumber));
    }

    @Override
    @Transactional
    public void deleteBaseObject(String name) {

        Demand demand = demandRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Demand", (Object) name));
        demand.softDelete();
    }

    @Override
    @Transactional
    public Demand deleteBaseObjectRevision(String name, long revisionNumber) {

        Demand demand = demandRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Demand", (Object) name));

        DemandRevision demandRevision = demandRevisionRepository.findByBase_NameAndRevisionNumber(name, revisionNumber)
                .orElseThrow(() -> new ObjectNotFoundException("DemandRevision", revisionNumber));

        if (demand.getRevisions().size() <= 1) {
            throw new IllegalStateException("Cannot delete last revision of Demand#" + name);
        }

        demandRevision.softDelete();

        return demand;
    }
}
