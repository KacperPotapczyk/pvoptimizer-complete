package com.github.kacperpotapczyk.pvoptimizer.backend.entity;

import java.util.Optional;
import java.util.Set;

public interface Revisable <T extends Revision>{

    Set<T> getRevisions();
    void addRevision(T revision);
    Optional<T> getRevision(long revisionNumber);
    Optional<Long> getLastRevisionNumber();
}
