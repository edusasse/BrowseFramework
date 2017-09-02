package br.com.browseframework.report.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Homonymous class created to allow access to the getResourceBundle method.
 * @author Eduardo
 *
 */
public class ResourceBundleMessageSource extends org.springframework.context.support.ResourceBundleMessageSource {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.support.ResourceBundleMessageSource#getResourceBundle(java.lang.String, java.util.Locale)
	 */
	public ResourceBundle getResourceBundle(String basename, Locale locale) {
		return super.getResourceBundle(basename, locale);
	}
	
}
