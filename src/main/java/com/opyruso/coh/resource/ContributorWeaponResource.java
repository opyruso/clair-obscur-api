package com.opyruso.coh.resource;

import com.opyruso.coh.entity.Weapon;
import com.opyruso.coh.entity.WeaponDetails;
import com.opyruso.coh.repository.WeaponRepository;
import com.opyruso.coh.model.dto.WeaponWithDetails;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/contrib/weapons")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ContributorWeaponResource {

    @Inject
    WeaponRepository repository;

    @PUT
    @Path("{id}")
    @RolesAllowed("contributor")
    @Transactional
    public Response update(@PathParam("id") String id, WeaponWithDetails payload) {
        if (payload.character == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("character is required")
                    .build();
        }
        Weapon entity = repository.findByIdWeaponAndCharacter(id, payload.character);
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
            WeaponDetails details = entity.details.stream()
                    .filter(d -> d.lang.equals(payload.lang))
                    .findFirst()
                    .orElseGet(() -> {
                        WeaponDetails d = new WeaponDetails();
                        d.idWeapon = id;
                        d.idCharacter = payload.character;
                        d.lang = payload.lang;
                        d.weapon = entity;
                        entity.details.add(d);
                        return d;
                    });
            details.idCharacter = payload.character;
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
