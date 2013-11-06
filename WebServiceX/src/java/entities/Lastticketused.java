/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Carlos
 */
@Entity
@Table(name = "lastticketused")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lastticketused.findAll", query = "SELECT l FROM Lastticketused l"),
    @NamedQuery(name = "Lastticketused.findByLogin", query = "SELECT l FROM Lastticketused l WHERE l.login = :login"),
    @NamedQuery(name = "Lastticketused.findByTicketid", query = "SELECT l FROM Lastticketused l WHERE l.ticketid = :ticketid")})
public class Lastticketused implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "login")
    private String login;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ticketid")
    private String ticketid;

    public Lastticketused() {
    }

    public Lastticketused(String login) {
        this.login = login;
    }

    public Lastticketused(String login, String ticketid) {
        this.login = login;
        this.ticketid = ticketid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
        hash += (login != null ? login.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lastticketused)) {
            return false;
        }
        Lastticketused other = (Lastticketused) object;
        if ((this.login == null && other.login != null) || (this.login != null && !this.login.equals(other.login))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Lastticketused[ login=" + login + " ]";
    }
    
}
