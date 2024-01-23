package com.github.kacperpotapczyk.pvoptimizer.backend.entity;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractRevision;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Revision implements SoftDeletable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "revision_number", nullable = false)
    private long revisionNumber;

    @Column(name = "created_date")
    @CreationTimestamp
    private Date createdDate;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted;

    public Revision(long revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Revision that)) return false;

        return getRevisionNumber() == that.getRevisionNumber();
    }

    @Override
    public int hashCode() {
        return (int) (getRevisionNumber() ^ (getRevisionNumber() >>> 32));
    }
}