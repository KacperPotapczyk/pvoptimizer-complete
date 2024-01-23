package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.Demand;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.DemandRevision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DemandService extends BaseObjectService<Demand, DemandRevision> {}
