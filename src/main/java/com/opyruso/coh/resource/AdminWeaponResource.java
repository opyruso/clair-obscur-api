package com.opyruso.coh.resource;

import com.opyruso.coh.entity.*;
import com.opyruso.coh.repository.*;
import com.opyruso.coh.model.dto.WeaponWithDetails;
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
    @Inject
    CharacterRepository characterRepository;
    @Inject
    DamageTypeRepository damageTypeRepository;
    @Inject
    DamageBuffTypeRepository damageBuffTypeRepository;

    @POST
    @RolesAllowed("admin")
    @Transactional
    public Response create(WeaponWithDetails payload) {
        Weapon weapon = new Weapon();
        weapon.idWeapon = payload.idWeapon;
        weapon.character = characterRepository.findById(payload.character);
        weapon.damageType = payload.damageType == null ? null : damageTypeRepository.findById(payload.damageType);
        weapon.damageBuffType1 = payload.damageBuffType1 == null ? null : damageBuffTypeRepository.findById(payload.damageBuffType1);
        weapon.damageBuffType2 = payload.damageBuffType2 == null ? null : damageBuffTypeRepository.findById(payload.damageBuffType2);

        WeaponDetails details = new WeaponDetails();
        details.idWeapon = payload.idWeapon;
        details.lang = payload.lang;
        details.name = payload.name;
        details.region = payload.region;
        details.unlockDescription = payload.unlockDescription;
        details.weaponEffect1 = payload.weaponEffect1;
        details.weaponEffect2 = payload.weaponEffect2;
        details.weaponEffect3 = payload.weaponEffect3;
        details.weapon = weapon;

        weapon.details = new java.util.ArrayList<>(java.util.List.of(details));

        repository.persist(weapon);
        return Response.status(Response.Status.CREATED).entity(weapon).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed("admin")
    @Transactional
    public Response update(@PathParam("id") String id, WeaponWithDetails payload) {
        Weapon entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        entity.character = characterRepository.findById(payload.character);
        entity.damageType = payload.damageType == null ? null : damageTypeRepository.findById(payload.damageType);
        entity.damageBuffType1 = payload.damageBuffType1 == null ? null : damageBuffTypeRepository.findById(payload.damageBuffType1);
        entity.damageBuffType2 = payload.damageBuffType2 == null ? null : damageBuffTypeRepository.findById(payload.damageBuffType2);

        if (entity.details == null) {
            entity.details = new java.util.ArrayList<>();
        }
        WeaponDetails details = entity.details.stream()
                .filter(d -> d.lang.equals(payload.lang))
                .findFirst()
                .orElseGet(() -> {
                    WeaponDetails d = new WeaponDetails();
                    d.idWeapon = id;
                    d.lang = payload.lang;
                    d.weapon = entity;
                    entity.details.add(d);
                    return d;
                });

        details.name = payload.name;
        details.region = payload.region;
        details.unlockDescription = payload.unlockDescription;
        details.weaponEffect1 = payload.weaponEffect1;
        details.weaponEffect2 = payload.weaponEffect2;
        details.weaponEffect3 = payload.weaponEffect3;

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
