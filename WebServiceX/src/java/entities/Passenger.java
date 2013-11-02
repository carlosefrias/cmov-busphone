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
@Table(name = "passenger")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Passenger.findAll", query = "SELECT p FROM Passenger p"),
    @NamedQuery(name = "Passenger.findByLogin", query = "SELECT p FROM Passenger p WHERE p.login = :login"),
    @NamedQuery(name = "Passenger.findByPassword", query = "SELECT p FROM Passenger p WHERE p.password = :password"),
    @NamedQuery(name = "Passenger.findByName", query = "SELECT p FROM Passenger p WHERE p.name = :name"),
    @NamedQuery(name = "Passenger.findBySurname", query = "SELECT p FROM Passenger p WHERE p.surname = :surname"),
    @NamedQuery(name = "Passenger.findByCreditcardtype", query = "SELECT p FROM Passenger p WHERE p.creditcardtype = :creditcardtype"),
    @NamedQuery(name = "Passenger.findByCreditcardnumber", query = "SELECT p FROM Passenger p WHERE p.creditcardnumber = :creditcardnumber"),
    @NamedQuery(name = "Passenger.findByCreditcardvalidity", query = "SELECT p FROM Passenger p WHERE p.creditcardvalidity = :creditcardvalidity")})
public class Passenger implements Serializable {
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
    @Column(name = "password")
    private String password;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "surname")
    private String surname;
    @Size(max = 45)
    @Column(name = "creditcardtype")
    private String creditcardtype;
    @Column(name = "creditcardnumber")
    private Integer creditcardnumber;
    @Size(max = 20)
    @Column(name = "creditcardvalidity")
    private String creditcardvalidity;

    public Passenger() {
    }

    public Passenger(String login) {
        this.login = login;
    }

    public Passenger(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCreditcardtype() {
        return creditcardtype;
    }

    public void setCreditcardtype(String creditcardtype) {
        this.creditcardtype = creditcardtype;
    }

    public Integer getCreditcardnumber() {
        return creditcardnumber;
    }

    public void setCreditcardnumber(Integer creditcardnumber) {
        this.creditcardnumber = creditcardnumber;
    }

    public String getCreditcardvalidity() {
        return creditcardvalidity;
    }

    public void setCreditcardvalidity(String creditcardvalidity) {
        this.creditcardvalidity = creditcardvalidity;
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
        if (!(object instanceof Passenger)) {
            return false;
        }
        Passenger other = (Passenger) object;
        if ((this.login == null && other.login != null) || (this.login != null && !this.login.equals(other.login))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Passenger[ login=" + login + " ]";
    }
    
}
