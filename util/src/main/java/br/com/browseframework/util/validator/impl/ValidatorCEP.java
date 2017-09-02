package br.com.browseframework.util.validator.impl;

import br.com.browseframework.util.validator.Validator;

public class ValidatorCEP implements Validator {

	/*
	 * (non-Javadoc)
	 * @see util.validator.Validator#validator(java.lang.String)
	 */
	@Override
	public boolean validator(String numero) {
		boolean retorno = false;
		if(numero != null){
			try{
				Long.parseLong(getAbsolutValue(numero));
				retorno = true;
			}catch(Exception e){
				
			}
		}
		return retorno;
	}

	/*
	 * (non-Javadoc)
	 * @see util.validator.Validator#getAbsolutValue(java.lang.String)
	 */
	@Override
	public String getAbsolutValue(String numero){
		if(numero != null){
	        numero = numero.replaceAll("[^0-9]", "");
		}
		return numero;
	}

}
