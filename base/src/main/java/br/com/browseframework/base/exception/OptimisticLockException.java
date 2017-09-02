package br.com.browseframework.base.exception;

import br.com.browseframework.base.exception.enums.BusinessExceptionMessage;

/**
 * Thrown when two users attempt to change/delete the same record in the database sametime
 * @author Eduardo
 *
 */
public class OptimisticLockException extends BusinessException {
	
	private static final long serialVersionUID = -5977262501526699276L;

	public OptimisticLockException() {
	super(BusinessExceptionMessage.OPTIMISTICLOCK_ERROR.getKey(), "Record changed or deleted by another user, please restart the process.");
    }
}