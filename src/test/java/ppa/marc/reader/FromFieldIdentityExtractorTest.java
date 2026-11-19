package ppa.marc.reader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import ppa.marc.domain.Field;
import ppa.marc.domain.SubField;


public class FromFieldIdentityExtractorTest {

	private static final String IDENTITY = "identity";
	private FromFieldIdentityExtractor extractor = new FromFieldIdentityExtractor();
	private List<Field> fields = new ArrayList<Field>();
	private static Field ID_FIELD = new Field(1);
	
	@BeforeEach
	
	protected void setUp() throws Exception {
		ID_FIELD.getSubFields().clear();
	}
	
	@Test
	
	public void testGrantedIdentityFieldIsMissingThenThrowsMarcFormatException() throws Exception {
		expectExceptionWithMessage("Record does not have a record identifying field.");
	}

	@Test

	public void testGrantedIdentityFieldIsNullThenThrowsMarcFormatException() throws Exception {
		addIdentityField(null);
		expectExceptionWithMessage("Identifying field " + ID_FIELD + " has null value.");
	}

	@Test

	public void testGrantedOneField001ThenItsValueIsReturnedAsId() throws Exception {
		addIdentityField(IDENTITY);
		assertEquals(IDENTITY, extractor.extractIdentity(fields));
	}

	@Test

	public void testGrantedOneField001WithAnotherFieldThen001ValueIsReturnedAsId() throws Exception {
		addOtherField();
		addIdentityField(IDENTITY);
		assertEquals(IDENTITY, extractor.extractIdentity(fields));
	}

	@Test

	public void testGrantedTwoFields001ThenExceptionIsThrown() throws Exception {
		addIdentityField(IDENTITY);
		addIdentityField(IDENTITY);
		expectExceptionWithMessage("Record has more than one identifying field.");
	}

	private void expectExceptionWithMessage(String message) {
		try {
			extractor.extractIdentity(fields);
			fail();
		} catch(MarcFormatException expected) {
			assertTrue(expected.getMessage().contains(message));
		}
	}
	
	private void addIdentityField(String identity) {
		ID_FIELD.getSubFields().add(new SubField(identity));
		fields.add(ID_FIELD);
	}
	private void addOtherField() {
		fields.add(new Field(002));
	}
	
}
