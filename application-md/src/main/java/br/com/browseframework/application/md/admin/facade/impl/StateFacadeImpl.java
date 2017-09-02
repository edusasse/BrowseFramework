package br.com.browseframework.application.md.admin.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.browseframework.application.md.admin.crud.annotations.AdminTx;
import br.com.browseframework.application.md.admin.crud.facade.AdminTxCrudFacadeImpl;
import br.com.browseframework.application.md.admin.dao.StateDAO;
import br.com.browseframework.application.md.admin.domain.State;
import br.com.browseframework.application.md.admin.facade.StateFacade;
import br.com.browseframework.base.data.Filter;
import br.com.browseframework.base.data.type.CriterionType;

@Service(value = "stateAdminFacade")
public class StateFacadeImpl extends AdminTxCrudFacadeImpl<Long, State> implements StateFacade {
 
	@Autowired
	public StateFacadeImpl(StateDAO dao) {
		super(dao); 
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.admin.facade.StateFacade#findStateByURLNickname(java.lang.String)
	 */
	@AdminTx
	public State findStateByURLNickname(String urlNickname){
		State result = null;
		if (urlNickname != null){
			result = findAll(null, new CriterionType[]{new Filter("urlNickname", urlNickname)}).getFirst();
		}
		return result;
	}
}