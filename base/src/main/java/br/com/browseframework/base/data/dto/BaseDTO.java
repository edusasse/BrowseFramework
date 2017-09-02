package br.com.browseframework.base.data.dto;

import java.io.Serializable;

/**
 * Represents an abstract implementation for DTO
 * @author Eduardo
 *
 * @param <ID> Represents either native or composite key.
 */
public abstract class BaseDTO<ID extends Serializable> implements DTO, Serializable {

	private static final long serialVersionUID = -596992142744959572L;

	/**
	 * Represents the primary key, either native or composite key.
	 * @return
	 */
	public abstract ID getId();

	/**
	 * Sets the primary key.
	 * @param id
	 */
	public abstract void setId(ID id);

	/**
	 * Verifies ID if is a non persistent object. 
	 * @return
	 */
	public boolean isNew() {
		// If a numeric 
		if (Number.class.isInstance(getId())){
			final Number idNumber = ((Number) getId());
			return idNumber == null || idNumber.longValue() <= 0;
		} else {
			// If non numeric verifies if its null yet
			return getId() == null;
		}
	}
	 
	/**
	 * To avoid this problem we recommend using the "semi"-unique attributes of
	 * your persistent class to implement equals() (and hashCode()). Basically
	 * you should think of your database identifier as not having business
	 * meaning at all (remember, surrogate identifier attributes and
	 * automatically generated vales are recommended anyway). The database
	 * identifier property should only be an object identifier, and basically
	 * should be used by Hibernate only. Of course, you may also use the
	 * database identifier as a convenient read-only handle, e.g. to build links
	 * in web applications.
	 */
	@Override
	public int hashCode() {
		return this.getId() == null ? System.identityHashCode(this) : getId().hashCode();
	}

	/**
	 * String representation of the object.
	 */
	@Override
	public String toString() {
		String result = null;
		final StringBuilder sb = new StringBuilder();
		sb.append("Object ID [");
		sb.append((this.getId() == null ? "n/a" : getId().toString()) );
		sb.append("] Class [");
		sb.append(this.getClass().getName());
		result = sb.toString();
		
		return result;
	}

	/**
	 * Compares object IDs
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj != null && !isNew() && BaseDTO.class.isInstance(obj)) {
			return getId().equals(((BaseDTO) obj).getId());
		}
		return result;
	}
}