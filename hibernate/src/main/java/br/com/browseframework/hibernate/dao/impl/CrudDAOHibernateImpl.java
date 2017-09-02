package br.com.browseframework.hibernate.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.OptimisticLockingFailureException;

import br.com.browseframework.base.data.DataPage;
import br.com.browseframework.base.data.dto.BaseDTO;
import br.com.browseframework.base.data.type.CriterionType;
import br.com.browseframework.base.data.type.DataPageType;
import br.com.browseframework.base.data.type.FilterType;
import br.com.browseframework.base.data.type.OrderType;
import br.com.browseframework.base.data.type.PageType;
import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.hibernate.criteria.CriteriaBuilder;
import br.com.browseframework.hibernate.dao.CrudDAOHibernate;

/**
 * CRUD DAO implementation.
 * 
 * @author Eduardo
 *
 * @param <ID>
 * @param <T>
 */
public abstract class CrudDAOHibernateImpl<ID extends Serializable, T extends BaseDTO<ID>> implements CrudDAOHibernate<ID, T> {
	
	private final SessionFactory sessionFactory;

	/**
	 * Contructor sets the session factory.
	 * @param sessionFactory
	 */
	public CrudDAOHibernateImpl(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Allows manipulating criteria for findById.
	 * @param criteria
	 */
	protected abstract void criteriaFindById(Criteria criteria);

	/**
	 * Allows manipulating criteria for findAll
	 * @param criteria
	 */
	protected abstract void criteriaFindAll(Criteria criteria);

	/**
	 * Allows manipulating criteria for findAllCount
	 * @param criteria
	 */
	protected abstract void criteriaFindAllCount(Criteria criteria);

	/**
	 * Transforms the id of an object in a concrete Class type.
	 * @param id
	 * @return
	 */
	protected abstract ID getConcreteId(ID id);
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.hibernate.dao.CrudDAOHibernate#getPersistentClass()
	 */
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

	/**
	 * Retrieves a session from session factory.
	 * @return
	 */
	protected Session getCurrentSession(){
		Session result = sessionFactory.getCurrentSession();
		if (result == null || (result != null && (!result.isOpen() || !result.isConnected())) ){
			result = sessionFactory.openSession();
		}
		
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.Crud#removeById(java.io.Serializable)
	 */
	@SuppressWarnings("unchecked")
	public void removeById(ID id) {
		Session session = null;
		try {
			// obtains a session
			session = getCurrentSession();
			// obtains the implementation id
			id = getConcreteId(id);
			// obtains the object using the id
			final T dto = (T) session.get(getPersistentClass(), id);
			// when dto is null throws a lock exception
			if (dto == null) {
				throw new OptimisticLockingFailureException("Not able to retrieve the object with the class [" + getPersistentClass() + "] and the given ID [" + id +"]");
			}
			// removes the object
			session.delete(dto);
		} finally {
			
		}
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.Crud#remove(br.com.browseframework.base.data.dto.BaseDTO)
	 */
	@SuppressWarnings("unchecked")
	public void remove(T dto) {
		if (dto == null){
			throw new GenericBusinessException("The informed DTO is null!");
		}
		Session session = null;
		try {
			// obtains a session
			session = getCurrentSession();
			// when the session doesn't contains the object
			if (!session.contains(dto)) {
				// obtains the object using the id
				ID id = dto.getId();
				dto = (T) session.get(getPersistentClass(), id);
				if (dto == null) {
					throw new OptimisticLockingFailureException("Not able to retrieve in the session, the object with the class [" + getPersistentClass() + "] and the given ID [" + id +"]");
				}
			}
			// removes the object
			session.delete(dto);
		} finally {
			
		}
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.Crud#findAll(br.com.browseframework.base.data.type.PageType)
	 */
	@Override
	public DataPageType<T> findAll(PageType p) {
		return findAll(p, null, null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.Crud#findAll(br.com.browseframework.base.data.type.PageType, br.com.browseframework.base.data.type.CriterionType[])
	 */
	@Override
	public DataPageType<T> findAll(PageType p, CriterionType[] criterions) {
		// Separates the criterions by type
		final List<OrderType> orderingsList = new ArrayList<OrderType>();
		final List<FilterType> filtersList = new ArrayList<FilterType>();
		for (CriterionType ct : criterions){
			if (FilterType.class.isInstance(ct)){
				filtersList.add((FilterType) ct);
			} else {
				orderingsList.add((OrderType) ct);
			}
		}
		// Creates array
		// Order
		OrderType[] ordering = null;
		if (orderingsList.size() > 0){
			ordering = new OrderType[orderingsList.size()];
			ordering = orderingsList.toArray(ordering);
		}
		// Filter
		FilterType[] filters = null;
		if (filtersList.size() > 0){
			filters = new FilterType[filtersList.size()];
			filters = filtersList.toArray(filters);
		}
		
		return findAll(p, ordering, filters);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.Crud#findAll(br.com.browseframework.base.data.type.PageType, br.com.browseframework.base.data.type.OrderType[], br.com.browseframework.base.data.type.FilterType[])
	 */
	@SuppressWarnings("unchecked")
	public DataPageType<T> findAll(PageType p, OrderType[] orderings, FilterType[] filters){
		if (p != null && !p.isValid()) {
			throw new GenericBusinessException("The informed Page is not valid!");
		}
		
		Session session = null;
		try {
			// obtains a session
			session = getCurrentSession();
			// creates criteria and configures it with the page parameters
			final Criteria criteria = session.createCriteria(getPersistentClass());
			if (p != null) {
				criteria.setFirstResult(p.getStartRow());
				criteria.setMaxResults(p.getPageSize());
			}

			// Implementation findAll definitions
			criteriaFindAll(criteria);

			// Builds criteria
			CriteriaBuilder.getInstance().buildFilter(criteria, filters);
			CriteriaBuilder.getInstance().buildOrdering(criteria, orderings);
			
			
			// Executes criteria
			final List<T> list = (List<T>) criteria.list();

			// Obtains number of records
			final Number count = findAllCount(filters);

			// Creates the result Data Page
			final DataPageType<T> result = new DataPage<T>(list, count, p);
			return result;
		} finally {
			
		}
	}
	 
	/**
	 * Row count for the executed query.
	 * @param filters
	 * @return
	 */
	protected Number findAllCount(FilterType[] filters) {
		Number result = 0;
		
		Session session = null;
		try {
			// obtains a session
			session = getCurrentSession();
			final Criteria criteria = session.createCriteria(getPersistentClass());
			
			// Implementation findAll definitions
			criteriaFindAllCount(criteria);
			
			// Builds criteria
			CriteriaBuilder.getInstance().buildFilter(criteria, filters); 

			// Rows count
			criteria.setProjection(Projections.rowCount());

			// Executes criteria
			result = (Number) criteria.uniqueResult();
		} finally {
			
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.Crud#findById(java.io.Serializable)
	 */
	@SuppressWarnings("unchecked")
	public T findById(ID id) {
		T result = null;
		Session session = null;
		try {
			// obtains a session
			session = getCurrentSession();
			
			// obtains the implementation id
			id = getConcreteId(id);
			
			// creates criteria
			final Criteria criteria = session.createCriteria(getPersistentClass());
			
			// Implementation findAById definitions
			criteriaFindById(criteria);
			
			// Refers to primary key id
			criteria.add(Restrictions.eq("id", id));
			
			// Executes criteria
			result = (T) criteria.uniqueResult();
		} finally {
			
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.Crud#save(br.com.browseframework.base.data.dto.BaseDTO)
	 */
	@SuppressWarnings("unchecked")
	public T save(T dto) {
		T result = null;
		
		if (dto == null){
			throw new GenericBusinessException("The informed DTO is null!");
		}

		Session session = null;
		try {
			// obtains a session
			session = getCurrentSession();
			result = (T) session.merge(dto);
		} finally {
			
		}
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.Crud#refresh(br.com.browseframework.base.data.dto.BaseDTO)
	 */
	public void refresh(T dto) {
		Session session = null;
		try {
			// obtains a session
			session = getCurrentSession();
			session.refresh(dto);
		} finally {
			
		}

	}

	// GETTERS && SETTERS 
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
}
