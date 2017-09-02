package br.com.browseframework.application.engine.enums;

@SuppressWarnings("rawtypes")
public enum EntityColumnPropertyGroupEnum {
	Embeddable(javax.persistence.Embeddable.class),
	GeneratedValue(javax.persistence.GeneratedValue.class),
	GenericGenerator(org.hibernate.annotations.GenericGenerator.class),
	Column(javax.persistence.Column.class);
	
	private Class clazz;
	
	EntityColumnPropertyGroupEnum(Class clazz){
		this.clazz = clazz;
	}
	
	public Class getClazz() {
		return clazz;
	}
}
