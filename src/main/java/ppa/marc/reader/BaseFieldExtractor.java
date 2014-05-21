package ppa.marc.reader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Named;
import javax.inject.Singleton;

import ppa.marc.domain.Field;

@Named
@Singleton
public class BaseFieldExtractor {

	private static final String IDENTIFIER = "(\\d{3,3})";
	private static final String OPTIONAL_INDICATORS = "(\\.[_\\d]{2,2}){0,1}";
	private static final String OPTIONAL_VALUE_AFTER_SPACE = "(\\s+.*)*";
	
	private static final Pattern PATTERN = Pattern.compile(IDENTIFIER + OPTIONAL_INDICATORS + OPTIONAL_VALUE_AFTER_SPACE);
	
	public Field parseBaseField(String fieldAsString) throws MarcFormatException {
		if(fieldAsString == null) throw new MarcFormatException("Field is null.");
		if(fieldAsString.equals("")) throw new MarcFormatException("Field is empty.");
		Matcher matcher = PATTERN.matcher(fieldAsString);
		if(matcher.matches()) {
			return createFieldFromMatcher(matcher);
		}
		throw new MarcFormatException("Field '" + fieldAsString + "' is not valid.");
	}

	private Field createFieldFromMatcher(Matcher matcher) {
		String id = matcher.group(1);
		String indicators = matcher.group(2);
		if(indicators == null) return new Field(Integer.parseInt(id));
		return new Field(Integer.parseInt(id), indicators.charAt(1), indicators.charAt(2));
	}

}
