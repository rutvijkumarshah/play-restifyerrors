package controllers;

import com.github.restifyerrors.exceptions.HTTPErrorType;
import com.github.restifyerrors.exceptions.HTTPException;
import models.User;
import org.codehaus.jackson.JsonNode;
import play.mvc.BodyParser;
import play.mvc.Controller;
import com.github.restifyerrors.*;
import play.mvc.Result;

/**
 * Created with IntelliJ IDEA.
 * User: sharu03
 * Date: 8/19/13
 * Time: 9:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class APIController extends Controller {

    @RESTifyErrors
    @BodyParser.Of(BodyParser.Json.class)
    public static Result addUser(){
        JsonNode reqJson = request().body().asJson();

        APIHelper.addUser(reqJson);
        return created();

    }

    @RESTifyErrors
    public static Result updateUser(Long id){
        throw new HTTPException(HTTPErrorType.FORBIDDEN_REQUEST,"Update operation not allowed",null,"user-update-not-allowedt");
    }

    @RESTifyErrors
    public static Result deleteUser(long userId){
        APIHelper.deleteUser(userId);
        return ok();
    }

    @RESTifyErrors
    public static Result listUsers(){
        JsonNode json=null;
       try{
        json= play.libs.Json.toJson(APIHelper.getUsers());
       }catch (Exception e){
           e.printStackTrace();
           throw e;
       }
        return ok(json);
    }
    @RESTifyErrors
    public static Result getUser(long id){
        JsonNode json=null;
        try{
            json= play.libs.Json.toJson(APIHelper.getUser(id));
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return ok(json);
    }
}
