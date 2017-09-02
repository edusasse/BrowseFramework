package br.com.browseframework.application.md.admin.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.browseframework.application.md.admin.crud.facade.AdminTxCrudFacadeImpl;
import br.com.browseframework.application.md.admin.dao.PersonDAO;
import br.com.browseframework.application.md.admin.domain.Person;
import br.com.browseframework.application.md.admin.facade.PersonFacade;

@Service(value = "personAdminFacade")
public class PersonFacadeImpl extends AdminTxCrudFacadeImpl<Long, Person> implements PersonFacade {
 
	@Autowired
	public PersonFacadeImpl(PersonDAO dao) {
		super(dao); 
	}
	 
}