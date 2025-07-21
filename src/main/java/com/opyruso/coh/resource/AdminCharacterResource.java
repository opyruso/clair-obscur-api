package com.opyruso.coh.resource;

import com.opyruso.coh.entity.Character;
import com.opyruso.coh.entity.CharacterDetails;
import com.opyruso.coh.repository.CharacterRepository;
import com.opyruso.coh.model.dto.CharacterWithDetails;
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
    public Response create(CharacterWithDetails payload) {
        Character character = new Character();
        character.idCharacter = payload.idCharacter;

        CharacterDetails details = new CharacterDetails();
        details.idCharacter = payload.idCharacter;
        details.lang = payload.lang;
        details.name = payload.name;
        details.story = payload.story;
        details.character = character;

        character.details = new java.util.ArrayList<>(java.util.List.of(details));

        repository.persist(character);
        return Response.status(Response.Status.CREATED).entity(character).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed("admin")
    @Transactional
    public Response update(@PathParam("id") String id, CharacterWithDetails payload) {
        Character entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (entity.details == null) {
            entity.details = new java.util.ArrayList<>();
        }
        CharacterDetails details = entity.details.stream()
                .filter(d -> d.lang.equals(payload.lang))
                .findFirst()
                .orElseGet(() -> {
                    CharacterDetails d = new CharacterDetails();
                    d.idCharacter = id;
                    d.lang = payload.lang;
                    d.character = entity;
                    entity.details.add(d);
                    return d;
                });

        details.name = payload.name;
        details.story = payload.story;

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
