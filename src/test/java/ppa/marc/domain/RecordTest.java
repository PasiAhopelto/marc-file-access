package ppa.marc.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class RecordTest {

	private static final String LABEL = "label";
	Record record = new Record("identifier");
	Field[] fields = {
		new Field(10, 'a', 'b'),
		new Field(200, 'a', 'd')
	};

	@BeforeEach
	protected void setUp() throws Exception {
		record.getFields().add(fields[0]);
		record.getFields().add(fields[1]);
	}
	
	@Test
	public void testIdentifierCanBeSet() throws Exception {
		assertEquals("identifier", record.getName());
	}
	
	@Test
	public void testReferenceToFieldListIsAccessible() throws Exception {
		assertSame(fields[0], record.getFields().get(0));
	}
	
	@Test
	public void testToString() throws Exception {
		assertEquals(fields[0].toString() + fields[1].toString(), record.toString());
	}

	@Test
	public void testIdentifierIsSettable() throws Exception {
		record.setName("id");
		assertEquals("id", record.getName());
	}

	@Test
	public void testGrantedLabelIsSetThenItIsReturnedByGetter() throws Exception {
		record.setLabel(LABEL);
		assertEquals(LABEL, record.getLabel());
	}

	@Test
	public void testGrantedNamesDifferThenEqualsReturnsFalse() throws Exception {
		assertFalse(new Record("other").equals(record));
	}

	@Test
	public void testGrantedNamesAreSameThenEqualsReturnsFalse() throws Exception {
		record.getFields().clear();
		assertTrue(new Record(record.getName()).equals(record));
	}

	@Test
	public void testGrantedLabelsDifferThenEqualsReturnsFalse() throws Exception {
		record.getFields().clear();
		record.setLabel("other label");
		assertFalse(new Record(record.getName()).equals(record));
	}

	@Test
	public void testGrantedFieldsDifferThenEqualsReturnsFalse() throws Exception {
		assertFalse(new Record(record.getName()).equals(record));
	}
	
}
