package br.com.browseframework.base.exception;

import br.com.browseframework.base.exception.enums.BusinessExceptionMessage;

/**
 * Use on database generic exception.
 * @author Eduardo
 *
 */
public class GenericDataBaseException extends DataBaseException {

	private static final long serialVersionUID = -7741534312312123239L;

	public GenericDataBaseException(Throwable t) {
		super(BusinessExceptionMessage.DATABASE_ERROR.getKey(), "An unexpected error occurred in the database, more details below.", t);
	}

	public GenericDataBaseException(int errorCode, Throwable t) {
		super(BusinessExceptionMessage.DATABASE_ERROR.getKey(), "An unexpected error occurred in the database, more details below. Error code ["	+ errorCode + "].", t);
	}
}