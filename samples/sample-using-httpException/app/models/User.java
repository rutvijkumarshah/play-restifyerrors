package models;

import play.db.ebean.Model;
import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

/**
 * Created with IntelliJ IDEA.
 * User: Rutvijkumar Shah
 * Date: 8/19/13
 * Time: 8:01 AM
 * To change this template use File | Settings | File Templates.
 */

@Entity
public class User extends Model {

    @Id
    @Constraints.Min(10)
    public Long id;

    @Constraints.Required
    public String name;

    @Constraints.Email
    public String emailAddres;

    public static Finder<Long,User> find = new Finder<Long,User>(
            Long.class, User.class);

}
