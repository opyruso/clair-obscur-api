package com.opyruso.coh.resource;

import com.opyruso.coh.entity.Weapon;
import com.opyruso.coh.repository.WeaponRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin/weapons")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminWeaponResource {

    @Inject
    WeaponRepository repository;

    @POST
    @RolesAllowed("admin")
    @Transactional
    public Response create(Weapon weapon) {
        repository.persist(weapon);
        return Response.status(Response.Status.CREATED).entity(weapon).build();
    }

    @PUT
    @RolesAllowed("admin")
    @Transactional
    public Response update(Weapon weapon) {
        String id = weapon.idWeapon;
        Weapon entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        entity.character = weapon.character;
        entity.damageType = weapon.damageType;
        entity.damageBuffType1 = weapon.damageBuffType1;
        entity.damageBuffType2 = weapon.damageBuffType2;
        entity.details.clear();
        if (weapon.details != null) {
            weapon.details.forEach(d -> d.idWeapon = id);
            entity.details.addAll(weapon.details);
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
