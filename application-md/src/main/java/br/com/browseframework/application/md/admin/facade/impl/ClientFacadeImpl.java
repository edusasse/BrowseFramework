package br.com.browseframework.application.md.admin.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.browseframework.application.md.admin.crud.annotations.AdminTx;
import br.com.browseframework.application.md.admin.crud.facade.AdminTxCrudFacadeImpl;
import br.com.browseframework.application.md.admin.dao.ClientDAO;
import br.com.browseframework.application.md.admin.domain.Client;
import br.com.browseframework.application.md.admin.facade.ClientFacade;
import br.com.browseframework.base.data.Filter;
import br.com.browseframework.base.data.type.CriterionType;

@Service(value = "clientAdminFacade")
public class ClientFacadeImpl extends AdminTxCrudFacadeImpl<Long, Client> implements ClientFacade {
 
	@Autowired
	public ClientFacadeImpl(ClientDAO dao) {
		super(dao); 
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.admin.facade.ClientFacade#findClientByURLNickname(java.lang.String)
	 */
	@AdminTx
	public Client findClientByURLNickname(String urlNickname){
		Client result = null;
		if (urlNickname != null){
			result = findAll(null, new CriterionType[]{new Filter("urlNickname", urlNickname)}).getFirst();
		}
		return result;
	}
}