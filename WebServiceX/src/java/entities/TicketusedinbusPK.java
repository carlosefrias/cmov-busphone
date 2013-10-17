/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Carlos
 */
@Embeddable
public class TicketusedinbusPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ticketid")
    private String ticketid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "busid")
    private int busid;

    public TicketusedinbusPK() {
    }

    public TicketusedinbusPK(String ticketid, int busid) {
        this.ticketid = ticketid;
        this.busid = busid;
    }

    public String getTicketid() {
        return ticketid;
    }

    public void setTicketid(String ticketid) {
        this.ticketid = ticketid;
    }

    public int getBusid() {
        return busid;
    }

    public void setBusid(int busid) {
        this.busid = busid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ticketid != null ? ticketid.hashCode() : 0);
        hash += (int) busid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TicketusedinbusPK)) {
            return false;
        }
        TicketusedinbusPK other = (TicketusedinbusPK) object;
        if ((this.ticketid == null && other.ticketid != null) || (this.ticketid != null && !this.ticketid.equals(other.ticketid))) {
            return false;
        }
        if (this.busid != other.busid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TicketusedinbusPK[ ticketid=" + ticketid + ", busid=" + busid + " ]";
    }
    
}
