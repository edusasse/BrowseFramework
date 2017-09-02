package br.com.browseframework.base.data.type;

public interface CriterionType {

	public abstract boolean isIgnore();

	public abstract void setIgnore(boolean ignore);

	public abstract String getPropertyName();

	public abstract void setPropertyName(String propertyName);

}