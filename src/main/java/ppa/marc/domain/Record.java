package ppa.marc.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * UNIMARC bibliographic record that consists of <code>Field</code>s.
 */
public class Record {

	private final List<Field> fields = new ArrayList<Field>();
	private String name;
	private String label;

	/**
	 * Constructor for a record.
	 * @param name Record's name. It isn't stored into UNIMARC file as such, and it doesn't need to be unique. It could for example be derived from UNIMARC's field 200.
	 */
	public Record(String name) {
		this.name = name;
	}

	/**
	 * @return Record's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Record's fields.
	 */
	public List<Field> getFields() {
		return fields;
	}

	/**
	 * Human (and machine) readable string representation of the record where fields are separated by newlines.  
	 * @return This object as string.
	 * @see Field
	 */
	public String toString() {
		StringBuffer asString = new StringBuffer();
		for(Field field : fields) {
			asString.append(field.toString());
		}
		return asString.toString();
	}

	/**
	 * @param name Record's name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param label Record's label, which is the first 24 characters of a record in case or UNIMARC.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return Record's label.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Tests equality against other record.
	 * @param other Record to test against.
	 * @return True, if records are equal: names, labels and fields are identical.
	 */
	public boolean equals(Object obj) {
		if(obj instanceof Record) {
			Record other = (Record) obj;
			return new EqualsBuilder().append(name, other.name).append(label, other.label).append(fields, other.fields).isEquals();
		}
		return false;
	}
	
}
