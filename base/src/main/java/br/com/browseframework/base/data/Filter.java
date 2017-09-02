package br.com.browseframework.base.data;

import org.apache.log4j.Logger;

import br.com.browseframework.base.data.enums.Join;
import br.com.browseframework.base.data.enums.Operator;
import br.com.browseframework.base.data.enums.Restriction;
import br.com.browseframework.base.data.type.FilterType;

/**
 * Filter is used to transport the JPA criterion.
 * @author Eduardo
 *
 */
public class Filter extends Criterion implements FilterType {
	
    private static Logger log = Logger.getLogger(Filter.class);
	
	// Filter restriction
	private Restriction restriction;
	
	// Property value
	private Object propertyValue;
	
	// Property value relative class
	@SuppressWarnings("rawtypes")
	private Class propertyClassType;
	
	// Operator is default setted as "AND" on contructor
	private Operator operator;
	
	// Join kind
	private Join joinType;
	
	// Ignore case
	private boolean ignoreCase;
	
	// Conjuctions group attributes
	private String conjunctionGroup;
	private String conjunctionParentGroup;
	
	// SUB-QUERY
	// ---------
	// Class type to create the sub-query
	@SuppressWarnings("rawtypes")
	private Class subQueryClassType;
	// The projected column from de sub-query
	private String subQueryProjectedColumn;
	// The column that will 'in' the sub-query (ex.: "select ... where <subQueryInverseColumn> in (select subQueryProjectedColumn <..sub-query>)")
	private String subQueryInverseColumn;
	
	/**
	 * Creates the filter defining as default:
	 * - Restriction as Restriction.LIKE;
	 * - Operator as Operator.AND;
	 * - Ignore as Boolean.FALSE;
	 */
	public Filter() {
		setOperator(Operator.AND);
		setIgnore(Boolean.FALSE);
		setIgnoreCase(Boolean.FALSE);
		setJoinType(Join.INNER);
		setRestriction(Restriction.EQUALS);
	}

	/**
	 * Creates the filter defining as default:
	 * - Restriction as Restriction.LIKE;
	 * - Operator as Operator.AND;
	 * - Ignore as Boolean.FALSE;
	 */
	public Filter(String propertyName, Object propertyValue, Restriction restriction) {
		this();
		setPropertyName(propertyName);
		setPropertyValue(propertyValue);
		setRestriction(restriction);
	}
	
	/**
	 * Creates the filter defining as default:
	 * - Operator as Operator.AND;
	 * - Ignore as Boolean.FALSE;
	 */
	public Filter(String propertyName, Object propertyValue) {
		this();
		setPropertyName(propertyName);
		setPropertyValue(propertyValue);
	}
	
	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#validate()
	 */
	@Override
	public void validate() {
		log.debug("Validating filter named [" + getPropertyName() + "] value [" + getPropertyValue() + "]");
		// When restriction isn't null and it's LIKE, property value is defined and its a Number 
		if (getRestriction() != null && (Restriction.LIKE.equals(getRestriction()) ||  Restriction.NOT_LIKE.equals(restriction)) && getPropertyValue() != null && Number.class.isInstance(getPropertyValue())){
			if (Restriction.LIKE.equals(getRestriction())){ 
				this.restriction = Restriction.EQUALS;
			} else {
				this.restriction = Restriction.NOT_EQUALS;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#getPropertyClassType()
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Class getPropertyClassType() {
		Class result = this.propertyClassType;

		// When no informed
		if (this.propertyClassType == null){
			// and the property value is not null
			if (getPropertyValue() != null){
				// the result receives the property values class type
				result = getPropertyValue().getClass();
				if (result == null){
					throw new IllegalAccessError(""); // FIXME with GenericBusiness
				}
				setPropertyClassType(result); 
			}
		}
		return result;
	}
	
	// GETTERS && SETTERS 
	
	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#getRestriction()
	 */
	@Override
	public Restriction getRestriction() {
		return this.restriction;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#setRestriction(br.com.browseframework.data.enums.Restriction)
	 */
	@Override
	public void setRestriction(Restriction restriction) {
		this.restriction = restriction;
	}
	
	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#getPropertyValue()
	 */
	@Override
	public Object getPropertyValue() {
		return propertyValue;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#setPropertyValue(java.lang.Object)
	 */
	@Override
	public void setPropertyValue(Object propertyValue) {
		this.propertyValue = propertyValue;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#setPropertyClassType(java.lang.Class)
	 */
	@Override
	public void setPropertyClassType(@SuppressWarnings("rawtypes") Class propertyClassType) {
		this.propertyClassType = propertyClassType;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#getOperator()
	 */
	@Override
	public Operator getOperator() {
		return operator;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#setOperator(br.com.browseframework.data.enums.Operator)
	 */
	@Override
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#getConjunctionGroup()
	 */
	@Override
	public String getConjunctionGroup() {
		return conjunctionGroup;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#setConjunctionGroup(java.lang.String)
	 */
	@Override
	public void setConjunctionGroup(String conjunctionGroup) {
		this.conjunctionGroup = conjunctionGroup;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#getConjunctionParentGroup()
	 */
	@Override
	public String getConjunctionParentGroup() {
		return conjunctionParentGroup;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#setConjunctionParentGroup(java.lang.String)
	 */
	@Override
	public void setConjunctionParentGroup(String conjunctionParentGroup) {
		this.conjunctionParentGroup = conjunctionParentGroup;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#getSubQueryClassType()
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Class getSubQueryClassType() {
		return subQueryClassType;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#setSubQueryClassType(java.lang.Class)
	 */
	@Override
	public void setSubQueryClassType(@SuppressWarnings("rawtypes") Class subQueryClassType) {
		this.subQueryClassType = subQueryClassType;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#getSubQueryProjectedColumn()
	 */
	@Override
	public String getSubQueryProjectedColumn() {
		return subQueryProjectedColumn;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#setSubQueryProjectedColumn(java.lang.String)
	 */
	@Override
	public void setSubQueryProjectedColumn(String subQueryProjectedColumn) {
		this.subQueryProjectedColumn = subQueryProjectedColumn;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#getSubQueryInverseColumn()
	 */
	@Override
	public String getSubQueryInverseColumn() {
		return subQueryInverseColumn;
	}

	/* (non-Javadoc)
	 * @see br.com.browseframework.data.FilterType#setSubQueryInverseColumn(java.lang.String)
	 */
	@Override
	public void setSubQueryInverseColumn(String subQueryInverseColumn) {
		this.subQueryInverseColumn = subQueryInverseColumn;
	}

	public Join getJoinType() {
		return joinType;
	}

	public void setJoinType(Join joinType) {
		this.joinType = joinType;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}


}
