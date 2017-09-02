package br.com.browseframework.application.engine.builder.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import br.com.browseframework.application.engine.builder.util.JavassistUtils;
import br.com.browseframework.base.exception.GenericBusinessException;
 
@SuppressWarnings("rawtypes")
public class HibernateBeanBuilder {

	protected static final String ORG_HIBERNATE_ANNOTATIONS_ACCESS_TYPE = "org.hibernate.annotations.AccessType";
	protected static final String ORG_HIBERNATE_ANNOTATIONS_ACCESS_TYPE_value = "value";
	protected static final String JAVAX_PERSISTENCE_COLUMN = "javax.persistence.Column";
	protected static final String JAVAX_PERSISTENCE_GENERATEDVALUE = "javax.persistence.GeneratedValue";
	private static final String JAVAX_PERSISTENCE_ID = "javax.persistence.Id";
	protected static final String JAVAX_PERSISTENCE_ENTITY = "javax.persistence.Entity";
	protected static final String JAVAX_PERSISTENCE_ENTITY_schema = "schema";
	protected static final String JAVAX_PERSISTENCE_ENTITY_name = "name";
	protected static final String JAVAX_PERSISTENCE_ENTITY_access = "access";
	protected static final String JAVAX_PERSISTENCE_COLUMN_name = "name";
	public static final String BASEDTO = "br.com.browseframework.base.data.dto.BaseDTO";
	
	/**
	 * Returns the Entity class.
	 * @param interfaces
	 * @param superClassName
	 * @param packageName
	 * @param className
	 * @param tableName
	 * @return
	 * @throws CannotCompileException
	 * @throws NotFoundException
	 */
	public static CtClass buildEntity(String[] interfaces, String superClassName, String packageName, String className, String schemaName, String tableName)throws CannotCompileException, NotFoundException{
		if (tableName == null || (tableName != null && tableName.trim().equals(""))){
			throw new GenericBusinessException("Table name cannot be empty!");
		}
		// Class name and package
		final CtClass result = JavassistUtils.createClass(packageName.trim()+"."+className );
				
		// Inheritance
		if (superClassName != null){
			JavassistUtils.addSuperClass(result, superClassName);
		}
		
		// Interfaces
		if (interfaces != null){
			JavassistUtils.addInterfaces(result, interfaces);
		}

		// Configures class with Annotation
		final ConstPool constpool = JavassistUtils.configureJavaAnnotation(result);
		
		// Entity annotation configuration
		// -------------------------------
		final Annotation annotEntity = new Annotation(JAVAX_PERSISTENCE_ENTITY, constpool);
		final AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
		result.getClassFile().addAttribute(attr);		
		// defines the table schema
		if (schemaName != null){
			annotEntity.addMemberValue(JAVAX_PERSISTENCE_ENTITY_schema, new StringMemberValue(schemaName,result.getClassFile().getConstPool()));
			attr.addAnnotation(annotEntity);
		}
		// defines the table name
		annotEntity.addMemberValue(JAVAX_PERSISTENCE_ENTITY_name, new StringMemberValue(tableName,result.getClassFile().getConstPool()));
		attr.addAnnotation(annotEntity);
		
		return result;
	}

	public static void addIdField(Class fieldTypeClass, String fieldName, String columnName, CtClass cc) throws NotFoundException, IOException, CannotCompileException, RuntimeException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException {
		// Field
		final CtField f = JavassistUtils.addField(fieldTypeClass, fieldName, cc);

		// Configures with Annotation
		final ConstPool constpool = JavassistUtils.configureJavaAnnotation(cc);
		
		// Adds id column
		// --------------
		final AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
		// @Id
		final Annotation annotID = new Annotation(JAVAX_PERSISTENCE_ID, constpool);
		annotationsAttribute.addAnnotation(annotID);
		
		// @GeneratedValue(strategy = GenerationType.IDENTITY)
		final Annotation annotGV = new Annotation(JAVAX_PERSISTENCE_GENERATEDVALUE, constpool);
		annotationsAttribute.addAnnotation(annotGV);
		
		// @Column(name = "id")
		final Annotation annotCol = new Annotation(JAVAX_PERSISTENCE_COLUMN, constpool);
		annotCol.addMemberValue(JAVAX_PERSISTENCE_COLUMN_name, new StringMemberValue(columnName, constpool));
		annotationsAttribute.addAnnotation(annotCol);
		f.getFieldInfo().addAttribute(annotationsAttribute);

		// Adds the implementation of abstract method getId in BaseDTO
		final AnnotationsAttribute annotationsAttribute2 = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
		final CtMethod m = CtNewMethod.make("public java.io.Serializable getId() { return (java.io.Serializable) " + fieldName + "; }", cc);
		final Annotation annotOver = new Annotation(Override.class.getCanonicalName(), constpool);
		annotationsAttribute2.addAnnotation(annotOver);
		m.getMethodInfo().addAttribute(annotationsAttribute2);
		cc.addMethod(m);
		
		final AnnotationsAttribute annotationsAttribute3 = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
		final CtMethod m3 = CtNewMethod.make("public void setId(java.io.Serializable id) { this." + fieldName + " = (" + fieldTypeClass.getCanonicalName() + ") " + fieldName + "; }", cc);
		final Annotation annotOver2 = new Annotation(Override.class.getCanonicalName(), constpool);
		annotationsAttribute3.addAnnotation(annotOver2);
		m3.getMethodInfo().addAttribute(annotationsAttribute3);
		cc.addMethod(m3);
	}
	
	public static void addColumnField(Class fieldTypeClass, String fieldName, String columnName, CtClass cc) throws NotFoundException, IOException, CannotCompileException, RuntimeException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException {
		// Field
		CtField f = JavassistUtils.addField(fieldTypeClass, fieldName, cc);

		// Configures with Annotation
		ConstPool constpool = JavassistUtils.configureJavaAnnotation(cc);
		
		// Adds id column
		// --------------
		final AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
		// @Column(name = "id")
		final Annotation annotCol = new Annotation(JAVAX_PERSISTENCE_COLUMN, constpool);
		annotCol.addMemberValue(JAVAX_PERSISTENCE_COLUMN_name, new StringMemberValue(columnName, constpool));
		annotationsAttribute.addAnnotation(annotCol);
		f.getFieldInfo().addAttribute(annotationsAttribute);
	}
}