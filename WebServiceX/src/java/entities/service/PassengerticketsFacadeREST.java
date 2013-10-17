/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.service;

import entities.Passengertickets;
import entities.PassengerticketsPK;
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
@Path("entities.passengertickets")
public class PassengerticketsFacadeREST extends AbstractFacade<Passengertickets> {
    @PersistenceContext(unitName = "WebServiceXPU")
    private EntityManager em;

    private PassengerticketsPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;loginpassenger=loginpassengerValue;ticketid=ticketidValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        entities.PassengerticketsPK key = new entities.PassengerticketsPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> loginpassenger = map.get("loginpassenger");
        if (loginpassenger != null && !loginpassenger.isEmpty()) {
            key.setLoginpassenger(loginpassenger.get(0));
        }
        java.util.List<String> ticketid = map.get("ticketid");
        if (ticketid != null && !ticketid.isEmpty()) {
            key.setTicketid(ticketid.get(0));
        }
        return key;
    }

    public PassengerticketsFacadeREST() {
        super(Passengertickets.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Passengertickets entity) {
        super.create(entity);
    }

    @PUT
    @Override
    @Consumes({"application/xml", "application/json"})
    public void edit(Passengertickets entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        entities.PassengerticketsPK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Passengertickets find(@PathParam("id") PathSegment id) {
        entities.PassengerticketsPK key = getPrimaryKey(id);
        return super.find(key);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Passengertickets> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Passengertickets> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
