package ppa.marc.reader;

import java.util.Collection;
import java.util.List;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ppa.marc.domain.Field;

@Component
public class FromFieldIdentityExtractor {

	private static int ID_FIELD_ID = 1;
	
	public String extractIdentity(List<Field> fields) throws MarcFormatException {
		Collection<Field> identifyingFields = selectIdentifyingFields(fields);
		if(identifyingFields.size() == 0) throw new MarcFormatException("Record does not have a record identifying field.");
		if(identifyingFields.size() > 1) throw new MarcFormatException("Record has more than one identifying field.");
		String identifier = identifyingFields.iterator().next().getSubFields().get(0).getValue();
		if(identifier == null) throw new MarcFormatException("Identifying field " + fields.get(0) + " has null value.");
		return identifier;
	}

	private Collection<Field> selectIdentifyingFields(List<Field> fields) {
		return fields.stream()
				.filter(field -> field.getId() == ID_FIELD_ID)
				.collect(Collectors.toList());
	}
}
