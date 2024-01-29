package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.production.Production;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.production.ProductionRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.repository.ProductionRepository;
import com.github.kacperpotapczyk.pvoptimizer.backend.repository.ProductionRevisionRepository;
import jakarta.transaction.Transactional;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProductionServiceImpl implements ProductionService {

    private final ProductionRepository productionRepository;
    private final ProductionRevisionRepository productionRevisionRepository;

    @Autowired
    public ProductionServiceImpl(ProductionRepository productionRepository, ProductionRevisionRepository productionRevisionRepository) {
        this.productionRepository = productionRepository;
        this.productionRevisionRepository = productionRevisionRepository;
    }

    @Override
    public boolean existsById(long id) {
        return productionRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return productionRepository.existsByName(name);
    }

    @Override
    public Production findByName(String name) {
        return productionRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Production", (Object) name));
    }

    @Override
    @Transactional
    public Production newBaseObject(Production production) {

        if (!existsByName(production.getName()) && production.getRevisions().size()==1) {
            production.setId(0L);
            ProductionRevision productionRevision = production.getRevisions().stream().findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Base object must have one revision"));
            productionRevision.setRevisionNumber(1L);
            productionRevision.setProduction(production);
            productionRevision.getProductionValues().forEach(demandValue -> demandValue.setProductionRevision(productionRevision));

            production.setRevisions(Set.of(productionRevision));

            return productionRepository.save(production);
        }
        else {
            throw new IllegalArgumentException("Production with name: " + production.getName() + " already exists in DB");
        }
    }

    @Override
    @Transactional
    public Production addRevision(String name, ProductionRevision revision) {

        Production production = findByName(name);
        Long lastRevisionNumber = production.getLastRevisionNumber().orElse(0L);
        revision.setRevisionNumber(lastRevisionNumber + 1L);
        revision.setProduction(production);
        revision.getProductionValues().forEach(demandValue -> demandValue.setProductionRevision(revision));

        production.addRevision(revision);

        return productionRepository.save(production);
    }

    @Override
    public Page<Production> getBaseObjects(Pageable pageable) {

        return productionRepository.findAllNotDeleted(pageable);
    }

    @Override
    public Production getBaseObjectWithRevision(String name, long revisionNumber) {

        Production production = productionRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Production", (Object) name));

        ProductionRevision productionRevision = productionRevisionRepository.findByBase_NameAndRevisionNumber(name, revisionNumber)
                .orElseThrow(() -> new ObjectNotFoundException("ProductionRevision", revisionNumber));

        production.setRevisions(Set.of(productionRevision));
        return production;
    }

    @Override
    public ProductionRevision getBaseObjectRevision(String name, long revisionNumber) {

        return productionRevisionRepository.findByBase_NameAndRevisionNumber(name, revisionNumber)
                .orElseThrow(() -> new ObjectNotFoundException("ProductionRevision", revisionNumber));
    }

    @Override
    @Transactional
    public void deleteBaseObject(String name) {

        Production production = productionRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Production", (Object) name));
        production.softDelete();

        productionRepository.save(production);
    }

    @Override
    @Transactional
    public Production deleteBaseObjectRevision(String name, long revisionNumber) {

        Production production = productionRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Production", (Object) name));

        ProductionRevision productionRevision = productionRevisionRepository.findByBase_NameAndRevisionNumber(name, revisionNumber)
                .orElseThrow(() -> new ObjectNotFoundException("ProductionRevision", revisionNumber));

        if (production.getRevisions().size() <= 1) {
            throw new IllegalStateException("Cannot delete last revision of Production#" + name);
        }

        productionRevision.softDelete();
        productionRevisionRepository.save(productionRevision);

        return production;
    }
}
