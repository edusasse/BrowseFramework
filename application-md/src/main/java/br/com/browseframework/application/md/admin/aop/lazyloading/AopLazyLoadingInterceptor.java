package br.com.browseframework.application.md.admin.aop.lazyloading;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.com.browseframework.hibernate.aop.lazyloading.AbstractAopLazyLoadingInterceptor;

/**
 * Interceptor para Admin que intercepta métodos get no package [br.com.browseframeworksample.domain]
 * 
 * @author Eduardo
 *
 */
@Aspect
public class AopLazyLoadingInterceptor extends AbstractAopLazyLoadingInterceptor {

	@Autowired
	@Qualifier("sessionFactoryAdmin")
	protected SessionFactory sessionFactory;
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.hibernate.aop.lazyloading.AbstractAopLazyLoadingInterceptor#lazyLoadingInterceptor(org.aspectj.lang.ProceedingJoinPoint)
	 */
	@Override
	@Around("execution(public * br.com.browseframework.application.md.admin.domain..get*(..))")
	public Object lazyLoadingInterceptor(ProceedingJoinPoint pjp) throws Throwable {
		return super.lazyLoadingInterceptor(pjp);
	 }

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.hibernate.aop.lazyloading.AbstractAopLazyLoadingInterceptor#getSessionFactory()
	 */
	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.hibernate.aop.lazyloading.AbstractAopLazyLoadingInterceptor#setSessionFactory(org.hibernate.SessionFactory)
	 */
	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
