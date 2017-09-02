package br.com.browseframework.application.md.admin.crud.facade;

import java.io.Serializable;

import org.springframework.transaction.annotation.Transactional;

import br.com.browseframework.application.md.admin.crud.annotations.AdminTx;
import br.com.browseframework.base.crud.dao.CrudDAO;
import br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl;
import br.com.browseframework.base.data.dto.BaseDTO;
import br.com.browseframework.base.data.type.CriterionType;
import br.com.browseframework.base.data.type.DataPageType;
import br.com.browseframework.base.data.type.FilterType;
import br.com.browseframework.base.data.type.OrderType;
import br.com.browseframework.base.data.type.PageType;

@Transactional(rollbackFor = Throwable.class, value="transactionManagerAdmin")
public abstract class AdminTxCrudFacadeImpl<ID extends Serializable, T extends BaseDTO<ID>> extends CrudFacadeImpl<ID, T> {
 
	public AdminTxCrudFacadeImpl(CrudDAO<ID, T> crudDAO) {
		super(crudDAO);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.crud.Crud#save(br.com.browseframework.data.dto.BaseDTO)
	 */
	@AdminTx
	public T save(T dto) {
		return super.save(dto);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.crud.Crud#removeById(java.io.Serializable)
	 */
	@AdminTx
	public void removeById(ID id) {
		super.removeById(id);
	};
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.crud.Crud#remove(br.com.browseframework.data.dto.BaseDTO)
	 */
	@AdminTx
	public void remove(T dto) {
		super.remove(dto);
	};
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.crud.Crud#refresh(br.com.browseframework.data.dto.BaseDTO)
	 */
	@AdminTx
	public void refresh(T dto) {
		super.refresh(dto);
	};
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.Crud#findById(java.io.Serializable)
	 */
	@AdminTx
	public T findById(ID id){
		return super.findById(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.crud.Crud#findAll(br.com.browseframework.data.type.PageType, br.com.browseframework.data.type.CriterionType[])
	 */
	@AdminTx
	public DataPageType<T> findAll(PageType p, CriterionType[] criterion) {
		return super.findAll(p, criterion);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.crud.Crud#findAll(br.com.browseframework.data.type.PageType, br.com.browseframework.data.type.OrderType, br.com.browseframework.data.type.FilterType)
	 */
	@AdminTx
	public DataPageType<T> findAll(PageType p, OrderType[] orderings, FilterType[] filters) {
		return super.findAll(p, orderings, filters);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.crud.Crud#findAll(br.com.browseframework.data.type.PageType)
	 */
	@AdminTx
	public DataPageType<T> findAll(PageType p) {
		return super.findAll(p);
	}
	
}