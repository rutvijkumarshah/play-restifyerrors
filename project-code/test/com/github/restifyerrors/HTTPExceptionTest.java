package com.github.restifyerrors;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.github.restifyerrors.exceptions.HTTPErrorType;
import com.github.restifyerrors.exceptions.HTTPException;
public class HTTPExceptionTest {

	@Test
	public void testHTTPException(){
		HTTPException e =null;
		
		e =new HTTPException();
		assertEquals(e.getHttpErrorType(), HTTPErrorType.INTERNAL_SERVER_ERROR);
		assertNull(e.getMessageKey());
		assertEquals("{}",e.getJSON().toString());
		
		e =new HTTPException(HTTPErrorType.BAD_REQUEST, "BAD");
		assertEquals(e.getHttpErrorType(), HTTPErrorType.BAD_REQUEST);
		assertNull(e.getMessageKey());
		assertEquals("{\"message\":\"BAD\"}",e.getJSON().toString());
		
		e =new HTTPException(HTTPErrorType.BAD_REQUEST, "BAD",new Exception());
		assertEquals(e.getHttpErrorType(), HTTPErrorType.BAD_REQUEST);
		assertNull(e.getMessageKey());
		assertEquals("{\"message\":\"BAD\"}",e.getJSON().toString());
		
		
		e =new HTTPException(HTTPErrorType.BAD_REQUEST, "BAD",new Exception(),"MSG_KEY");
		assertEquals(e.getHttpErrorType(), HTTPErrorType.BAD_REQUEST);
		assertNotNull(e.getMessageKey());
		assertEquals("{\"message\":\"BAD\",\"messageKey\":\"MSG_KEY\"}",e.getJSON().toString());
		
		
		e =new HTTPException(HTTPErrorType.BAD_REQUEST, "BAD",new Exception(),"MSG_KEY",new HashMap<String,String>());
		assertEquals(e.getHttpErrorType(), HTTPErrorType.BAD_REQUEST);
		assertNotNull(e.getMessageKey());
		assertEquals("{\"message\":\"BAD\",\"messageKey\":\"MSG_KEY\"}",e.getJSON().toString());
		
		e =new HTTPException(HTTPErrorType.BAD_REQUEST, "BAD",new Exception(),"MSG_KEY",null);
		assertEquals(e.getHttpErrorType(), HTTPErrorType.BAD_REQUEST);
		assertNotNull(e.getMessageKey());
		assertEquals("{\"message\":\"BAD\",\"messageKey\":\"MSG_KEY\"}",e.getJSON().toString());
		
		
		Map<String,String> info=new HashMap<String,String>();
		info.put("key1","value1");
		info.put("key2","value3");
		e =new HTTPException(HTTPErrorType.BAD_REQUEST, "BAD",new Exception(),"MSG_KEY",info);
		assertEquals(e.getHttpErrorType(), HTTPErrorType.BAD_REQUEST);
		assertNotNull(e.getMessageKey());
		assertEquals("{\"message\":\"BAD\",\"messageKey\":\"MSG_KEY\",\"infos\":{\"key2\":\"value3\",\"key1\":\"value1\"}}",e.getJSON().toString());
		
	}
}
