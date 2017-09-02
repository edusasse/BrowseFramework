package br.com.browseframework.util.validator;

import java.util.HashMap;
import java.util.Map;

import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.util.validator.enums.DocumentType;

public class ValidatorDocument {
	// Map with instantiated validators
	private Map<DocumentType, Validator> mapValidator = new HashMap<DocumentType, Validator>();

	/**
	 * Method that performs validation of a document.
	 * @param documentType
	 * @param numero
	 * @return
	 */
	public boolean validateDocument(DocumentType documentType,
			String numero) {
		Validator v = getMapValidator().get(documentType);
		boolean retorno = true;
		if (v != null) {
			retorno = v.validator(numero);
		}
		return retorno;
	}

	/**
	 * Method that returns the absolute value.
	 * @param documentType
	 * @param numero
	 * @return
	 */
	public String getAbsolutValue(DocumentType documentType,String numero){
		Validator v = getMapValidator().get(documentType);
		if(v != null){
			try{
				numero = v.getAbsolutValue(numero);
			}catch(Exception e){
				throw new GenericBusinessException("Não foi possível recuperar o valor absoluto["+documentType+"]["+numero+"]["+e.getMessage()+"]");
			}
		}
		return numero;
	}

	// GETTERS && SETTER

	public Map<DocumentType, Validator> getMapValidator() {
		return this.mapValidator;
	}
	
	public void setMapValidator(
			Map<DocumentType, Validator> mapValidador) {
		this.mapValidator = mapValidador;
	}

}