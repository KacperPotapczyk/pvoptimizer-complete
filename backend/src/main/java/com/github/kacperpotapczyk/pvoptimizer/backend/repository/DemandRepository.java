package com.github.kacperpotapczyk.pvoptimizer.backend.repository;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.Demand;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandRepository extends BaseObjectRepository<Demand> {}