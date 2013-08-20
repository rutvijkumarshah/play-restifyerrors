package controllers;

import com.avaje.ebean.InvalidValue;
import com.avaje.ebean.ValidationException;
import com.github.restifyerrors.exceptions.HTTPErrorType;
import com.github.restifyerrors.exceptions.HTTPException;
import models.User;
import org.codehaus.jackson.JsonNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sharu03
 * Date: 8/18/13
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class APIHelper {

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
                user.emailAddres=reqJson.get("email").asText();
            }
            if(reqJson.get("name")!=null){
                user.name=reqJson.get("name").asText();
            }
            user.save();
        }catch (ValidationException ve){
            //HTTPErrorType httpErrorType,String message,Throwable exception,String messageKey, Map<String,String> infos)
            Map<String,String> infos=new HashMap<String,String>();
            for (InvalidValue invalidValue : ve.getErrors()) {
                infos.put(invalidValue.getPropertyName(),invalidValue.getValue().toString());
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
