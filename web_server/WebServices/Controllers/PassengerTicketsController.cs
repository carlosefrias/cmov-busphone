using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;
using WebServices.Models;

namespace WebServices.Controllers
{
    public class PassengerTicketsController : ApiController
    {
        private PassengerTicketsContext db = new PassengerTicketsContext();

        // GET api/PassengerTickets
        public IEnumerable<PassengerTickets> GetPassengerTickets()
        {
            return db.PassengerTickets.AsEnumerable();
        }

        // GET api/PassengerTickets/5
        public PassengerTickets GetPassengerTickets(int id)
        {
            PassengerTickets passengertickets = db.PassengerTickets.Find(id);
            if (passengertickets == null)
            {
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.NotFound));
            }

            return passengertickets;
        }

        // PUT api/PassengerTickets/5
        public HttpResponseMessage PutPassengerTickets(int id, PassengerTickets passengertickets)
        {
            if (!ModelState.IsValid)
            {
                return Request.CreateErrorResponse(HttpStatusCode.BadRequest, ModelState);
            }

            if (id != passengertickets.PassengerTicketsId)
            {
                return Request.CreateResponse(HttpStatusCode.BadRequest);
            }

            db.Entry(passengertickets).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException ex)
            {
                return Request.CreateErrorResponse(HttpStatusCode.NotFound, ex);
            }

            return Request.CreateResponse(HttpStatusCode.OK);
        }

        // POST api/PassengerTickets
        public HttpResponseMessage PostPassengerTickets(PassengerTickets passengertickets)
        {
            if (ModelState.IsValid)
            {
                db.PassengerTickets.Add(passengertickets);
                db.SaveChanges();

                HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.Created, passengertickets);
                response.Headers.Location = new Uri(Url.Link("DefaultApi", new { id = passengertickets.PassengerTicketsId }));
                return response;
            }
            else
            {
                return Request.CreateErrorResponse(HttpStatusCode.BadRequest, ModelState);
            }
        }

        // DELETE api/PassengerTickets/5
        public HttpResponseMessage DeletePassengerTickets(int id)
        {
            PassengerTickets passengertickets = db.PassengerTickets.Find(id);
            if (passengertickets == null)
            {
                return Request.CreateResponse(HttpStatusCode.NotFound);
            }

            db.PassengerTickets.Remove(passengertickets);

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException ex)
            {
                return Request.CreateErrorResponse(HttpStatusCode.NotFound, ex);
            }

            return Request.CreateResponse(HttpStatusCode.OK, passengertickets);
        }

        protected override void Dispose(bool disposing)
        {
            db.Dispose();
            base.Dispose(disposing);
        }
    }
}