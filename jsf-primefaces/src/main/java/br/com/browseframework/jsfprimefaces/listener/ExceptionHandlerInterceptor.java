package br.com.browseframework.jsfprimefaces.listener;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.PhaseId;

import org.apache.log4j.Logger;

import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;

/**
 * Provides a implementation of ExceptionHandler.
 * @author Eduardo
 *
 */
public class ExceptionHandlerInterceptor extends ExceptionHandlerWrapper {

	static final Logger log = Logger.getLogger(ExceptionHandlerInterceptor.class);	
	private ExceptionHandler wrapped;

	/*
	 * (non-Javadoc)
	 * @see javax.faces.context.ExceptionHandlerWrapper#handle()
	 */
	@Override
	public void handle() throws FacesException {
		// Iterates over all exceptions
		final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
		
		while (i.hasNext()) {
			try {
				final ExceptionQueuedEvent event = (ExceptionQueuedEvent) i.next();
				final ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
	
				// Obtains the exception
				Throwable t = context.getException();
				// Obtains cause
				Throwable cause = context.getException().getCause();

				// Checks if there is ascertainable cause
				if (cause == null) {
					log.error("No ascertainable cause [" + t != null ? t.getMessage() : "NO EXCEPTION" + "]");
				}
				
				// Obtains real cause and exception
				String msg = null;
				if (cause != null){
					int limit = 20;
					while (cause != null && limit > 0){
						if (cause.getCause() == null && cause.getMessage() != null){
							msg = cause.getMessage();
							t = cause;
							break;
						} else if (cause.getCause() != null){
							cause = cause.getCause();
						}
						limit--;
					}
				}
				
				// Set default message
				if (msg == null && cause != null && cause.getMessage() != null){
					msg = "An unexpected error occurred in the business layer, more details below.";
				}

				// If it is in a loading phase redirects to an error page
				if (FacesContext.getCurrentInstance().getCurrentPhaseId().equals(PhaseId.RENDER_RESPONSE) || cause == null || (cause != null && cause.getMessage() == null) ) {
					try {
						doNotifyFatalError(t, cause);
					} catch (Exception e){
						log.error("Error on notify fatal error [" + e.getMessage() + "]");
					}
					doDisplayOnLoadingPhase(t, msg);	
				} else {
					try {
						doNotifyDisplayError(t, cause);
					} catch (Exception e){
						log.error("Error on notify display error [" + e.getMessage() + "]");
					}
					// Displays on screen the error case is another phase
					doDisplayOnScreen(t, cause);					
				}

			} finally {
				// After being captured exception removes the queue
				i.remove();
			}
		}
		
		getWrapped().handle();
	}

	/**
	 * If it is in a loading phase redirects to an error page
	 * @param t
	 * @param msg
	 */
	public void doDisplayOnLoadingPhase(Throwable t, String msg) {
		try {
			throw t;
		} catch (Throwable e) {
			log.error("Not able to throw exception [" + msg + "]");
			throw new GenericBusinessException(e);
		}
	}

	/**
	 * Displays on screen the error case is another phase
	 * @param t
	 * @param cause
	 */
	public void doDisplayOnScreen(Throwable t, Throwable cause) {
		String message = null;
		String detail = null;
		if (GenericBusinessException.class.isInstance(t)){
			final GenericBusinessException gbe = (GenericBusinessException) t;
			message = FacesUtil.getResourceBundleString(gbe.getMessageKey());
		} else {
			if (t != null){
				message = t.getMessage();
				if (cause != null){
					detail = cause.getMessage();
				}
			}
		}
		FacesContext.getCurrentInstance().addMessage(null, new MessageUtil(FacesMessage.SEVERITY_ERROR, message, detail));
		log.error("Error trapped and added message on screen. Error [" + message + "] Detail [" + detail + "]");
	}

	/**
	 * For third purpose.
	 * @param t
	 * @param cause
	 */
	public void doNotifyFatalError(Throwable t, Throwable cause) {
		// for specific use
	}

	/**
	 * For third purpose.
	 * @param t
	 * @param cause
	 */
	public void doNotifyDisplayError(Throwable t, Throwable cause) {
		// for specific use
	}

	// GETTERS && SETTERS
	
	public ExceptionHandlerInterceptor(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return wrapped;
	}

}