package com.opyruso.coh.repository;

import com.opyruso.coh.entity.Suggestion;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SuggestionRepository implements PanacheRepositoryBase<Suggestion, String> {
}
