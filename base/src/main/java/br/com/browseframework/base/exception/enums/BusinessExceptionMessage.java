package br.com.browseframework.base.exception.enums;

public enum BusinessExceptionMessage {
	AUTHENTICATION_ERROR("authentication.error"),
	GENERIC_ERROR("generic.error"),
	DATABASE_ERROR("atabase.error"),
	OPTIMISTICLOCK_ERROR("optimisticlock.error");
	
	private String key;
	
	private BusinessExceptionMessage(String key){
		setKey(key);
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
}
