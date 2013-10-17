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
public class PassengerticketsPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "loginpassenger")
    private String loginpassenger;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ticketid")
    private String ticketid;

    public PassengerticketsPK() {
    }

    public PassengerticketsPK(String loginpassenger, String ticketid) {
        this.loginpassenger = loginpassenger;
        this.ticketid = ticketid;
    }

    public String getLoginpassenger() {
        return loginpassenger;
    }

    public void setLoginpassenger(String loginpassenger) {
        this.loginpassenger = loginpassenger;
    }

    public String getTicketid() {
        return ticketid;
    }

    public void setTicketid(String ticketid) {
        this.ticketid = ticketid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (loginpassenger != null ? loginpassenger.hashCode() : 0);
        hash += (ticketid != null ? ticketid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PassengerticketsPK)) {
            return false;
        }
        PassengerticketsPK other = (PassengerticketsPK) object;
        if ((this.loginpassenger == null && other.loginpassenger != null) || (this.loginpassenger != null && !this.loginpassenger.equals(other.loginpassenger))) {
            return false;
        }
        if ((this.ticketid == null && other.ticketid != null) || (this.ticketid != null && !this.ticketid.equals(other.ticketid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PassengerticketsPK[ loginpassenger=" + loginpassenger + ", ticketid=" + ticketid + " ]";
    }
    
}
