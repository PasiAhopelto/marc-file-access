package ppa.marc.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * UNIMARC field for control or bibliographic data.
 */
public class Field {

	int id;
	char firstIndicator;
	char secondIndicator;
	protected final List<SubField> subFields = new ArrayList<SubField>();
	protected boolean isControlField; 
	
	/**
	 * Constructor for a data field.
	 * @param id Field's identifier, for example "327".
	 * @param firstIndicator Field's first indicator character.
	 * @param secondIndicator Field's second indicator character.
	 */
	public Field(int id, char firstIndicator, char secondIndicator) {
		this.id = id;
		this.firstIndicator = firstIndicator;
		this.secondIndicator = secondIndicator;
	}

	/**
	 * Constructor for a control field.
	 * @param id Field's identifier, for example "1" for field id "001".
	 */
	public Field(int id) {
		this.id = id;
		this.firstIndicator = Character.UNASSIGNED;
		this.secondIndicator = Character.UNASSIGNED;
		this.isControlField = true;
	}

	/**
	 * @return Field's first indicator, or Character.UNDEFINED if this is a control field.
	 */
	public char getFirstIndicator() {
		return firstIndicator;
	}

	/**
	 * @return Field's second indicator, or Character.UNDEFINED if this is a control field.
	 */
	public char getSecondIndicator() {
		return secondIndicator;
	}

	/**
	 * Getter for subfields.  Note that there isn't method for adding new subfields, so use <code>field.getSubFields().add(subField)</code> for that.
	 * @return Reference to list containing subfields.
	 */
	public List<SubField> getSubFields() {
		return subFields;
	}

	/**
	 * @return Field's identifier.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Compares this object for equality against other object.
	 * @param obj Other object to compare against.
	 * @return True, if object's have identical identifiers, indicators and values.
	 */
	public boolean equals(Object obj) {
		if(!(obj instanceof Field)) return false;
		Field other = (Field) obj;
		return new EqualsBuilder().append(id, other.id).append(firstIndicator, other.firstIndicator).append(secondIndicator, other.secondIndicator).append(subFields, other.subFields).isEquals();
	}

	/**
	 * Converts this object to a string where control fields use following formatting:
	 * <pre>
	 * 001    0860788709
	 * </pre>
     * Data fields use following formatting:
	 * </pre>
	 * <pre>
	 * 711.02 |a Ashgate/Variorum
     *        |4 650
	 * </pre>
	 * @return Object as a string that is both human and machine readable.  
	 */
	public String toString() {
		final StringBuffer asString = new StringBuffer(idAsString());
		appendIndicators(asString);
		asString.append(' ');
		appendSubFields(asString);
		return asString.toString();
	}

	private CharSequence idAsString() {
		return StringUtils.leftPad(String.valueOf(id), 3, '0');
	}

	private void appendIndicators(final StringBuffer asString) {
		if(firstIndicator == Character.UNASSIGNED) {
			asString.append("   ");
		}
		else {
			asString.append(".");
			asString.append(changeSpaceToUnderscore(firstIndicator));
			asString.append(changeSpaceToUnderscore(secondIndicator));
		}
	}

	private char changeSpaceToUnderscore(char indicator) {
		if(indicator == ' ') return '_';
		return indicator;
	}

	private void appendSubFields(final StringBuffer asString) {
		for(SubField subField : subFields) {
			if(asString.toString().contains("\n")) asString.append("       ");
			asString.append(subField.toString());
			asString.append('\n');
		}
	}

	/**
	 * @param id Field's identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param firstIndicator Fields first indicator.
	 */
	public void setFirstIndicator(Character firstIndicator) {
		this.firstIndicator = firstIndicator;
	}

	/**
	 * @param secondIndicator Field's second indicator.
	 */
	public void setSecondIndicator(Character secondIndicator) {
		this.secondIndicator = secondIndicator;
	}

	/**
	 * Tells whether field is a control field.  Control field's indicators are not saved to MARC record file.
	 * @return True if this is a control field.
	 */
	public boolean isControlField() {
		return isControlField;
	}

	/**
	 * Sets control field status.  Control field's indicators aren't saved to MARC file.
	 * @param isControlField True, if this field is a control field.
	 */
	public void setIsControlField(boolean isControlField) {
		this.isControlField = isControlField;
	}

}
