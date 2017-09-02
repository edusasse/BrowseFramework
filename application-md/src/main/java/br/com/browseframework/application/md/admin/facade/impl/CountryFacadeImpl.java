package br.com.browseframework.application.md.admin.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.browseframework.application.md.admin.crud.facade.AdminTxCrudFacadeImpl;
import br.com.browseframework.application.md.admin.dao.CountryDAO;
import br.com.browseframework.application.md.admin.domain.Country;
import br.com.browseframework.application.md.admin.facade.CountryFacade;

@Service(value = "countryAdminFacade")
public class CountryFacadeImpl extends AdminTxCrudFacadeImpl<Long, Country> implements CountryFacade {
 
	@Autowired
	public CountryFacadeImpl(CountryDAO dao) {
		super(dao); 
	}
	
}