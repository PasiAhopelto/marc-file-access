package ppa.marc.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ppa.marc.domain.SubField;

public class SubFieldExtractor {
	private static final String FIELDS_AND_SUBFIELDS = "\\d+(.[\\d_]{2}){0,1}\\s+(.+)";
	private static final String SUBFIELD = "\\s*(\\|(\\p{Alnum}) {0,1}){0,1}(.*?)";
	private static final String LINE_SEPARATOR = "\\n";
	
	public List<SubField> parseSubFields(String fieldAsString) throws MarcFormatException {
		if(fieldAsString == null) throw new MarcFormatException("Field is null.");
		ArrayList<SubField> subFields = new ArrayList<SubField>();
		parseSubfields(fieldAsString, subFields);
		if(subFields.size() == 0) throw new MarcFormatException("Field '" + fieldAsString + "' should have at least one subfield.");
		return subFields;
	}

	private void parseSubfields(String fieldAsString, ArrayList<SubField> subFields) {
		Pattern subfieldsPattern = Pattern.compile(FIELDS_AND_SUBFIELDS, Pattern.DOTALL);
		Matcher subfieldsMatcher = subfieldsPattern.matcher(fieldAsString);
		if(subfieldsMatcher.matches()) {
			parseSubfieldLineByLine(subFields, subfieldsMatcher);
		}
	}

	private void parseSubfieldLineByLine(ArrayList<SubField> subFields, Matcher subfieldsMatcher) {
		for(String line : subfieldsMatcher.group(2).split(LINE_SEPARATOR)) {
			parseSubfield(subFields, line);
		}
	}

	private void parseSubfield(ArrayList<SubField> subFields, String subFieldsAsString) {
		Pattern subfieldPattern = Pattern.compile(SUBFIELD);
		Matcher subfieldMatcher = subfieldPattern.matcher(subFieldsAsString);
		if(subfieldMatcher.matches()) {
			extractSubfieldIdAndValue(subFields, subfieldMatcher);
		}
	}

	private void extractSubfieldIdAndValue(ArrayList<SubField> subFields, Matcher subfieldMatcher) {
		String subFieldIdentifier = subfieldMatcher.group(2);
		if(isDataSubfield(subFieldIdentifier)) {
			subFields.add(new SubField(subfieldMatcher.group(2).charAt(0), subfieldMatcher.group(3)));
		}
		else {
			subFields.add(new SubField(subfieldMatcher.group(3)));
		}
	}

	private boolean isDataSubfield(String subFieldIdentifier) {
		return subFieldIdentifier != null;
	}
}
