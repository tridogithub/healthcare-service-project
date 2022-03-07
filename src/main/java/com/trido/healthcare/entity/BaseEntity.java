package com.trido.healthcare.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import com.trido.healthcare.config.auth.BearerContextHolder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private UUID id;


    @NotNull
    @Column(name = "created_by")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID createdBy;

    @NotNull
    @Column(name = "created_on")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Timestamp createdOn;

    @NotNull
    @Column(name = "updated_by")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID updatedBy;

    @NotNull
    @Column(name = "updated_on")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Timestamp updatedOn;

    @NotNull
    @Column(name = "is_deleted")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean isDeleted = false;


    @PrePersist
    protected void preparePersist() {
        String userId = BearerContextHolder.getContext().getUserId();
        UUID createdBy = userId == null ? UUID.randomUUID() : UUID.fromString(userId);
        this.createdBy = createdBy;
        updatedBy = createdBy;
        createdOn = Timestamp.from(Instant.now());
        updatedOn = Timestamp.from(Instant.now());
    }

    @PreUpdate
    protected void prepareUpdate() {
        String userId = BearerContextHolder.getContext().getUserId();
        updatedBy = userId == null ? UUID.randomUUID() : UUID.fromString(userId);
        updatedOn = Timestamp.from(Instant.now());
    }
}
