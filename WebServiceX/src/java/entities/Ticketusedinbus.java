/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Carlos
 */
@Entity
@Table(name = "ticketusedinbus")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ticketusedinbus.findAll", query = "SELECT t FROM Ticketusedinbus t"),
    @NamedQuery(name = "Ticketusedinbus.findByTicketid", query = "SELECT t FROM Ticketusedinbus t WHERE t.ticketusedinbusPK.ticketid = :ticketid"),
    @NamedQuery(name = "Ticketusedinbus.findByBusid", query = "SELECT t FROM Ticketusedinbus t WHERE t.ticketusedinbusPK.busid = :busid")})
public class Ticketusedinbus implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TicketusedinbusPK ticketusedinbusPK;
    @JoinColumn(name = "ticketid", referencedColumnName = "idticket", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Ticket ticket;
    @JoinColumn(name = "busid", referencedColumnName = "idbus", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Bus bus;

    public Ticketusedinbus() {
    }

    public Ticketusedinbus(TicketusedinbusPK ticketusedinbusPK) {
        this.ticketusedinbusPK = ticketusedinbusPK;
    }

    public Ticketusedinbus(String ticketid, int busid) {
        this.ticketusedinbusPK = new TicketusedinbusPK(ticketid, busid);
    }

    public TicketusedinbusPK getTicketusedinbusPK() {
        return ticketusedinbusPK;
    }

    public void setTicketusedinbusPK(TicketusedinbusPK ticketusedinbusPK) {
        this.ticketusedinbusPK = ticketusedinbusPK;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ticketusedinbusPK != null ? ticketusedinbusPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ticketusedinbus)) {
            return false;
        }
        Ticketusedinbus other = (Ticketusedinbus) object;
        if ((this.ticketusedinbusPK == null && other.ticketusedinbusPK != null) || (this.ticketusedinbusPK != null && !this.ticketusedinbusPK.equals(other.ticketusedinbusPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Ticketusedinbus[ ticketusedinbusPK=" + ticketusedinbusPK + " ]";
    }
    
}
