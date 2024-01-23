package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.BaseObject;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.Revision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaseObjectService<T extends BaseObject<R>, R extends Revision> {

    boolean existsById(long id);

    boolean existsByName(String name);

    T findByName(String name);

    T newBaseObject(T baseObject);

    T addRevision(String name, R revision);

    Page<T> getBaseObjects(Pageable pageable);

    T getBaseObjectWithRevision(String name, long revisionNumber);

    R getBaseObjectRevision(String name, long revisionNumber);

    void deleteBaseObject(String name);

    T deleteBaseObjectRevision(String name, long revisionNumber);
}
