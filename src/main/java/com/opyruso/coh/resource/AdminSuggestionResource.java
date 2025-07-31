package com.opyruso.coh.resource;

import com.opyruso.coh.entity.Suggestion;
import com.opyruso.coh.repository.SuggestionRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/admin/suggestions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminSuggestionResource {

    @Inject
    SuggestionRepository repository;

    @GET
    @RolesAllowed("admin")
    public Response list() {
        List<Suggestion> suggestions = repository.listAll();
        return Response.ok(suggestions).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed("admin")
    @Transactional
    public Response delete(@PathParam("id") String id) {
        Suggestion suggestion = repository.findById(id);
        if (suggestion == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        repository.delete(suggestion);
        return Response.noContent().build();
    }
}
