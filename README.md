#Browse Framework

    This is probably not really a framework, but a least it helps to define how things should be handled and defined in some standardized way.

## Stack

* Java 6
* Spring
* Hibernate
* JSF(Primefaces)
* Maven
* AspectJ

#Content

##Base

    Here we can find some base classes for CURD-Operation, DTOs, Exceptions, Pagination, Filter, etc.

##Hibernate

    We have some cool CrudDAOHibernate implementation that defines how things should work inside a DAO. In Addition it uses the special Filter implementation that makes things much easier.  
    We have the AopLazyLoadingInterceptor that intercepts "GET" methods and attaches a session to the object, thus loading the desired content as expected.
    
##JSF-Primefaces

    Some cool converters like the GenericIdFacadeConverter, that dynamically loads the bean from the class name retrieving/delivering the object/id. Some useful classes for Listerners, a FacesUtil, etc.
    
##Report
    
    A nice JasperReport abstraction.
    
##Util

    Some useful things to work with dates, hashes, images, jar, e-mail, pdf, reflection, spring, tree, validator, etc.