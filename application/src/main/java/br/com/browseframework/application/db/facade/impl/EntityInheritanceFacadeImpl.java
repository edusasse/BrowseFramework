package br.com.browseframework.application.db.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframework.application.db.dao.EntityInheritanceDAO;
import br.com.browseframework.application.db.domain.EntityInheritance;
import br.com.browseframework.application.db.facade.EntityInheritanceFacade;

@Service(value = "entityInheritanceFacade")
@Transactional(rollbackFor = Throwable.class)
public class EntityInheritanceFacadeImpl extends CrudFacadeApplicationImpl<Long, EntityInheritance> implements EntityInheritanceFacade {
 
	@Autowired
	public EntityInheritanceFacadeImpl(EntityInheritanceDAO dao) {
		super(dao);
	}
	
}