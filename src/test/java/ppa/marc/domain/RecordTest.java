package ppa.marc.domain;

import junit.framework.TestCase;

public class RecordTest extends TestCase {

	private static final String LABEL = "label";
	Record record = new Record("identifier");
	Field[] fields = {
		new Field(10, 'a', 'b'),
		new Field(200, 'a', 'd')
	};

	protected void setUp() throws Exception {
		record.addFields(fields[0], fields[1]);
	}
	
	public void testIdentifierCanBeSet() throws Exception {
		assertEquals("identifier", record.getName());
	}
	
	public void testReferenceToFieldListIsAccessible() throws Exception {
		assertSame(fields[0], record.getFields().get(0));
	}
	
	public void testToString() throws Exception {
		assertEquals(fields[0].toString() + fields[1].toString(), record.toString());
	}

	public void testIdentifierIsSettable() throws Exception {
		record.setName("id");
		assertEquals("id", record.getName());
	}

	public void testGrantedLabelIsSetThenItIsReturnedByGetter() throws Exception {
		record.setLabel(LABEL);
		assertEquals(LABEL, record.getLabel());
	}

	public void testGrantedNamesDifferThenEqualsReturnsFalse() throws Exception {
		assertFalse(new Record("other").equals(record));
	}

	public void testGrantedNamesAreSameThenEqualsReturnsFalse() throws Exception {
		record.getFields().clear();
		assertTrue(new Record(record.getName()).equals(record));
	}

	public void testGrantedLabelsDifferThenEqualsReturnsFalse() throws Exception {
		record.getFields().clear();
		record.setLabel("other label");
		assertFalse(new Record(record.getName()).equals(record));
	}

	public void testGrantedFieldsDifferThenEqualsReturnsFalse() throws Exception {
		assertFalse(new Record(record.getName()).equals(record));
	}
	
}
