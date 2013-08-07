package com.github.restifyerrors.exceptions;

/****
 * Represents list of HTTP Errors supported by this plugin
 * 
 * @author Rutvijkumar Shah
 *
 */
public enum HTTPErrorType{
	BAD_REQUEST,
	FORBIDDEN_REQUEST,
	NOT_FOUND_REQUEST,
	UN_AUTHORIZED_REQUEST,
	INTERNAL_SERVER_ERROR
}
