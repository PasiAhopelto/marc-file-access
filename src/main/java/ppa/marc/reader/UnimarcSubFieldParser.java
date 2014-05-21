package ppa.marc.reader;

import javax.inject.Named;
import javax.inject.Singleton;

import ppa.marc.domain.SubField;

@Named("subFieldParser")
@Singleton
public class UnimarcSubFieldParser implements SubFieldParser {

	public SubField parse(String subFieldAsString, boolean isZeroZeroField) {
		if(subFieldAsString.length() == 0) throw new RuntimeException("Subfield doesn't contain subfield identifier or data.");
		if(isZeroZeroField) return new SubField(subFieldAsString);
		return new SubField(subFieldAsString.charAt(0), subFieldAsString.substring(1));
	}

}
