package ppa.marc.reader;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

@Named("identityExtractor")
@Singleton
public class UnimarcIdentityExtractor implements IdentityExtractor {

	public String getIdentity(List<String> fieldsAsStrings) {
		String firstField = fieldsAsStrings.get(0);
		return firstField.replaceFirst("^__\\s", "");
	}

}
