package com.github.restifyerrors;

/***
 * ResultBuilder interface defines contract which will be implemented by API Consumers.
 * It provides flexibility for API Consumers to build custom "Result" by handling their custom exception.
 * 
 * HTTPException mechanism is simple and easy to use but it returns pre-defined error payload and HTTP status code.
 * If API consumers needs to override this and returns different payload or HTTP status code, they can provide implementation of ResultBuilder and register 
 * with RestifyErrorsRegistry   
 * 
 * 
 * @author Rutvijkumar Shah
 *
 * @param E any exception
 */
public interface ResultBuilder<E extends Throwable> {
	public play.mvc.Result getResult(E exception);
}
