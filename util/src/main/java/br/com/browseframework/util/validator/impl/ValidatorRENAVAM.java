package br.com.browseframework.util.validator.impl;

import java.text.ParseException;

import javax.swing.text.MaskFormatter;

import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.util.validator.Validator;
import br.com.browseframework.util.validator.enums.DocumentType;

public class ValidatorRENAVAM implements Validator {

	/*
	 * (non-Javadoc)
	 * @see util.validator.Validator#validator(java.lang.String)
	 */
	@Override
	public boolean validator(String numero) {
		boolean retorno = false;
		try {
			String digCalculado = String.valueOf(digitCalculate(numero));
			String digPassado = numero.substring(numero.length() - 1, numero.length());
			if (digCalculado.equals("-1")){
				retorno = false;
			} else if (digCalculado.equals(digPassado)) {
				retorno = true;
			} else {
				retorno = false;
			}
		} catch (Exception e){
			retorno = false;
		}

		return retorno;
	}

	public static int digitCalculate(String renavam) {
		int soma = 0;
		try {
			for (int i = 0; i < 8; i++) {
				soma += Integer.parseInt(renavam.substring(i, i + 1)) * (i + 2);
			}
			soma = soma % 11;
		} catch (Exception e){
			soma = -1;
		}
		return soma == 10 ? 0 : soma;
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
					DocumentType.RENAVAM.getMask());
			mf.setCommitsOnValidEdit(true);
			mf.setValueContainsLiteralCharacters(false);
			retorno = (String) mf.stringToValue(numero);
		} catch (ParseException e) {
			throw new GenericBusinessException(
					"Não foi possível recuperar o valor absoluto para ["
							+ numero + "][" + DocumentType.CHASSI.getMask()
							+ "]");
		}
		return retorno;
	}

}
