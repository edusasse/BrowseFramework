package br.com.browseframework.report.jasper.parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRParameter;
import br.com.browseframework.base.exception.GenericBusinessException;

public class JasperReportParametersHelper {

	/**
	 * Returns a list of ReportParameterHelper class from a JRParameter list.
	 * @param listReportParameters
	 * @return
	 */
	public static List<ReportParameterHelper> getListOfReportParameterHelperFromJRParameterList(List<JRParameter> listReportParameters) {
		// Creates a new list with the local parameter class
		final List<ReportParameterHelper> result = new ArrayList<ReportParameterHelper>();
		try {
			for (JRParameter jrp : listReportParameters) {
				final ReportParameterHelper rph = new ReportParameterHelper();
				// Name
				rph.setName(jrp.getName());
				// Description
				rph.setDescription(jrp.getDescription());
				// Class
				rph.setClazz(jrp.getValueClass());
				// Prompt
				rph.setPrompt(!jrp.isSystemDefined() && jrp.isForPrompting());
				// TODO default value contains java code to be executed and showed to the user
				result.add(rph);
			}
		} catch (Exception e) {
			throw new GenericBusinessException(	"An exception occured during the parameters load. Error [" + e.getMessage() + "]");
		}
		
		return result;
	}

	/**
	 * Load a value in the informed parameter list by the parameter name.
	 * @param result
	 * @param name
	 * @param value
	 */
	public static void loadReportParamerterValue(List<ReportParameterHelper> list, String name, Object value) {
		for (ReportParameterHelper pr : list) {
			if (pr.getName().equals(name)) {
				pr.setValue(value);
				break;
			}
		}
	}

	/**
	 * Returns a list ignoring the non-promptable parameters.
	 * @param list
	 * @return
	 */
	public static List<ReportParameterHelper> getPromptableParametersList(List<ReportParameterHelper> list) {
		final List<ReportParameterHelper> listParamtersEntrada = new ArrayList<ReportParameterHelper>();
		if (list == null) {
			throw new GenericBusinessException("Informed parameters list is null!");
		}
		
		for (ReportParameterHelper pr : list) {
			if (pr.isPrompt()) {
				listParamtersEntrada.add(pr);
			}
		}
		
		return listParamtersEntrada;
	}

	/**
	 * Verifies if the given parameter name is in list.
	 * @param list
	 * @param parameterName
	 * @return
	 */
	public static boolean hasParameter(List<ReportParameterHelper> list, String parameterName) {
		boolean result = false;
		if (list != null) {
			for (ReportParameterHelper p : list) {
				if (parameterName.equals(p.getName())) {
					result = true;
				}
			}
		}
		return result;
	}
	
	/**
	 * Converts ReportParameterHelper list to jasper default parameter map.
	 * @param list
	 * @return
	 */
	public static Map<String, Object> convertListToParameterMap(List<ReportParameterHelper> list){
		final Map<String, Object> result = new HashMap<String, Object>();
		if (list != null){
			for (ReportParameterHelper rph : list){
				result.put(rph.getName(), rph.getValue());
			}
		}
		return result;
	}
}