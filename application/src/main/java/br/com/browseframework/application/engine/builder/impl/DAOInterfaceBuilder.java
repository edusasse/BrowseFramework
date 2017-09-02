package br.com.browseframework.application.engine.builder.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import br.com.browseframework.application.engine.builder.util.JavassistUtils;

 
public class DAOInterfaceBuilder {
	private final static String REPOSITORY_ANNOTATION = "org.springframework.stereotype.Repository";
	private final static String REPOSITORY_ANNOTATION_VALUE = "value";
	private final static String AUTOWIRED_ANNOTATION = "org.springframework.beans.factory.annotation.Autowired";

	public static Class buildInterface(String superClassName, String packageName, String className) throws NotFoundException, IOException, CannotCompileException, RuntimeException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
		Class result = null;

		// Nome da classe e Heranca
		CtClass cc = JavassistUtils.createInterface(packageName.trim()+"."+className );
		
		// Heranca
		if (superClassName != null){
			JavassistUtils.addSuperClass(cc, superClassName);
		}
	  
		// cc.writeFile("c:\\temp\\jad\\teste");
		result = cc.toClass();
		
		return result;
	}
}