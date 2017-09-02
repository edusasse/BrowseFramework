package br.com.browseframework.base.exception;

import br.com.browseframework.base.exception.enums.BusinessExceptionMessage;

/**
 * Used when no user is logged in.
 * @author Eduardo
 *
 */
public class AuthenticationException extends BusinessException {

	private static final long serialVersionUID = -4459676713111562569L;

	public AuthenticationException() {
		super(BusinessExceptionMessage.AUTHENTICATION_ERROR.getKey(), "User not logged in, please login.");
	} 
	
	public AuthenticationException(String message) {
		super(BusinessExceptionMessage.AUTHENTICATION_ERROR.getKey(), message);
	}
}