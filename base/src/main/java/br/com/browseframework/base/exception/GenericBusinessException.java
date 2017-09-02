package br.com.browseframework.base.exception;

import br.com.browseframework.base.exception.enums.BusinessExceptionMessage;

/**
 * Use to throw an application business error.
 * @author Eduardo
 *
 */
public class GenericBusinessException extends BusinessException {

	private static final long serialVersionUID = 8680830505310453996L;

	public GenericBusinessException(Throwable t) {
		super(null, null, null, t != null ? t.getMessage() : "An unexpected error occurred in the business layer, more details below.", t);
	}
	
	public GenericBusinessException(Long errorCode, Throwable t) {
		super(errorCode, BusinessExceptionMessage.GENERIC_ERROR.getKey(), null, "An unexpected error occurred in the business layer, more details below.", t);
	}
	
	public GenericBusinessException(Long errorCode, String message){
		super(errorCode, null, null, message, null);
	}
	
	public GenericBusinessException(String message){
		super( (String) null , message);
	}
	
	public GenericBusinessException(String messageKey, String message){
		super(messageKey, message);
	}
}
