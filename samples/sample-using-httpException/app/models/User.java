package models;

import com.avaje.ebean.validation.Email;
import com.avaje.ebean.validation.NotNull;
import play.db.ebean.Model;
import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.db.ebean.Model;


@Entity
public class User extends Model {

    @Id
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Email
    private String emailAddress;

    public static Finder<Long,User> find = new Finder<Long,User>(
            Long.class, User.class);


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

}
