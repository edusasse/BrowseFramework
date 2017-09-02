package br.com.browseframework.base.crud.dao;

import java.io.Serializable;

import br.com.browseframework.base.crud.Crud;
import br.com.browseframework.base.data.dto.BaseDTO;

/**
 * CRUD DAO interface
 * @author Eduardo
 *
 * @param <ID>
 * @param <T>
 */
public interface CrudDAO<ID extends Serializable, T extends BaseDTO<ID>> extends Crud<ID, T> {

}