package controllers;

import com.avaje.ebean.InvalidValue;
import com.avaje.ebean.ValidationException;
import com.github.restifyerrors.exceptions.HTTPErrorType;
import com.github.restifyerrors.exceptions.HTTPException;
import models.User;
import org.codehaus.jackson.JsonNode;
import com.avaje.ebean.Ebean;
import play.data.Form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Helper {

    public static List<User> getUsers(){
        return User.find.all();
    }

    public static void addUser(JsonNode reqJson){
        String userName=null;
        String email=null;
        try {
            if(reqJson == null) {
                throw new HTTPException(HTTPErrorType.BAD_REQUEST,"Bad Request:Content-Type is not application/json",null,"bad-request-json-header-missing");
            }
            User user=new User();
            if(reqJson.get("email")!=null){
                user.setEmailAddress(reqJson.get("email").asText());
            }
            if(reqJson.get("name")!=null){
                user.setName(reqJson.get("name").asText());
            }
            user.save();

        }catch (ValidationException ve){
            Map<String,String> infos=new HashMap<String,String>();
            String value=null;

            for (InvalidValue invalidValue : ve.getErrors()) {
                if(invalidValue.getValue()!=null){
                    value=invalidValue.getValue().toString();
                }
                infos.put(invalidValue.getPropertyName(),value);
            }
            throw new HTTPException(HTTPErrorType.BAD_REQUEST,"Bad Request",ve,"user-bad-request",infos);
        }

    }

    public static User getUser(Long id){
        User u=User.find.byId(id);
        if(u == null){
            throw new HTTPException(HTTPErrorType.BAD_REQUEST,"NotFound",null,"user-not-found");
        }
        return u;
    }
    public static void deleteUser(Long id){
        User u=getUser(id);
        u.delete();
    }

}
