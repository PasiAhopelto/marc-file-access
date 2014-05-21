package ppa.marc.reader;

import ppa.marc.domain.Field;

public interface FieldParser {

	Field parseField(int id, String fieldAsString);
	
}
