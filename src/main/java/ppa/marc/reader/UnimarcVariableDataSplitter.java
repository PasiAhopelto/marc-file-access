package ppa.marc.reader;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import ppa.marc.common.UnimarcConstants;

@Component
public class UnimarcVariableDataSplitter implements VariableDataSplitter {

	public List<String> split(List<Integer> fieldIdentifiers, String dataFieldsWithoutEndOfRecordIndicator) {
		String[] dataAsStrings = StringUtils.split(dataFieldsWithoutEndOfRecordIndicator, UnimarcConstants.FIELD_TERMINATOR);
		if(dataAsStrings.length != fieldIdentifiers.size()) throw new RuntimeException("Expected " + fieldIdentifiers.size() + " data entries, but found " + dataAsStrings.length + ".");
		return Arrays.asList(dataAsStrings);
	}

}
