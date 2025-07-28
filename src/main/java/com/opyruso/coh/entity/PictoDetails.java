package com.opyruso.coh.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@Entity
@Table(name = "picto_details")
@IdClass(PictoDetails.PK.class)
public class PictoDetails extends AuditableEntity {

    @Id
    @Column(name = "id_picto")
    public String idPicto;

    @Id
    @Column(name = "lang")
    public String lang;

    @Column(name = "region")
    public String region;

    @Column(name = "unlock_description")
    public String unlockDescription;

    @ManyToOne
    @JoinColumn(name = "id_picto", insertable = false, updatable = false)
    @JsonIgnore
    public Picto picto;

    public static class PK implements Serializable {
        public String idPicto;
        public String lang;

        @Override
        public int hashCode() {
            return (idPicto + "#" + lang).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            PK other = (PK) obj;
            return idPicto.equals(other.idPicto) && lang.equals(other.lang);
        }
    }
}
