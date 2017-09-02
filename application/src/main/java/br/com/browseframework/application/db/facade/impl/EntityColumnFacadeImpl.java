package br.com.browseframework.application.db.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframework.application.db.dao.EntityColumnDAO;
import br.com.browseframework.application.db.domain.EntityColumn;
import br.com.browseframework.application.db.facade.EntityColumnFacade;
import br.com.browseframework.application.db.facade.EntityFacade;
import br.com.browseframework.base.exception.GenericBusinessException;

@Service(value = "entityColumnFacade")
@Transactional(rollbackFor = Throwable.class)
public class EntityColumnFacadeImpl extends CrudFacadeApplicationImpl<Long, EntityColumn> implements EntityColumnFacade {
 
	@Autowired
	private EntityFacade entityFacade;
	
	@Autowired
	public EntityColumnFacadeImpl(EntityColumnDAO dao) {
		super(dao);
	}
	
	@Override
	public void save(EntityColumn dto) {
		if (dto.getEntity() == null){
			throw new GenericBusinessException("Entity column needs a Entity!");
		}
		// Checks if the Entity as other identity column
		if (dto.isIdentity()){
			entityFacade.refresh(dto.getEntity());
			if (dto.getEntity().getColumns() != null){
				for (EntityColumn ec : dto.getEntity().getColumns()){
					if (!ec.equals(dto) && ec.isIdentity()){
						throw new GenericBusinessException("Entity Column [" + ec.getName() + "] already assigned as identity column!");
					}
				}
			}
		}
		super.save(dto);
	}
	
}