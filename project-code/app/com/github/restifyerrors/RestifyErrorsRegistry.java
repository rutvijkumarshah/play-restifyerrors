package com.github.restifyerrors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.restifyerrors.exceptions.HTTPErrorType;


/***
 * Registry for registering exception and its mappings.
 * This class will drive custom exception and its mapping to Restful error codes.
 * Registry supports "static" as well as "dynamic" mapping for exception class.
 * 
 * "static" mapping  be used as one to one mapping for Exception class and its mapping ( http error code or ResultBuilder)
 * static mapping will look for exact class name for exception for mapping it will not consider subclasses of registered exception class.
 * 
 * "dynamic" mapping consider all subclasses for this mapping.
 * dynamic mapping will be executed in order it registered. 
 * 
 * 
 * @author Rutvijkumar Shah
 *
 */
@SuppressWarnings("rawtypes")
public final class RestifyErrorsRegistry {

	/***
	 * Registry map for mapping exception wit this info.
	 */
	
	private final static Map<Class,ExceptionInfo> staticRegistry=new HashMap<Class,ExceptionInfo>();
	private final static ArrayList<ExceptionInfo> dynamicRegistryList=new ArrayList<ExceptionInfo>();
	
	
	/***
	 * Registers Exception class with HttpError.
	 * Whenever application throws exception which matches with registered exception's class then it will return registered httpError code in response
	 *  
	 * @param exceptionClass
	 * @param httpError
	 */
	public static void registerStaticMapping(final Class exceptionClass,final HTTPErrorType httpError){
		if(exceptionClass!=null ){
			staticRegistry.put(exceptionClass, new ExceptionInfo(exceptionClass, httpError,false));
		}
	}	

	/***
	 * Registers Exception class with ResultBuilder
	 * Whenever application throws exception which matches with registered exception's class then it executes registered builder and returns response.
	 *  
	 * @param exceptionClass
	 * @param httpError
	 */
	public static void registerStaticMapping(final Class exceptionClass,final ResultBuilder builder){
		if(exceptionClass!=null && builder  !=null){
			staticRegistry.put(exceptionClass, new ExceptionInfo(exceptionClass,builder,false ));
		}
	}
	

	/***
	 * Registers Exception class & all its subclasses with HttpError.
	 * Whenever application throws exception which matches with registered exception's class  or pass instance-of test,it will return registered httpError code in response.
	 *  
	 * @param exceptionClass
	 * @param httpError
	 */
	public static void registerDynamicMapping(final Class exceptionClass,final HTTPErrorType httpError){
		if(exceptionClass!=null ){
			dynamicRegistryList.add(new ExceptionInfo(exceptionClass, httpError,true));
		}
	}
	
	/***
	 * Registers Exception class & all its subclasses with with ResultBuilder.
	 * Whenever application throws exception which matches with registered exception's class or pass instance-of test, it executes registered builder and returns response.
	 *  
	 * @param exceptionClass
	 * @param httpError
	 */
	public static void registerDynamicMapping(final Class exceptionClass,final ResultBuilder builder){
		if(exceptionClass!=null && builder  !=null){
			dynamicRegistryList.add(new ExceptionInfo(exceptionClass,builder,true));
		}
		
	}
	// Non public internal API
	/***
	 * Returns registered Exception Information
	 * @param errorObj
	 * @return
	 */
	 protected static ExceptionInfo getExceptionInfo(final Throwable errorObj){
		 ExceptionInfo exceptionInfo=null;
		 if(errorObj!=null){
			//Checking with static registry
				exceptionInfo = staticRegistry.get(errorObj.getClass());
				//Exception not found in static registry
				//Looking up into dynamic registry
				if(exceptionInfo==null){
					for (ExceptionInfo registeredException : dynamicRegistryList) {
						
						//Matched with dynamic Registry's exception 
						if(registeredException.getExceptionClass().equals(errorObj.getClass())){
							exceptionInfo=registeredException;
							break;
						}
						if(registeredException.getExceptionClass().isInstance(errorObj)){
							exceptionInfo=registeredException;
							break;
						}
					}
				}
		 }
		return exceptionInfo;
	} 
	 
	/**
	 * Clears Registry by clearing all items from static registry map and dynamic registry list
	 * 
	 */
	protected static void clearRegistry(){
		staticRegistry.clear();
		dynamicRegistryList.clear();
	}
}
