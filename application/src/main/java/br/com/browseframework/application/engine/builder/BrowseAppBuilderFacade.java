package br.com.browseframework.application.engine.builder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframework.application.db.domain.Entity;

@SuppressWarnings("rawtypes")
public interface BrowseAppBuilderFacade {

	public abstract Class buildHibernateBean(String domainPackgeName,
			Entity entity) throws CannotCompileException, NotFoundException,
			IOException, RuntimeException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchFieldException, ClassNotFoundException;

	public abstract Class buildHibernateBean(String[] interfaces,
			Class dtoClass, String domainPackgeName, Entity entity)
			throws CannotCompileException, NotFoundException, IOException,
			RuntimeException, InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchFieldException,
			ClassNotFoundException;

	/**
	 * Creates the columns.
	 * @param entityCtClass
	 * @param entity
	 * @throws NotFoundException
	 * @throws IOException
	 * @throws CannotCompileException
	 * @throws RuntimeException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchFieldException
	 * @throws ClassNotFoundException
	 */
	public abstract void doEntityColumn(CtClass entityCtClass, Entity entity)
			throws NotFoundException, IOException, CannotCompileException,
			RuntimeException, InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchFieldException,
			ClassNotFoundException;

}