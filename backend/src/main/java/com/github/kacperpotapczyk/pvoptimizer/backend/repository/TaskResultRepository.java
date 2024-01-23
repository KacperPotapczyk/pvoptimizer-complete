package com.github.kacperpotapczyk.pvoptimizer.backend.repository;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.TaskResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskResultRepository extends JpaRepository<TaskResult, Long> {

    @Query("select count(*) > 0 " +
            "from TaskResult r " +
            "where r.task.name = :taskName")
    boolean existsByTaskName(@Param("taskName")String taskName);

    @Query("select r " +
            "from TaskResult r " +
            "where r.task.name = :taskName")
    Optional<TaskResult> findByTaskName(@Param("taskName") String taskName);

    @Query("select r " +
            "from TaskResult r " +
            "where r.task.id = :taskId")
    Optional<TaskResult> findByTaskId(@Param("taskId") long taskId);
}
