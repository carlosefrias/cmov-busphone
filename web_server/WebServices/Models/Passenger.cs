using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebServices.Models
{
    public class Passenger
    {
        public string PassengerId { get; set; }//login do passageiro
        public string name { get; set; }
        public string password {get; set;}
        public string creditCardType {get; set;}
        public string creditCardNumber {get; set;}
        public string creditCardValidity {get; set;}//Não estava a funcionar o POST com DateTime
    }
}