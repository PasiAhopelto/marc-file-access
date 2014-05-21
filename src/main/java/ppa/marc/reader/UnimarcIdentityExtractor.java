package ppa.marc.reader;

import java.util.List;


public class UnimarcIdentityExtractor implements IdentityExtractor {

	public String getIdentity(List<String> fieldsAsStrings) {
		String firstField = fieldsAsStrings.get(0);
		return firstField.replaceFirst("^__\\s", "");
	}

}
