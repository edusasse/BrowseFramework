package br.com.browseframework.application.engine.enums;

@SuppressWarnings("rawtypes")
public enum TypeReferenceEnum {
	// *********************************
	// * DO NOT CHANGE THE ORDER BELOW *
	// *********************************
	Long(Long.class),
	String(String.class),
	Integer(Integer.class),
	Double(Double.class),
	Float(Float.class);
	
	private Class clazz;
	
	TypeReferenceEnum(Class clazz){
		this.clazz = clazz;
	}
	
	public Class getClazz() {
		return clazz;
	}
}