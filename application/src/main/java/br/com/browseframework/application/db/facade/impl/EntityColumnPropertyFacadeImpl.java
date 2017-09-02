package br.com.browseframework.application.db.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframework.application.db.dao.EntityColumnPropertyDAO;
import br.com.browseframework.application.db.domain.EntityColumnProperty;
import br.com.browseframework.application.db.facade.EntityColumnPropertyFacade;

@Service(value = "entityColumnPropertyFacade")
@Transactional(rollbackFor = Throwable.class)
public class EntityColumnPropertyFacadeImpl extends CrudFacadeApplicationImpl<Long, EntityColumnProperty> implements EntityColumnPropertyFacade {
 
	@Autowired
	public EntityColumnPropertyFacadeImpl(EntityColumnPropertyDAO dao) {
		super(dao);
	}
	
}