package br.com.browseframework.jsfprimefaces.util;

import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.Flash;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import br.com.browseframework.base.exception.GenericBusinessException;

/**
 * Static class with JSF helper methods.
 * 
 * @author Eduardo
 *
 */
public class FacesUtil {

	static Logger log = Logger.getLogger(FacesUtil.class.getName());

	/**
	 * Retrieves a request paramter.
	 * @param name
	 * @return
	 */
	public static String getRequestParameter(String name) {
		return (String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
	}
 
	/**
	 * Returns the External Context.
	 * @return
	 */
	public static ExternalContext getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}
	
	/**
	 * Returns the Request Context.
	 * @return
	 */
	public static RequestContext getRequestContext() {
		return RequestContext.getCurrentInstance();
	}
	
	/**
	 * Returns the Session Map.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map getSessionMap() {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	}
	
	/**
	 * Returns the Servlet Context.
	 * @return
	 */
	public static ServletContext getServletContext() {
		return (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
	}
	
	/**
	 * Returns the Servlet Request.
	 * @return
	 */
	public static HttpServletRequest getServletRequest() {
		return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}
	
	/**
	 * Returns the Servlet Response.
	 * @return
	 */
	public static HttpServletResponse getServletResponse() {
		return (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}
	
	/**
	 * Method that does the redirect to the required pages.
	 * 
	 * @param message
	 * @param url
	 */
	public static void redirect(String url, String message) {
		log.debug("Redirecting to [" + url + "]");
		if (url != null && url.trim().length() > 0) {
			try {
				final FacesContext fc = FacesContext.getCurrentInstance();
				final NavigationHandler nh = fc.getApplication().getNavigationHandler();
				nh.handleNavigation(fc, null, url + "?faces-redirect=true");
			} catch (Exception io) {
				log.error("Not able to redirect [" + url + "]");
				throw new GenericBusinessException("Not able to redirect [" + url + "]. Error [" + io.getMessage() + "]");
			}
			if (message != null && !message.trim().equals("")) {
				infoMessage(message);
			}
		}
	}

	/**
	 * Method that does the redirect to the required pages.
	 * 
	 * @param url
	 */
	public static void redirect(String url) {
		redirect(url, null);
	}

	/**
	 * Method that does the forward to the required pages.
	 * 
	 * @param url
	 */
	public static void forward(String url) {
		log.info("Forwarding to [" + url + "]");
		if (url != null && url.trim().length() > 0) {
			try {
				FacesContext fc = FacesContext.getCurrentInstance();
				NavigationHandler nh = fc.getApplication().getNavigationHandler();
				nh.handleNavigation(fc, null, url);
			} catch (Exception io) {
				log.error("Not able to forward [" + url + "]");
				throw new GenericBusinessException("Not able to forward [" + url + "]. Error [" + io.getMessage() + "]");
			}
		}
	}

	/**
	 * Displays a message with severity INFO.
	 * 
	 * @param message
	 */
	public static void infoMessage(String message) {
		message(message, FacesMessage.SEVERITY_INFO);
	}

	/**
	 * Displays a message with severity ERROR.
	 * 
	 * @param message
	 */
	public static void errorMessage(String message) {
		message(message, FacesMessage.SEVERITY_ERROR);
	}

	/**
	 * Displays a message with severity passed as parameter.
	 * 
	 * @param message
	 * @param severity
	 */
	public static void message(String message, FacesMessage.Severity severity) {
		final FacesContext context = FacesContext.getCurrentInstance();
		final FacesMessage facesMessage = new FacesMessage();
		facesMessage.setDetail(message);
		facesMessage.setSeverity(severity);
		facesMessage.setSummary(message);
		context.addMessage(null, facesMessage);
	}

//	/**
//	 * Resolves an expression JSF programmatically.
//	 * @param expression
//	 */
//	public static Object resolveEL(String expression){
//		final FacesContext facesContext = FacesContext.getCurrentInstance();
//		final Application app = facesContext.getApplication();
//		
//		return app.evaluateExpressionGet(facesContext, expression, Object.class);
//	}
//	
	/**
	 * Method for taking a reference to a JSF binding expression and returning
	 * the matching object (or creating it).
	 * 
	 * @param expression
	 *            EL expression
	 * @return Managed object
	 */
	public static Object resolveExpression(String expression) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final Application app = facesContext.getApplication();
		final ExpressionFactory elFactory = app.getExpressionFactory();
		final ELContext elContext = facesContext.getELContext();
		final ValueExpression valueExp = elFactory.createValueExpression(elContext,	expression, Object.class);
		return valueExp.getValue(elContext);
	}
	
	/**
	 * Method for setting a new object into a JSF managed bean Note: will fail
	 * silently if the supplied object does not match the type of the managed
	 * bean.
	 * 
	 * @param expression
	 *            EL expression
	 * @param newValue
	 *            new value to set
	 */
	public static void setExpressionValue(String expression, Object newValue) {
		if (!(expression.startsWith("#{") && expression.endsWith("}")) ){
			expression = "#{" + expression + "}";
		}
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final Application app = facesContext.getApplication();
		final ExpressionFactory elFactory = app.getExpressionFactory();
		final ELContext elContext = facesContext.getELContext();
		final ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);

		// Check that the input newValue can be cast to the property type
		// expected by the managed bean.
		// If the managed Bean expects a primitive we rely on Auto-Unboxing
		// I could do a more comprehensive check and conversion from the object
		// to the equivilent primitive but life is too short
		final Class<?> bindClass = valueExp.getType(elContext);
		if (bindClass.isPrimitive() || bindClass.isInstance(newValue)) {
			valueExp.setValue(elContext, newValue);
		}
	}

	/**
	 * Java script code to be inserted on RequestContext.
	 * @param javaScript
	 */
	public static void runJavaScriptCode(String javaScript){
        final RequestContext context = RequestContext.getCurrentInstance();    
        context.execute(javaScript);  
	}
	
	/**
	 * Returns the current FacesContext, if there is one, or creates it. 
	 * @param request
	 * @param response
	 * @return
	 */
	public static FacesContext getFacesContext(HttpServletRequest request, HttpServletResponse response) {
		// Get current FacesContext.
		FacesContext result = FacesContext.getCurrentInstance();

		// Check current FacesContext.
		if (result == null) {

			// Create new Lifecycle.
			final LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
			final Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

			// Create new FacesContext.
			final FacesContextFactory contextFactory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
			result = contextFactory.getFacesContext(request.getSession().getServletContext(), request, response, lifecycle);

			// Create new View.
			final UIViewRoot view = result.getApplication().getViewHandler().createView(result, "");
			result.setViewRoot(view);

			// Set current FacesContext.
			FacesContextWrapper.setCurrentInstance(result);
		}

		return result;
	}
 
	/**
	 * Wrap the protected FacesContext.setCurrentInstance() in a inner class.
	 */
	private static abstract class FacesContextWrapper extends FacesContext {
		protected static void setCurrentInstance(FacesContext facesContext) {
			FacesContext.setCurrentInstance(facesContext);
		}
	}

	/**
	 * Retrieves the value of an specific key by the message bundle by name. 
	 * @param resourceBundleName
	 * @param resourceBundleKey
	 * @return
	 * @throws MissingResourceException
	 */
	public static String getResourceBundleString(String resourceBundleName, String resourceBundleKey) throws MissingResourceException {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, resourceBundleName);
		return bundle.getString(resourceBundleKey);
	}
	
	/**
	 * Retrieves the value of an specific key from the default. 
	 * @param resourceBundleName
	 * @param resourceBundleKey
	 * @return
	 * @throws MissingResourceException
	 */
	public static String getResourceBundleString(String resourceBundleKey) throws MissingResourceException {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final String resourceBundleName = facesContext.getApplication().getMessageBundle();
		final Locale locale = facesContext.getViewRoot().getLocale();
		final ResourceBundle bundle = ResourceBundle.getBundle(resourceBundleName, locale);
		return bundle.getString(resourceBundleKey);
	}
	
	/**
	 * Creates a MethodExpression.
	 * @param expression
	 * @param expectedReturnType
	 * @param expectedParamTypes
	 * @return
	 */
	public static MethodExpression buildMethodExpression(String expression, Class<?> expectedReturnType, Class<?>[] expectedParamTypes){
		final ELContext elContext = FacesContext.getCurrentInstance().getELContext();  
		final MethodExpression methodExpression = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().createMethodExpression(elContext, expression, expectedReturnType, expectedParamTypes);  
		return methodExpression;
	
	}
	
	/**
	 * Creates a MethodExpression that needs to have a non-return expression method.
	 * @param expression
	 * @return
	 */
	public static MethodExpression buildMethodExpression(String expression){
		final ELContext elContext = FacesContext.getCurrentInstance().getELContext();  
		final MethodExpression methodExpression = FacesContext.getCurrentInstance().getApplication().getExpressionFactory().createMethodExpression(elContext, expression, null, new Class[0]);  
		return methodExpression;
	}

	/**
	 * Return the threadsafe Flash for this application. The default
	 * implementation will throw UnsupportedOperationException. Compliant JSF
	 * runtimes must provide an implementation of this method.
	 * 
	 * @return
	 */
	public static Flash getFlashScope() {
		return (FacesContext.getCurrentInstance().getExternalContext().getFlash());
	}

	/**
	 * Retrieves the value of a context param informed, usually, in web.xml file.
	 * @return
	 */
    public static String getContextInitParameter(String name){
    	final FacesContext ctx = FacesContext.getCurrentInstance();
    	final String result = ctx.getExternalContext().getInitParameter(name);
    	return result;
    }
    
    /**
	 * Adds callback paramter to the request context.
	 * @return
	 */
    public static void addRequestContextCallbackParam(String name, Object value){
    	getRequestContext().addCallbackParam(name, value);
    }
    
    /**
     * Returns the content type by a file extension.
     * @param fileExtension
     * @return
     */
    public static String getContentType(String fileExtension) {
		if (fileExtension.equalsIgnoreCase("htm")
				|| fileExtension.equalsIgnoreCase("html")
				|| fileExtension.equalsIgnoreCase("log")) {
			return "text/HTML";
		}
		if (fileExtension.equalsIgnoreCase("txt")) {
			return "text/plain";
		}
		if (fileExtension.equalsIgnoreCase("doc")
				|| fileExtension.equalsIgnoreCase("docx")) {
			return "application/ms-word";
		}
		if (fileExtension.equalsIgnoreCase("tiff")
				|| fileExtension.equalsIgnoreCase("tif")) {
			return "image/tiff";
		}
		if (fileExtension.equalsIgnoreCase("asf")) {
			return "video/x-ms-asf";
		}
		if (fileExtension.equalsIgnoreCase("avi")) {
			return "video/avi";
		}
		if (fileExtension.equalsIgnoreCase("zip")) {
			return "application/zip";
		}
		if (fileExtension.equalsIgnoreCase("xls")
				|| fileExtension.equalsIgnoreCase("xlsx")
				|| fileExtension.equalsIgnoreCase("csv")) {
			return "application/vnd.ms-excel";
		}
		if (fileExtension.equalsIgnoreCase("gif")) {
			return "image/gif";
		}
		if (fileExtension.equalsIgnoreCase("jpg")
				|| fileExtension.equalsIgnoreCase("jpeg")) {
			return "image/jpeg";
		}
		if (fileExtension.equalsIgnoreCase("png")
				|| fileExtension.equalsIgnoreCase("png")) {
			return "image/png";
		}
		if (fileExtension.equalsIgnoreCase("bmp")) {
			return "image/bmp";
		}
		if (fileExtension.equalsIgnoreCase("wav")) {
			return "audio/wav";
		}
		if (fileExtension.equalsIgnoreCase("mp3")) {
			return "audio/mpeg3";
		}
		if (fileExtension.equalsIgnoreCase("mpg")
				|| fileExtension.equalsIgnoreCase("mpeg")) {
			return "video/mpeg";
		}
		if (fileExtension.equalsIgnoreCase("rtf")) {
			return "application/rtf";
		}
		if (fileExtension.equalsIgnoreCase("asp")) {
			return "text/asp";
		}
		if (fileExtension.equalsIgnoreCase("pdf")) {
			return "application/pdf";
		}
		if (fileExtension.equalsIgnoreCase("fdf")) {
			return "application/vnd.fdf";
		}
		if (fileExtension.equalsIgnoreCase("ppt")
				|| fileExtension.equalsIgnoreCase("ppts")) {
			return "application/mspowerpoint";
		}
		if (fileExtension.equalsIgnoreCase("dwg")) {
			return "image/vnd.dwg";
		}
		if (fileExtension.equalsIgnoreCase("msg")) {
			return "application/msoutlook";
		}
		if (fileExtension.equalsIgnoreCase("xml")
				|| fileExtension.equalsIgnoreCase("sdxl")) {
			return "application/xml";
		}
		if (fileExtension.equalsIgnoreCase("xdp")) {
			return "application/vnd.adobe.xdp+xml";
		} else {
			return "application/octet-stream";
		}
	}

}
