package br.com.browseframework.application.md.admin.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.browseframework.application.md.admin.crud.facade.AdminTxCrudFacadeImpl;
import br.com.browseframework.application.md.admin.dao.CityDAO;
import br.com.browseframework.application.md.admin.domain.City;
import br.com.browseframework.application.md.admin.facade.CityFacade;

@Service(value = "cityAdminFacade")
public class CityFacadeImpl extends AdminTxCrudFacadeImpl<Long, City> implements CityFacade {
 
	@Autowired
	public CityFacadeImpl(CityDAO dao) {
		super(dao); 
	}
	
}