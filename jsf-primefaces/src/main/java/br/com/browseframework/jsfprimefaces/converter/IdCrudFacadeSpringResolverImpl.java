package br.com.browseframework.jsfprimefaces.converter;

import java.util.Map;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.com.browseframework.base.crud.facade.CrudFacade;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;
import br.com.browseframework.util.spring.SpringUtil;

/**
 * Filtering spring beans by CrudFacade type determines the facade bean. 
 * @author Eduardo
 *
 */
public class IdCrudFacadeSpringResolverImpl implements IdFacadeResolver {

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.jsfprimefaces.converter.IdFacadeResolver#getFacadeByBaseDTOCLass(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String getFacadeByBaseDTOCLass(Class dtoClass) {
		String result = null;
		final Map<String, CrudFacade> mapCrudFacades = WebApplicationContextUtils.getRequiredWebApplicationContext(FacesUtil.getServletContext()).getBeansOfType(CrudFacade.class);
		for (CrudFacade cf : mapCrudFacades.values()){
			// Un proxies the facade object
			try { 
				cf = (CrudFacade) SpringUtil.unProxy(cf);
			} catch (Exception e){
				continue;
			}
			if (cf != null && dtoClass.equals(cf.getPersistentClass())){
				final Service service = cf.getClass().getAnnotation(Service.class);
				if (service != null){
					result = service.value();
					break;
				}
			}
		}
		
		return result;
	}

}