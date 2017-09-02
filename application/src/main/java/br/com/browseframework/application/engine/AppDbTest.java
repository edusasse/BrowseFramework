package br.com.browseframework.application.engine;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.browseframework.application.db.domain.Entity;
import br.com.browseframework.application.db.domain.EntityColumn;
import br.com.browseframework.application.db.domain.Type;
import br.com.browseframework.application.db.facade.EntityColumnFacade;
import br.com.browseframework.application.db.facade.EntityFacade;
import br.com.browseframework.application.db.facade.TypeFacade;

public class AppDbTest {
	private static AppDbTest app;
	private static ClassPathXmlApplicationContext appContext;

	public static ClassPathXmlApplicationContext getAppContext() {
		return appContext;
	}

	public static void setAppContext(ClassPathXmlApplicationContext appContext) {
		AppDbTest.appContext = appContext;
	}

	private AppDbTest() {
	}

	public static AppDbTest getInstance() {
		if (app == null) {
			app = new AppDbTest();
		}
		return app;
	}

	public static void main(String[] args) throws CannotCompileException, NotFoundException, IOException, RuntimeException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException {
		// Spring
		// ---------
		// Carrega o contexto Spring
		appContext = new ClassPathXmlApplicationContext("config/application.xml");
		EntityFacade entityFacade = (EntityFacade) appContext.getBean("entityFacade");
		EntityColumnFacade entityColumnFacade = (EntityColumnFacade) appContext.getBean("entityColumnFacade");
		TypeFacade typeFacade = (TypeFacade) appContext.getBean("typeFacade");
		
		Type typeTEXT = typeFacade.findById(1l);
		
		Entity e = new Entity();
		e.setName("aaa");
		
		EntityColumn ec = new EntityColumn();
		ec.setEntity(e);
		ec.setName("DES_TESTE");
		ec.setType(typeTEXT);
		
		entityFacade.save(e);
		entityColumnFacade.save(ec);
		
		System.out.println("<<FIM>>");
	}
}
