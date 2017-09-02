package br.com.browseframework.base.exception;

/**
 * Use to throw an application permission error.
 * @author Eduardo
 *
 */
public class PermissionException extends BusinessException {

	private static final long serialVersionUID = 2783735924532692246L;

	public PermissionException(String message) {
		super(message, (String) null);
	}
}
