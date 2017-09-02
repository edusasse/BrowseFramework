package br.com.browseframework.jsfprimefaces.util.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * CNPJ Converter.
 * 
 * @author Eduardo
 * 
 */
public class CnpjConverter implements Converter {
	
	/**
	 * Converts the masked value to a un-masked value.
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		String result = value;
		if (value != null && !value.trim().equals("")) {
			result = value.replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("/", "");
		}

		return result;
	}

	/**
	 * Convert the object value for a masked value.
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		String result = String.valueOf(value);
		if (result != null && result.length() == 14){
			result = result.substring(0, 2) + "." + result.substring(2, 5) + "."
					+ result.substring(5, 8) + "/" + result.substring(8, 12) + "-"
					+ result.substring(12, 14);
		}

		return result;
	}
}
