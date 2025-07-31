package com.opyruso.coh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "suggestions")
public class Suggestion extends AuditableEntity {

    @Id
    @Column(name = "id_suggestion")
    public String idSuggestion;

    @Column(name = "user_id")
    public String userId;

    @Column(name = "type")
    public String type;

    @Column(name = "title")
    public String title;

    @Lob
    @Column(name = "description")
    public String description;
}
