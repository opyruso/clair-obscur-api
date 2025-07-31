package com.opyruso.coh.resource;

import com.opyruso.coh.entity.Suggestion;
import com.opyruso.coh.repository.SuggestionRepository;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.UUID;

@Path("/suggestions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class SuggestionResource {

    public static class SuggestionPayload {
        public String type;
        public String title;
        public String description;
    }

    @Inject
    SuggestionRepository repository;

    @Inject
    SecurityIdentity identity;

    private String getUserId() {
        return identity.getPrincipal().getName();
    }

    private String generateKey() {
        return UUID.randomUUID().toString();
    }

    @POST
    @Transactional
    public Response create(SuggestionPayload payload) {
        Suggestion suggestion = new Suggestion();
        suggestion.idSuggestion = generateKey();
        suggestion.userId = getUserId();
        suggestion.type = payload.type;
        suggestion.title = payload.title;
        suggestion.description = payload.description;
        repository.persist(suggestion);
        return Response.status(Response.Status.CREATED)
                .entity(Map.of("id", suggestion.idSuggestion))
                .build();
    }
}
