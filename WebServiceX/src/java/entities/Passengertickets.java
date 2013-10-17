/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Carlos
 */
@Entity
@Table(name = "passengertickets")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Passengertickets.findAll", query = "SELECT p FROM Passengertickets p"),
    @NamedQuery(name = "Passengertickets.findByLoginpassenger", query = "SELECT p FROM Passengertickets p WHERE p.passengerticketsPK.loginpassenger = :loginpassenger"),
    @NamedQuery(name = "Passengertickets.findByTicketid", query = "SELECT p FROM Passengertickets p WHERE p.passengerticketsPK.ticketid = :ticketid")})
public class Passengertickets implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PassengerticketsPK passengerticketsPK;

    public Passengertickets() {
    }

    public Passengertickets(PassengerticketsPK passengerticketsPK) {
        this.passengerticketsPK = passengerticketsPK;
    }

    public Passengertickets(String loginpassenger, String ticketid) {
        this.passengerticketsPK = new PassengerticketsPK(loginpassenger, ticketid);
    }

    public PassengerticketsPK getPassengerticketsPK() {
        return passengerticketsPK;
    }

    public void setPassengerticketsPK(PassengerticketsPK passengerticketsPK) {
        this.passengerticketsPK = passengerticketsPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (passengerticketsPK != null ? passengerticketsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Passengertickets)) {
            return false;
        }
        Passengertickets other = (Passengertickets) object;
        if ((this.passengerticketsPK == null && other.passengerticketsPK != null) || (this.passengerticketsPK != null && !this.passengerticketsPK.equals(other.passengerticketsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Passengertickets[ passengerticketsPK=" + passengerticketsPK + " ]";
    }
    
}
