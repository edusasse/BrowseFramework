package br.com.browseframework.base.data;

import br.com.browseframework.base.data.type.CriterionType;

/**
 * Abstract class to define a Criterion for the CRUD element.
 * @author Eduardo
 *
 */
public class Criterion implements CriterionType {
	// When the filter should be ignored for an user specific reason or in the criteria build process
	private boolean ignore;
	// Property name represents the absolute table column name or a path to an attribute
	private String propertyName;
	
	// GETTES & SETTERS
	
	/* (non-Javadoc)
	 * @see br.com.browseframework.data.CriterionType#isIgnore()
	 */
	@Override
	public boolean isIgnore() {
		return ignore;
	}
	
	/* (non-Javadoc)
	 * @see br.com.browseframework.data.CriterionType#setIgnore(boolean)
	 */
	@Override
	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}
	
	/* (non-Javadoc)
	 * @see br.com.browseframework.data.CriterionType#getPropertyName()
	 */
	@Override
	public String getPropertyName() {
		return propertyName;
	}
	
	/* (non-Javadoc)
	 * @see br.com.browseframework.data.CriterionType#setPropertyName(java.lang.String)
	 */
	@Override
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
}
