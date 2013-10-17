/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.service;

import entities.Ticketusedinbus;
import entities.TicketusedinbusPK;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.PathSegment;

/**
 *
 * @author Carlos
 */
@Stateless
@Path("entities.ticketusedinbus")
public class TicketusedinbusFacadeREST extends AbstractFacade<Ticketusedinbus> {
    @PersistenceContext(unitName = "WebServiceXPU")
    private EntityManager em;

    private TicketusedinbusPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;ticketid=ticketidValue;busid=busidValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        entities.TicketusedinbusPK key = new entities.TicketusedinbusPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> ticketid = map.get("ticketid");
        if (ticketid != null && !ticketid.isEmpty()) {
            key.setTicketid(ticketid.get(0));
        }
        java.util.List<String> busid = map.get("busid");
        if (busid != null && !busid.isEmpty()) {
            key.setBusid(new java.lang.Integer(busid.get(0)));
        }
        return key;
    }

    public TicketusedinbusFacadeREST() {
        super(Ticketusedinbus.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Ticketusedinbus entity) {
        super.create(entity);
    }

    @PUT
    @Override
    @Consumes({"application/xml", "application/json"})
    public void edit(Ticketusedinbus entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        entities.TicketusedinbusPK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Ticketusedinbus find(@PathParam("id") PathSegment id) {
        entities.TicketusedinbusPK key = getPrimaryKey(id);
        return super.find(key);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Ticketusedinbus> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Ticketusedinbus> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
