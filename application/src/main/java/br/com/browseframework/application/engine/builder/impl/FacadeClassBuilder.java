package br.com.browseframework.application.engine.builder.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;
import br.com.browseframework.application.engine.builder.util.JavassistUtils;
import br.com.browseframework.base.crud.dao.CrudDAO;

@SuppressWarnings("rawtypes") 
public class FacadeClassBuilder {
	private final static String REPOSITORY_ANNOTATION = "org.springframework.stereotype.Repository";
	private final static String REPOSITORY_ANNOTATION_VALUE = "value";
	private final static String AUTOWIRED_ANNOTATION = "org.springframework.beans.factory.annotation.Autowired";

	public static Class buildClass(String[] interfaces, String superClassName, String autowiredDAOClassName, String packageName, String className, String repositoryValue) throws NotFoundException, IOException, CannotCompileException, RuntimeException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException {
		Class result = null;

		// Nome da classe
		CtClass cc = JavassistUtils.createClass(packageName.trim()+"."+className );
		
		// Herança
		if (superClassName != null){
			JavassistUtils.addSuperClass(cc, superClassName);
		}
		
		// Interfaces
		if (interfaces != null){
			JavassistUtils.addInterfaces(cc, interfaces);
		}
		
		// Prepara para Annotation 
		final ClassFile ccFile = cc.getClassFile();
		final ConstPool constpool = ccFile.getConstPool();
		ccFile.setVersionToJava5();
		
		// Repository - Classe
		final AnnotationsAttribute attrRepository = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
		final Annotation annotRepository = new Annotation(REPOSITORY_ANNOTATION, constpool);
		annotRepository.addMemberValue(REPOSITORY_ANNOTATION_VALUE, new StringMemberValue(repositoryValue, ccFile.getConstPool()));
		attrRepository.addAnnotation(annotRepository);
		ccFile.addAttribute(attrRepository);

		// Construtor
		final CtConstructor constructor = JavassistUtils.addConstructor(cc, new Class[]{CrudDAO.class});

		// Autowired - Construtor
		final AnnotationsAttribute attrAutowired = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
		final Annotation annotAutowired = new Annotation(AUTOWIRED_ANNOTATION, constpool);
		attrAutowired.addAnnotation(annotAutowired);
		constructor.getMethodInfo().addAttribute(attrAutowired);
		
		result = cc.toClass();
		
		return result;
	}
}