package br.com.browseframework.application.engine.exception;

/**
 * Control exception in browse application layer.
 * 
 * @author Eduardo
 * 
 */
public class BrowseApplicationException extends RuntimeException {

	private static final long serialVersionUID = -4941388354300829295L;

	public BrowseApplicationException(String message) {
		super(message, null);
	}

	public BrowseApplicationException(String message, Throwable t) {
		super(message, t);
	}

}