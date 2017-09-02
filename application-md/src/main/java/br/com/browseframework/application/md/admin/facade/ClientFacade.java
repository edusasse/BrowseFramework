package br.com.browseframework.application.md.admin.facade;

import br.com.browseframework.application.md.admin.domain.Client;
import br.com.browseframework.base.crud.facade.CrudFacade;

public interface ClientFacade extends CrudFacade<Long, Client> {
 
	/**
	 * Retrieves Client by his url nickname.
	 * @param urlNickname
	 * @return
	 */
	public Client findClientByURLNickname(String urlNickname);
	
}