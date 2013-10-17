package entities;

import entities.Ticketusedinbus;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.0.v20130507-rNA", date="2013-10-17T21:19:59")
@StaticMetamodel(Ticket.class)
public class Ticket_ { 

    public static volatile SingularAttribute<Ticket, Boolean> isused;
    public static volatile SingularAttribute<Ticket, Ticketusedinbus> ticketusedinbus;
    public static volatile SingularAttribute<Ticket, Date> timeodvalidation;
    public static volatile SingularAttribute<Ticket, Boolean> isvalidated;
    public static volatile SingularAttribute<Ticket, String> type;
    public static volatile SingularAttribute<Ticket, String> idticket;

}