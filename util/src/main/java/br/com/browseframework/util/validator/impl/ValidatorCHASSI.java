package br.com.browseframework.util.validator.impl;

import java.text.ParseException;

import javax.swing.text.MaskFormatter;

import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.util.validator.Validator;
import br.com.browseframework.util.validator.enums.DocumentType;

public class ValidatorCHASSI implements Validator {

	/*
	 * (non-Javadoc)
	 * @see util.validator.Validator#validator(java.lang.String)
	 */
	@Override
	public boolean validator(String numero) {
		boolean retorno = false;
		 
		if (numero.length() == 17) {
			retorno = true;
		} else {
			retorno = false;
		}

		return retorno;
	}
 
	/*
	 * (non-Javadoc)
	 * @see util.validator.Validator#getAbsolutValue(java.lang.String)
	 */
	@Override
	public String getAbsolutValue(String numero) {
		String retorno = null;
		try{
			final MaskFormatter mf = new MaskFormatter(DocumentType.CHASSI.getMask());
			mf.setCommitsOnValidEdit(true);
			mf.setValueContainsLiteralCharacters(false);
			retorno = (String) mf.stringToValue(numero);	
		}catch(ParseException e){
			throw new GenericBusinessException("Não foi possível recuperar o valor absoluto para ["+numero+"]["+DocumentType.CHASSI.getMask()+"]");
		}
		return retorno;
	}

}
