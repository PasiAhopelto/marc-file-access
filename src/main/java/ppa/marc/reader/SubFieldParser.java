package ppa.marc.reader;

import ppa.marc.domain.SubField;

public interface SubFieldParser {

	// TODO throw better exception
	SubField parse(String subFieldAsString, boolean isZeroZeroField);

}
