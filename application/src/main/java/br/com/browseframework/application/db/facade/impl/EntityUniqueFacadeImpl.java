package br.com.browseframework.application.db.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframework.application.db.dao.EntityUniqueDAO;
import br.com.browseframework.application.db.domain.EntityUnique;
import br.com.browseframework.application.db.facade.EntityUniqueFacade;

@Service(value = "entityUniqueFacade")
@Transactional(rollbackFor = Throwable.class)
public class EntityUniqueFacadeImpl extends CrudFacadeApplicationImpl<Long, EntityUnique> implements EntityUniqueFacade {
 
	@Autowired
	public EntityUniqueFacadeImpl(EntityUniqueDAO dao) {
		super(dao);
	}
	
}