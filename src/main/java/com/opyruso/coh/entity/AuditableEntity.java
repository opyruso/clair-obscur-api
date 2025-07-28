package com.opyruso.coh.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingListener.class)
public abstract class AuditableEntity extends PanacheEntityBase {

    @Column(name = "creation_date")
    public Instant creationDate;

    @Column(name = "creation_user")
    public String creationUser;

    @Column(name = "last_update_date")
    public Instant lastUpdateDate;

    @Column(name = "last_update_user")
    public String lastUpdateUser;
}
