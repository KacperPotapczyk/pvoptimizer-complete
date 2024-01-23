package com.github.kacperpotapczyk.pvoptimizer.backend.repository;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ContractRevisionRepository extends JpaRepository<ContractRevision, Long> {

    @Query("select r from ContractRevision r " +
            "where r.contract.name = :name and r.contract.isDeleted = false " +
            "and r.revisionNumber = :revisionNumber and r.isDeleted = false")
    Optional<ContractRevision> findByBase_NameAndRevisionNumber(String name, long revisionNumber);
}
