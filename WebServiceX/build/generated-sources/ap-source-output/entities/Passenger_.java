package entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.0.v20130507-rNA", date="2013-10-17T21:19:59")
@StaticMetamodel(Passenger.class)
public class Passenger_ { 

    public static volatile SingularAttribute<Passenger, Date> creditcardvalidity;
    public static volatile SingularAttribute<Passenger, Integer> creditcardnumber;
    public static volatile SingularAttribute<Passenger, String> name;
    public static volatile SingularAttribute<Passenger, String> surname;
    public static volatile SingularAttribute<Passenger, String> login;
    public static volatile SingularAttribute<Passenger, String> creditcardtype;
    public static volatile SingularAttribute<Passenger, String> password;

}