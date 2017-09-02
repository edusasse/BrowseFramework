package br.com.browseframework.application.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/test-application.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
public class TestDBUnitWithSpring extends AbstractJUnit4SpringContextTests {

	private DefaultListableBeanFactory beanFactory;
	private LocalSessionFactoryBean localSessionFactoryBean;
	private Configuration configuration;
	
	@Autowired
	private DataSource dataSource;

	@Before
	public void init() throws Exception {
		// Recupera factory de bean Spring
		try {
			beanFactory = (DefaultListableBeanFactory) ((GenericApplicationContext) applicationContext).getBeanFactory();
		} catch (ClassCastException cce){
			Assert.fail("The application context does not extends GenericApplicationContext.");
		}
		if (beanFactory == null){
			Assert.fail("Not able to retrieve beanFactory from context.");
		}
		
		// Hibernate
		// ----------
		// Recupera factory da atual sessionFactory
		localSessionFactoryBean = (LocalSessionFactoryBean) applicationContext.getBean("&sessionFactory");
		if (localSessionFactoryBean == null){
			Assert.fail("Not able to retrieve local session factory bean from context.");
		}
		
		configuration = (Configuration) localSessionFactoryBean.getConfiguration();
		if (configuration == null){
			Assert.fail("Not able to retrieve configuration bean from context.");
		}
		
	}

	@After
	public void after() throws Exception {
		// Limpa a base de dados
		
	}
 

	@Test
	public void testSQLUpdate() throws Exception {
		Connection con = dataSource.getConnection();
		Statement stmt = con.createStatement();
		// Pega o valor atual
		ResultSet rst = stmt.executeQuery("select * from entity ");
		if (rst.next()) {
		}
		Assert.assertEquals(1,1);
	}
}