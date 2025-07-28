package com.opyruso.coh.resource;

import com.opyruso.coh.entity.Outfit;
import com.opyruso.coh.entity.OutfitDetails;
import com.opyruso.coh.repository.OutfitRepository;
import com.opyruso.coh.model.dto.OutfitWithDetails;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/contrib/outfits")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ContributorOutfitResource {

    @Inject
    OutfitRepository repository;

    @PUT
    @Path("{id}")
    @RolesAllowed("contributor")
    @Transactional
    public Response update(@PathParam("id") String id, OutfitWithDetails payload) {
        Outfit entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (payload.region != null || payload.unlockDescription != null) {
            if (payload.lang == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("lang is required when updating details")
                        .build();
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
            if (payload.region != null) {
                details.region = payload.region;
            }
            if (payload.unlockDescription != null) {
                details.unlockDescription = payload.unlockDescription;
            }
        }
        repository.getEntityManager().flush();
        return Response.ok(entity).build();
    }
}
