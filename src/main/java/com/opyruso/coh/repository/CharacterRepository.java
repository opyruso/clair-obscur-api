package com.opyruso.coh.repository;

import com.opyruso.coh.entity.Character;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CharacterRepository implements PanacheRepositoryBase<Character, String> {
}
