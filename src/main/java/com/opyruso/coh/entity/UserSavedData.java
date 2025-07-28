package com.opyruso.coh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_saved_data")
public class UserSavedData extends AuditableEntity {

    @Id
    @Column(name = "user_id")
    public String userId;

    @Lob
    @Column(name = "data")
    public String data;
}
