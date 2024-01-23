package com.github.kacperpotapczyk.pvoptimizer.backend.repository;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.BaseObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@NoRepositoryBean
public interface BaseObjectRepository<T extends BaseObject<?>> extends JpaRepository<T, Long> {

    @Query("select d from #{#entityName} d where d.name = :name and d.isDeleted = FALSE")
    Optional<T> findByName(@Param("name") String name);

    @Query("select (count(d) > 0) from #{#entityName} d where d.name = :name and d.isDeleted = FALSE")
    boolean existsByName(@Param("name") String name);

    @Query("select d from #{#entityName} d where d.isDeleted = FALSE")
    Page<T> findAllNotDeleted(Pageable pageable);
}
