package com.opyruso.coh.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "capacity_type")
public class CapacityType extends PanacheEntityBase {

    @Id
    @Column(name = "id_capacity_type")
    public String idCapacityType;

    @OneToMany(mappedBy = "capacityType", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    public List<CapacityTypeDetails> details;
}
