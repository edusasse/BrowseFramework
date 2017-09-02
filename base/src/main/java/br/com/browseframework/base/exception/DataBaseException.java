package br.com.browseframework.base.exception;

import br.com.browseframework.base.exception.enums.BusinessExceptionMessage;

/**
 * Used when database error occurs.
 * @author Eduardo
 *
 */
public class DataBaseException extends BusinessException {

	private static final long serialVersionUID = 4128507976954917712L;

	public DataBaseException(String messageKey, String message, Throwable t) {
		super(null, messageKey, null, message, t);
	}

	public DataBaseException(String messageKey, Throwable t) {
		super(null, messageKey, null, "An unexpected error occurred in the database, more details below.", t);
	}

	public DataBaseException(Throwable t) {
		super(null, BusinessExceptionMessage.DATABASE_ERROR.getKey(), null, "An unexpected error occurred in the database, more details below.", t);
	}
}