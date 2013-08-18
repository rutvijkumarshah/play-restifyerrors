package com.github.restifyerrors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Result;
import play.mvc.Results;

import com.github.restifyerrors.exceptions.HTTPErrorType;
import com.github.restifyerrors.exceptions.HTTPException;
import com.github.restifyerrors.execptions.AppBaseException;
import com.github.restifyerrors.execptions.AppDerivedException;
import com.github.restifyerrors.execptions.AppDerivedIOException;

/***
 * Unit Testcaes for Registry
 * 
 * @author Rutvijkumar Shah
 *
 */
public class ExceptionRegistryTest {

    @Before
    public void setUp(){
    	RestifyErrorsRegistry.clearRegistry();
    }
    
	@Test
	public void staticMappingTest() {
		RestifyErrorsRegistry.registerStaticMapping(IllegalArgumentException.class, HTTPErrorType.BAD_REQUEST);
		IllegalArgumentException ex=new IllegalArgumentException();
		ExceptionInfo exceptionInfo = RestifyErrorsRegistry.getExceptionInfo(ex);
		assertNotNull(exceptionInfo);
		assertEquals(HTTPErrorType.BAD_REQUEST, exceptionInfo.getErrorType());
		assertNull(exceptionInfo.getResultBuilder());

	}
	@Test
	public void dynamicMappingTest() {
		RestifyErrorsRegistry.registerDynamicMapping(Exception.class, HTTPErrorType.INTERNAL_SERVER_ERROR);
		RuntimeException ex=new RuntimeException();
		ExceptionInfo exceptionInfo = RestifyErrorsRegistry.getExceptionInfo(ex);
		assertNotNull(exceptionInfo);
		assertEquals(HTTPErrorType.INTERNAL_SERVER_ERROR, exceptionInfo.getErrorType());
		assertNull(exceptionInfo.getResultBuilder());

	}
	@Test
	public void dynamicMappingTestWithSubTypes() {
		RestifyErrorsRegistry.registerDynamicMapping(AppBaseException.class, HTTPErrorType.FORBIDDEN_REQUEST);
		RestifyErrorsRegistry.registerDynamicMapping(AppDerivedException.class, HTTPErrorType.BAD_REQUEST);
		
		Exception ex=new AppDerivedIOException();
		ExceptionInfo exceptionInfo = RestifyErrorsRegistry.getExceptionInfo(ex);
		assertNotNull(exceptionInfo);
		assertEquals(HTTPErrorType.FORBIDDEN_REQUEST, exceptionInfo.getErrorType());
		assertNull(exceptionInfo.getResultBuilder());
		
		
		ex=new AppDerivedException();
		exceptionInfo = RestifyErrorsRegistry.getExceptionInfo(ex);
		assertNotNull(exceptionInfo);
		assertEquals(HTTPErrorType.FORBIDDEN_REQUEST, exceptionInfo.getErrorType());
		assertNull(exceptionInfo.getResultBuilder());

	}
	@Test
	public void dynamicAndStaticMappingTest() {
		setupData_without_builder();
		
		Exception ex=new AppDerivedIOException();
		ExceptionInfo exceptionInfo = RestifyErrorsRegistry.getExceptionInfo(ex);
		assertNotNull(exceptionInfo);
		assertEquals(HTTPErrorType.UN_AUTHORIZED_REQUEST, exceptionInfo.getErrorType());
		assertNull(exceptionInfo.getResultBuilder());
		
		
		ex=new AppBaseException();
		exceptionInfo = RestifyErrorsRegistry.getExceptionInfo(ex);
		assertNotNull(exceptionInfo);
		assertEquals(HTTPErrorType.FORBIDDEN_REQUEST, exceptionInfo.getErrorType());
		assertNull(exceptionInfo.getResultBuilder());
		
		ex=new AppDerivedException();
		exceptionInfo = RestifyErrorsRegistry.getExceptionInfo(ex);
		assertNotNull(exceptionInfo);
		assertEquals(HTTPErrorType.FORBIDDEN_REQUEST, exceptionInfo.getErrorType());
		assertNull(exceptionInfo.getResultBuilder());
		
		//HTTPException should NOT be used in registry
		//As Registry rules creates confusion in expected error type
		ex=new HTTPException(HTTPErrorType.BAD_REQUEST, null);
		exceptionInfo = RestifyErrorsRegistry.getExceptionInfo(ex);
		assertNotNull(exceptionInfo);
		assertEquals(HTTPErrorType.INTERNAL_SERVER_ERROR, exceptionInfo.getErrorType());
		assertNull(exceptionInfo.getResultBuilder());
		
		ex=new FileNotFoundException();
		exceptionInfo = RestifyErrorsRegistry.getExceptionInfo(ex);
		assertNull(exceptionInfo);
		
		exceptionInfo = RestifyErrorsRegistry.getExceptionInfo(null);
		assertNull(exceptionInfo);
		

	}
	
	private void setupData_without_builder(){
		
		RestifyErrorsRegistry.registerStaticMapping(IOException.class, HTTPErrorType.INTERNAL_SERVER_ERROR);
		RestifyErrorsRegistry.registerStaticMapping(IllegalArgumentException.class, HTTPErrorType.BAD_REQUEST);
		RestifyErrorsRegistry.registerStaticMapping(AppDerivedIOException.class, HTTPErrorType.UN_AUTHORIZED_REQUEST);
		RestifyErrorsRegistry.registerStaticMapping(HTTPException.class, HTTPErrorType.INTERNAL_SERVER_ERROR);
		
		
		RestifyErrorsRegistry.registerDynamicMapping(AppBaseException.class, HTTPErrorType.FORBIDDEN_REQUEST);
		RestifyErrorsRegistry.registerDynamicMapping(AppDerivedException.class, HTTPErrorType.BAD_REQUEST);
		RestifyErrorsRegistry.registerDynamicMapping(RuntimeException.class, HTTPErrorType.UN_AUTHORIZED_REQUEST);
		
		RestifyErrorsRegistry.registerStaticMapping(null, HTTPErrorType.INTERNAL_SERVER_ERROR);
		RestifyErrorsRegistry.registerDynamicMapping(null, HTTPErrorType.UN_AUTHORIZED_REQUEST);
		
	}
	
	private ResultBuilder<Throwable> getBuilder(final Result result){
		ResultBuilder<Throwable> builder=new ResultBuilder<Throwable>() {
			
			@Override
			public Result getResult(Throwable exception) {
				return result;
			}
		};
		return builder;
	}
	private void setupData_with_builder(){
	
		RestifyErrorsRegistry.registerStaticMapping(IOException.class, getBuilder(Results.internalServerError()));
		RestifyErrorsRegistry.registerStaticMapping(IllegalArgumentException.class, getBuilder(Results.badRequest()));
		RestifyErrorsRegistry.registerStaticMapping(AppDerivedIOException.class, getBuilder(Results.unauthorized()));
		RestifyErrorsRegistry.registerStaticMapping(HTTPException.class,getBuilder(Results.internalServerError()));
		
		
		RestifyErrorsRegistry.registerDynamicMapping(AppBaseException.class, getBuilder(Results.forbidden()));
		RestifyErrorsRegistry.registerDynamicMapping(AppDerivedException.class, getBuilder(Results.badRequest()));
		RestifyErrorsRegistry.registerDynamicMapping(RuntimeException.class,getBuilder(Results.unauthorized()));
		
		ResultBuilder<Throwable>  nullBuilder=null;
		RestifyErrorsRegistry.registerStaticMapping(null, nullBuilder);
		RestifyErrorsRegistry.registerDynamicMapping(null, nullBuilder);
		
		Class klass=null;
		RestifyErrorsRegistry.registerStaticMapping(klass, nullBuilder);
		RestifyErrorsRegistry.registerDynamicMapping(klass, nullBuilder);
		

		RestifyErrorsRegistry.registerStaticMapping(IOException.class, nullBuilder);
		RestifyErrorsRegistry.registerDynamicMapping(RuntimeException.class, nullBuilder);
		
	}
	
	@Test
	public void dynamicAndStaticMappingWithBuilderTest() {
		setupData_with_builder();
		
		Exception ex=new AppDerivedIOException();
		ExceptionInfo exceptionInfo = RestifyErrorsRegistry.getExceptionInfo(ex);
		assertNotNull(exceptionInfo);
		assertEquals(null, exceptionInfo.getErrorType());
		assertNotNull(exceptionInfo.getResultBuilder());
		assertEquals(Results.unauthorized().toString(), exceptionInfo.getResultBuilder().getResult(ex).toString());
		
	
		ex=new AppBaseException();//forbidden
		exceptionInfo = RestifyErrorsRegistry.getExceptionInfo(ex);
		assertNotNull(exceptionInfo);
		assertEquals(null, exceptionInfo.getErrorType());
		assertNotNull(exceptionInfo.getResultBuilder());
		assertEquals(Results.forbidden().toString(), exceptionInfo.getResultBuilder().getResult(ex).toString());
		
		ex=new AppDerivedException(); //forbidden
		exceptionInfo = RestifyErrorsRegistry.getExceptionInfo(ex);
		assertNotNull(exceptionInfo);
		assertEquals(null, exceptionInfo.getErrorType());
		assertNotNull(exceptionInfo.getResultBuilder());
		assertEquals(Results.forbidden().toString(), exceptionInfo.getResultBuilder().getResult(ex).toString());
		assertEquals(true, exceptionInfo.isSubclassesConsidered());

		//HTTPException should NOT be used in registry
		//As Registry rules creates confusion in expected error type
		ex=new HTTPException(HTTPErrorType.BAD_REQUEST, null); //internal server
		exceptionInfo = RestifyErrorsRegistry.getExceptionInfo(ex);
		assertNotNull(exceptionInfo);
		assertEquals(null, exceptionInfo.getErrorType());
		assertNotNull(exceptionInfo.getResultBuilder());
		assertEquals(Results.internalServerError().toString(), exceptionInfo.getResultBuilder().getResult(ex).toString());
		
		ex=new FileNotFoundException();
		exceptionInfo = RestifyErrorsRegistry.getExceptionInfo(ex);
		assertNull(exceptionInfo);
		
		exceptionInfo = RestifyErrorsRegistry.getExceptionInfo(null);
		assertNull(exceptionInfo);
		

	}

}
