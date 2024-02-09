package com.github.kacperpotapczyk.pvoptimizer.backend.repository;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage.StorageRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StorageRevisionRepository extends JpaRepository<StorageRevision, Long> {

    @Query("select r from StorageRevision r " +
            "where r.storage.name = :name and r.storage.isDeleted = false " +
            "and r.revisionNumber = :revisionNumber and r.isDeleted = false")
    Optional<StorageRevision> findByBase_NameAndRevisionNumber(String name, long revisionNumber);
}
