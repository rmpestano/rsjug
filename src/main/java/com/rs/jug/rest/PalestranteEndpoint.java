package com.rs.jug.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import com.rs.jug.model.Palestrante;

/**
 * 
 */
@Stateless
@Path("/palestrantes")
public class PalestranteEndpoint
{
   @PersistenceContext(unitName = "forge-default")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(Palestrante entity)
   {
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(PalestranteEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      Palestrante entity = em.find(Palestrante.class, id);
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      em.remove(entity);
      return Response.noContent().build();
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/json")
   public Response findById(@PathParam("id") Long id)
   {
      TypedQuery<Palestrante> findByIdQuery = em.createQuery("SELECT DISTINCT p FROM Palestrante p WHERE p.id = :entityId ORDER BY p.id", Palestrante.class);
      findByIdQuery.setParameter("entityId", id);
      Palestrante entity;
      try
      {
         entity = findByIdQuery.getSingleResult();
      }
      catch (NoResultException nre)
      {
         entity = null;
      }
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      return Response.ok(entity).build();
   }

   @GET
   @Produces("application/json")
   public List<Palestrante> listAll()
   {
      final List<Palestrante> results = em.createQuery("SELECT DISTINCT p FROM Palestrante p ORDER BY p.id", Palestrante.class).getResultList();
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(Palestrante entity)
   {
      entity = em.merge(entity);
      return Response.noContent().build();
   }
   
   @GET
   @Path("/teste")
   @Produces("application/json")
   public Response teste(){
	   return Response.ok().build();
   }
}