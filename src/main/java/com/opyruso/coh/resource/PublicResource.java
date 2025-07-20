package com.opyruso.coh.resource;

import com.opyruso.coh.entity.CohBuild;
import com.opyruso.coh.repository.CohBuildRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Path("/public/builds")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PublicResource {

    @Inject
    CohBuildRepository repository;

    @POST
    @Transactional
    public Response create(String body) {
        String id = generateKey();
        CohBuild build = new CohBuild();
        build.idBuild = id;
        build.content = Base64.getEncoder().encodeToString(body.getBytes(StandardCharsets.UTF_8));
        repository.persist(build);
        return Response.status(Response.Status.CREATED).entity(Map.of("id", id)).build();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") String id) {
        CohBuild build = repository.findById(id);
        if (build == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        String json = new String(Base64.getDecoder().decode(build.content), StandardCharsets.UTF_8);
        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }

    private String generateKey() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
