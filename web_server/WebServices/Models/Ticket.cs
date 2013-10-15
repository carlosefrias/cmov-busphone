using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebServices.Models
{
    public class Ticket
    {
        public string ticketId { get; set; }//identificador único do bilhete
        public string type { get; set; }
        public bool isvalidated { get; set; }
        public bool isused { get; set; }
        public string timeofvalitation { get; set; }//Com DateTime dá problemas no POST
        public int busID { get; set; }
    }
}