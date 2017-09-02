package br.com.browseframework.hibernate.criteria;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.CriteriaImpl.Subcriteria;
import org.hibernate.sql.JoinType;

import br.com.browseframework.base.data.enums.Join;
import br.com.browseframework.base.data.enums.Operator;
import br.com.browseframework.base.data.enums.OrderDirection;
import br.com.browseframework.base.data.enums.Restriction;
import br.com.browseframework.base.data.type.FilterType;
import br.com.browseframework.base.data.type.OrderType;
import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.base.exception.GenericReflectionException;
import br.com.browseframework.util.date.DateFormatterUtil;
import br.com.browseframework.util.reflection.ReflectionUtil;

/**
 * Criteria builder using Browse Framework data specifications.
 * 
 * @author Eduardo
 *
 */
public class CriteriaBuilder {

	// Singleton
	private static CriteriaBuilder criteriaBuilder = null;
	// User by reflection to change order by columns
	protected static final String ORDER_ENTRIES = "orderEntries";
	protected static final String SUBCRITERIALIST = "subcriteriaList";

	// Default OR group
	public final String DEFAULT_OR_GROUP = "@where";
	
	// indicates if the default 'or' group has already been used
	protected boolean defaultGroupUsed = false; 
	
	/**
	 * Empty private constructor.
	 */
	private CriteriaBuilder(){ }
	
	/**
	 * Returns a single CriteriaBuilder instance.
	 * @return
	 */
	public static CriteriaBuilder getInstance(){
		if (criteriaBuilder == null){
			criteriaBuilder = new CriteriaBuilder();
		}
		return criteriaBuilder;
	}
	
	/**
	 * Builds the ordering criterions.
	 * @param criteria
	 * @param orderings
	 */
	public void buildOrdering(Criteria criteria, OrderType[] orderings) {
		if (orderings == null || orderings.length == 0) {
			return;
		}
		
		// removes all ordering elements
		resetOrderingList(criteria);
		
		for (OrderType order : orderings) {
			// If a ignored paramter then the for continues
			if (order.isIgnore()){
				continue;
			}
			
			// Last element only
			final String[] propertyNameArray = order.getPropertyName().split("\\.");
			String properyName = propertyNameArray[propertyNameArray.length-1];
			if (propertyNameArray.length == 2){
				final String alias = propertyNameArray[0]+"_";
				final String associationPath = propertyNameArray[0];
				
				boolean alreadyExists = false;
				List<Subcriteria> list = getSubCriteriaList(criteria);
				if (list != null){
					for (Subcriteria s : list){
						if (s.getAlias().equals(alias)){
							alreadyExists = true;
							break;
						}
					}
				}
				
				if (!alreadyExists){
					criteria.createAlias(associationPath, alias);
				}
				
				properyName = alias + "." + properyName;
			}
			
			// Adds to the ordering entries
			if (OrderDirection.ASC.equals(order.getOrderDirection())){
				criteria.addOrder(Order.asc(properyName));
			} else {
				criteria.addOrder(Order.desc(properyName));
			}
		}		
	}
	
	/**
	 * Using reflection the ordering entries are all removed
	 * @param criteria
	 */
	@SuppressWarnings("rawtypes")
	protected void resetOrderingList(Criteria criteria){
		try {
			final Field orderEntries = ReflectionUtil.getDeclaredFieldForClass(CriteriaImpl.class, ORDER_ENTRIES);
			orderEntries.setAccessible(true);	
			orderEntries.set(criteria, new ArrayList());
		} catch (Exception e ){
			throw new GenericReflectionException("Unable to clear the list of ordering entries!");
		}
	}
	
	/**
	 * Using reflection the ordering entries are all removed
	 * @param criteria
	 */
	@SuppressWarnings({"unchecked" })
	protected List<Subcriteria> getSubCriteriaList(Criteria criteria){
		List<Subcriteria> result = null;
		try {
			final Field orderEntries = ReflectionUtil.getDeclaredFieldForClass(CriteriaImpl.class, SUBCRITERIALIST);
			orderEntries.setAccessible(true);	
			result =  (List<Subcriteria>) orderEntries.get(criteria);
		} catch (Exception e ){
			throw new GenericReflectionException("Unable to clear the list of ordering entries!");
		}
		
		return result;
	}
	
	/**
	 * Builds the filter criterions.
	 * @param criteria
	 * @param criterions
	 */
	public void buildFilter(Criteria criteria, FilterType[] filters) {
		if (filters == null || filters.length == 0) {
			return;
		}
		
		// Stores the criterias already during que criteria build
		final Map<String, Criteria> criterias = new HashMap<String, Criteria>();
		// "OR" Groups
		final Map<String, CriterionProxy> mapCriteriaGroup = new HashMap<String, CriterionProxy>();
		// Default criteria proxed
		final CriterionProxy criteriaDefault = new CriterionProxy(criteria);
		
		// Stores current Junction
		Junction currentJunction = null;
		// Parcial proxed criteria
		CriterionProxy parcial = null;
		
		for (FilterType filter: filters) {
			// If a ignored paramter then the for continues
			if (filter.isIgnore()){
				continue;
			}			
		 
			// Splits the property paths
			String[] pss = filter.getPropertyName().split("\\.");

			// Checks if the parent group has already been initialized
			if (filter.getConjunctionParentGroup() != null && !filter.getConjunctionParentGroup().trim().equals("")){
				if (mapCriteriaGroup.get(filter.getConjunctionParentGroup()) == null){
					throw new GenericBusinessException("No group named [" + filter.getConjunctionParentGroup() + "] was found. Check the parameters definition order.");
				}
			}
			
			// JUNCTION GROUPS
			// ---------------
			// If uses a reseved group name 
			if (filter.getConjunctionGroup() != null && filter.getConjunctionGroup().equals(DEFAULT_OR_GROUP)){
				throw new GenericBusinessException("Not allowed to define the reserved name [" + DEFAULT_OR_GROUP + "] to a Disjunction group.");
			}
			
			// In the case of the parent group is not informed
			if (filter.getConjunctionParentGroup() == null || (filter.getConjunctionParentGroup() != null && filter.getConjunctionParentGroup().trim().equals("") )){
				// .. its a OR operator AND the group is not informed
				if (Operator.OR.equals(filter.getOperator()) && (filter.getConjunctionGroup() == null || (filter.getConjunctionGroup() != null && filter.getConjunctionGroup().trim().equals(""))) ){ // .. seja OR e não tenha grupo
					if (!defaultGroupUsed){
						filter.setConjunctionGroup(DEFAULT_OR_GROUP);
						defaultGroupUsed = true;
					} else {
						filter.setConjunctionGroup(DEFAULT_OR_GROUP);
					}
				}
			}
			
			if ( (filter.getConjunctionParentGroup() != null && !filter.getConjunctionParentGroup().trim().equals("") ) || // Caso tenha grupo pai OU
			   ( (filter.getConjunctionGroup()    != null && !filter.getConjunctionGroup().trim().equals(""))) ) { // .. tenha grupo 
				
				// Create the specific kind for the junction
				if (Operator.AND.equals(filter.getOperator())) {
					currentJunction = Restrictions.conjunction();
				} else if (Operator.OR.equals(filter.getOperator())) {
					currentJunction = Restrictions.disjunction();
				}
				
				// creates a parcial junction
				parcial = new CriterionProxy(currentJunction);
				
				// if you have only parent group
				if (filter.getConjunctionGroup() == null || (filter.getConjunctionGroup() != null && filter.getConjunctionGroup().trim().equals(""))){
					// obtains the parent criteria
					CriterionProxy gpai = mapCriteriaGroup.get(filter.getConjunctionParentGroup());
					// adds the criteria to the parent
					parcial.setObject(gpai.getCriterion());
				} else if (mapCriteriaGroup.get(filter.getConjunctionGroup()) == null) { // In the case of the informed group does not exists yet
					// storing the group
					mapCriteriaGroup.put(filter.getConjunctionGroup(), parcial);
					// obtains the parent criteria
					CriterionProxy gpai = null;
					if (mapCriteriaGroup.get(filter.getConjunctionParentGroup()) == null){ // pode ser na criteria principal
						gpai = criteriaDefault;
					} else {
						gpai = mapCriteriaGroup.get(filter.getConjunctionParentGroup());
					}
					// adds the criteria to the parent
					gpai.add(parcial.getCriterion());
				}
			} else { // no group
				parcial = new CriterionProxy(criteria); // criteriaDefault;
			}
			 
			if (filter.getSubQueryClassType() != null) {
				createSubQuery(criterias, criteriaDefault, parcial, filter, "");
			} else {
				String alias = "";
				if (pss.length > 1) {
					// Creates the subcriterias for the complete path
					createCriteria(criterias, criteriaDefault, parcial, filter);
					alias = getAlias(pss,pss.length - 2)+".";
				}
				String propertyName = "";
				if(pss.length>1){
					propertyName = pss[pss.length - 1];
				} else {
					propertyName = filter.getPropertyName();
				}
				
				adjustParameterValue(filter);				
				assignPropertyCriteriaValue(parcial, filter, alias, propertyName);
			}
		} // end for paramters
 
	}
	
	/**
	 * Creates the criteria structure for the given properties.
	 * @param parcial
	 * @param filter
	 */
	protected void createCriteria(Map<String, Criteria> criterias, CriterionProxy criteriaDefault, CriterionProxy parcial, FilterType filter) {
		final String[] pss = filter.getPropertyName().split("\\.");
		
		// when comes a Junction
		if (parcial.isJunction()){
			// Creates partial to current junction
			CriterionProxy parcialBKP = new CriterionProxy(parcial.getObject());
			for (int i = 0; i < pss.length -1; i++) {
				// If the parcial criteria already contains a criteria, and that criteria is the parent criteria of the current element, so they are in the same hierarchy restrictions
				if (parcial.getCriteria() != null && (i > 0 && criterias.get(getAlias(pss,i-1)).equals(parcial.getCriteria()))) {
					parcial.setObject(parcial.createCriteria(pss[i], getAlias(pss,i), getTranslatedJoinType(filter.getJoinType()))); // retrieves the existing criteria and continues to structure
					criterias.put(getAlias(pss, i), parcial.getCriteria()); // stores the criteria
				} else if (criterias.containsKey(getAlias(pss,i))){ // If the criteria have already been created
					parcial.setObject(criterias.get(pss[i])); // retrieves and makes available to the parcial
				} else {
					// If parcial has a Criteria
					if (Criteria.class.isInstance(parcial.getObject())){
						parcial.setObject(parcial.createCriteria(pss[i], getAlias(pss,i), getTranslatedJoinType(filter.getJoinType())));
					} else {
						// otherwise creates a criteria on default (where)
						parcial.setObject(criteriaDefault.createCriteria(pss[i], getAlias(pss,i), getTranslatedJoinType(filter.getJoinType())));
					}
					criterias.put(getAlias(pss, i), parcial.getCriteria()); // stores both cases
				}
			}
			parcial.setObject(parcialBKP.getObject());
		} else { // .. when not from a junction
			for (int i = 0; i < pss.length - 1; i++) {
				// When a *DetachedCriteria* adds the filter
				if (DetachedCriteria.class.isInstance(parcial.getObject())){
					if (criterias.containsKey(getAlias(pss,i))){ // If the criteria have already been created
						parcial.setObject(criterias.get(getAlias(pss,i))); // retrieves and makes available to the partial
					} else {
						parcial.setObject( ((DetachedCriteria) parcial.getObject()).createCriteria(pss[i], getAlias(pss,i))); // cria na sub-query
						// Stores on the map
						criterias.put(getAlias(pss, i), parcial.getCriteria());
					}
				} else { // If it is a simple criteria
					if (criterias.containsKey(getAlias(pss,i))){ // If the criteria have already been created
						parcial.setObject(criterias.get(getAlias(pss,i))); // retrieves and makes available to the parcial
					} else {
						// Adds property
						parcial.setObject(parcial.createCriteria(pss[i], getAlias(pss,i), getTranslatedJoinType(filter.getJoinType())));
						// Stores on the map
						criterias.put(getAlias(pss, i), parcial.getCriteria());
					}
				}
			}
		}
	}
	
	/**
	 * Adjust the filter value to the informed class
	 * @param criteriaDefault
	 * @param parcial
	 * @param filter
	 * @param pss
	 * @param alias
	 * @return
	 */
	protected void adjustParameterValue(FilterType filter){
		// Validates the parameter
		filter.validate();
		// If a 'in' restriction is informed it must be a Collection on an Object array
		if (filter.getPropertyValue() != null && Restriction.IN.equals(filter.getRestriction()) || Restriction.NOT_IN.equals(filter.getRestriction())){
			// if not an Collection or Array
			if (!(Collection.class.isInstance(filter.getPropertyValue()) || Object[].class.isInstance(filter.getPropertyValue()))){
				// changes restriction type
				if (Restriction.IN.equals(filter.getRestriction())){
					filter.setRestriction(Restriction.EQUALS);
				} else {
					filter.setRestriction(Restriction.NOT_EQUALS);
				}
				// Changes paramter classs
				if (Collection.class.equals(filter.getPropertyClassType()) || Object[].class.equals(filter.getPropertyClassType())){
					filter.setPropertyClassType(filter.getPropertyValue().getClass());
				}
			}
		}
		
		// Other restrictions than 'in/not in' will have type verified 
		if (!(Restriction.IN.equals(filter.getRestriction()) || Restriction.NOT_IN.equals(filter.getRestriction()))){
			// *Long* class typed, with a non null value assigned and storing a non Long value
			if (Long.class.equals(filter.getPropertyClassType()) && (filter.getPropertyValue() != null && !Long.class.isInstance(filter.getPropertyValue())) ){
					filter.setPropertyValue(Long.parseLong(filter.getPropertyValue().toString()));
			} else // *Integer*
				if (Integer.class.equals(filter.getPropertyClassType()) && (filter.getPropertyValue() != null && !Integer.class.isInstance(filter.getPropertyValue())) ){
					filter.setPropertyValue(Integer.parseInt(filter.getPropertyValue().toString()));
			} else // *Byte*
				if (Byte.class.equals(filter.getPropertyClassType()) && (filter.getPropertyValue() != null && !Byte.class.isInstance(filter.getPropertyValue())) ){
					filter.setPropertyValue(Byte.parseByte(filter.getPropertyValue().toString()));
			} else // *BigDecimal*
				if (BigDecimal.class.equals(filter.getPropertyClassType()) && (filter.getPropertyValue() != null && !BigDecimal.class.isInstance(filter.getPropertyValue())) ){
					filter.setPropertyValue(new BigDecimal(filter.getPropertyValue().toString()));
			} else // *Double*
				if (Double.class.equals(filter.getPropertyClassType()) && (filter.getPropertyValue() != null && !Double.class.isInstance(filter.getPropertyValue())) ){
					filter.setPropertyValue(Double.parseDouble(filter.getPropertyValue().toString()));
			} else // *Float*
				if (Float.class.equals(filter.getPropertyClassType()) && (filter.getPropertyValue() != null && !Float.class.isInstance(filter.getPropertyValue())) ){
					filter.setPropertyValue(Float.parseFloat(filter.getPropertyValue().toString()));
			} else // *Boolean*
				if (Boolean.class.equals(filter.getPropertyClassType()) && (filter.getPropertyValue() != null && !Boolean.class.isInstance(filter.getPropertyValue())) ){
					filter.setPropertyValue(Boolean.parseBoolean(filter.getPropertyValue().toString().toLowerCase()));
			} else // *Date*
				if (Date.class.equals(filter.getPropertyClassType()) && (filter.getPropertyValue() != null && !Date.class.isInstance(filter.getPropertyValue())) ){
					convertParamterDate(filter);
			} else // *Enum*
				if (filter.getPropertyValue() != null && filter.getPropertyClassType().isEnum() && !filter.getPropertyValue().getClass().isEnum()) {
					convertParamterEnum(filter);
				}
		}
	}

	/**
	 * Convert to an Enum list the String valued paramter
	 * @param filter
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void convertParamterEnum(FilterType filter) {
		String parValue = filter.getPropertyValue().toString();				
		// creates the list that will contain the the Enum value or the
		// matching enum names
		final List<Enum> values = new ArrayList<Enum>(); 
		try {
			// gets the field for the informed enum class
			Field[] flds = filter.getPropertyClassType().getDeclaredFields();				
			// Iterates over all the fields
			for (Field fdAtt : flds) {
				fdAtt.setAccessible(true); // turn the field acessible
				// If its not an enum constant AND the field is String typed 
				if (!fdAtt.isEnumConstant() && fdAtt.getType().equals(String.class)) {
					// Iterates again over all the fields 
					for (Field fdEnum : flds) {
						// .. and now get only the enum constants
						if (fdEnum.isEnumConstant()) {
							// obtains the Enum constant
							final Enum enumConstant = Enum.valueOf((Class) filter.getPropertyClassType(), fdEnum.getName());
							// obtains the Enum constant values for the current field property
							final String enumConstantValue = fdAtt.get(enumConstant).toString();
							// Compares the constant name
							if (enumConstant.toString().equals(parValue)){
								values.add(enumConstant);
							} else
								// ignoring case and with a contain filter
								if (enumConstantValue.toUpperCase().contains(parValue.toUpperCase())){
									values.add(enumConstant);
							}
						}
					}
				}
			}
			
			// Converts the restriction
			if (values.size() > 1){
				if (Restriction.EQUALS.equals(filter.getRestriction())){
					filter.setRestriction(Restriction.IN);
				} else if (Restriction.NOT_EQUALS.equals(filter.getRestriction())){
					filter.setRestriction(Restriction.NOT_IN);
				}
				// Set paramter value as a list of enums
				filter.setPropertyValue(values);
			} else if (values.size() == 1){
				// Set paramter value with the unique element
				filter.setPropertyValue(values.get(0));
			}
			
		} catch (IllegalArgumentException ila) {
			throw new GenericBusinessException("Not able to convert the value for a Enum type. Paramter [" + filter.getPropertyName() + "]");
		} catch (IllegalAccessException e) {
			throw new GenericBusinessException("Not able to convert the value for a Enum type. Paramter [" + filter.getPropertyName() + "]");
		}
	}

	/**
	 * Convert to Date the String valued paramter 
	 * @param filter
	 */
	protected void convertParamterDate(FilterType filter) {
		// TODO Use locale to find out the corresponding date format 
		String mask;
		String dt = filter.getPropertyValue().toString().trim();
		if (dt.length() == 8 && dt.indexOf("/") < 0) {
			mask = "ddMMyyyy";
		} else if (dt.length() == 8 && dt.charAt(2) == '/' && dt.charAt(5) == '/') {
			mask = "dd/MM/yy";
		} else if (dt.length() == 10 && dt.charAt(2) == '/' && dt.charAt(5) == '/') {
			mask = "dd/MM/yyyy";
		} else if (dt.length() == 16 && dt.charAt(2) == '/' && dt.charAt(5) == '/' && dt.charAt(13) == ':') {
			mask = "dd/MM/yyyy hh:mm";
		} else if (dt.length() == 19 && dt.charAt(2) == '/' && dt.charAt(5) == '/' && dt.charAt(13) == ':'  && dt.charAt(16) == ':') {
			mask = "dd/MM/yyyy hh:mm:ss";
		} else if (dt.length() == 10 && dt.charAt(4) == '/' && dt.charAt(7) == '/') {
			mask = "yyyy/MM/dd";
		} else if (dt.length() == 16 && dt.charAt(4) == '/' && dt.charAt(7) == '/' && dt.charAt(13) == ':') {
			mask = "yyyy/MM/dd hh:mm";
		} else if (dt.length() == 16 && dt.charAt(4) == '/' && dt.charAt(7) == '/' && dt.charAt(13) == ':' && dt.charAt(16) == ':') {
			mask = "yyyy/MM/dd hh:mm:ss";
		} else {
			throw new GenericBusinessException("Not able to convert the text [" + dt + "] as a java.util.Date");
		}
		try {
			filter.setPropertyValue(DateFormatterUtil.convertStringToDate(dt, mask));
		} catch (ParseException e) {
			throw new GenericBusinessException("Error converting date from a string for Parameter named [" + filter.getPropertyName() + "]. Erro [" + e.getMessage() + "]");
		}
	}
	
	/**
	 * Assigns the property value
	 * @param parcial
	 * @param restriction
	 * @param alias
	 * @param propertyName
	 * @param objectClass
	 * @param value
	 */
	@SuppressWarnings("rawtypes")
	protected void assignPropertyCriteriaValue(CriterionProxy parcial, FilterType filter, String alias, String propertyName) {
		// helper
		final Restriction restriction = filter.getRestriction();
		final Class objectClass = filter.getPropertyClassType();
		final Object value = filter.getPropertyValue();
		
		Criterion criterion = null;
		// Alias + PropertyName
		String propertyNameWithAlias = alias+propertyName;
		
		if (value == null){
			if (Restriction.EQUALS.equals(restriction) || Restriction.LIKE.equals(restriction)){
				criterion = Restrictions.isNull(propertyNameWithAlias);
			} else if (Restriction.NOT_EQUALS.equals(restriction) || Restriction.NOT_LIKE.equals(restriction)){
				criterion = Restrictions.isNotNull(propertyNameWithAlias);
			} else {
				throw new GenericBusinessException("Not informed a valid restriction to a null value. Paramer name [" + propertyName + "]");
			}
		} else if (Restriction.IN.equals(restriction) || Restriction.NOT_IN.equals(restriction)){
			if (Collection.class.isInstance(value)){
				criterion = Restrictions.in(propertyNameWithAlias, (Collection) value);
			} else if (Object[].class.isInstance(value)) {
				criterion = Restrictions.in(propertyNameWithAlias, (Object[]) value);
			} else {
				throw new GenericBusinessException("Not a valid class type for an IN restriction.");
			}
			// for a 'not in' restriction adds the 'not' restriction
			if (Restriction.NOT_IN.equals(restriction)){
				criterion = Restrictions.not(criterion);
			}
		} else if (Restriction.EQUALS.equals(restriction)){
			criterion = Restrictions.eq(propertyNameWithAlias,objectClass.cast(value));
		} else if (Restriction.NOT_EQUALS.equals(restriction)){
			criterion = Restrictions.ne(propertyNameWithAlias,objectClass.cast(value));
		} else if (Restriction.LIKE.equals(restriction) || Restriction.NOT_LIKE.equals(restriction)){
			criterion = Restrictions.like(propertyNameWithAlias, objectClass.cast(value));
			// for a 'not like' restriction adds the 'not' restriction
			if (Restriction.NOT_LIKE.equals(restriction)){
				criterion = Restrictions.not(criterion);
			}
		} else if (Restriction.GE.equals(restriction)){
			criterion = Restrictions.ge(propertyNameWithAlias,objectClass.cast(value));
		} else if (Restriction.GT.equals(restriction)){
			criterion = Restrictions.gt(propertyNameWithAlias,objectClass.cast(value));
		} else if (Restriction.LE.equals(restriction)){
			criterion = Restrictions.le(propertyNameWithAlias,objectClass.cast(value));
		} else if (Restriction.LT.equals(restriction)){
			criterion = Restrictions.lt(propertyNameWithAlias,objectClass.cast(value));
		}
		
		// Ignores case for string fields
		if (filter.isIgnoreCase()){
			( (SimpleExpression) criterion).ignoreCase();
		}

		parcial.add(criterion);
	}
	
	/**
	 * Adds a sub-query to given criteria
	 * @param criterias
	 * @param criteriaDefault
	 * @param parcial
	 * @param filter
	 * @param alias
	 */
	protected void createSubQuery(Map<String, Criteria> criterias, CriterionProxy criteriaDefault, CriterionProxy parcial, FilterType filter, String alias){
		// If it has a class associated with a filter considers to be "in" using a subselect
		if (filter.getSubQueryClassType() != null) {
			// Cria sub-query
			final DetachedCriteria dc = DetachedCriteria.forClass(filter.getSubQueryClassType(), filter.getSubQueryClassType().getSimpleName().toLowerCase());
			dc.setProjection(Property.forName(filter.getSubQueryClassType().getSimpleName().toLowerCase() + "." + filter.getSubQueryProjectedColumn()));
			// Loads the variable of main query
			final String[] sepVarCriteriaPrincipal = filter.getSubQueryInverseColumn().split("\\.");
			if (sepVarCriteriaPrincipal.length > 1) {
				// Creates the structure for the main variable
				createCriteriaStructure(new HashMap<String, Criteria>(), criteriaDefault, parcial, sepVarCriteriaPrincipal, getTranslatedJoinType(filter.getJoinType()));
			} else {
				parcial.setObject(criteriaDefault.getObject());
			}
			// Adds a subquery to main query through the last level
			parcial.add(Property.forName(sepVarCriteriaPrincipal[sepVarCriteriaPrincipal.length - 1]).in(dc)); // adiciona na criteria default
			// Loads the partial sub-query to accomplish the passage of parameters below
			parcial = new CriterionProxy(dc);
			// Add property filter to sub-query
			if (filter.getPropertyValue() != null){
				final CriterionProxy cp = new CriterionProxy(dc);
				
				// Splits the property paths
				String[] pssProperty = filter.getPropertyName().split("\\.");
				
				if (pssProperty.length > 1){
					createCriteria(new HashMap<String, Criteria>(), cp, cp, filter);
					alias = getAlias(pssProperty,pssProperty.length - 2)+".";
				}
				
				String propertyName = "";
				if(pssProperty.length>1){
					propertyName = pssProperty[pssProperty.length - 1];
				} else {
					propertyName = filter.getPropertyName();
				}
				
				adjustParameterValue(filter);				
				assignPropertyCriteriaValue(parcial, filter, alias, propertyName);
			}
		}
	}
	
	/**
	 * Create the sub-criteria structure
	 * @param criteriaDefault
	 * @param parcial
	 * @param pss
	 */
	protected void createCriteriaStructure(Map<String, Criteria> criterias, final CriterionProxy criteriaDefault, CriterionProxy parcial, String[] pss, int joinType) {
		for (int i = 0; i < pss.length - 1; i++) {
			if (!criterias.containsKey(pss[i])) {
				// If the part already contains a criteria and this
				// Criteria is the criteria father of the current element
				if (parcial.getCriteria() != null && (i > 0 && criterias.get(pss[i - 1]).equals(parcial.getCriteria()))) {
					parcial.setObject(parcial.createCriteria(pss[i],pss[i], joinType));
					criterias.put(pss[i], parcial.getCriteria());
				} else {
					// If it is a filter that adds DetachedCriteria
					if (DetachedCriteria.class.isInstance(parcial.getObject())){
						parcial.setObject( ((DetachedCriteria) parcial.getObject()).createCriteria(pss[i]));
					} else {
						parcial.setObject(criteriaDefault.createCriteria(pss[i],pss[i], joinType));
					}
					criterias.put(pss[i], parcial.getCriteria());
				}
			} else {
				parcial.setObject(criterias.get(pss[i]));
			}
		}
	}
	
	/**
	 * Returns an alias name for the given path.
	 * @param pss
	 * @param iLimit Limits the path considering a dot splitted path starting from 0
	 * @return
	 */
	protected String getAlias(String[] pss, int iLimit){
		String retorno = "";
		for (int i = 0; i <= iLimit; i++){
			retorno += pss[i]+"_";
		}
		return retorno;
	}
	
	/**
	 * Returns the translated hibernate join type value.
	 * @param joinType
	 * @return
	 */
	protected int getTranslatedJoinType(Join joinType){
		int result = JoinType.INNER_JOIN.getJoinTypeValue();
		if (joinType != null){
			if (Join.INNER.equals(joinType)){
				result = JoinType.INNER_JOIN.getJoinTypeValue();
			} else if (Join.LEFT.equals(joinType)){
				result = JoinType.LEFT_OUTER_JOIN.getJoinTypeValue();
			}
		}
		
		return result;
	}
}
