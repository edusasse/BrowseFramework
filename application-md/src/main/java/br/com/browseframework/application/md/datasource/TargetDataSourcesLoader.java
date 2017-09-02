package br.com.browseframework.application.md.datasource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.com.browseframework.application.md.admin.domain.Client;
import br.com.browseframework.application.md.admin.facade.ClientFacade;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TargetDataSourcesLoader {

	@Autowired
	@Qualifier("aiDataSourceUser")
	private AIRoutingDataSource dataSourceUser;
	
	@Autowired
	private ClientFacade clientFacade;
	
	public void doLoadTargetDataSourcesDefault() {
		try {
			// Map containing the connections
			final Map<Object, Object> mapDS = new HashMap<Object, Object>();
			
			// Default
			final ComboPooledDataSource ds1 = new ComboPooledDataSource();
			ds1.setDriverClass("org.hsqldb.jdbc.JDBCDriver");
			String dbPath = System.getProperty("user.home") + "\\" + "ALLWAYS_EMPTY_DB";
			ds1.setJdbcUrl("jdbc:hsqldb:file:" + dbPath + ";user=SA;create=true");
			mapDS.put("DEFAULT", ds1);
		
			dataSourceUser.setTargetDataSources(mapDS);
			dataSourceUser.afterPropertiesSet();
		} catch (Exception t) {
			t.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void doLoadTargetDataSources() {
		try {
			// Map containing the connections
			final Map<Object, Object> mapDS = new HashMap<Object, Object>();
			
			// Default
			final ComboPooledDataSource ds1 = new ComboPooledDataSource();
			mapDS.put("DEFAULT", ds1);
			
			// Create the ComboPooledDataSource for the 
			final List<Client> listOfClients = clientFacade.findAll(null).getData();
			if (listOfClients != null){
				for (Client client : listOfClients){
					final ComboPooledDataSource ds = new ComboPooledDataSource();
					mapDS.put(client.getId(), ds);
					ds.setDriverClass("com.mysql.jdbc.Driver");
					ds.setJdbcUrl("jdbc:mysql://localhost:3306/" + client.getDatabase());
					ds.setUser(client.getLogin());
					ds.setPassword(client.getPassword());
				}
			}
			
			dataSourceUser.setTargetDataSources(mapDS);
			dataSourceUser.afterPropertiesSet();
		} catch (Exception t) {
			t.printStackTrace();
			System.exit(-1);
		}
	}

}