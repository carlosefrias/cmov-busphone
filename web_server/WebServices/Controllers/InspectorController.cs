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
    public class InspectorController : ApiController
    {
        private InspectorContext db = new InspectorContext();

        // GET api/Inspector
        public IEnumerable<Inspector> GetInspectors()
        {
            return db.Inspectors.AsEnumerable();
        }

        // GET api/Inspector/5
        public Inspector GetInspector(string id)
        {
            Inspector inspector = db.Inspectors.Find(id);
            if (inspector == null)
            {
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.NotFound));
            }

            return inspector;
        }

        // PUT api/Inspector/5
        public HttpResponseMessage PutInspector(string id, Inspector inspector)
        {
            if (!ModelState.IsValid)
            {
                return Request.CreateErrorResponse(HttpStatusCode.BadRequest, ModelState);
            }

            if (id != inspector.InspectorID)
            {
                return Request.CreateResponse(HttpStatusCode.BadRequest);
            }

            db.Entry(inspector).State = EntityState.Modified;

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

        // POST api/Inspector
        public HttpResponseMessage PostInspector(Inspector inspector)
        {
            if (ModelState.IsValid)
            {
                db.Inspectors.Add(inspector);
                db.SaveChanges();

                HttpResponseMessage response = Request.CreateResponse(HttpStatusCode.Created, inspector);
                response.Headers.Location = new Uri(Url.Link("DefaultApi", new { id = inspector.InspectorID }));
                return response;
            }
            else
            {
                return Request.CreateErrorResponse(HttpStatusCode.BadRequest, ModelState);
            }
        }

        // DELETE api/Inspector/5
        public HttpResponseMessage DeleteInspector(string id)
        {
            Inspector inspector = db.Inspectors.Find(id);
            if (inspector == null)
            {
                return Request.CreateResponse(HttpStatusCode.NotFound);
            }

            db.Inspectors.Remove(inspector);

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException ex)
            {
                return Request.CreateErrorResponse(HttpStatusCode.NotFound, ex);
            }

            return Request.CreateResponse(HttpStatusCode.OK, inspector);
        }

        protected override void Dispose(bool disposing)
        {
            db.Dispose();
            base.Dispose(disposing);
        }
    }
}