package ppa.marc.domain;

import junit.framework.TestCase;

public class SubFieldTest extends TestCase {

	private static final String VALUE = "value";
	private static final char ID = 'a';
	SubField subField = new SubField(ID, VALUE);

	public void testIdentityCanBeGet() throws Exception {
		assertEquals(Character.valueOf(ID), subField.getId());
	}
	
	public void testValueCanBeGet() throws Exception {
		assertEquals(VALUE, subField.getValue());
	}

	public void testEquality() throws Exception {
		SubField other = new SubField('a', "value");
		assertEquals(other, subField);
	}

	public void testHashCode() throws Exception {
		SubField other = new SubField('a', "value");
		assertEquals(other.hashCode(), subField.hashCode());
	}
	
	public void testToString() throws Exception {
		assertEquals("|a data", new SubField('a', "data").toString());
		assertEquals("data", new SubField("data").toString());
	}

	public void testFieldsAreSettable() throws Exception {
		subField.setId('x');
		subField.setValue("atad");
		assertEquals(new SubField('x', "atad"), subField);
	}

	public void testIdIsSetToUnassignedIfUnset() throws Exception {
		assertTrue(Character.UNASSIGNED == new SubField("value").getId());
	}
	
}
