package com.opyruso.coh.resource;

import com.opyruso.coh.entity.Picto;
import com.opyruso.coh.entity.PictoDetails;
import com.opyruso.coh.repository.PictoRepository;
import com.opyruso.coh.model.dto.PictoWithDetails;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin/pictos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminPictoResource {

    @Inject
    PictoRepository repository;

    @POST
    @RolesAllowed("admin")
    @Transactional
    public Response create(PictoWithDetails payload) {
        Picto picto = repository.findById(payload.idPicto);
        boolean isNew = false;
        if (picto == null) {
            picto = new Picto();
            picto.idPicto = payload.idPicto;
            repository.persist(picto);
            isNew = true;
        }
        if (payload.level != null) {
            picto.level = payload.level;
        }
        if (payload.bonusDefense != null) {
            picto.bonusDefense = payload.bonusDefense;
        }
        if (payload.bonusSpeed != null) {
            picto.bonusSpeed = payload.bonusSpeed;
        }
        if (payload.bonusCritChance != null) {
            picto.bonusCritChance = payload.bonusCritChance;
        }
        if (payload.bonusHealth != null) {
            picto.bonusHealth = payload.bonusHealth;
        }
        if (payload.luminaCost != null) {
            picto.luminaCost = payload.luminaCost;
        }

        if (payload.name != null) {
            picto.name = payload.name;
        }
        if (payload.descrptionBonusLumina != null) {
            picto.descrptionBonusLumina = payload.descrptionBonusLumina;
        }

        if (payload.region != null || payload.unlockDescription != null) {
            if (picto.details == null) {
                picto.details = new java.util.ArrayList<>();
            }

            PictoDetails details = new PictoDetails();
            details.idPicto = payload.idPicto;
            details.lang = payload.lang;
            details.region = payload.region;
            details.unlockDescription = payload.unlockDescription;
            details.picto = picto;

            picto.details.add(details);
            if (!isNew) {
                repository.getEntityManager().persist(details);
            }
        }

        repository.getEntityManager().flush();
        return Response.status(Response.Status.CREATED).entity(picto).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed("admin")
    @Transactional
    public Response update(@PathParam("id") String id, PictoWithDetails payload) {
        Picto entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (payload.level != null) {
            entity.level = payload.level;
        }
        if (payload.bonusDefense != null) {
            entity.bonusDefense = payload.bonusDefense;
        }
        if (payload.bonusSpeed != null) {
            entity.bonusSpeed = payload.bonusSpeed;
        }
        if (payload.bonusCritChance != null) {
            entity.bonusCritChance = payload.bonusCritChance;
        }
        if (payload.bonusHealth != null) {
            entity.bonusHealth = payload.bonusHealth;
        }
        if (payload.luminaCost != null) {
            entity.luminaCost = payload.luminaCost;
        }

        if (payload.name != null) {
            entity.name = payload.name;
        }
        if (payload.descrptionBonusLumina != null) {
            entity.descrptionBonusLumina = payload.descrptionBonusLumina;
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
            PictoDetails details = entity.details.stream()
                    .filter(d -> d.lang.equals(payload.lang))
                    .findFirst()
                    .orElseGet(() -> {
                        PictoDetails d = new PictoDetails();
                        d.idPicto = id;
                        d.lang = payload.lang;
                        d.picto = entity;
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

    @DELETE
    @Path("{id}")
    @RolesAllowed("admin")
    @Transactional
    public Response delete(@PathParam("id") String id, @QueryParam("lang") String lang) {
        Picto entity = repository.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (lang != null) {
            if (entity.details != null) {
                PictoDetails details = entity.details.stream()
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
