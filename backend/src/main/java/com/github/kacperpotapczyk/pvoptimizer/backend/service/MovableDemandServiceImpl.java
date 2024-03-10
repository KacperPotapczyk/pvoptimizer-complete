package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand.MovableDemand;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand.MovableDemandRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.repository.MovableDemandRepository;
import com.github.kacperpotapczyk.pvoptimizer.backend.repository.MovableDemandRevisionRepository;
import jakarta.transaction.Transactional;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MovableDemandServiceImpl implements MovableDemandService {

    private final MovableDemandRepository movableDemandRepository;
    private final MovableDemandRevisionRepository movableDemandRevisionRepository;

    @Autowired
    public MovableDemandServiceImpl(MovableDemandRepository movableDemandRepository, MovableDemandRevisionRepository movableDemandRevisionRepository) {
        this.movableDemandRepository = movableDemandRepository;
        this.movableDemandRevisionRepository = movableDemandRevisionRepository;
    }

    @Override
    public boolean existsById(long id) {

        return movableDemandRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {

        return movableDemandRepository.existsByName(name);
    }

    @Override
    public MovableDemand findByName(String name) {

        return movableDemandRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("MovableDemand", (Object) name));
    }

    @Override
    @Transactional
    public MovableDemand newBaseObject(MovableDemand movableDemand) {

        if (!existsByName(movableDemand.getName()) && movableDemand.getRevisions().size()==1) {

            movableDemand.setId(0L);
            MovableDemandRevision movableDemandRevision = movableDemand.getRevisions().stream().findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Base object must have one revision"));

            movableDemandRevision.setRevisionNumber(1L);
            movableDemand.addRevision(movableDemandRevision);

            movableDemandRevision.getMovableDemandStarts().forEach(movableDemandStart -> movableDemandStart.setMovableDemandRevision(movableDemandRevision));
            movableDemandRevision.getMovableDemandValues().forEach(movableDemandValue -> movableDemandValue.setMovableDemandRevision(movableDemandRevision));

            return movableDemandRepository.save(movableDemand);
        } else {
            throw new IllegalArgumentException("Invalid Movable demand data");
        }
    }

    @Override
    public MovableDemand addRevision(String name, MovableDemandRevision revision) {

        MovableDemand movableDemand = findByName(name);
        Long lastRevisionNumber = movableDemand.getLastRevisionNumber().orElse(0L);

        revision.setRevisionNumber(lastRevisionNumber + 1L);
        revision.getMovableDemandStarts().forEach(movableDemandStart -> movableDemandStart.setMovableDemandRevision(revision));
        revision.getMovableDemandValues().forEach(movableDemandValue -> movableDemandValue.setMovableDemandRevision(revision));

        movableDemand.addRevision(revision);

        return movableDemandRepository.save(movableDemand);
    }

    @Override
    public Page<MovableDemand> getBaseObjects(Pageable pageable) {

        return movableDemandRepository.findAllNotDeleted(pageable);
    }

    @Override
    public MovableDemand getBaseObjectWithRevision(String name, long revisionNumber) {

        MovableDemand movableDemand = findByName(name);
        MovableDemandRevision movableDemandRevision = movableDemandRevisionRepository.findByBase_NameAndRevisionNumber(name, revisionNumber)
                .orElseThrow(() -> new ObjectNotFoundException("MovableDemandRevision", revisionNumber));

        movableDemand.setRevisions(Set.of(movableDemandRevision));
        return movableDemand;
    }

    @Override
    public MovableDemandRevision getBaseObjectRevision(String name, long revisionNumber) {

        return movableDemandRevisionRepository.findByBase_NameAndRevisionNumber(name, revisionNumber)
                .orElseThrow(() -> new ObjectNotFoundException("MovableDemandRevision", revisionNumber));
    }

    @Override
    @Transactional
    public void deleteBaseObject(String name) {

        MovableDemand movableDemand = findByName(name);
        movableDemand.softDelete();
        movableDemandRepository.save(movableDemand);
    }

    @Override
    @Transactional
    public MovableDemand deleteBaseObjectRevision(String name, long revisionNumber) {

        MovableDemand movableDemand = findByName(name);
        if (movableDemand.getRevisions().size() <= 1) {
            throw new IllegalStateException("Cannot delete last revision of MovableDemand#" + name);
        }

        MovableDemandRevision movableDemandRevision = getBaseObjectRevision(name, revisionNumber);
        movableDemandRevision.softDelete();
        movableDemandRevisionRepository.save(movableDemandRevision);

        return movableDemand;
    }
}
