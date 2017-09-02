package br.com.browseframework.application.test;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframework.application.db.domain.Entity;
import br.com.browseframework.application.db.domain.EntityColumn;
import br.com.browseframework.application.db.domain.Type;
import br.com.browseframework.application.db.facade.EntityColumnFacade;
import br.com.browseframework.application.db.facade.EntityFacade;
import br.com.browseframework.application.db.facade.TypeFacade;
import br.com.browseframework.application.engine.builder.BrowseAppBuilderFacade;
import br.com.browseframework.application.engine.enums.TypeReferenceEnum;

/**
 * Creates an Entity with two columns. One is the identity column and the other a simple text column.
 * Type entities are created as needed.
 * Validates the Fields and each of its Getters and Setters created, so as the overrided getId and setId(Serializable) from BaseDTO.
 * Validated annotations:
 * -Class:
 * javax.persistence.Entity
 * 
 * -Field:
 * javax.persistence.Id
 * javax.persistence.GeneratedValue
 * javax.persistence.Column
 * 
 * @author Eduardo
 *
 */
@TransactionConfiguration(transactionManager="browseApplicationTransactionManager")
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/test-application.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TestSimpleEntity extends AbstractTransactionalJUnit4SpringContextTests {
	
	static final Logger log = Logger.getLogger(TestSimpleEntity.class);
	
	@Autowired
	private EntityFacade entityFacade;
	
	@Autowired
	private EntityColumnFacade entityColumnFacade;
	
	@Autowired
	private TypeFacade typeFacade;

	@Autowired
	private BrowseAppBuilderFacade browseAppBuilderFacade;
	
	@Before
	public void init() {
		// Obtains BrowseAppBuilderFacade bean
		Assert.assertNotNull("Could not obtain BrowseAppBuilderFacade Bean!", browseAppBuilderFacade);
	} 

	@Test
	public void test() throws Exception {
		// Test Types
		// ------------
		final Type type01 = new Type();
		type01.setName("Text");
		type01.setReference(TypeReferenceEnum.String);
		try {
			typeFacade.save(type01);
		} catch (Exception e){
			Assert.fail(e.getMessage());
		}
		
		final Type type02 = new Type();
		type02.setName("Big Integer");
		type02.setReference(TypeReferenceEnum.Long);		
		try {
			typeFacade.save(type02);
		} catch (Exception e){
			Assert.fail(e.getMessage());
		}
		
		// Test Entities and Columns
		// --------------------------
		final Entity entity01 = new Entity();
		entity01.setName("stock");
		entity01.setSchema("test2");
		try {
			entityFacade.save(entity01);
		} catch (Exception e){
			Assert.fail(e.getMessage());
		}
		
		// Entity01 - Column
		final EntityColumn entityColumn01 = new EntityColumn();
		entityColumn01.setEntity(entity01);
		entityColumn01.setIdentity(true);
		entityColumn01.setName("cod_id");
		entityColumn01.setType(type02);
		try {
			entityColumnFacade.save(entityColumn01);
		} catch (Exception e){
			Assert.fail(e.getMessage());
		}
		
		// Entity02 - Column
		final EntityColumn entityColumn02 = new EntityColumn();
		entityColumn02.setEntity(entity01);
		entityColumn02.setIdentity(false);
		entityColumn02.setName("des_name");
		entityColumn02.setType(type01);
		try {
			entityColumnFacade.save(entityColumn02);
		} catch (Exception e){
			Assert.fail(e.getMessage());
		}
		
		log.info("Refreshes the entity to retrieve the columns");
		entityFacade.refresh(entity01);
		
		Class tableClass = null;
		try {
			tableClass = browseAppBuilderFacade.buildHibernateBean("com.test", entity01);
		} catch (CannotCompileException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (NotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (RuntimeException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// Annotation - Entity
		// --------------------
		Assert.assertNotNull("Annotation - javax.persistence.Entity not found!", tableClass.getAnnotation(javax.persistence.Entity.class));
		
		// Field and Field Annotations
		// --------
		try {
			// field codId
			final Field fieldCodId = tableClass.getDeclaredField("codId");
			Assert.assertNotNull("Field - Not found field [codId]!", fieldCodId);
			// annotations from codId field		
			Assert.assertNotNull("Annotation - javax.persistence.Id not found on [codId]!", fieldCodId.getAnnotation(javax.persistence.Id.class));
			Assert.assertNotNull("Annotation - javax.persistence.GeneratedValue not found on [codId]!", fieldCodId.getAnnotation(javax.persistence.GeneratedValue.class));
			Assert.assertNotNull("Annotation - javax.persistence.Column not found on [codId]!", fieldCodId.getAnnotation(javax.persistence.Column.class));
			// ------
			// field codId
			final Field fieldDesName = tableClass.getDeclaredField("desName");
			Assert.assertNotNull("Field - Not found field [desName]!", fieldDesName);
			Assert.assertNotNull("Annotation - javax.persistence.Column not found on [desName]!", fieldDesName.getAnnotation(javax.persistence.Column.class));
		} catch (SecurityException e1) {
			e1.printStackTrace();
			fail(e1.getMessage());
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
			fail(e1.getMessage());
		}
		
		// Methods
		// --------
		try {
			// BaseDTO
			Assert.assertNotNull("Method - Not found overrided method from BaseDTO [setId]!", tableClass.getDeclaredMethod("setId", new Class[]{Serializable.class}));
			Assert.assertNotNull("Method - Not found overrided method from BaseDTO [getId]!", tableClass.getDeclaredMethod("getId", null));
			// Knowed methods for the class
			Assert.assertNotNull("Method - Not found property SET method for [codId]!", tableClass.getDeclaredMethod("setCodId", new Class[]{entityColumn01.getType().getReference().getClazz()}));
			Assert.assertNotNull("Method - Not found property GET method for [codId]!", tableClass.getDeclaredMethod("getCodId", null));
			Assert.assertNotNull("Method - Not found property SET method for [desName]!", tableClass.getDeclaredMethod("setDesName", new Class[]{entityColumn02.getType().getReference().getClazz()}));
			Assert.assertNotNull("Method - Not found property GET method for [desName]!", tableClass.getDeclaredMethod("getDesName", null));
		} catch (SecurityException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
	
	@After
	public void after() throws Exception {
		log.info("Simple Entity test done!");
	}

}