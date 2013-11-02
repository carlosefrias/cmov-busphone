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
@Table(name = "ticket")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ticket.findAll", query = "SELECT t FROM Ticket t"),
    @NamedQuery(name = "Ticket.findByIdticket", query = "SELECT t FROM Ticket t WHERE t.idticket = :idticket"),
    @NamedQuery(name = "Ticket.findByType", query = "SELECT t FROM Ticket t WHERE t.type = :type"),
    @NamedQuery(name = "Ticket.findByIschecked", query = "SELECT t FROM Ticket t WHERE t.ischecked = :ischecked"),
    @NamedQuery(name = "Ticket.findByIsvalidated", query = "SELECT t FROM Ticket t WHERE t.isvalidated = :isvalidated"),
    @NamedQuery(name = "Ticket.findByTimeodvalidation", query = "SELECT t FROM Ticket t WHERE t.timeodvalidation = :timeodvalidation"),
    @NamedQuery(name = "Ticket.findByIdbus", query = "SELECT t FROM Ticket t WHERE t.idbus = :idbus")})
public class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "idticket")
    private String idticket;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ischecked")
    private boolean ischecked;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isvalidated")
    private boolean isvalidated;
    @Size(max = 20)
    @Column(name = "timeodvalidation")
    private String timeodvalidation;
    @Column(name = "idbus")
    private Integer idbus;

    public Ticket() {
    }

    public Ticket(String idticket) {
        this.idticket = idticket;
    }

    public Ticket(String idticket, String type, boolean ischecked, boolean isvalidated) {
        this.idticket = idticket;
        this.type = type;
        this.ischecked = ischecked;
        this.isvalidated = isvalidated;
    }

    public String getIdticket() {
        return idticket;
    }

    public void setIdticket(String idticket) {
        this.idticket = idticket;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public boolean getIsvalidated() {
        return isvalidated;
    }

    public void setIsvalidated(boolean isvalidated) {
        this.isvalidated = isvalidated;
    }

    public String getTimeodvalidation() {
        return timeodvalidation;
    }

    public void setTimeodvalidation(String timeodvalidation) {
        this.timeodvalidation = timeodvalidation;
    }

    public Integer getIdbus() {
        return idbus;
    }

    public void setIdbus(Integer idbus) {
        this.idbus = idbus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idticket != null ? idticket.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ticket)) {
            return false;
        }
        Ticket other = (Ticket) object;
        if ((this.idticket == null && other.idticket != null) || (this.idticket != null && !this.idticket.equals(other.idticket))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Ticket[ idticket=" + idticket + " ]";
    }
    
}
