package br.com.browseframework.util.validator;

/**
 * Validator interface for Document Types
 */
public interface Validator {
	/**
	 * Validate the type
	 * @param numero
	 * @return
	 */
	public boolean validator(String numero);
	
	/**
	 * Returns the value passed without the relevant character mask
	 * @param valor
	 * @return
	 */
	public String getAbsolutValue(String valor);
}
