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
    public class PassengerController : ApiController
    {
        private PassengerContext db = new PassengerContext();

        // GET api/Passenger
        public IEnumerable<Passenger> GetPassengers()
        {
            return db.Passengers.AsEnumerable();
        }

        // GET api/Passenger/5
        public Passenger GetPassenger(string id)
        {
            Passenger passenger = db.Passengers.Find(id);
            if (passenger == null)
            {
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.NotFound));
            }

            return passenger;
        }

        // PUT api/Passenger/5
        public HttpResponseMessage PutPassenger(string id, Passenger passenger)
        {
            if (!ModelState.IsValid)
            {
                return Request.CreateErrorResponse(HttpStatusCode.BadRequest, ModelState);
            }

            if (id != passenger.PassengerId)
            {
                return Request.CreateResponse(HttpStatusCode.BadRequest);
            }

            db.Entry(passenger).State = EntityState.Modified;

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

        // POST api/Passenger
        public HttpResponseMessage PostPassenger(Passenger passenger)
        {
            if (ModelState.IsValid)
            {
                db.Passengers.Add(passenger);
                db.SaveChanges();

                HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.Created, passenger);
                response.Headers.Location = new Uri(Url.Link("DefaultApi", new { id = passenger.PassengerId }));
                return response;
            }
            else
            {
                return Request.CreateErrorResponse(HttpStatusCode.BadRequest, ModelState);
            }
        }

        // DELETE api/Passenger/5
        public HttpResponseMessage DeletePassenger(string id)
        {
            Passenger passenger = db.Passengers.Find(id);
            if (passenger == null)
            {
                return Request.CreateResponse(HttpStatusCode.NotFound);
            }

            db.Passengers.Remove(passenger);

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException ex)
            {
                return Request.CreateErrorResponse(HttpStatusCode.NotFound, ex);
            }

            return Request.CreateResponse(HttpStatusCode.OK, passenger);
        }

        protected override void Dispose(bool disposing)
        {
            db.Dispose();
            base.Dispose(disposing);
        }
    }
}