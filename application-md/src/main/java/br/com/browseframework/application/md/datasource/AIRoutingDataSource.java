package br.com.browseframework.application.md.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import br.com.browseframework.application.md.admin.domain.Client;

public class AIRoutingDataSource extends AbstractRoutingDataSource {
	
	@Autowired
	private ClientResolver clientResolver;
	
	@Autowired
	private TargetDataSourcesLoader targetDataSourcesLoader;
	
	public void init(){
		targetDataSourcesLoader.doLoadTargetDataSourcesDefault();
		targetDataSourcesLoader.doLoadTargetDataSources();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		Object result = "DEFAULT";
		final Client client = clientResolver.getClient();
		if (client != null){
			result = clientResolver.getClient().getId();
		}
		return result;
	}

//	// GETTERS && SETTERS
//	
//	public static void main(String[] args) {
//		try {
//			// XML Context loader
//			GenericXmlApplicationContext context = new GenericXmlApplicationContext();
//			// set to not validade xsd schemas
//			context.setValidating(false);
//
//			context.load("config/main-application.xml");
//			context.refresh();
//
//		} catch (Exception t) {
//			t.printStackTrace();
//			System.exit(-1);
//		}
//	}
}
