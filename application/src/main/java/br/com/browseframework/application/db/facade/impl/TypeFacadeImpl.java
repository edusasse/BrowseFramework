package br.com.browseframework.application.db.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.browseframework.application.db.dao.TypeDAO;
import br.com.browseframework.application.db.domain.Type;
import br.com.browseframework.application.db.facade.TypeFacade;

@Service(value = "typeFacade")
public class TypeFacadeImpl extends CrudFacadeApplicationImpl<Long, Type> implements TypeFacade {
 
	@Autowired
	public TypeFacadeImpl(TypeDAO dao) {
		super(dao);
	}
	
}