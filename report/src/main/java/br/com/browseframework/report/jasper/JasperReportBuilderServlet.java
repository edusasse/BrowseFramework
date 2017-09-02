package br.com.browseframework.report.jasper;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;

/**
 * TODO to be finished.
 * @author Eduardo
 *
 */
public class JasperReportBuilderServlet extends JasperReportBuilder {

	@SuppressWarnings("unused")
	private static transient Logger log = Logger.getLogger(JasperReportBuilderServlet.class);

	/**
	 * Writes the Pdf strem to response output stream.
	 * @param jasperPrint
	 * @param response
	 * @throws Exception
	 */
	public void doWritePdf(JasperPrint jasperPrint, HttpServletResponse response) throws Exception {
		fillHeaders(response, "application/pdf");
		final OutputStream out = response.getOutputStream();
		super.doExportReportToPdfStream(jasperPrint, out);
		out.close();
	}

	/**
	 * Fill header with content
	 * @param response
	 * @param contentType
	 */
	protected void fillHeaders(HttpServletResponse response, String contentType) {
		response.setHeader("Content-Disposition", "inline");
		response.setContentType(contentType);
	}
}