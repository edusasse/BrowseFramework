package br.com.browseframework.jsfprimefaces.listener;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.stereotype.Component;

/**
 * Responsible for displaying messages of ERROR, WARNING and SUCCESS
 * @author Eduardo
 *
 */
@Component(value = "messageUtil")
public class MessageUtil extends FacesMessage {
 
	private static final long serialVersionUID = -7148392546270198359L;

	/**
     * 
     */
    public MessageUtil() {
	super();
    }

    /**
     * @param summary
     */
    public MessageUtil(String summary) {
	super(summary);
    }

    /**
     * @param summary
     * @param detail
     */
    public MessageUtil(String summary, String detail) {
	super(summary, detail);
    }

    /**
     * @param severity
     * @param summary
     * @param detail
     */
    public MessageUtil(Severity severity, String summary, String detail) {
	super(severity, summary, detail);
    }

    /**
     * Message severity level indicating an informational message rather than an
     * error.
     * 
     * @param summary
     * @param detail
     */
    public static void info(String summary, String detail) {
	FacesContext.getCurrentInstance().addMessage(null, new MessageUtil(FacesMessage.SEVERITY_INFO, summary, detail));
    }

    /**
     * Message severity level indicating that an error might have occurred.
     * 
     * @param summary
     * @param detail
     */
    public static void warn(String summary, String detail) {
	FacesContext.getCurrentInstance().addMessage(null,
		new MessageUtil(FacesMessage.SEVERITY_WARN, summary, detail));
    }

    /**
     * Message severity level indicating that an error has occurred.
     * 
     * @param summary
     * @param detail
     */
    public static void error(String summary, String detail) {
	FacesContext.getCurrentInstance().addMessage(null,
		new MessageUtil(FacesMessage.SEVERITY_ERROR, summary, detail));
    }

    /**
     * Message severity level indicating that a serious error has occurred.
     * 
     * @param summary
     * @param detail
     */
    public static void fatal(String summary, String detail) {
	FacesContext.getCurrentInstance().addMessage(null, new MessageUtil(FacesMessage.SEVERITY_FATAL, summary, detail));
    }
}
