package ppa.marc.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class SubFieldTest {

	private static final String VALUE = "value";
	private static final char ID = 'a';
	SubField subField = new SubField(ID, VALUE);

	@Test
	public void testIdentityCanBeGet() throws Exception {
		assertEquals(Character.valueOf(ID), subField.getId());
	}
	
	@Test
	public void testValueCanBeGet() throws Exception {
		assertEquals(VALUE, subField.getValue());
	}

	@Test
	public void testEquality() throws Exception {
		SubField other = new SubField('a', "value");
		assertEquals(other, subField);
	}

	@Test
	public void testHashCode() throws Exception {
		SubField other = new SubField('a', "value");
		assertEquals(other.hashCode(), subField.hashCode());
	}
	
	@Test
	public void testToString() throws Exception {
		assertEquals("|a data", new SubField('a', "data").toString());
		assertEquals("data", new SubField("data").toString());
	}

	@Test
	public void testFieldsAreSettable() throws Exception {
		subField.setId('x');
		subField.setValue("atad");
		assertEquals(new SubField('x', "atad"), subField);
	}

	@Test
	public void testIdIsSetToUnassignedIfUnset() throws Exception {
		assertTrue(Character.UNASSIGNED == new SubField("value").getId());
	}
	
}
