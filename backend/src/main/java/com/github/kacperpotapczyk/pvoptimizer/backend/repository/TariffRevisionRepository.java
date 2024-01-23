package com.github.kacperpotapczyk.pvoptimizer.backend.repository;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.TariffRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TariffRevisionRepository extends JpaRepository<TariffRevision, Long> {

    @Query("select r from TariffRevision r " +
            "where r.tariff.name = :name and r.tariff.isDeleted = false " +
            "and r.revisionNumber = :revisionNumber and r.isDeleted = false")
    Optional<TariffRevision> findByBase_NameAndRevisionNumber(String name, long revisionNumber);
}
