package br.com.browseframework.report.jasper.parameter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

public class ReportParameterHelper implements Serializable, Cloneable {

	private static final long serialVersionUID = -1784748697984440125L;

	private static transient Logger log = Logger.getLogger(ReportParameterHelper.class);
	
	private String name;
	private String description;
	@SuppressWarnings("rawtypes")
	private Class clazz;
	private Object defaultValue;
	private Object value;
	private boolean prompt;

	/**
	 * Returns the translated class name.
	 * @return
	 */
	public String getTranslatedClassName(){
		String retorno = "Undefined";
		try {
			if (clazz.getCanonicalName().equals(String.class.getCanonicalName())){
				retorno = "Text";
			} else if (clazz.getCanonicalName().equals(Date.class.getCanonicalName())){
				retorno = "Date";
			} else if (clazz.getCanonicalName().equals(BigDecimal.class.getCanonicalName())){
				retorno = "Decimal";
			} else if (clazz.getCanonicalName().equals(Integer.class.getCanonicalName()) || clazz.getCanonicalName().equals(Long.class.getCanonicalName()) || clazz.getCanonicalName().equals(Byte.class.getCanonicalName())){
				retorno = "Integer";
			} else {
				retorno = clazz.getName();
			}
		} catch (Exception npe){
			log.error("Not able to get translated class name for paramter [" + getName() + "] Class [" + getClazz() + "]");
		}
		return retorno;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	// GETTERS && SETTERS
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@SuppressWarnings("rawtypes")
	public Class getClazz() {
		return clazz;
	}

	public void setClazz(@SuppressWarnings("rawtypes") Class clazz) {
		this.clazz = clazz;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Object getValue() {
		if (this.value == null){
			return getDefaultValue();
		}
		return this.value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isPrompt() {
		return prompt;
	}

	public void setPrompt(boolean prompt) {
		this.prompt = prompt;
	}
}
