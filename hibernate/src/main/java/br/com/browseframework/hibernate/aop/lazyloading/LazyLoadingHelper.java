package br.com.browseframework.hibernate.aop.lazyloading;

import java.util.HashSet;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.collection.internal.AbstractPersistentCollection;
import org.hibernate.proxy.HibernateProxy;

import br.com.browseframework.base.data.dto.BaseDTO;

public class LazyLoadingHelper {
	private static Logger log = Logger.getLogger(LazyLoadingHelper.class);
	
    @SuppressWarnings("rawtypes")
	private static HashSet classes = new HashSet();

    /**
     * Method responsible for processing
     * @param session Session with bank dadoes
     * @param target Entity
     * @param result Proxy to be initialized
     * @param method method that invokes the proxy - used only for log -
     */
    public static void process(Session session, Object target, Object retorno, String method){
	    // ABSTRACT PERSISTENT COLLECTION (List, Map, Set)
	    if (AbstractPersistentCollection.class.isInstance(retorno)) {
	    	// log.debug("Intercepting Collection - method [" + pjp.getSignature().getName() + "] on [" + pjp.getTarget().getClass().getCanonicalName() + "]");
	        final AbstractPersistentCollection ps = (AbstractPersistentCollection) retorno;
	        if (!ps.wasInitialized() && (ps.getSession() == null || (ps.getSession() != null && ps.getSession().isClosed()))){ 
	        		reattachSession(session, target, ps, method);
	        } else {
				// log.debug("Intercepting Collection WITH SESSION - method [" + pjp.getSignature().getName() + "] on [" + pjp.getTarget().getClass().getCanonicalName() + "]");
			}
	    // HIBERNATE PROXY (BaseDTO)
	    } else if (HibernateProxy.class.isInstance(retorno) && BaseDTO.class.isInstance(retorno)){
	    	// log.debug("Intercepting BaseDTO - method [" + pjp.getSignature().getName() + "] on [" + pjp.getTarget().getClass().getCanonicalName() + "]");
	        if (retorno instanceof HibernateProxy && !Hibernate.isInitialized(retorno) && ((HibernateProxy) retorno).getHibernateLazyInitializer().getSession() == null) {
	        	reattachSession( session, target, retorno, method);
	        } else {
				// log.debug("Intercepting BaseDTO WITH SESSION - method [" + pjp.getSignature().getName() + "] on [" + pjp.getTarget().getClass().getCanonicalName() + "]");
			}
	    }
    }
    
    public static void reattachSession(Session session, Object target, Object obj){
    	reattachSession(session, target, obj, null);
    }
    
    /**
     * Process responsible for making the object transactional and charge information.
     * @param session session with bank dadoes
     * @param target Entity
     * @param retorno Proxy to be initialized
     * @param method method that relies on the proxy - used only for log - 
     */
    @SuppressWarnings("unchecked")
	public static void reattachSession(Session session, Object target, Object obj, String method){
    	// log.debug("Intercepting " + (AbstractPersistentCollection.class.isInstance(obj) ? "Collection" : "BaseDTO") + " WITHOUT SESSION - method [" + pjp.getSignature().getName() + "] on [" + pjp.getTarget().getClass().getCanonicalName() + "]");
        // Obtains transaction
		final Transaction tx = session.getTransaction();
		if (!tx.isActive()){
			// log.debug("Intercepting " + (AbstractPersistentCollection.class.isInstance(obj) ? "Collection" : "BaseDTO") + " - new transaction - method [" + pjp.getSignature().getName() + "] on [" + pjp.getTarget().getClass().getCanonicalName() + "]");
			tx.begin(); // .. inicia uma nova transação
		} else {
			// log.debug("Intercepting " + (AbstractPersistentCollection.class.isInstance(obj) ? "Collection" : "BaseDTO") + " - existing transaction - method [" + pjp.getSignature().getName() + "] on [" + pjp.getTarget().getClass().getCanonicalName() + "]");
		}
		if (AbstractPersistentCollection.class.isInstance(obj)){
			getClasses().add( ((AbstractPersistentCollection) obj).getKey());
		}
        try {
        	// Update the persistent instance with the identifier of the given detached instance.
        	// If there is a persistent instance with the same identifier, an exception is thrown.
        	// This operation is performed to cascade associated instances if the association is mapped with cascade = "save-update". session.update(target);
        	session.update(target);
        	// Force initialization of a proxy or a Collection
        	// Note: This only guarantees the initialization of a Collection or a proxy, but it is not guaranteed that the internal elements is also initializedHibernate.initialize(obj);
            Hibernate.initialize(obj);
        } catch (Exception ex) {
        	log.error ("Failed to reallocate transaction - method [" + method + "] to [" + target.getClass().getCanonicalName() + "] Error [" + ex.getMessage () + "]");
        } finally {
        	// guard classes to avoid infinite loop in the query
    		if (AbstractPersistentCollection.class.isInstance(obj)){
    			getClasses().remove(((AbstractPersistentCollection) obj).getKey());
    		}
    		// Finalizing the transaction started
            if (tx != null){
    			try {
    				tx.rollback();
    			} catch (Exception e){
    				log.error("Error finalizing transaction \"rollback\" - method [" + method + "] on [" + target.getClass().getCanonicalName() + "]");
    				log.error(e.getStackTrace());
    			}
    		}
        }
    }

    // GETTERS && SETTERS
    
	@SuppressWarnings("rawtypes")
	public static HashSet getClasses() {
		return classes;
	}

	@SuppressWarnings("rawtypes")
	public static void setClasses(HashSet classes) {
		LazyLoadingHelper.classes = classes;
	}
}
