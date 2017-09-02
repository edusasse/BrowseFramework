package br.com.browseframework.application.engine.builder.util;

import java.util.HashSet;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;

import org.apache.commons.proxy.ProxyUtils;
import org.apache.commons.proxy.exception.ObjectProviderException;

/**
 * Some utility methods for dealing with Javassist. This class is not part of the public API!
 * @author Eduardo
 *
 */
@SuppressWarnings("rawtypes")
public class JavassistUtils {
	// ----------------------------------------------------------------------------------------------------------------------
	// Fields
	// ----------------------------------------------------------------------------------------------------------------------
	private static final ClassPool classPool = new ClassPool();

	static {
		classPool.appendClassPath(new LoaderClassPath(ClassLoader.getSystemClassLoader()));
	}

	private static final Set classLoaders = new HashSet();

	// ----------------------------------------------------------------------------------------------------------------------
	// Static Methods
	// ----------------------------------------------------------------------------------------------------------------------

	/**
	 * Adds a field to a class.
	 * 
	 * @param fieldType
	 *            the field's type
	 * @param fieldName
	 *            the field name
	 * @param enclosingClass
	 *            the class receiving the new field
	 * @throws CannotCompileException
	 *             if a compilation problem occurs
	 */
	public static CtField addField(Class fieldType, String fieldName, CtClass enclosingClass) throws CannotCompileException {
		final CtField result = new CtField(resolve(fieldType), fieldName, enclosingClass);
		enclosingClass.addField(result);
		return result;
	}

	public static CtField addField(String fieldTypeClassName, String fieldName, CtClass enclosingClass) throws CannotCompileException, NotFoundException, ClassNotFoundException {
		final Class fieldType = Class.forName(fieldTypeClassName);
		return addField(fieldType, fieldName, enclosingClass);
	}
	
	/**
	 * Finds the {@link CtClass} corresponding to the Java {@link Class} passed
	 * in.
	 * 
	 * @param clazz
	 *            the Java {@link Class}
	 * @return the {@link CtClass}
	 */
	@SuppressWarnings("unchecked")
	public static CtClass resolve(Class clazz) {
		synchronized (classLoaders) {
			try {
				final ClassLoader loader = clazz.getClassLoader();
				if (loader != null && !classLoaders.contains(loader)) {
					classLoaders.add(loader);
					classPool.appendClassPath(new LoaderClassPath(loader));
				}
				return classPool.get(ProxyUtils.getJavaClassName(clazz));
			} catch (NotFoundException e) {
				throw new ObjectProviderException(
						"Unable to find class " + clazz.getName()
								+ " in default Javassist class pool.", e);
			}
		}
	}

	/**
	 * Adds interfaces to a {@link CtClass}
	 * 
	 * @param ctClass
	 *            the {@link CtClass}
	 * @param proxyClasses
	 *            the interfaces
	 * @throws NotFoundException 
	 */
	public static void addInterfaces(CtClass ctClass, String[] interfaces) throws NotFoundException {
		for (int i = 0; i < interfaces.length; i++) {
			String interfaze = interfaces[i];
			final CtClass ctInterface = classPool.get(interfaze);
			ctClass.addInterface(ctInterface);
		}
	}
	
	/**
	 * Adds interfaces to a {@link CtClass}
	 * 
	 * @param ctClass
	 *            the {@link CtClass}
	 * @param proxyClasses
	 *            the interfaces
	 */
	public static void addInterfaces(CtClass ctClass, Class[] proxyClasses) {
		for (int i = 0; i < proxyClasses.length; i++) {
			Class proxyInterface = proxyClasses[i];
			ctClass.addInterface(resolve(proxyInterface));
		}
	}
	
	/**
	 * Creates a new {@link CtClass} derived from the Java {@link Class} using
	 * the supplied base name.
	 * 
	 * @param baseName
	 *            the base name
	 * @param superClass
	 *            the superclass
	 * @return the new derived {@link CtClass}
	 * @throws CannotCompileException 
	 * @throws NotFoundException 
	 */
	public synchronized static CtClass createClass(String baseName,	String superClass) throws CannotCompileException, NotFoundException {
		final CtClass superCtClass = classPool.get(superClass);
		return createClass(baseName, superCtClass.toClass());
	}

	/**
	 * Creates a new {@link CtClass} derived from the Java {@link Class} using
	 * the supplied base name.
	 * 
	 * @param baseName
	 *            the base name
	 * @param superclass
	 *            the superclass
	 * @return the new derived {@link CtClass}
	 */
	public synchronized static CtClass createClass(String baseName,	Class superclass) {
		final CtClass result = classPool.makeClass(baseName, resolve(superclass));
		return result;
	}
	

	/**
	 * Creates a new {@link CtClass} derived from the Java {@link Class} using
	 * the supplied base name.
	 * 
	 * @param baseName
	 *            the base name
	 * @param superclass
	 *            the superclass
	 * @return the new derived {@link CtClass}
	 */
	public synchronized static CtClass createClass(String baseName) {
		return classPool.makeClass(baseName);
	}
	
	/**
	 * Creates a new {@link CtClass} derived from the Java {@link Class} using
	 * the supplied base name.
	 * 
	 * @param baseName
	 *            the base name
	 * @param superClass
	 *            the superclass
	 * @return the new derived {@link CtClass}
	 * @throws CannotCompileException 
	 * @throws NotFoundException 
	 */
	public synchronized static CtClass createInterface(String baseName,	String superClass) throws CannotCompileException, NotFoundException {
		final CtClass superCtClass = classPool.get(superClass);
		return createInterface(baseName, superCtClass.toClass());
	}
	
	/**
	 * Creates a new {@link CtClass} derived from the Java {@link Class} using
	 * the supplied base name.
	 * 
	 * @param baseName
	 *            the base name
	 * @param superClass
	 *            the superclass
	 * @return the new derived {@link CtClass}
	 * @throws CannotCompileException 
	 * @throws NotFoundException 
	 */
	public synchronized static CtClass createInterface(String baseName) throws CannotCompileException, NotFoundException {
		return classPool.makeInterface(baseName);
	}
	
	/**
	 * Creates a new {@link CtClass} derived from the Java {@link Class} using
	 * the supplied base name.
	 * 
	 * @param baseName
	 *            the base name
	 * @param superclass
	 *            the superclass
	 * @return the new derived {@link CtClass}
	 */
	public synchronized static CtClass createInterface(String baseName,	Class superclass) {
		return classPool.makeInterface(baseName, resolve(superclass));
	}

	/**
	 * 
	 * @param cc
	 * @param constructorParamters
	 * @throws CannotCompileException 
	 */
	public synchronized static CtConstructor addConstructor(CtClass cc, Class[] constructorParamters) throws CannotCompileException {
		final CtConstructor constructor = new CtConstructor(JavassistUtils.resolve(constructorParamters), cc);
		constructor.setBody( "{super($$);}" );
		cc.addConstructor(constructor);
		return constructor;
	}

	public synchronized static void addSuperClass(CtClass cc, String superClassName) throws CannotCompileException, NotFoundException{
		cc.setSuperclass(classPool.get(superClassName));
	}
	
	/**
	 * Resolves an array of Java {@link Class}es to an array of their
	 * corresponding {@link CtClass}es.
	 * 
	 * @param classes
	 *            the Java {@link Class}es
	 * @return the corresponding {@link CtClass}es
	 */
	public static CtClass[] resolve(Class[] classes) {
		final CtClass[] ctClasses = new CtClass[classes.length];
		for (int i = 0; i < ctClasses.length; ++i) {
			ctClasses[i] = resolve(classes[i]);
		}
		return ctClasses;
	}
	
	/**
	 * Configures class for annotation setting java version to 5.
	 * @param cc
	 * @return
	 */
	public static ConstPool configureJavaAnnotation(CtClass cc){		
		final ClassFile ccFile = cc.getClassFile();
		final ConstPool result = ccFile.getConstPool();
		ccFile.setVersionToJava5();
		
		return result;
	}
	
	public static void createGettersAndSetters(CtClass cc) throws CannotCompileException, NotFoundException{
	
		CtField[] fields = cc.getDeclaredFields();
		 
		for (int m = 0; m < fields.length; m++) {
			final String name = fields[m].getName().substring(0,1).toUpperCase() 
						+ (fields[m].getName().length() > 1 ? fields[m].getName().substring(1, fields[m].getName().length()) : "");
			if (name.equals("Id")){
				continue;
			}
			final String getIs = Boolean.class.getCanonicalName().equals(fields[m].getType().getName()) ? "is" : "get";
			
			cc.addMethod(CtNewMethod.setter("set" + name, fields[m]));
			cc.addMethod(CtNewMethod.getter(getIs + name, fields[m]));
			
		}
	}

	public static void addMethod(CtClass cc, String method) throws CannotCompileException{
		CtMethod m = CtNewMethod.make(method,cc);
		cc.addMethod(m);
	}
}
