package br.com.browseframework.base.data.type;

import br.com.browseframework.base.data.enums.Join;
import br.com.browseframework.base.data.enums.Operator;
import br.com.browseframework.base.data.enums.Restriction;

public interface FilterType extends CriterionType {

	/**
	 * Validates the Filter.
	 * - When restriction isn't null and it's LIKE/NOT_LIKE, property value is defined and its a Number the restriction is seted respectively EQUALS/NOT_EQUALS 
	 * @param restriction
	 */
	public abstract void validate();

	/**
	 * Returns the property value type when not informed.x
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public abstract Class getPropertyClassType();

	public abstract Restriction getRestriction();

	public abstract void setRestriction(Restriction restriction);

	public abstract Object getPropertyValue();

	public abstract void setPropertyValue(Object propertyValue);

	public abstract void setPropertyClassType(
			@SuppressWarnings("rawtypes") Class propertyClassType);

	public abstract Operator getOperator();

	public abstract void setOperator(Operator operator);

	public abstract String getConjunctionGroup();

	public abstract void setConjunctionGroup(String conjunctionGroup);

	public abstract String getConjunctionParentGroup();

	public abstract void setConjunctionParentGroup(String conjunctionParentGroup);

	@SuppressWarnings("rawtypes")
	public abstract Class getSubQueryClassType();

	public abstract void setSubQueryClassType(
			@SuppressWarnings("rawtypes") Class subQueryClassType);

	public abstract String getSubQueryProjectedColumn();

	public abstract void setSubQueryProjectedColumn(
			String subQueryProjectedColumn);

	public abstract String getSubQueryInverseColumn();

	public abstract void setSubQueryInverseColumn(String subQueryInverseColumn);
	
	public Join getJoinType();
	
	public boolean isIgnoreCase();

}