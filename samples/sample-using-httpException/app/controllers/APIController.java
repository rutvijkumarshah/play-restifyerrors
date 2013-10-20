package controllers;

import com.github.restifyerrors.exceptions.HTTPErrorType;
import com.github.restifyerrors.exceptions.HTTPException;
import org.codehaus.jackson.JsonNode;
import play.mvc.BodyParser;
import play.mvc.Controller;
import com.github.restifyerrors.*;
import play.mvc.Result;

public class APIController extends Controller {

    @RESTifyErrors
    @BodyParser.Of(BodyParser.Json.class)
    public static Result addUser(){

        JsonNode reqJson = request().body().asJson();
        Helper.addUser(reqJson);
        return created();
    }

    @RESTifyErrors
    public static Result updateUser(Long id){
        throw new HTTPException(HTTPErrorType.FORBIDDEN_REQUEST,"Update operation not allowed",null,"user-update-not-allowed");
    }

    @RESTifyErrors
    public static Result deleteUser(long userId){
        Helper.deleteUser(userId);
        return ok();
    }

    @RESTifyErrors
    public static Result listUsers(){
        JsonNode json=null;
        json= play.libs.Json.toJson(Helper.getUsers());
        return ok(json);
    }
    @RESTifyErrors
    public static Result getUser(long id){
        JsonNode json=null;
        json= play.libs.Json.toJson(Helper.getUser(id));
        return ok(json);
    }
}
