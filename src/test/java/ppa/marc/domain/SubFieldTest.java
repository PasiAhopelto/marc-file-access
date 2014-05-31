package ppa.marc.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.meanbean.test.BeanTester;
import org.meanbean.test.EqualsMethodTester;

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
		new BeanTester().testBean(SubField.class);
	}

	public void testHashCode() throws Exception {
		new EqualsMethodTester().testEqualsMethod(SubField.class, new String[0]);
	}
	
	public void testToString() throws Exception {
		assertEquals("|a data", new SubField('a', "data").toString());
		assertEquals("data", new SubField("data").toString());
	}

	public void testFieldsAreSettable() throws Exception {
		setSubFieldValues('x', "atad");
		assertEquals(new SubField('x', "atad"), subField);
	}

	public void testIdIsSetToUnassignedIfUnset() throws Exception {
		assertTrue(Character.UNASSIGNED == new SubField("value").getId());
	}
	
	public void testIsSerializable() throws Exception {
		setSubFieldValues('a', "value");
		byte[] serialized = serializeSubfield();
		assertEquals(subField, (SubField) new ObjectInputStream(new ByteArrayInputStream(serialized)).readObject());
	}

	private byte[] serializeSubfield() throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
		objectOutputStream.writeObject(subField);
		objectOutputStream.close();
		return output.toByteArray();
	}

	private void setSubFieldValues(char identifier, String value) {
		subField.setId(identifier);
		subField.setValue(value);
	}
	
}
