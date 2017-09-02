package br.com.browseframework.util.spring;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

/**
 * Spring util methods.
 * 
 * @author Eduardo
 *
 */
public class SpringUtil {

	/**
	 * Returns target object.
	 * @param cf
	 * @return
	 * @throws Exception
	 */
	public static Object unProxy(Object cf) throws Exception {
		Object result = null;
		
		if (AopUtils.isAopProxy(cf) && cf instanceof Advised) {
			result = ((Advised) cf).getTargetSource().getTarget();
		}

		return result;
	}
}
