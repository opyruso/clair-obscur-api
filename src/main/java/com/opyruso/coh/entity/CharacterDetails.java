package com.opyruso.coh.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@Entity
@Table(name = "character_details")
@IdClass(CharacterDetails.PK.class)
public class CharacterDetails extends PanacheEntityBase {

    @Id
    @Column(name = "id_character")
    public String idCharacter;

    @Id
    @Column(name = "lang")
    public String lang;

    @Column(name = "name")
    public String name;

    @Column(name = "story")
    public String story;

    @ManyToOne
    @JoinColumn(name = "id_character", insertable = false, updatable = false)
    @JsonIgnore
    public Character character;

    public static class PK implements Serializable {
        public String idCharacter;
        public String lang;

        @Override
        public int hashCode() {
            return (idCharacter + "#" + lang).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            PK other = (PK) obj;
            return idCharacter.equals(other.idCharacter) && lang.equals(other.lang);
        }
    }
}
