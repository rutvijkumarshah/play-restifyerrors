package com.github.restifyerrors;

import com.github.restifyerrors.exceptions.HTTPErrorType;

/***
 * Object which captures exception information
 * 
 * 
 * @author Rutvijkumar Shah
 *
 */
 class ExceptionInfo {
	private HTTPErrorType errorType;
	public HTTPErrorType getErrorType() {
		return errorType;
	}
	private Class exceptionClass;
	private boolean subclassesConsidered;
	private ResultBuilder resultBuilder;
	
	public ExceptionInfo(Class exceptionKlass,HTTPErrorType httpError,boolean subclassesConsidered){
		this.exceptionClass=exceptionKlass;
		this.errorType=httpError;
		this.subclassesConsidered=subclassesConsidered;
	}
	public ExceptionInfo(Class exceptionKlass,ResultBuilder builder,boolean subclassesConsidered){
		this.exceptionClass=exceptionKlass;
		this.resultBuilder=builder;
		this.subclassesConsidered=subclassesConsidered;
	}
	
	public Class getExceptionClass() {
		return exceptionClass;
	}
	public boolean isSubclassesConsidered() {
		return subclassesConsidered;
	}
	public ResultBuilder getResultBuilder() {
		return resultBuilder;
	}
	
}
