package br.com.browseframework.application.engine;
 
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
 
public class CustomerRoutingDataSource extends AbstractRoutingDataSource {
 
   @Override
   protected Object determineCurrentLookupKey() {
      return "DEFAULT";
   }   
   
}
