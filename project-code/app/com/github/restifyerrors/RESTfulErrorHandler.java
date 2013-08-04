package com.github.restifyerrors;


import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

import com.avaje.ebean.InvalidValue;
import com.github.restifyerrors.exceptions.HTTPException;
import com.github.restifyerrors.exceptions.HTTPErrorType;

import play.Logger;
import play.Logger.ALogger;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
/***
 * 
 * RestfulErrorHandler converts any exception thrown from Service/API layer to proper HTTP Error. 
 * This will be annotated on Controller methods.
 * 
 * @author Rutvijkumar Shah
 *
 */
public class RESTfulErrorHandler extends Action.Simple {
	
	private static ALogger logger=Logger.of(RESTfulErrorHandler.class);
	
	@Override
	public Result call(Context arg0) throws Throwable {
		Result result=null;
		try{
			try{
				//Intercepts the call and delegate to controller
				result= delegate.call(arg0);
			}catch (Exception e) {
				//Catch any exception log it & tries to convert known exception to APIException
				logger.error("Exception :"+e,e);
				translateToKnownException(e);
			}
		}catch (HTTPException e) {
			//Mapping API Exception to HTTP Errors 
			JsonNode json = e.getJSON();
			if(e.getHttpErrorType().equals(HTTPErrorType.BAD_REQUEST)){
				result=badRequest(json);
			}else if (e.getHttpErrorType().equals(HTTPErrorType.FORBIDDEN_REQUEST)){
				result=forbidden(json);
			}else if (e.getHttpErrorType().equals(HTTPErrorType.NOT_FOUND_REQUEST)){
				result=notFound(json);
			}else if (e.getHttpErrorType().equals(HTTPErrorType.UN_AUTHORIZED_REQUEST)){
				result=unauthorized(json);
			}else if (e.getHttpErrorType().equals(HTTPErrorType.INTERNAL_SERVER_ERROR)){
				result=internalServerError(json);
			}
			
		}catch (Exception e) {
			result= internalServerError("Internal Error");
		}
		return result;
	}
	/****
	 * 
	 * @param e
	 * @throws Exception
	 */
	private void translateToKnownException(Exception e) throws Exception{
		
		//This is thrown from EBean ORM Validation 
		if ( e instanceof com.avaje.ebean.ValidationException){
			com.avaje.ebean.ValidationException ve=(com.avaje.ebean.ValidationException)e;
			InvalidValue[] invalidValues = ve.getErrors();
			String message="Invalid properties";
			Map<String,String> invalidProps=new HashMap<String, String>();
			for (InvalidValue invalidValue : invalidValues) {
				String propertyName = invalidValue.getPropertyName();
				invalidProps.put(propertyName, invalidValue.getValue().toString());
			}
			//Marking it as bad request
			throw new HTTPException(HTTPErrorType.BAD_REQUEST,message,e,null,invalidProps);
		}
		
		
		throw e;
		
	}

}
