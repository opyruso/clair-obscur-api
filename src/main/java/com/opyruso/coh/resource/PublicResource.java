package com.opyruso.coh.resource;

import com.opyruso.coh.entity.CohBuild;
import com.opyruso.coh.model.dto.BuildPayload;
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
import io.quarkus.hibernate.orm.panache.Sort;
import io.quarkus.panache.common.Page;

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
        BuildPayload dto = new BuildPayload();
        dto.title = build.title;
        dto.description = build.description;
        dto.recommendedLevel = build.recommendedLevel;
        dto.content = new String(Base64.getDecoder().decode(build.content), StandardCharsets.UTF_8);
        return Response.ok(dto).build();
    }

    @GET
    @Path("latest")
    public Response latest() {
        var list = repository.findAll(Sort.descending("creationDate")).page(Page.ofSize(10)).list()
                .stream()
                .map(b -> Map.of(
                        "id", b.idBuild,
                        "title", b.title,
                        "description", b.description,
                        "recommendedLevel", b.recommendedLevel,
                        "author", b.author,
                        "firstname", b.firstname,
                        "creationDate", b.creationDate))
                .toList();
        return Response.ok(list).build();
    }

    private String generateKey() {
        return UUID.randomUUID().toString();
    }
}
