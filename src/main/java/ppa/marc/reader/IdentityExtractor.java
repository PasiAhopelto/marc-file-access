package ppa.marc.reader;

import java.util.List;

public interface IdentityExtractor {

	String getIdentity(List<String> fieldsAsStrings);
	
}
