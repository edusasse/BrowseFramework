package br.com.browseframework.util.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import br.com.browseframework.base.exception.BusinessException;
import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.base.exception.GenericReflectionException;

/**
 * Reflection manipulation util.
 * @author Eduardo
 *
 */
public class ReflectionUtil {
 
	/**
	 * Retrieves the GET method to inform the attribute.
	 * @param clazz
	 * @param fieldName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static Method getGetMethodByFieldName(@SuppressWarnings("rawtypes") Class clazz, String fieldName) throws SecurityException, NoSuchFieldException{
		Method result = null;
		// Gets the attribute
		final Field atributo = getDeclaredFieldForClass(clazz, fieldName);
		if (atributo == null){
			throw new NoSuchFieldException("The field [" + fieldName + "] was not found in the class [" + clazz.getCanonicalName() + "] or its super classes.");
		}
		
		// Search the method in the class where the attribute was declared
		for (Method m: atributo.getDeclaringClass().getMethods()){
			// chooses the name of the prefix
			String prefix = "get";
			if (boolean.class.equals(atributo.getType())){
				prefix = "is";
			}

			// get prefix | is concatenated to the name of the field where the first letter should be capitalized
			final String methodName = prefix+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1,fieldName.length());
			// verifies the name of the method, the return to the method must equal the attribute type and should not be a parameter required for the method
			if (m.getName().equals(methodName) && m.getReturnType().equals(atributo.getType()) && m.getParameterTypes().length == 0){
				result = m;
				break;
			}
			if (result == null){
				// TODO: SuperClass check
			}
		}
		return result;
	}
	
	/**
	 * Retrieves the method for attribute SET informed
	 * @param clazz
	 * @param fieldName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static Method getSetMethodByFieldName(@SuppressWarnings("rawtypes") Class clazz, String fieldName) throws SecurityException, NoSuchFieldException{
		Method result = null;
		// Gets the attribute
		final Field atributo = getDeclaredFieldForClass(clazz, fieldName);
		if (atributo == null){
			throw new NoSuchFieldException("O atributo " + fieldName + " não foi encontrado para classe " + clazz.getCanonicalName() + " ou suas super classes.");
		}
		
		// Search the method in the class where the attribute was declared
		for (Method m: atributo.getDeclaringClass().getMethods()){
			// prefix "set" concatenated to the name of the field where the first letter should be capitalized
			final String methodName = "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1,fieldName.length());
			// verifies the name of the method, it must receive only one parameter to be the type of the attribute
			if (m.getName().equals(methodName) && m.getParameterTypes().length == 1 && m.getParameterTypes()[0].equals(atributo.getType())){
				result = m;
				break;
			}
		}
		return result;
	}
	
	/**
	 * Returns the attribute in the class informed or in some super class.
	 * @param clazz
	 * @param fieldName
	 * @return
	 * @throws SecurityException
	 */
	public static Field getDeclaredFieldForClass(@SuppressWarnings("rawtypes") Class clazz, String fieldName) throws SecurityException {
		Field result = null;
		
		if (fieldName != null && fieldName.trim().length() > 0){
			// Aux
			@SuppressWarnings("rawtypes")
			Class clazzAux = clazz; // defines the initial class as the passed class
			Field fieldAux = null;
			
			String[] ca = fieldName.split("\\.");
			
			boolean nil = false;
			for (String f : ca){
				fieldAux = getDeclaredFieldForClassImpl(clazzAux, f);
				if (fieldAux == null){
					nil = true;
					break; 
				} else {
					clazzAux = fieldAux.getType();
				}
			}
			
			if (!nil){
				result = fieldAux;
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the attribute in the class informed or in some super class.
	 * @param clazz
	 * @param fieldName
	 * @return
	 * @throws SecurityException
	 */
	private static Field getDeclaredFieldForClassImpl(@SuppressWarnings("rawtypes") Class clazz, String fieldName) throws SecurityException {
		Field result = null;
		try {
			// Search to find the attribute in the class informed
			result = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			result = null;
			// Exception if the shutter is not necessary to treat as the search continued in the super class
		}

		// If you have not found the attribute
		if (result == null)
			// If the super class is not, nor Object Class
			if (!(clazz.getSuperclass().equals(Object.class)) && !(clazz.getSuperclass().equals(Class.class))) {
				// performs recursive call passing the super class
				return getDeclaredFieldForClass(clazz.getSuperclass(), fieldName);
			}
		return result;
	}
	
	/**
	 * Returns the list of attributes of the class and the whole chain of inheritance until the level of Object.
	 * @param clazz
	 * @return
	 */
	public static List<Field> getListDeclaredFieldForClass(@SuppressWarnings("rawtypes") Class clazz){
		final List<Field> result = new ArrayList<Field>();
		// chamada recursiva
		getListDeclaredFieldForClass(clazz, result);
		return result;
	}
	
	/**
	 * Load the list of attributes of the class.
	 * @param clazz
	 * @param listOfFields
	 */
	private static void getListDeclaredFieldForClass(@SuppressWarnings("rawtypes") Class clazz, List<Field> listOfFields) {
		// adiciona todos os atributos da classe na lista
		for (Field f : clazz.getDeclaredFields()) {
			listOfFields.add(f);
		}
		// Caso a super classe não seja Object e nem Class
		if (!(clazz.getSuperclass().equals(Object.class)) && !(clazz.getSuperclass().equals(Class.class))) {
			// faz nova chamada recursiva
			getListDeclaredFieldForClass(clazz.getSuperclass(), listOfFields);
		}	 
	}

	/**
	 * Loads the value entered in the attribute by calling the method SET.
	 * @param object Objeto
	 * @param path Atributo
	 * @param value Valor a carregar
	 * @return
	 */
	public static Object setObjectValue(Object object, String path, Object value) {
		// Validações
		if (object == null){
			throw new GenericBusinessException("The object can not be null. You will not be possible to obtain the attribute ["+ path +"] ");
		}
		if (Object.class.equals(object.getClass())){
			throw new GenericBusinessException("The object can not be of the type [Object]");
		}
		if (Class.class.equals(object.getClass())){
			throw new GenericBusinessException("The object can not be of the type [Class] ");
		}
		
		String[] ca = path.split("\\.");
		for (int i = 0; i < ca.length; i++) {
			try {	
				if (object == null){
					 continue;
				}
				Field f = getDeclaredFieldForClass(object.getClass(), ca[i]);
				if (f == null){
					continue;
				}
				// Loads the object to the class expected
				final Object objAjustado = adjustClassToObject(value, f.getType());
				// if you have reached the last element
				if (i + 1 == ca.length) {
					// method retrieves the set of attribute
					final Method metodoSet = getSetMethodByFieldName(object.getClass(), ca[i]);
					if (metodoSet == null){
						throw new GenericReflectionException("Unable to retrieve the method \"set \" for attribute ["+ ca[i] +" ("+ path +")] class ["+ object.getClass () +"] ");
					}
					// method relies on well by setting the value
					metodoSet.invoke(object, objAjustado);
				} else {
					// the get method to retrieve the attribute
					final Method metodoGet = getGetMethodByFieldName(object.getClass(), ca[i]);
					if (metodoGet == null){
						throw new GenericReflectionException("Não foi possível recuperar o método \"get\" para o atributo [" + ca[i] + "] assim não sera possível chegar ao elemento final do caminho ["+ path + "] na classe [" + object.getClass() + "]");
					}
					// invokes
					object = metodoGet.invoke(object, (Object[]) null);
				}
			} catch (SecurityException e) {
				throw new GenericReflectionException("Unable to load the value ["+ value +"] ["+ e.getMessage () +"] ", e);
			} catch (IllegalArgumentException e) {
				throw new GenericReflectionException("Unable to load the value ["+ value +"] ["+ e.getMessage () +"] ", e);
			} catch (IllegalAccessException e) {
				throw new GenericReflectionException("Unable to load the value ["+ value +"] ["+ e.getMessage () +"] ", e);
			} catch (NoSuchFieldException e) {
				throw new GenericReflectionException("Unable to load the value ["+ value +"] ["+ e.getMessage () +"] because the attribute was not found. ", e);
			} catch (InvocationTargetException e) {
				throw new GenericReflectionException("Ocorreu um erro ao carregar o valor ["+value+"]["+e.getMessage()+"]",e);
			}
		}
		return object;
	}

	/**
	 * Returns the class attribute to the last level of informed, seeking in their class informed and their super classes
	 * @param clazz
	 * @param path
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Class getClassForPath(Class clazz, String path) {
		String[] ca = path.split("\\.");
		for (String c : ca) {
			try {
				if (clazz != null) {
					Field f = getDeclaredFieldForClass(clazz, c);
					if (f != null) {
						clazz = f.getType();
					} else {
						clazz = null;
					}
				}
			} catch (SecurityException e) {
				throw new GenericReflectionException("Unable to retrieve the class ["+ clazz +"] in path ["+ path +"] ["+ e.getMessage () +"] ", e);
			} catch (IllegalArgumentException e) {
				throw new GenericReflectionException("Unable to retrieve the class ["+ clazz +"] in path ["+ path +"] ["+ e.getMessage () +"] ", e);
			}
		}
		return clazz;
	}
	 
	/**
	 * Retrieves method for the informed object
	 * @param o
	 * @param metodo
	 * @param parametros
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Method getMethod(Object o, String metodo, Class... parametros) {
		Method result = null;
		if (o != null) {
			result = getMethod(o.getClass(), metodo, parametros);
		}
		return result;
	}

	/**
	 * Retrieves method for the informed object
	 * @param c
	 * @param method
	 * @param parametros
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Method getMethod(Class c, String method,	Class... parametros) {
		Method result = null;
		try {
			result = c.getMethod(method, parametros);
		} catch (NoSuchMethodException e) {
			throw new GenericReflectionException("Unable to retrieve the method [" + method + "] in class [" + c + "] [" + e.getMessage () + "]", e);
		} catch (SecurityException e) {
			throw new GenericReflectionException("Unable to retrieve the method [" + method + "] in class [" + c + "] [" + e.getMessage () + "]", e);
		}
		return result;
	}

	/**
	 * Method that performs the conversion of a String in a class.
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Class getClass(String clazz) {
		Class result = null;
		try {
			result = Class.forName(clazz);
		} catch (ClassNotFoundException e) {
			throw new GenericReflectionException("Could not instantiate class [" + clazz + "]", e);
		}
		return result;
	}

	/**
	 * Method that invokes the method via the invoke reflection, thus performing a method on the object passed as parameter.
	 * 
	 * @param object
	 * @param methodName
	 * @param paramterClasses
	 * @param parameterObjects
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object invokeMethod(Object object, String methodName, Class[] paramterClasses, Object[] parameterObjects) {
		Object result = null;
		Method m = ReflectionUtil.getMethod(object, methodName,  paramterClasses);
		if (m != null && object != null) {
			try {
				result = m.invoke(object, parameterObjects);
			} catch (IllegalAccessException e) {
				throw new GenericReflectionException(
						"Unable to execute the method [" + methodName
								+ "] object [" + object + "]", e);
			} catch (IllegalArgumentException e) {
				throw new GenericReflectionException(
						"Unable to execute the method [" + methodName
								+ "] object [" + object + "]", e);
			} catch (InvocationTargetException e) {
				if (BusinessException.class.isInstance(e.getCause())) {
					throw (BusinessException) e.getCause();
				} else {
					throw new GenericReflectionException(
							"Unable to execute the method [" + methodName
									+ "] object [" + object + "]", e);
				}
			}
		}
		return result;
	}
	
	/**
	 * Retrieves the value of an object passed through the path recursively.
	 * @param object
	 * @param path
	 * @return
	 * @throws GenericReflectionException
	 */
	public static Object getObjectValue(Object object, String path) throws GenericReflectionException {
		
		String[] ca = path.split("\\.");
		for (String c : ca) {
			try {
				if (object == null){
					continue;
				}
				Field f = getDeclaredFieldForClass(object.getClass(), c);
				if (f == null){
					continue;
				}
				// the get method to retrieve the attribute
				final Method metodoGet = getGetMethodByFieldName(object.getClass(), c);
				// invokes
				object = metodoGet.invoke(object, (Object[]) null);							
			} catch (SecurityException e) {
				throw new GenericReflectionException(
						"Could not find the 'Field' ["
								+ path + "] on object [" + object + "]" +
						e);
			} catch (IllegalArgumentException e) {
				throw new GenericReflectionException(
						"Could not find the 'Field' ["
								+ path + "] on object [" + object + "]" +
						e);
			} catch (IllegalAccessException e) {
				throw new GenericReflectionException(
						"Could not find the 'Field' ["
								+ path + "] on object [" + object + "]" +
						e);
			} catch (NoSuchFieldException e) {
				throw new GenericReflectionException(
						"Could not find the 'Field' ["
								+ path + "] on object [" + object + "]" +
						e);
			} catch (InvocationTargetException e) {
				throw new GenericReflectionException(
						"Não foi possível executar o método GET para o 'Field' ["
								+ path + "] on object [" + object + "]" +
						e);
			}
		}
		return object;
	}
	
	/**
	 * Adjust the object to the given class.
	 * @param o
	 * @param c
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Object adjustClassToObject(Object o, Class c) {
		if (c != null && !c.isInstance(o)) {
			if (c.getName().startsWith("java.lang") || c.isPrimitive()) {
				try {
					if (!c.isPrimitive() && !Object.class.equals(c)) {
						Constructor x = c.getConstructor(String.class);
						if (o != null && o.toString().length() > 0) {
							o = x.newInstance(o.toString().trim());
						} else {
							o = null;
						}
					} else if (c.isPrimitive()) {
						if ("int".equals(c.getName())) {
							o = Integer.parseInt(o.toString().trim());
						} else if ("boolean".equals(c.getName())) {
							o = Boolean.parseBoolean(o.toString().trim());
						} else {
							throw new GenericReflectionException(
									"Not found:"
											+ c.getName());
						}
					}
				} catch (InstantiationException e) {
					throw new GenericReflectionException(
							"Not able to adjust value ["
									+ o + "] class [" + c.getName() + "]", e);
				} catch (IllegalAccessException e) {
					throw new GenericReflectionException(
							"Not able to adjust value ["
									+ o + "] class [" + c.getName() + "]", e);
				} catch (IllegalArgumentException e) {
					throw new GenericReflectionException(
							"Not able to adjust value ["
									+ o + "] class [" + c.getName() + "]", e);
				} catch (InvocationTargetException e) {
					throw new GenericReflectionException(
							"Not able to adjust value ["
									+ o + "] class [" + c.getName() + "]", e);
				} catch (NoSuchMethodException e) {
					throw new GenericReflectionException(
							"Not able to adjust value ["
									+ o + "] class [" + c.getName() + "]", e);
				} catch (SecurityException e) {
					throw new GenericReflectionException(
							"Not able to adjust value ["
									+ o + "] class [" + c.getName() + "]", e);
				}
			} else if (c.getName().startsWith("java.math")) {
				if (BigInteger.class.equals(c)) {
					if (o == null || (o != null && o.equals(""))){
						o = "0";
					}  
					
					o = new BigInteger(o.toString().trim());
				} else if (BigDecimal.class.equals(c)) {
					if(o != null && o.toString().trim().length()>0){
						o = new BigDecimal(o.toString().trim());
					}
				}
			} else if (c.isEnum()) {
				for (Object en : c.getEnumConstants()) {
					if (en.toString().trim().equals(o) || en.equals(o)) {
						o = en;
						break;
					}
				}
			}
		}
		return o;
	}
}
