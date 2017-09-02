package br.com.browseframework.hibernate.aop.lazyloading;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.hibernate.SessionFactory;

public abstract class AbstractAopLazyLoadingInterceptor {
	private static Logger log = Logger.getLogger(AbstractAopLazyLoadingInterceptor.class);
	 
    public AbstractAopLazyLoadingInterceptor() {
    	log.info("Lazy Loading Interceptor started!");
	}
    
    /**
     * The process is done by updating the persistent instance through a transaction
     * By the method Session.update () which takes an object persistent and relocates a transaction
     * Previously created the same. Após have persistent object in a transactional environment is used
     * The Hibernate.initialize () to execute the load or Hibernate Proxy Collection.
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	public Object lazyLoadingInterceptor(ProceedingJoinPoint pjp) throws Throwable {
		// log.debug("Intercepting method [" + pjp.getSignature().getName() + "] on [" + pjp.getTarget().getClass().getCanonicalName() + "]");
        Object retorno = null;
        // If for some reason the joinpoint void comes not caused exception
        if (pjp != null){
        	try {
	        	retorno = pjp.proceed();
	        } catch (Exception e){
	        	log.error(pjp.getSignature().getName() + " Erro [" + e.getMessage() + "]");
	        	throw e;
	        }
	        // Verifies if the object is annotated with @NonLazyLoading, if so it gets ignored
	        if (!pjp.getSignature().getName().equals("getId") // never lazy
	        		&& getSessionFactory() != null // to avoid null exception
	        		&& pjp.getTarget() != null // needs a target
	        		&& !pjp.getTarget().getClass().isAnnotationPresent(NonLazyLoading.class) // class lazy restriction
	        	) {
	        	LazyLoadingHelper.process(getSessionFactory().getCurrentSession(), pjp.getTarget(), retorno, pjp.getSignature().getName()); 
	        }
        }
        return retorno;
    }
    
	public abstract SessionFactory getSessionFactory();

	public abstract void setSessionFactory(SessionFactory sessionFactory);
 
}