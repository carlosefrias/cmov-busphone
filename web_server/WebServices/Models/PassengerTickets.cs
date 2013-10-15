using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebServices.Models
{
    public class PassengerTickets
    {
        public int PassengerTicketsId { get; set; }//precisa de um identificador
        public Passenger passager { get; set; }
        public Ticket ticket { get; set; }
    }
}