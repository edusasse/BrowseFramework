package br.com.browseframework.jsfprimefaces.converter;

/**
 * Allows GenericIdFacadeConverter to identify the Facade class from the informed object.
 * @author Eduardo
 *
 */
public interface IdFacadeResolver {

	/**
	 * Need to return a valid facade service name and related to the given class. 
	 * @param baseDtoClass
	 * @return
	 */
	public String getFacadeByBaseDTOCLass(@SuppressWarnings("rawtypes") Class baseDtoClass);
}
