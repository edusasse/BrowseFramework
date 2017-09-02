package br.com.browseframework.application.db.facade.impl;

import java.io.Serializable;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframework.base.crud.dao.CrudDAO;
import br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl;
import br.com.browseframework.base.data.dto.BaseDTO;
import br.com.browseframework.base.data.type.CriterionType;
import br.com.browseframework.base.data.type.DataPageType;
import br.com.browseframework.base.data.type.FilterType;
import br.com.browseframework.base.data.type.OrderType;
import br.com.browseframework.base.data.type.PageType;

/**
 * Created to override empty transaction manager from Browse Framework.
 * @author Eduardo
 *
 * @param <ID>
 * @param <T>
 */
@Transactional(rollbackFor = Throwable.class, value="browseApplicationTransactionManager")
public abstract class CrudFacadeApplicationImpl<ID extends Serializable, T extends BaseDTO<ID>> extends CrudFacadeImpl<ID, T> {
 
	/**
	 * Contructor that receives DAO.
	 * @param dao
	 */
	public CrudFacadeApplicationImpl(CrudDAO<ID, T> crudDAO) {
		super(crudDAO);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl#save(br.com.browseframework.base.data.dto.BaseDTO)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, value="browseApplicationTransactionManager")
	public void save(T dto) {
		crudDAO.save(dto);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl#removeById(java.io.Serializable)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, value="browseApplicationTransactionManager")
	public void removeById(ID id) {
		crudDAO.removeById(id);
	};
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl#remove(br.com.browseframework.base.data.dto.BaseDTO)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, value="browseApplicationTransactionManager")
	public void remove(T dto) {
		crudDAO.remove(dto);
	};
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl#refresh(br.com.browseframework.base.data.dto.BaseDTO)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, value="browseApplicationTransactionManager")
	public void refresh(T dto) {
		crudDAO.refresh(dto);
	};
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl#findById(java.io.Serializable)
	 */
	@Override
	@Transactional(value="browseApplicationTransactionManager")
	public T findById(ID id){
		return super.findById(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl#findAll(br.com.browseframework.base.data.type.PageType, br.com.browseframework.base.data.type.CriterionType[])
	 */
	@Override
	@Transactional(value="browseApplicationTransactionManager")
	public DataPageType<T> findAll(PageType p, CriterionType[] criterion) {
		return super.findAll(p, criterion);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl#findAll(br.com.browseframework.base.data.type.PageType, br.com.browseframework.base.data.type.OrderType[], br.com.browseframework.base.data.type.FilterType[])
	 */
	@Override
	@Transactional(value="browseApplicationTransactionManager")
	public DataPageType<T> findAll(PageType p, OrderType[] orderings, FilterType[] filters) {
		return findAll(p, orderings, filters);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl#findAll(br.com.browseframework.base.data.type.PageType)
	 */
	@Override
	@Transactional(value="browseApplicationTransactionManager")
	public DataPageType<T> findAll(PageType p) {
		return super.findAll(p);
	}
}
