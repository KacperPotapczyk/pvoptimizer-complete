package com.github.kacperpotapczyk.pvoptimizer.backend.repository;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    boolean existsByName(String name);

    Optional<Task> findByName(String name);

    boolean existsByNameAndReadOnly(String name, boolean readOnly);

    Optional<Task> findByNameAndReadOnly(String name, boolean readOnly);

    @Query("select t.id from Task t where t.name = :name")
    long findIdByName(@Param("name") String name);

    @Query("SELECT cr " +
            "FROM Task t " +
            "JOIN t.contractRevisions cr " +
            "JOIN cr.contract c " +
            "WHERE t.id = :taskId AND c.id = :contractId")
    ContractRevision getContractRevisionByTaskIdAndContractId(@Param("taskId") long taskId, @Param("contractId") long contractId);
}
