package br.com.browseframework.application.db.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframework.application.db.dao.EntityDAO;
import br.com.browseframework.application.db.domain.Entity;
import br.com.browseframework.application.db.facade.EntityFacade;

@Service(value = "entityFacade")
@Transactional(rollbackFor = Throwable.class)
public class EntityFacadeImpl extends CrudFacadeApplicationImpl<Long, Entity> implements EntityFacade {
 
	@Autowired
	public EntityFacadeImpl(EntityDAO dao) {
		super(dao);
	}
	
}