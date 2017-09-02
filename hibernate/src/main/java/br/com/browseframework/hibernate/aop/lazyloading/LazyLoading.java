package br.com.browseframework.hibernate.aop.lazyloading;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Entity annotaded with this will be ignored by the LazyLoading interceptor.
 * @author Eduardo
 *
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE})
public @interface LazyLoading {

}
