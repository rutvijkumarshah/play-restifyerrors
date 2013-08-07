package com.github.restifyerrors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import play.mvc.With;


/***
 * 
 * RESTifyErrors annotation for controller methods to convert exceptions to HTTP
 * Error code.
 * 
 * This annotation MUST be applied to method and SHOULD be the last annotation
 * before method body starts.
 *  
 * for example, if controller methods have more than
 * one annotation to apply add this annotation to last
 * 
 * <pre>
 * {@code
 *   	
 * 		@ThirdPartyFeature
 * 		@BodyParser.Of(Json.class)
 * 		@RESTifyErrors
 * 		public static Result getUsers(String userId){
 * 			//Some code to return result 
 * 		}
 * 
 * }
 * </pre>

 * 
 * @author Rutvijkumar Shah
 * 
 */
@With(RESTfulErrorHandler.class)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RESTifyErrors {

}