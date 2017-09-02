package br.com.browseframework.jsfprimefaces.listener;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * ExceptionHandlerFactory is a factory object that creates (if needed) and returns a new ExceptionHandler instance.
 * @author Eduardo
 *
 */
public class ExceptionHandlerInterceptorFactory extends ExceptionHandlerFactory {

	protected ExceptionHandlerFactory parent;

	/**
	 * Injects the handle.
	 * @param parent
	 */
	public ExceptionHandlerInterceptorFactory(ExceptionHandlerFactory parent) {
		setParent(parent);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.faces.context.ExceptionHandlerFactory#getExceptionHandler()
	 */
	@Override
	public ExceptionHandler getExceptionHandler() {
		final ExceptionHandler result = new ExceptionHandlerInterceptor(getParent().getExceptionHandler());
		return result;
	}

	// GETTERS && SETTERS 
	
	public ExceptionHandlerFactory getParent() {
		return parent;
	}

	public void setParent(ExceptionHandlerFactory parent) {
		this.parent = parent;
	}
}