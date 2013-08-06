package com.github.restifyerrors;
import org.junit.*;
import static org.junit.Assert.*;
import com.github.restifyerrors.ExceptionInfo;
import com.github.restifyerrors.RestifyErrorsRegistry;
import com.github.restifyerrors.exceptions.HTTPErrorType;

import play.mvc.*;
import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

/***
 * 
 * @author Rutvijkumar Shah
 *
 */
public class ExceptionRegistryTest {


    @Before
    public void setUp(){
    	RestifyErrorsRegistry.registerStaticMapping(IllegalArgumentException.class, HTTPErrorType.BAD_REQUEST);
    	RestifyErrorsRegistry.registerDynamicMapping(Exception.class, HTTPErrorType.INTERNAL_SERVER_ERROR);
    }
    
	@Test
	public void staticMappingTest() {
		IllegalArgumentException ex=new IllegalArgumentException();
		ExceptionInfo exceptionInfo = RestifyErrorsRegistry.getExceptionInfo(ex);
		assertNotNull(exceptionInfo);
		assertEquals(HTTPErrorType.BAD_REQUEST, exceptionInfo.getErrorType());
		assertNull(exceptionInfo.getResultBuilder());

	}
	@Test
	public void dynamicMappingTest() {
		RuntimeException ex=new RuntimeException();
		ExceptionInfo exceptionInfo = RestifyErrorsRegistry.getExceptionInfo(ex);
		assertNotNull(exceptionInfo);
		assertEquals(HTTPErrorType.INTERNAL_SERVER_ERROR, exceptionInfo.getErrorType());
		assertNull(exceptionInfo.getResultBuilder());

	}
}
