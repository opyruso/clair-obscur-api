package com.opyruso.coh.resource;

import com.opyruso.coh.entity.Character;
import com.opyruso.coh.entity.DamageBuffType;
import com.opyruso.coh.entity.DamageType;
import com.opyruso.coh.entity.Weapon;
import com.opyruso.coh.entity.WeaponDetails;
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
        Character character = characterRepository.findById(payload.character);
        if (character == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid character").build();
        }
        DamageType damageType = payload.damageType == null ? null : damageTypeRepository.findById(payload.damageType);
        if (payload.damageType != null && damageType == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid damage type").build();
        }
        DamageBuffType damageBuffType1 = payload.damageBuffType1 == null ? null : damageBuffTypeRepository.findById(payload.damageBuffType1);
        if (payload.damageBuffType1 != null && damageBuffType1 == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid damage buff type 1").build();
        }
        DamageBuffType damageBuffType2 = payload.damageBuffType2 == null ? null : damageBuffTypeRepository.findById(payload.damageBuffType2);
        if (payload.damageBuffType2 != null && damageBuffType2 == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid damage buff type 2").build();
        }

        Weapon weapon = repository.findByIdWeaponAndCharacter(payload.idWeapon, payload.character);
        boolean isNew = false;
        if (weapon == null) {
            weapon = new Weapon();
            weapon.idWeapon = payload.idWeapon;
            weapon.idCharacter = character.idCharacter;
            weapon.character = character;
            repository.persist(weapon);
            isNew = true;
        }
        weapon.damageType = damageType;
        weapon.damageBuffType1 = damageBuffType1;
        weapon.damageBuffType2 = damageBuffType2;

        if (payload.name != null) {
            weapon.name = payload.name;
        }
        if (payload.weaponEffect1 != null) {
            weapon.weaponEffect1 = payload.weaponEffect1;
        }
        if (payload.weaponEffect2 != null) {
            weapon.weaponEffect2 = payload.weaponEffect2;
        }
        if (payload.weaponEffect3 != null) {
            weapon.weaponEffect3 = payload.weaponEffect3;
        }

        if (payload.region != null || payload.unlockDescription != null) {
            if (weapon.details == null) {
                weapon.details = new java.util.ArrayList<>();
            }

            WeaponDetails details = new WeaponDetails();
            details.idWeapon = payload.idWeapon;
            details.idCharacter = character.idCharacter;
            details.lang = payload.lang;
            details.region = payload.region;
            details.unlockDescription = payload.unlockDescription;
            details.weapon = weapon;

            weapon.details.add(details);
            if (!isNew) {
                repository.getEntityManager().persist(details);
            }
        }

        repository.getEntityManager().flush();
        return Response.status(Response.Status.CREATED).entity(weapon).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed("admin")
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
        final Character finalCharacter = entity.character;

        if (payload.damageType != null) {
            DamageType damageType = damageTypeRepository.findById(payload.damageType);
            if (damageType == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid damage type").build();
            }
            entity.damageType = damageType;
        }

        if (payload.damageBuffType1 != null) {
            DamageBuffType damageBuffType1 = damageBuffTypeRepository.findById(payload.damageBuffType1);
            if (damageBuffType1 == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid damage buff type 1").build();
            }
            entity.damageBuffType1 = damageBuffType1;
        }

        if (payload.damageBuffType2 != null) {
            DamageBuffType damageBuffType2 = damageBuffTypeRepository.findById(payload.damageBuffType2);
            if (damageBuffType2 == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid damage buff type 2").build();
            }
            entity.damageBuffType2 = damageBuffType2;
        }

        if (payload.name != null) {
            entity.name = payload.name;
        }
        if (payload.weaponEffect1 != null) {
            entity.weaponEffect1 = payload.weaponEffect1;
        }
        if (payload.weaponEffect2 != null) {
            entity.weaponEffect2 = payload.weaponEffect2;
        }
        if (payload.weaponEffect3 != null) {
            entity.weaponEffect3 = payload.weaponEffect3;
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
                        d.idCharacter = finalCharacter.idCharacter;
                        d.lang = payload.lang;
                        d.weapon = entity;
                        entity.details.add(d);
                        return d;
                    });

            details.idCharacter = finalCharacter.idCharacter;

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

    @DELETE
    @Path("{id}")
    @RolesAllowed("admin")
    @Transactional
    public Response delete(@PathParam("id") String id, @QueryParam("lang") String lang) {
        Weapon entity = repository.findByIdWeapon(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (lang != null) {
            if (entity.details != null) {
                WeaponDetails details = entity.details.stream()
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
