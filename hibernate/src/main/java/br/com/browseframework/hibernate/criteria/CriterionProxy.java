package br.com.browseframework.hibernate.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.internal.CriteriaImpl.Subcriteria;

import br.com.browseframework.base.exception.GenericBusinessException;

public class CriterionProxy {
	private Object o = null;

	CriterionProxy(Object o) {
		setObject(o);
	}

	public void setObject(Object o) {
		this.o = o;
	}

	public Object add(Criterion criterion) {
		if (Conjunction.class.isInstance(o)) {
			return ((Conjunction) o).add(criterion);
		} else if (Disjunction.class.isInstance(o)) {
			return ((Disjunction) o).add(criterion);
		} else if (Criteria.class.isInstance(o)) {
			return ((Criteria) o).add(criterion);
		} else if (DetachedCriteria.class.isInstance(o)) {
			return ((DetachedCriteria) o).add(criterion);
		}
		throw new GenericBusinessException(
				"Não foi possível determinar um tipo!");
	}

	@SuppressWarnings("deprecation")
	public Criteria createCriteria(String associationPath, String alias,
			int joinType) {
		Criteria retorno = null;
		if (Criteria.class.isInstance(o)) {
			if (alias == null) {
				retorno = ((Criteria) o).createCriteria(associationPath,
						joinType);
			} else {
				retorno = ((Criteria) o).createCriteria(associationPath, alias,
						joinType);
			}
		}
		return retorno;
	}

	public Criteria getCriteria() {
		Criteria retorno = null;
		if (Criteria.class.isInstance(o)) {
			retorno = ((Criteria) o);
		}
		return retorno;
	}

	public Criterion getCriterion() {
		if (Criterion.class.isInstance(o)) {
			return (Criterion) o;
		} else {
			throw new IllegalArgumentException("Não é um criterion.");
		}
	}

	public boolean isJunction() {
		return (o != null && (Junction.class.isInstance(o)));
	}

	public boolean isCriterion() {
		return (Criterion.class.isInstance(o));
	}

	public boolean isSubbcriteria() {
		boolean retorno = false;
		if (Subcriteria.class.isInstance(o)
				|| DetachedCriteria.class.isInstance(o)) {
			retorno = true;
		}
		return retorno;
	}

	public Object getObject() {
		return o;
	}
}