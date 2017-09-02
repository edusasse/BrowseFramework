package br.com.browseframework.application.engine.enums;

public enum EntityColumnPropertyNameEnum {
	GeneratedValue_generator("generator"),
	GenericGenerator_name("name"),
	GenericGenerator_strategy("strategy"),
	Column_name("name");
	
	private String name;
	
	EntityColumnPropertyNameEnum(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
