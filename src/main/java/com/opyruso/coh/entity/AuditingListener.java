package com.opyruso.coh.entity;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Instant;

public class AuditingListener {

    private SecurityIdentity getIdentity() {
        try {
            return CDI.current().select(SecurityIdentity.class).get();
        } catch (IllegalStateException e) {
            return null;
        }
    }

    @PrePersist
    public void prePersist(AuditableEntity entity) {
        Instant now = Instant.now();
        entity.creationDate = now;
        entity.lastUpdateDate = now;
        SecurityIdentity id = getIdentity();
        if (id != null && !id.isAnonymous()) {
            String user = id.getPrincipal().getName();
            entity.creationUser = user;
            entity.lastUpdateUser = user;
        }
    }

    @PreUpdate
    public void preUpdate(AuditableEntity entity) {
        entity.lastUpdateDate = Instant.now();
        SecurityIdentity id = getIdentity();
        if (id != null && !id.isAnonymous()) {
            entity.lastUpdateUser = id.getPrincipal().getName();
        }
    }
}
