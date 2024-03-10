package com.github.kacperpotapczyk.pvoptimizer.backend.repository;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand.MovableDemandRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MovableDemandRevisionRepository extends JpaRepository<MovableDemandRevision, Long> {

    @Query("select r from MovableDemandRevision r " +
            "where r.movableDemand.name = :name and r.movableDemand.isDeleted = false " +
            "and r.revisionNumber = :revisionNumber and r.isDeleted = false")
    Optional<MovableDemandRevision> findByBase_NameAndRevisionNumber(String name, long revisionNumber);
}
