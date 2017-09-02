package br.com.browseframework.base.crud.facade.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframework.base.crud.dao.CrudDAO;
import br.com.browseframework.base.crud.facade.CrudFacade;
import br.com.browseframework.base.data.dto.BaseDTO;
import br.com.browseframework.base.data.type.CriterionType;
import br.com.browseframework.base.data.type.DataPageType;
import br.com.browseframework.base.data.type.FilterType;
import br.com.browseframework.base.data.type.OrderType;
import br.com.browseframework.base.data.type.PageType;

/**
 * CRUD Facade implementation.
 * @author Eduardo
 *
 * @param <ID>
 * @param <T>
 */
@Transactional
public abstract class CrudFacadeImpl<ID extends Serializable, T extends BaseDTO<ID>> implements CrudFacade<ID, T> {

	// Provides data source access
	protected CrudDAO<ID, T> crudDAO;

	/**
	 * Contructor that receives DAO.
	 * @param dao
	 */
	public CrudFacadeImpl(CrudDAO<ID, T> crudDAO) {
		this.crudDAO = crudDAO;
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.crud.Crud#save(br.com.browseframework.data.dto.BaseDTO)
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public T save(T dto) {
		return crudDAO.save(dto);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.crud.Crud#removeById(java.io.Serializable)
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void removeById(ID id) {
		crudDAO.removeById(id);
	};
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.crud.Crud#remove(br.com.browseframework.data.dto.BaseDTO)
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void remove(T dto) {
		crudDAO.remove(dto);
	};
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.crud.Crud#refresh(br.com.browseframework.data.dto.BaseDTO)
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void refresh(T dto) {
		crudDAO.refresh(dto);
	};
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.Crud#findById(java.io.Serializable)
	 */
	public T findById(ID id){
		return crudDAO.findById(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.crud.Crud#findAll(br.com.browseframework.data.type.PageType, br.com.browseframework.data.type.CriterionType[])
	 */
	@Override
	public DataPageType<T> findAll(PageType p, CriterionType[] criterion) {
		return crudDAO.findAll(p, criterion);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.crud.Crud#findAll(br.com.browseframework.data.type.PageType, br.com.browseframework.data.type.OrderType, br.com.browseframework.data.type.FilterType)
	 */
	@Override
	public DataPageType<T> findAll(PageType p, OrderType[] orderings, FilterType[] filters) {
		return crudDAO.findAll(p, orderings, filters);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.crud.Crud#findAll(br.com.browseframework.data.type.PageType)
	 */
	@Override
	public DataPageType<T> findAll(PageType p) {
		return crudDAO.findAll(p);
	}

	@SuppressWarnings("unchecked")
	public Class<T> getPersistentClass() {
		Class<T> result = null;
		if (getClass().getGenericSuperclass() instanceof ParameterizedType) {
			result = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
		} else if (getClass().getGenericSuperclass() instanceof Class) {
			final Class<?> c = (Class<?>) getClass().getGenericSuperclass();
			result = (Class<T>) ((ParameterizedType) c.getGenericSuperclass()).getActualTypeArguments()[1];
		} else {
			result = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		}
		return result;
	}
	
}