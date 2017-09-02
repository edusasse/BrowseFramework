package br.com.browseframework.base.crud.facade;

import java.io.Serializable;

import br.com.browseframework.base.crud.Crud;
import br.com.browseframework.base.data.dto.BaseDTO;

/**
 * CRUD Facade interface.
 * @author Eduardo
 *
 * @param <ID>
 * @param <T>
 */
public interface CrudFacade<ID extends Serializable, T extends BaseDTO<ID>> extends Crud<ID, T> { 
	
	public Class<T> getPersistentClass();
	
}