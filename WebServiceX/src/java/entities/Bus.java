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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Carlos
 */
@Entity
@Table(name = "bus")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bus.findAll", query = "SELECT b FROM Bus b"),
    @NamedQuery(name = "Bus.findByIdbus", query = "SELECT b FROM Bus b WHERE b.idbus = :idbus")})
public class Bus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idbus")
    private Integer idbus;

    public Bus() {
    }

    public Bus(Integer idbus) {
        this.idbus = idbus;
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
        hash += (idbus != null ? idbus.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bus)) {
            return false;
        }
        Bus other = (Bus) object;
        if ((this.idbus == null && other.idbus != null) || (this.idbus != null && !this.idbus.equals(other.idbus))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Bus[ idbus=" + idbus + " ]";
    }
    
}
