package ppa.marc.reader;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.lang.StringUtils;

import ppa.marc.common.UnimarcConstants;
import ppa.marc.domain.Field;

@Named("fieldParser")
@Singleton
public class UnimarcFieldParser implements FieldParser {

	private final SubFieldParser subFieldParser;

	@Inject
	public UnimarcFieldParser(SubFieldParser subFieldParser) {
		this.subFieldParser = subFieldParser;
	}

	public Field parseField(int id, String fieldAsString) {
		if(fieldAsString.length() == 0) throw new IllegalArgumentException("Field is empty.");
		String[] subFieldsAsString = StringUtils.split(fieldAsString, UnimarcConstants.SUBFIELD_DELIMITER);
		return extractField(id, subFieldsAsString);
	}

	private boolean isZeroZeroField(int id) {
		return (id < 10);
	}

	private Field extractField(int id, String[] subFieldsAsString) {
		Field field;
		if(isZeroZeroField(id)) {
			field = new Field(id);
			parseSubFieldsStartingFromIndex(0, id, subFieldsAsString, field);
		}
		else {
			field = new Field(id, subFieldsAsString[0].charAt(0), subFieldsAsString[0].charAt(1));
			parseSubFieldsStartingFromIndex(1, id, subFieldsAsString, field);
		}
		return field;
	}

	private void parseSubFieldsStartingFromIndex(int index, int id, String[] subFieldsAsString, Field field) {
		for(int i = index; i < subFieldsAsString.length; ++i) {
			field.getSubFields().add(subFieldParser.parse(subFieldsAsString[i], isZeroZeroField(id)));
		}
	}

}
