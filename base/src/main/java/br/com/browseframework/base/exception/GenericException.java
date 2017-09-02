package br.com.browseframework.base.exception;

/**
 * Use to throw an application generic error.
 * @author Eduardo
 *
 */
public class GenericException extends Throwable {

	private static final long serialVersionUID = 4192224454831268008L;

	private String localizedMessage;

	public GenericException(String message, Throwable t, String localizedMessage) {
		super(message, t);
		this.localizedMessage = localizedMessage;
	}

	@Override
	public String getLocalizedMessage() {
		return this.localizedMessage;
	}
}
