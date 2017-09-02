package br.com.browseframework.util.validator.impl;

import java.text.ParseException;

import javax.swing.text.MaskFormatter;

import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.util.validator.Validator;
import br.com.browseframework.util.validator.enums.DocumentType;

public class ValidatorCNH implements Validator {

	/*
	 * (non-Javadoc)
	 * @see util.validator.Validator#validator(java.lang.String)
	 */
	@Override
	public boolean validator(String numero) {
		boolean retorno = true;
		return retorno;
	}

	/*
	 * (non-Javadoc)
	 * @see util.validator.Validator#getAbsolutValue(java.lang.String)
	 */
	@Override
	public String getAbsolutValue(String numero) {
		String retorno = null;
		try {
			final MaskFormatter mf = new MaskFormatter(
					DocumentType.CNH.getMask());
			mf.setCommitsOnValidEdit(true);
			mf.setValueContainsLiteralCharacters(false);
			retorno = (String) mf.stringToValue(numero);
		} catch (ParseException e) {
			throw new GenericBusinessException(
					"Não foi possível recuperar o valor absoluto para ["
							+ numero + "][" + DocumentType.CNH.getMask()
							+ "]");
		}
		return retorno;
	}

}
