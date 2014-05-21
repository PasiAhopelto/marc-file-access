package ppa.marc.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class RecordToFieldsDivider {
	private static final Pattern FIELD_PATTERN = Pattern.compile("\\d\\d\\d(|(\\.[\\d_]{2}))(\\s+.+|\\s*)");
	private static final Pattern CONTINUES_FROM_PREVIOUS_LINE_FIELD_PATTERN = Pattern.compile("\\s*\\|\\p{Alnum}.+");
	
	public List<String> divideToFields(String recordAsString) throws MarcFormatException {
		List<StringBuffer> fields = new ArrayList<StringBuffer>();
		for(String line : recordAsString.split("\n", -1)) {
			if(line.length() == 0) failWithMessage(recordAsString, "contains empty line");
			handleSingleLine(recordAsString, fields, line);
		}
		if(fields.isEmpty()) failWithMessage(recordAsString, "could not be divided into fields");
		return toStringList(fields); 
	}

	private void handleSingleLine(String recordAsString, List<StringBuffer> fields, String line) throws MarcFormatException {
		Matcher matcher = FIELD_PATTERN.matcher(line);
		if(matcher.matches()) {
			fields.add(new StringBuffer(matcher.group()));
		}
		else {
			appendIfSubfieldOfPreviousField(fields, line);
		}
	}

	private void appendIfSubfieldOfPreviousField(List<StringBuffer> fields, String line) {
		Matcher continuedFieldMatcher = CONTINUES_FROM_PREVIOUS_LINE_FIELD_PATTERN.matcher(line);
		if(continuedFieldMatcher.matches() && fields.size() > 0) {
			fields.get(fields.size()-1).append("\n").append(line);
		}
	}

	private List<String> toStringList(List<StringBuffer> fields) {
		List<String> stringList = new ArrayList<String>();
		for(StringBuffer stringBuffer : fields) {
			stringList.add(stringBuffer.toString());
		}
		return stringList;
	}

	private void failWithMessage(String recordAsString, String message) throws MarcFormatException {
		throw new MarcFormatException("Record '" + recordAsString + "' " + message + ".");	
	}
}
