package br.com.browseframework.util.validator.impl;

import br.com.browseframework.util.validator.Validator;

public class ValidatorAnything implements Validator {

	/**
	 * Allways returns true
	 */
	@Override
	public boolean validator(String numero) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see util.validator.Validator#getAbsolutValue(java.lang.String)
	 */
	@Override
	public String getAbsolutValue(String numero) {
		return numero;
	}

}
