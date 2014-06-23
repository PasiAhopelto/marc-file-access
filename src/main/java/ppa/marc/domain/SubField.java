package ppa.marc.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * UNIMARC subfield for bibliographic data. Subfields contain concrete values stored into fields.
 */
public class SubField implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String value;
	private char id;

	/**
	 * Instantiates SubField with id and value set as null.
	 */
	public SubField() {
	}
	
	/**
	 * Constructor for a subfield with an identifier, that is for subfields that are commonly used with data fields.
	 * @param id Subfield's identifier, for example 'a'. Id does not need to be unique, if the subfield is repeatable.
	 * @param value Subfield's value as string.
	 */
	public SubField(char id, String value) {
		this.id = id;
		this.value = value;
	}

	/**
	 * Constructor for identifierless subfield, that are used with control fields.
	 * @param value Subfield's value.
	 */
	public SubField(String value) {
		this.id = Character.UNASSIGNED;
		this.value = value;
	}

	/**
	 * @return Subfield's value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return Identifier, or Character.UNASSIGNED if the identifier isn't defined.
	 */
	public Character getId() {
		return id;
	}

	/**
	 * Compares this subfield against other object for equality.
	 * @param obj Object to compare.
	 * @return True, if identifier and value are identical in the objects.
	 */
	public boolean equals(Object obj) {
		if(!(obj instanceof SubField)) return false;
		SubField other = (SubField) obj;
		return new EqualsBuilder().append(getId(), other.getId()).append(getValue(), other.getValue()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getId()).append(getValue()).toHashCode();
	}

	/**
	 * Returns this subfield as a string.
	 * @return Value in case of control field's subfield, otherwise the syntax is for <code>|a value</code> where a is subfield's identifier. 
	 */
	public String toString() {
		if(id == Character.UNASSIGNED) return value;
		return "|" + id + " " + value;
	}

	/**
	 * Setter for identifier.
	 * @param id Subfield's new identifier.
	 */
	public void setId(char id) {
		this.id = new Character(id);
	}

	/**
	 * Setter for value.
	 * @param value Subfield's new value.
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
