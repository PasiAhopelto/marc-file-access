package ppa.marc.reader;

import org.springframework.stereotype.Component;

import ppa.marc.domain.SubField;

@Component
public class UnimarcSubFieldParser implements SubFieldParser {

	public SubField parse(String subFieldAsString, boolean isZeroZeroField) {
		if(subFieldAsString.length() == 0) throw new RuntimeException("Subfield doesn't contain subfield identifier or data.");
		if(isZeroZeroField) return new SubField(subFieldAsString);
		return new SubField(subFieldAsString.charAt(0), subFieldAsString.substring(1));
	}

}
