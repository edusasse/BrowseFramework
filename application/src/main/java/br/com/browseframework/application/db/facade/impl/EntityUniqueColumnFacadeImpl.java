package br.com.browseframework.application.db.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframework.application.db.dao.EntityUniqueColumnDAO;
import br.com.browseframework.application.db.domain.EntityUniqueColumn;
import br.com.browseframework.application.db.facade.EntityUniqueColumnFacade;

@Service(value = "entityUniqueColumnFacade")
@Transactional(rollbackFor = Throwable.class)
public class EntityUniqueColumnFacadeImpl extends CrudFacadeApplicationImpl<Long, EntityUniqueColumn> implements EntityUniqueColumnFacade {
 
	@Autowired
	public EntityUniqueColumnFacadeImpl(EntityUniqueColumnDAO dao) {
		super(dao);
	}
	
}