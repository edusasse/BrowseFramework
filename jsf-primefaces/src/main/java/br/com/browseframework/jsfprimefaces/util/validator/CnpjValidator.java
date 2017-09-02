package br.com.browseframework.jsfprimefaces.util.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;

/**
 * CNPJ Validator for JSF.
 * 
 * @author Eduardo
 * 
 */
public class CnpjValidator implements Validator {

	public static final String RESOURCE_BUNDLE_BROWSEFRW_JSF_VALIDATOR_CNPJ = "browsefrw.jsf.validator.cnpj";

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object valorTela)
			throws ValidatorException {
		if (!validaCNPJ(String.valueOf(valorTela))) {
			FacesMessage message = new FacesMessage();
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary(FacesUtil.getResourceBundleString(RESOURCE_BUNDLE_BROWSEFRW_JSF_VALIDATOR_CNPJ));
			throw new ValidatorException(message);
		}
	}

	/**
	 * Validates CNPJ.
	 * @param cnpj
	 * @return
	 */
	public static boolean validaCNPJ(String cnpj){
		boolean result = false;
		
		try {
			result = validaCNPJImpl(cnpj);
		} catch (Exception e){
			result = false;
		}
		
		return result;
	}

	/**
	 * Validates CNPJ.
	 * 
	 * @param cnpj
	 * @return
	 * @throws GenericBusinessException
	 */
	public static boolean validaCNPJImpl(String cnpj) throws GenericBusinessException {
		boolean result = true;

		while (cnpj.length() < 14){
			cnpj = "0"+cnpj;
		}
		
		if (cnpj == null || cnpj.length() != 14)
			result = false;

		try {
			Long.parseLong(cnpj);
		} catch (NumberFormatException e) {
			result = false;
		}

		int soma = 0;
		String cnpj_calc = cnpj.substring(0, 12);

		char chr_cnpj[] = cnpj.toCharArray();
		for (int i = 0; i < 4; i++)
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9)
				soma += (chr_cnpj[i] - 48) * (6 - (i + 1));

		for (int i = 0; i < 8; i++)
			if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9)
				soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));

		int dig = 11 - soma % 11;
		cnpj_calc = (new StringBuilder(String.valueOf(cnpj_calc))).append(
				dig != 10 && dig != 11 ? Integer.toString(dig) : "0")
				.toString();
		soma = 0;
		for (int i = 0; i < 5; i++)
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9)
				soma += (chr_cnpj[i] - 48) * (7 - (i + 1));

		for (int i = 0; i < 8; i++)
			if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9)
				soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));

		dig = 11 - soma % 11;
		cnpj_calc = (new StringBuilder(String.valueOf(cnpj_calc))).append(
				dig != 10 && dig != 11 ? Integer.toString(dig) : "0")
				.toString();

		if (!cnpj.equals(cnpj_calc)) {
			result = false;
		}

		return result;
	}
	
}