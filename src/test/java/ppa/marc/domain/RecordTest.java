package ppa.marc.domain;

import org.meanbean.test.EqualsMethodTester;
import org.meanbean.test.HashCodeMethodTester;

import junit.framework.TestCase;

public class RecordTest extends TestCase {

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

	public void testEquals() {
		new EqualsMethodTester().testEqualsMethod(Record.class);
	}

	public void testHashcode() {
		new HashCodeMethodTester().testHashCodeMethod(Record.class);
	}
	
}
