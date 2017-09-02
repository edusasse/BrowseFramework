package br.com.browseframework.report.jasper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import br.com.browseframework.report.util.ResourceBundleMessageSource;

public class JasperReportBuilder implements ApplicationContextAware {

	private static transient Logger log = Logger.getLogger(JasperReportBuilder.class);
	
	@Autowired(required=false)
	private ResourceBundleMessageSource messageSource;
	
	protected ApplicationContext applicationContext;
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		log.info("Application Context setted.");
	}
	
	/**
	 * Builds the report using the informed parameters and connection.
	 * @param reportRelativePath
	 * @param reportParamters
	 * @param localeName
	 * @param resourceBundleRelativePath
	 * @param conn
	 * @param exportFormat
	 * @return
	 * @throws Exception
	 */
	public JasperPrint buildReport(
			String reportRelativePath,
			String resourceBundleRelativePath,
			Map<String, Object> reportParamters,
			String localeName,  
			Connection conn) throws Exception {
		final JasperReport jr = getJasperReport(reportRelativePath);
		return buildReport(jr, resourceBundleRelativePath, reportParamters, localeName, conn);
	} 
	
	/**
	 * Builds the report using the informed parameters and connection.
	 * @param reportRelativePath
	 * @param reportParamters
	 * @param localeName
	 * @param resourceBundleRelativePath
	 * @param conn
	 * @param exportFormat
	 * @return
	 * @throws Exception
	 */
	public JasperPrint buildReport(
			JasperReport jr,
			String resourceBundleRelativePath,
			Map<String, Object> reportParamters,
			String localeName,  
			Connection conn) throws Exception {

		JasperPrint result = null;
		  
		Map<String, Object> params = null;
		// When the locale is informed obtains the report map setting the resource bundle propertirs
		if (localeName != null && resourceBundleRelativePath != null) { 
			params = getReportParameterMap(localeName, resourceBundleRelativePath);
		} else {
			params = new HashMap<String, Object>();
		}
		
		// Set all the informed report parameters
		if (reportParamters != null) {
			for (String key : reportParamters.keySet()) {
				final Object o = reportParamters.get(key);
				if (o != null) {
					params.put(key, o);
				}
			}
		}
		
		// Report execution
		try {
			result = JasperFillManager.fillReport(jr, params, conn);
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
		
		return result;
	} 
	
	/**
	 * Builds the report from the informed Collection values. 
	 * @param reportRelativePath
	 * @param localeName
	 * @param localeParams
	 * @param data
	 * @param out
	 * @param exportFormat
	 * @throws Exception
	 */
	public JasperPrint buildReport(
			String reportRelativePath, 
			String localeName,
			String localeParams,
			Collection<Map<String, ?>> data) throws Exception {
		final JasperReport jr = getJasperReport(reportRelativePath);
		return buildReport(jr, localeName, localeParams, data);
	}
	
	/**
	 * Builds the report from the informed Collection values. 
	 * @param reportRelativePath
	 * @param localeName
	 * @param localeParams
	 * @param data
	 * @param out
	 * @param exportFormat
	 * @throws Exception
	 */
	public JasperPrint buildReport(
			JasperReport jr,
			String localeName,
			String localeParams,
			Collection<Map<String, ?>> data) throws Exception {
		JasperPrint result = null;
		final Map<String, Object> params = getReportParameterMap(localeName, localeParams);
		final JRMapCollectionDataSource ds = new JRMapCollectionDataSource(data);
		result = buildReport(jr, params, ds);
		
		return result;
	}

	/**
	 * Builds the report from the informed JRDataSource values. 
	 * @param out
	 * @param jr
	 * @param params
	 * @param ds
	 * @param exportFormat
	 * @return
	 * @throws JRException
	 * @throws Exception
	 * @throws IOException
	 */
	public JasperPrint buildReport(
			JasperReport jr,
			Map<String, Object> params, 
			JRDataSource ds) throws JRException, Exception, IOException {
		// Load report data
		JasperPrint result = null;
		if (ds == null) {
			result = JasperFillManager.fillReport(jr, params);
		} else {
			result = JasperFillManager.fillReport(jr, params, ds);
		}
		
		return result;
	}
	
	/**
	 * Creates the report map and some properties like time zone, application
	 * name, report title and the resource bundle are loaded. 
	 * @param localeName
	 * @param localeParams
	 * @return
	 */
	protected Map<String, Object> getReportParameterMap(String localeName, String localeParams) {
		Map<String, Object> result = null;
		
		if (messageSource != null){
			// Returns a valid locale
			
			final Locale locale = resolveLocale(localeName);
			// Obtains the map
			result = new HashMap<String, Object>();
			
			// Time Zone
			result.put(JRParameter.REPORT_TIME_ZONE, TimeZone.getDefault());

			// Load Resource Bundle file
			if (localeParams != null && locale != null) {
				final ResourceBundleMessageSource messageSource = getResourceBundleMessageSource();
				if (messageSource != null) {
					result.put(JRParameter.REPORT_RESOURCE_BUNDLE, messageSource.getResourceBundle(localeName, locale));
				}
			}
			// Retrieves application name
			result.put("REPORT_APPLICATION", messageSource.getMessage("application", null, locale));
			// Retrieves the report title
			result.put("REPORT_TITLE",	messageSource.getMessage("report.title", null, locale));	
		}
		
		return result;
	}

	/**
	 * Corrects the locale name.
	 * @param localeName
	 * @return
	 */
	private Locale resolveLocale(String localeName) {
		Locale locale = null;
		String[] lng_cntr = localeName.split("_");
		if (lng_cntr.length == 2) {
			locale = new Locale(lng_cntr[0], lng_cntr[1]);
		} else {
			locale = new Locale(localeName);
		}
		return locale;
	}
	 
	/**
	 * Retrieves a JasperReport instance from a relative file path.
	 * @param reportRelativePath
	 * @return
	 * @throws Exception
	 */
	public JasperReport getJasperReport(String reportRelativePath) throws Exception {
		JasperReport result = null;
		InputStream is = null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(reportRelativePath);
			result = (JasperReport) JRLoader.loadObject(is);
		} catch (Exception e) {
			throw e;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
		}
		
		return result;
	}

	/**
	 * Returns the report parameters.
	 * @param report
	 * @return
	 * @throws Exception
	 */
	public final List<JRParameter> getJasperReportParamters(String report)
			throws Exception {
		final JasperReport jr = getJasperReport(report);
		return getJasperReportParamters(jr);
	}

	/**
	 * Returns the report parameters.
	 * @param jreport
	 * @return
	 */
	public final List<JRParameter> getJasperReportParamters(JasperReport jreport) {
		final List<JRParameter> result = new ArrayList<JRParameter>();
		final JRParameter[] jparam = jreport.getParameters();
		for (JRParameter jrp : jparam) {
			result.add(jrp);
		}
		return result;
	}

	/**
	 * Returns the jasper report title.
	 * @param report
	 * @return
	 * @throws Exception
	 */
	public final String getJasperReportTitle(String report) throws Exception {
		final JasperReport jr = getJasperReport(report);
		return getJasperReportTitle(jr);
	}

	/**
	 * Returns the jasper report title.
	 * @param jreport
	 * @return
	 */
	public final String getJasperReportTitle(JasperReport jreport) {
		String retorno = null;
		try {
			// Sempre deve existir um titulo.
			retorno = jreport.getName();
		} catch (NullPointerException npe) {
			retorno = null;
		}
		return retorno;
	}

	/**
	 * Exports Report to PDF Stream.
	 * @param jasperPrint
	 * @param out
	 * @throws Exception
	 */
	public void doExportReportToPdfStream(JasperPrint jasperPrint, OutputStream out) throws Exception {
		JasperExportManager.exportReportToPdfStream(jasperPrint, out);
		out.flush();
	}
	
	// GETTERS && SETTERS
	
	public ResourceBundleMessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	protected ResourceBundleMessageSource getResourceBundleMessageSource() {
		return this.messageSource;
	}

}