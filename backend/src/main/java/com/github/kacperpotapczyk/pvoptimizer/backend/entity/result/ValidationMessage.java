package com.github.kacperpotapczyk.pvoptimizer.backend.entity.result;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "validation_message", indexes = {@Index(name = "idx_validation_message_result_id", columnList = "task_result_id")})
public class ValidationMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "task_result_id", nullable = false)
    private TaskResult taskResult;

    @Column(name = "level", nullable = false, columnDefinition = "int")
    @Enumerated(EnumType.ORDINAL)
    private ValidationMessageLevel level;

    @Column(name = "object_type", nullable = false, columnDefinition = "int")
    @Enumerated(EnumType.ORDINAL)
    private ValidationMessageObjectType objectType;

    @Column(name = "object_name", nullable = false)
    private String objectName;

    @Column(name = "object_id", nullable = false)
    private long objectId;

    @Column(name = "object_revision", nullable = false)
    private long objectRevision;

    @Column(name = "message")
    private String message;

    public ValidationMessage(
            ValidationMessageLevel level,
            ValidationMessageObjectType objectType,
            String objectName,
            long objectId,
            long objectRevision,
            String message
    ) {

        this.level = level;
        this.objectType = objectType;
        this.objectName = objectName;
        this.objectId = objectId;
        this.objectRevision = objectRevision;
        this.message = message;
    }
}