package br.com.browseframework.application.engine.builder.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframework.application.db.domain.Entity;
import br.com.browseframework.application.db.domain.EntityColumn;
import br.com.browseframework.application.db.domain.EntityColumnProperty;
import br.com.browseframework.application.engine.builder.BrowseAppBuilderFacade;
import br.com.browseframework.application.engine.builder.util.JavaClassUtil;
import br.com.browseframework.application.engine.builder.util.JavassistUtils;
import br.com.browseframework.application.engine.enums.EntityColumnPropertyGroupEnum;
import br.com.browseframework.application.engine.exception.BrowseApplicationException;
import br.com.browseframework.base.data.dto.BaseDTO;

@Service(value="browseAppBuilderFacade")
@Transactional(rollbackFor = Throwable.class)
@SuppressWarnings("rawtypes")
public class BrowseAppBuilderFacadeImpl implements BrowseAppBuilderFacade {

	// Controls the class name sufix numeration
	private Map<String, Integer> mapClassNumControl = new HashMap<String, Integer>();
	
	/* (non-Javadoc)
	 * @see br.com.browseframework.application.engine.builder.impl.BrowseAppBuilderFacade#buildHibernateBean(java.lang.String, br.com.browseframework.application.db.domain.Entity)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Class buildHibernateBean(String domainPackgeName, Entity entity) throws CannotCompileException, NotFoundException, IOException, RuntimeException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException{
		return buildHibernateBean(
				null, 
				BaseDTO.class, // default dto super class in the framework
				domainPackgeName,
				entity);
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.application.engine.builder.impl.BrowseAppBuilderFacade#buildHibernateBean(java.lang.String[], java.lang.Class, java.lang.String, br.com.browseframework.application.db.domain.Entity)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Class buildHibernateBean(String[] interfaces, Class dtoClass, String domainPackgeName, Entity entity) throws CannotCompileException, NotFoundException, IOException, RuntimeException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException{
		// Entity validation
		if (entity == null){
			throw new BrowseApplicationException("The informed entity is null!");
		}
		
		// Entity columns validation
		if (entity.getColumns() == null || (entity.getColumns() != null && entity.getColumns().isEmpty())){
			throw new BrowseApplicationException("The informed Entity ID [" + entity.getId() + "] Table name [" + entity.getName() + "] has no columns!");
		}
		
		// Domain package validation
		if (domainPackgeName == null || (domainPackgeName != null && domainPackgeName.trim().equals(""))){
			throw new BrowseApplicationException("The domain package must be informed!");
		}
		domainPackgeName = domainPackgeName.trim(); // removes any blank space

		// Class name
		String className = JavaClassUtil.getClassNameFromTableName(entity.getName());
		if (className == null){
			throw new BrowseApplicationException("Was not possible to determine a class name for Entity ID [" + entity.getId() + "] Table name [" + entity.getName() + "]");
		}
		
		// Class numeration control
		final String key = domainPackgeName.trim() + "." + className;
		Integer num = getMapClassNumControl().get(key);
		if (num == null){
			num = new Integer(0);
		} else {
			num = num + 1;
		}
		getMapClassNumControl().put(key, num);
		className = className + "_$_" + num.toString();

		// Builds entity CtClass
		final CtClass entityCtClass = HibernateBeanBuilder.buildEntity(
				interfaces, 
				dtoClass.getCanonicalName(),
				domainPackgeName, 
				className,
				entity.getSchema(), 
				entity.getName()
		);
		
		// creates the columns
		doEntityColumn(entityCtClass, entity);
		
		// Creates the class
		final Class result = entityCtClass.toClass();
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see br.com.browseframework.application.engine.builder.impl.BrowseAppBuilderFacade#doEntityColumn(javassist.CtClass, br.com.browseframework.application.db.domain.Entity)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void doEntityColumn(CtClass entityCtClass, Entity entity) throws NotFoundException, IOException, CannotCompileException, RuntimeException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException{
		for (EntityColumn ec : entity.getColumns()){
			// Class type
			Class idClassType = Object.class;
			if (ec.getType() != null && ec.getType().getReference() != null){
				idClassType = ec.getType().getReference().getClazz();
			}
			// Field
			final String fieldName = JavaClassUtil.getFieldNameFromTableColumnName(ec.getName());
			
			if (ec.isIdentity()){
				doEntityColumnIdentity(idClassType, fieldName, ec, entityCtClass);
			} else {
				// Adds the field to the CtClass
				HibernateBeanBuilder.addColumnField(idClassType, fieldName, ec.getName(), entityCtClass);
			}
		}
	
		// Create getters and setters methods due the proxy objects
		JavassistUtils.createGettersAndSetters(entityCtClass);
	}

	/**
	 * Create the identity column.
	 * @param entityCtClass
	 * @param ec
	 * @throws NotFoundException
	 * @throws IOException
	 * @throws CannotCompileException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchFieldException
	 * @throws ClassNotFoundException
	 */
	protected void doEntityColumnIdentity(Class idClassType, String fieldName, EntityColumn ec, CtClass entityCtClass) throws NotFoundException, IOException, CannotCompileException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException {
		// If the column is a Embeddable id this group information has to be present
		final EntityColumnProperty ecpEmbeddable = getEntityColumnPropertyByGrupo(EntityColumnPropertyGroupEnum.Embeddable, ec);

		if (ecpEmbeddable != null){
			// TODO create embeddable id for Entity
		} else {
			// Adds the field to the CtClass
			HibernateBeanBuilder.addIdField(idClassType, fieldName, ec.getName(), entityCtClass);
			
		}
	}
	
	/**
	 * Returns the EntityColumnProperty for the given group
	 * @param group
	 * @param ec
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public EntityColumnProperty getEntityColumnPropertyByGrupo(EntityColumnPropertyGroupEnum group, EntityColumn ec){
		EntityColumnProperty result = null;
		if (ec.getProperties() != null){
			for (EntityColumnProperty ecp : ec.getProperties()){
				if (ecp.getGrupo().equals(group)){
					result = ecp;
					break;
				}
			}
		}
		
		return result;
	}

	// GETTERS && SETTERS 
	
	public Map<String, Integer> getMapClassNumControl() {
		return mapClassNumControl;
	}
	 
}
