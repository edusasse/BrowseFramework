package br.com.browseframework.application.md.admin.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import br.com.browseframework.application.md.admin.dao.ClientDAO;
import br.com.browseframework.application.md.admin.domain.Client;
import br.com.browseframework.hibernate.dao.impl.CrudDAOHibernateImpl;

@Repository(value = "clientAdminDAO")
public class ClientDAOHibernateImpl extends CrudDAOHibernateImpl<Long, Client> implements ClientDAO {

	@Autowired
	public ClientDAOHibernateImpl(@Qualifier("sessionFactoryAdmin") SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.hibernate.dao.impl.CrudDAOHibernateImpl#criteriaFindAll(org.hibernate.Criteria)
	 */
	@Override
	protected void criteriaFindAll(Criteria criteria) {
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.hibernate.dao.impl.CrudDAOHibernateImpl#criteriaFindAllCount(org.hibernate.Criteria)
	 */
	@Override
	protected void criteriaFindAllCount(Criteria criteria) {
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.hibernate.dao.impl.CrudDAOHibernateImpl#criteriaFindById(org.hibernate.Criteria)
	 */
	@Override
	protected void criteriaFindById(Criteria criteria) {
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.hibernate.dao.impl.CrudDAOHibernateImpl#getConcreteId(java.io.Serializable)
	 */
	@Override
	protected Long getConcreteId(Long id) {
		return id;
	}

}