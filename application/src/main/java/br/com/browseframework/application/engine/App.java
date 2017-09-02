package br.com.browseframework.application.engine;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import br.com.browseframework.application.engine.builder.impl.DAOClassBuilder;
import br.com.browseframework.application.engine.builder.impl.DAOInterfaceBuilder;
import br.com.browseframework.application.engine.builder.impl.FacadeClassBuilder;
import br.com.browseframework.application.engine.builder.impl.HibernateBeanBuilder;
import br.com.browseframework.application.engine.builder.util.JavassistUtils;
import br.com.browseframework.base.crud.facade.CrudFacade;
import br.com.browseframework.base.data.dto.BaseDTO;

public class App {
	private static App app;
	private static ClassPathXmlApplicationContext appContext;

	public static ClassPathXmlApplicationContext getAppContext() {
		return appContext;
	}

	public static void setAppContext(ClassPathXmlApplicationContext appContext) {
		App.appContext = appContext;
	}

	private App() {
	}

	public static App getInstance() {
		if (app == null) {
			app = new App();
		}
		return app;
	}

	 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws CannotCompileException, NotFoundException, IOException, RuntimeException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException {
		String className  ="aa_x";
		
		final StringBuilder aux = new StringBuilder(className.length());
		final String[] words = className.split("\\_");
		for(int i=0,l=words.length;i<l;++i) {
			if (words[i].trim().length() != 0){
				aux.append(Character.toUpperCase(words[i].charAt(0))).append(words[i].substring(1));
			}
		}
		String result = aux.toString();
		if (result.length() > 1){
			result = aux.substring(0, 1).toLowerCase() + result.substring(1);			
		} else {
			result = result.toLowerCase();
		}
		System.out.println(result);

		/*
		System.exit(0);
		// Spring
		// ----------
		// Carrega o contexto Spring
		appContext = new ClassPathXmlApplicationContext("config/application.xml");
		// Recupera factory de bean Spring 
		final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) appContext.getBeanFactory();
		
		// Hibernate
		// ----------
		// Recupera factory da atual sessionFactory
		final LocalSessionFactoryBean localSessionFactoryBean = (LocalSessionFactoryBean) appContext.getBean("&sessionFactory");
		Configuration configuration = (Configuration) localSessionFactoryBean.getConfiguration();
		
		// ----------------------------------------------------
		// Cria a versão 1 da classe para a tabela MY_TABLE
		// ----------------------------------------------------
		final CtClass tableEntity = HibernateBeanBuilder.buildEntity(null, 
				"br.com.browseframework.base.data.dto.BaseDTO", 
				"com.edu.model.domain" , 
				"Table_$_1", "",
				"my_table");
		HibernateBeanBuilder.addIdField(Long.class.getCanonicalName(), "id", "cod_table", tableEntity);
		HibernateBeanBuilder.addColumnField(String.class.getCanonicalName(), "nome", "nom_table", tableEntity);
		JavassistUtils.createGettersAndSetters(tableEntity);
		
		// Cria a classe propriamente dita
		final Class tableClass = tableEntity.toClass();
		// toString das anotacoes
		for (Annotation a : tableClass.getAnnotations()){
			System.out.println(a);
		}
		for (Method a : tableClass.getDeclaredMethods()){
			System.out.println(a);
		}
		
		// -------------------------------------------
		// Adiciona a classe ao contexto Spring
		// -------------------------------------------
		configuration = configuration.addAnnotatedClass(tableClass);
		final ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();          
		final SessionFactory  sessionFactory = configuration.buildSessionFactory(serviceRegistry);  

		// Remove a sessionFactory..
		beanFactory.removeBeanDefinition("sessionFactory");
		// insere a nova sessionFactory contendo a classe para a entidade criada
		beanFactory.registerSingleton("sessionFactory", sessionFactory);
		
		// ------------------------------------
		// Cria a tabela atraves do hibernate
		// ------------------------------------
		final SchemaUpdate update = new SchemaUpdate(configuration);
		update.execute(true, true); // (imprime o DDL, faz as alteracoes)

		// ----------------------------------------
		// Cria DAO e FACADE para a entidade criada
		// ----------------------------------------
		// Cria interface para o DAO
		final Class ci = DAOInterfaceBuilder.buildInterface("br.com.browseframework.base.crud.dao.CrudDAO", "com.edu.model.dao", "TableDAO");
		// Cria classe para o DAO
		final Class myDAO = DAOClassBuilder.buildClass(new String[]{ci.getCanonicalName()}, "br.com.browseframework.hibernate.dao.impl.CrudDAOHibernateImpl", "com.edu.model.dao", "TableDAOImpl", "TableDAO");
		// Cria classe para o FACADE
		final Class myFAC = FacadeClassBuilder.buildClass(null, "br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl", ci.getCanonicalName(), "com.edu.model.facade", "TableFacadeImpl", "TableFacade");
		// Registra DAO
		beanFactory.registerBeanDefinition("TableDAO", BeanDefinitionBuilder.rootBeanDefinition(myDAO).getBeanDefinition());
		// Registra FACADE
		beanFactory.registerBeanDefinition("TableFacade", BeanDefinitionBuilder.rootBeanDefinition(myFAC.getName()).getBeanDefinition());
		
		// ---------------------------------------------------------
		// * * * * * * * * * * *  TESTE [1] * * * * * * * * * * * *  
		// ---------------------------------------------------------
		// Recupera FACADE
		final Object fac = appContext.getBean("TableFacade");		
		// Cria uma nova instancia para a entidade
		final BaseDTO dto = (BaseDTO) tableClass.newInstance();
		// Seta o valor para o campo nome
		final Field f = dto.getClass().getDeclaredField("nome");
		f.setAccessible(true);
		f.set(dto, "eduardo");

		
		// ---------------------------------------------------------
		// * * * * * * * * * * *  TESTE [2] * * * * * * * * * * * *  
		// ---------------------------------------------------------
		// Cria nova entidade para a tabela MY_TABLE
		final CtClass tableEntity2 = HibernateBeanBuilder.buildEntity(null, 
				"br.com.browseframework.base.data.dto.BaseDTO", 
				"com.edu.model.domain" , 
				"Table_$_2", "",
				"my_table");
		HibernateBeanBuilder.addIdField(Long.class.getCanonicalName(), "id", "cod_table", tableEntity2);
		HibernateBeanBuilder.addColumnField(String.class.getCanonicalName(), "nome", "nom_table", tableEntity2);
		HibernateBeanBuilder.addColumnField(String.class.getCanonicalName(), "sobrenome", "nom_sobre", tableEntity2); // <<--- ADICIONADA NOVA COLUNA	
		JavassistUtils.createGettersAndSetters(tableEntity2);
		final Class tableClass2 = tableEntity2.toClass();
		
		// Remove a classe relacionada a tabela MY_TABLE da configuracão do Hibernate
		configuration.getImports().remove("my_table");
		// Adiciona a nova classe a configuração do Hibernate
		configuration = configuration.addAnnotatedClass(tableClass2);
		final ServiceRegistry serviceRegistry2 = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();          
		final SessionFactory  sessionFactory2 = configuration.buildSessionFactory(serviceRegistry2);  
	    
		// Remove a sessionFactory..
		beanFactory.destroySingleton("sessionFactory");
		// insere a nova sessionFactory contendo a classe para a entidade criada
		beanFactory.registerSingleton("sessionFactory", sessionFactory2);
		
		// Atualiza a tabela MY_TABLE adicionando a coluna sobrenome(nom_sobre)
		final SchemaUpdate update2 = new SchemaUpdate(configuration);
		update2.execute(true, true);
		
		// Cria objeto para a nova entidade
		final BaseDTO dto2 = (BaseDTO) tableClass2.newInstance();
		final Field f2 = dto2.getClass().getDeclaredField("nome");
		f2.setAccessible(true);
		f2.set(dto2, "mayara");
		
		final Field f3 = dto2.getClass().getDeclaredField("sobrenome");
		f3.setAccessible(true);
		f3.set(dto2, "pickler");

		// Recupera novamente o facade do contexto. 
		// ** ISTO É MUITO IMPORTANTE POIS DESSA FORMA O SPRING ATUALIZA O SESSION FACTORY NO DAO **
		final Object fac2 = appContext.getBean("TableFacade");	
		// Salva
		((CrudFacade) fac2).save(dto2);
		
		final Object fac3 = appContext.getBean("TableFacade");
		// Salva o objeto
		((CrudFacade) fac3).save(dto);*/
	}
}
