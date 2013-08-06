package com.github.restifyerrors.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

public class HTTPException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8864969215171408533L;
	protected String messageKey;
	
	protected Map<String,String> infos=new HashMap<String, String>();

	protected HTTPErrorType httpErrorType=HTTPErrorType.INTERNAL_SERVER_ERROR;//Default if not specified 
			
	public HTTPErrorType getHttpErrorType() {
		return httpErrorType;
	}
	public HTTPException(HTTPErrorType httpErrorType,String message){
		super(message);
		this.httpErrorType=httpErrorType;
		
	}
	public HTTPException(HTTPErrorType httpErrorType,String message,Throwable exception){
		super(message, exception);
		this.httpErrorType=httpErrorType;
		
	}
	public HTTPException(HTTPErrorType httpErrorType,String message,Throwable exception,String messageKey){
		super(message, exception);
		this.messageKey=messageKey;
		this.httpErrorType=httpErrorType;
	}

	public HTTPException(HTTPErrorType httpErrorType,String message,Throwable exception,String messageKey, Map<String,String> infos){
		super(message, exception);
		this.messageKey=messageKey;
		this.httpErrorType=httpErrorType;
		this.infos=infos;
	}


	public String getMessageKey() {
		return messageKey;
	}


	public JsonNode getJSON() {
		ObjectNode jsonObj = play.libs.Json.newObject();
		String message = this.getMessage();
		//jsonObj.put("error", "");
		if (message != null) {
			jsonObj.put("message", this.getMessage());
		}
		if (this.messageKey != null) {
			jsonObj.put("messageKey", this.messageKey);
		}
		if (this.infos != null & !this.infos.isEmpty()) {
			ObjectNode infoObj = play.libs.Json.newObject();
			for (Map.Entry<String, String>  item: infos.entrySet()) {
				infoObj.put(item.getKey(), item.getValue());
			}
			jsonObj.put("infos", infoObj);
		}
		return jsonObj;
	}
}
