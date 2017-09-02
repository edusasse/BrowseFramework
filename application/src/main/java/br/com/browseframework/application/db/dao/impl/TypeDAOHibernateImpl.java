package br.com.browseframework.application.db.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import br.com.browseframework.application.db.dao.TypeDAO;
import br.com.browseframework.application.db.domain.Type;
import br.com.browseframework.hibernate.dao.impl.CrudDAOHibernateImpl;

@Repository(value = "typeDAO")
public class TypeDAOHibernateImpl extends CrudDAOHibernateImpl<Long, Type> implements TypeDAO {

	@Autowired
	public TypeDAOHibernateImpl(@Qualifier("browseApplicationSessionFactory") SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected void criteriaFindAll(Criteria arg0) {		
	}

	@Override
	protected void criteriaFindAllCount(Criteria arg0) {		
	}

	@Override
	protected void criteriaFindById(Criteria arg0) {
	}

	@Override
	protected Long getConcreteId(Long arg0) {
		return arg0;
	}
	
}