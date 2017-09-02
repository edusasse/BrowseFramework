package br.com.browseframework.jsfprimefaces.listener;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;

import org.apache.log4j.Logger;

import br.com.browseframework.base.exception.BusinessException;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;

import com.sun.faces.application.ActionListenerImpl;

/**
 * For exceptions in a action listener event.
 * Handles navigation to "error" defined path.
 * @author Eduardo
 *
 */
@SuppressWarnings("deprecation")
public class ActionListener extends ActionListenerImpl {
	static final Logger log = Logger.getLogger(ActionListener.class);
	
	/**
	 * Intercepts action process.
	 */
	public void processAction(ActionEvent event) {
	    try {
	        super.processAction(event);
	    } catch (Exception e) {
	    	String message = null;
	    	if (e != null){
	    		message = e.getMessage();
		    	if (FacesException.class.isInstance(e)){
		    		if (EvaluationException.class.isInstance(e.getCause())){
		    			if (BusinessException.class.isInstance(((EvaluationException) e.getCause()).getCause())){
		    				final BusinessException be = (BusinessException) ((EvaluationException) e.getCause()).getCause();
		    				if (be.getMessageKey() != null){
		    					try {
		    						message = FacesUtil.getResourceBundleString(be.getMessageKey());
		    					} catch (Exception ex){
		    						message = be.getMessage();
		    					}
		    				} else {
		    					message = be.getMessage();
		    				}
		    			}
		    		}
		    	}
	    	}
	    	
	        final FacesContext context = FacesContext.getCurrentInstance();
	        if (context.getCurrentPhaseId().equals(PhaseId.INVOKE_APPLICATION)){
	        	FacesUtil.message(message, FacesMessage.SEVERITY_ERROR);
	        } else if (context.getCurrentPhaseId().equals(PhaseId.RENDER_RESPONSE)){
	        	context.getApplication().getNavigationHandler().handleNavigation(context, null, "error");
	        } else {
		        log.error("Exception ocurred",e);
	        	FacesUtil.getFlashScope().setKeepMessages(true);
	        	FacesUtil.message(e.getMessage(), FacesMessage.SEVERITY_ERROR);
	        }
	    }
	}
}
