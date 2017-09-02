package br.com.browseframework.base.crud;

import java.io.Serializable;

import br.com.browseframework.base.data.dto.BaseDTO;
import br.com.browseframework.base.data.type.CriterionType;
import br.com.browseframework.base.data.type.DataPageType;
import br.com.browseframework.base.data.type.FilterType;
import br.com.browseframework.base.data.type.OrderType;
import br.com.browseframework.base.data.type.PageType;

/**
 * CRUD interface
 * @author Eduardo
 *
 * @param <ID>
 * @param <T>
 */
public interface Crud<ID extends Serializable, T extends BaseDTO<ID>> {

    /**
     * Insert for new object, update for others.
     * @param dto
     * @return
     */
    public T save(T dto);  

    /**
     * Removes the object by filtering its ID.
     * 
     * @param id
     */
    public void removeById(ID id);
    
    /**
     * Removes the object.
     * 
     * @param id
     */
    public void remove(T dto);
    
    /**
     * Refreshes the DTO instance.
     */
    public void refresh(T dto);
    
    /**
     * Search all records in a paged.
     * @param p Page, when null page is informed,  pagination will not happen
     * @param criterions Ordering/Filter criterions
     * @return
     */
    public DataPageType<T> findAll(PageType p, CriterionType[] criterions);
  
    /**
     * Search all records in a paged.
     * @param p Page, when null page is informed,  pagination will not happen
     * @param orderings Ordering criterion
     * @param filters Filter paramters
     * @return
     */
    public DataPageType<T> findAll(PageType p, OrderType[] orderings, FilterType[] filters);
    
    /**
     * Search all records in a paged.
     * @param p Page, when null page is informed,  pagination will not happen
     * @return
     */
    public DataPageType<T> findAll(PageType p);
     
    /**
     * Searches by ID
     * 
     * @param id
     * @return
     */
    public T findById(ID id);

}