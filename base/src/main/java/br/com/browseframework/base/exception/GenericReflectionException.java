package br.com.browseframework.base.exception;

/**
 * Use to throw an reflection generic error.
 * @author Eduardo
 *
 */
public class GenericReflectionException extends BusinessException {

	private static final long serialVersionUID = -7394409501802720407L;

	public GenericReflectionException(String message) {
		super(message);
	}

	public GenericReflectionException(String message, Throwable t) {
		super(message, t);
	}
	
}
