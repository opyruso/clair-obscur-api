package com.opyruso.coh.resource;

import com.opyruso.coh.entity.Character;
import com.opyruso.coh.repository.CharacterRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin/characters")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminCharacterResource {

    @Inject
    CharacterRepository repository;

    @POST
    @RolesAllowed("admin")
    @Transactional
    public Response create(Character character) {
        repository.persist(character);
        return Response.status(Response.Status.CREATED).entity(character).build();
    }

    @PUT
    @RolesAllowed("admin")
    @Transactional
    public Response update(Character character) {
        Character entity = repository.findById(character.idCharacter);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        entity.details.clear();
        if (character.details != null) {
            character.details.forEach(d -> d.idCharacter = character.idCharacter);
            entity.details.addAll(character.details);
        }
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed("admin")
    @Transactional
    public Response delete(@PathParam("id") String id) {
        boolean deleted = repository.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
