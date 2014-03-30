Play RestifyErrors [![Build Status](https://travis-ci.org/rutvijkumarshah/play-restifyerrors.svg?branch=master)](https://travis-ci.org/rutvijkumarshah/play-restifyerrors)
==============
RestifyErrors is micro-framework for exception mapping and converting exceptions into RESTFul responses for Play Applications.

RestifyErrors provides non-intrusive way to manage error RESTFul responses in your play application.
Using RestifyErrors moudle Play application does not need to explicitly code to return appropriate error responses.


Example :
---------------------------------
In this code snippet API Controller's addUser() method is annotated with @RESTifyErrors. This will take care of any exception thrown from this method and converting it to HTTP Response code.
```
public class APIController extends Controller {

    @RESTifyErrors
    @BodyParser.Of(BodyParser.Json.class)
    public static Result addUser(){

        JsonNode reqJson = request().body().asJson();
        Helper.addUser(reqJson);
        return created();
    }
     .........
}
```
Default HTTP response code for any exception is 500 (Internal server Error). 
Application can choose to use HTTPException (shipped with RestifyError) to provide more information. 
As shown in following code snippet.

As HttpException is runtime exception it does not requires to change your code interfaces/hiearchies. Application can choose its own custom exceptions and register with RESTifyErrors (refer more details section)
```
public class Helper {
      ....
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
            //Populating information map which will be sent as JSON with HTTP Response    
            for (InvalidValue invalidValue : ve.getErrors()) {
                if(invalidValue.getValue()!=null){
                    value=invalidValue.getValue().toString();
                }
                infos.put(invalidValue.getPropertyName(),value);
            }
            throw new HTTPException(HTTPErrorType.BAD_REQUEST,"Bad Request",ve,"user-bad-request",infos);
        }

    }

     ....
     
}

```

Configuration in Build.scala:
------------------
- Add restify_errors in application dependencies section 
```
    "restify_errors" % "restify_errors_2.10" % "0.0.7"
```
- Add URL resolver to Restify releases git repo
```
resolvers += Resolver.url("Restify Git Releases Repository", url("http://rutvijkumarshah.github.io/releases/"))(Resolver.ivyStylePatterns)
    )
```
Following is sample Build.scala
```
import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "sample-using-httpException"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "mysql" % "mysql-connector-java" % "5.1.24",
    "restify_errors" % "restify_errors_2.10" % "0.0.7"
  )

   val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      resolvers += Resolver.url("Restify Git Releases Repository", url("http://rutvijkumarshah.github.io/releases/"))(Resolver.ivyStylePatterns)
    )
}

```

Sample Applications:
--------------------------------------
Sampple application show casing how to use RestifyErrors is under sample dir.
```
git clone https://github.com/rutvijkumarshah/play-restifyerrors.git
cd play-restifyerrors/sample/sample-using-httpException
```

Contributions :
--------------
Currently this module is only supported for Java. Any pull request for scala implementation is welcome.

Version:
---------
Current Version : 0.0.7
