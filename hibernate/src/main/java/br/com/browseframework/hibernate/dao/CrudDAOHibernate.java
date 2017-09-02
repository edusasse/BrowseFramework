package br.com.browseframework.hibernate.dao;

import java.io.Serializable;

import br.com.browseframework.base.crud.Crud;
import br.com.browseframework.base.data.dto.BaseDTO;

/**
 * CRUD DAO interface.
 * @author Eduardo
 *
 * @param <ID>
 * @param <T>
 */
public interface CrudDAOHibernate<ID extends Serializable, T extends BaseDTO<ID>> extends Crud<ID, T> {

    /**
     * Corresponding DTO class.
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
	public Class getPersistentClass();
     
}