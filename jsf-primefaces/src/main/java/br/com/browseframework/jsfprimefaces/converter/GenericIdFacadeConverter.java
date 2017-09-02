package br.com.browseframework.jsfprimefaces.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.log4j.Logger;

import br.com.browseframework.base.crud.facade.CrudFacade;
import br.com.browseframework.base.data.dto.BaseDTO;
import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;

/**
 * The Id must represent a String bidirectional parsable.
 *  
 * @author Eduardo
 *
 */
@FacesConverter(value="genericIdFacadeConverter")
public class GenericIdFacadeConverter implements Converter {
	private static Logger log = Logger.getLogger(GenericIdFacadeConverter.class);
	
	public static final String ID_FACADE_CONVERTER_RESOLVER = "browse.ID_FACADE_CONVERTER_RESOLVER";
	
	private IdFacadeResolver idFacadeResolver = null;
	
	/**
	 * With the given key and facade retrieves the object.
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String key_facade) {
    	Object result = null;
    	
    	if (key_facade != null){
    		String[] v = key_facade.trim().split("@");
    		if (v.length == 2){
    			final String keyString = v[0];
    			// FIXME Long class type is forced
    			Long key = Long.MIN_VALUE;
    			boolean error = false;
    			try {
    				key = Long.parseLong(keyString);
    			} catch (Exception e){
    				log.error(e.getMessage());
    				error = true;
    			}
    			
    			if (!error){
    				final String facadeString = v[1];
			        final Object o = FacesUtil.resolveExpression("#{"+ facadeString + "}");
			        if (CrudFacade.class.isInstance(o)){
			        	final CrudFacade facade = (CrudFacade) o;
			        	result = facade.findById(key);
			        }
		    	}
    		} 
    	}
		return result;
    }
 
    /**
	 * When a DTO is passed retrieves the facade for the dto. Otherwise just
	 * cast the passed object to String.
	 */
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object dtoORkey_facade) {
    	String result = null;
    	
    	if (BaseDTO.class.isInstance(dtoORkey_facade)){
    		if (getIdFacadeResolver() == null){
    			doInit();
    		}
    		if (getIdFacadeResolver() != null){
    			// Id
    			// FIXME Long class type is forced
    			@SuppressWarnings("rawtypes")
				final Long id = (Long) ((BaseDTO) dtoORkey_facade).getId();
    			// Facade
    			final String facade = getIdFacadeResolver().getFacadeByBaseDTOCLass(dtoORkey_facade.getClass());
    			if (facade != null){
    				result = id+"@"+facade;
    			} else {
    				throw new GenericBusinessException("Was not able to determine facade for [" + dtoORkey_facade.getClass().getCanonicalName() + "]");
    			}
    		}
    	} else {
    		result = dtoORkey_facade.toString();
    	}
    	
    	return result;
    }
    
    /**
     * Retrieves the bean for the resolver.
     */
    protected void doInit(){
    	final String beanName = FacesUtil.getContextInitParameter(ID_FACADE_CONVERTER_RESOLVER);
    	if (beanName != null){
    		final Object o = FacesUtil.resolveExpression("#{"+beanName.trim()+"}");
    		if (o !=null){
    			if (IdFacadeResolver.class.isInstance(o)){
    				setIdFacadeResolver((IdFacadeResolver) o);
    			} else {
    				throw new GenericBusinessException("The informed paramter [" + beanName + "] must indicate a bean who implements [" + IdFacadeResolver.class.getCanonicalName() + "]");
    			}
    		}
    	}
    }

    // GETTERS && SETTERS
    
	public IdFacadeResolver getIdFacadeResolver() {
		return idFacadeResolver;
	}

	public void setIdFacadeResolver(IdFacadeResolver idFacadeResolver) {
		this.idFacadeResolver = idFacadeResolver;
	}
    
}