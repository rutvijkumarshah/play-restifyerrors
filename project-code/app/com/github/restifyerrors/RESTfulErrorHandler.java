package com.github.restifyerrors;



import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;

import com.github.restifyerrors.exceptions.HTTPErrorType;
import com.github.restifyerrors.exceptions.HTTPException;
/***
 * 
 * RestfulErrorHandler converts any exception thrown from Service/API layer to proper HTTP Error. 
 * This will be annotated on Controller methods.
 * 
 * @author Rutvijkumar Shah
 *
 */
public class RESTfulErrorHandler extends Action.Simple {
	
	private Result resultFromHttpErrorType(HTTPErrorType type,JsonNode json){
		Result result=null;
		if(type ==null ){
			result=internalServerError(json);
		}
		else if(type.equals(HTTPErrorType.BAD_REQUEST)){
			result=badRequest(json);
		}else if (type.equals(HTTPErrorType.FORBIDDEN_REQUEST)){
			result=forbidden(json);
		}else if (type.equals(HTTPErrorType.NOT_FOUND_REQUEST)){
			result=notFound(json);
		}else if (type.equals(HTTPErrorType.UN_AUTHORIZED_REQUEST)){
			result=unauthorized(json);
		}else{
			result=internalServerError(json);
		}
		return result;
	}
	
	
	@Override
	public Result call(Context arg0) throws Throwable {
		Result result=null;
		try{
			//Intercepts the call and delegate to controller
			result= delegate.call(arg0);
		}catch (HTTPException e) {
			//Mapping API Exception to HTTP Errors 
			JsonNode json = e.getJSON();
			result=this.resultFromHttpErrorType(e.getHttpErrorType(), json);
		}catch (Exception e) {
			
			result= internalServerError("Internal Error");//Default result
			//checking registry to get exception mapping
			ExceptionInfo exInfo=RestifyErrorsRegistry.getExceptionInfo(e);
			
			if(exInfo !=null){
				
				if(exInfo.getErrorType() !=null){
					ObjectNode jsonObj = play.libs.Json.newObject();
					jsonObj.put("error",exInfo.getErrorType().toString());
					result=this.resultFromHttpErrorType(exInfo.getErrorType(), jsonObj);
				}
				if(exInfo.getResultBuilder() !=null){
					result=exInfo.getResultBuilder().getResult(e);
				}
				
			}
		}
		return result;
	}
	

}
