package com.opyruso.coh.resource;

import com.opyruso.coh.entity.*;
import com.opyruso.coh.repository.*;
import com.opyruso.coh.model.dto.OutfitWithDetails;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin/outfits")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminOutfitResource {

    @Inject
    OutfitRepository repository;
    @Inject
    CharacterRepository characterRepository;

    @POST
    @RolesAllowed("admin")
    @Transactional
    public Response create(OutfitWithDetails payload) {
        Outfit outfit = repository.findById(payload.idOutfit);
        boolean isNew = false;
        if (outfit == null) {
            outfit = new Outfit();
            outfit.idOutfit = payload.idOutfit;
            repository.persist(outfit);
            isNew = true;
        }
        outfit.character = characterRepository.findById(payload.character);

        if (outfit.details == null) {
            outfit.details = new java.util.ArrayList<>();
        }

        OutfitDetails details = new OutfitDetails();
        details.idOutfit = payload.idOutfit;
        details.lang = payload.lang;
        details.name = payload.name;
        details.description = payload.description;
        details.outfit = outfit;

        outfit.details.add(details);
        if (!isNew) {
            repository.getEntityManager().persist(details);
        }

        repository.getEntityManager().flush();
        return Response.status(Response.Status.CREATED).entity(outfit).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed("admin")
    @Transactional
    public Response update(@PathParam("id") String id, OutfitWithDetails payload) {
        Outfit entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (payload.character != null) {
            entity.character = characterRepository.findById(payload.character);
        }

        if (entity.details == null) {
            entity.details = new java.util.ArrayList<>();
        }
        OutfitDetails details = entity.details.stream()
                .filter(d -> d.lang.equals(payload.lang))
                .findFirst()
                .orElseGet(() -> {
                    OutfitDetails d = new OutfitDetails();
                    d.idOutfit = id;
                    d.lang = payload.lang;
                    d.outfit = entity;
                    entity.details.add(d);
                    return d;
                });

        if (payload.name != null) {
            details.name = payload.name;
        }
        if (payload.description != null) {
            details.description = payload.description;
        }

        repository.getEntityManager().flush();

        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed("admin")
    @Transactional
    public Response delete(@PathParam("id") String id, @QueryParam("lang") String lang) {
        Outfit entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (lang != null) {
            if (entity.details != null) {
                OutfitDetails details = entity.details.stream()
                        .filter(d -> d.lang.equals(lang))
                        .findFirst().orElse(null);
                if (details != null) {
                    entity.details.remove(details);
                    repository.getEntityManager().remove(details);
                }
            }
            if (entity.details == null || entity.details.isEmpty()) {
                repository.delete(entity);
            }
        } else {
            repository.delete(entity);
        }
        return Response.noContent().build();
    }
}
