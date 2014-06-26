package ppa.marc.domain;

import org.meanbean.test.EqualsMethodTester;

import junit.framework.TestCase;

public class FieldTest extends TestCase {

	private static final char SECOND_INDICATOR = '1';
	private static final char FIRST_INDICATOR = ' ';
	private static final int ID = 1;
	Field field = new Field(ID, FIRST_INDICATOR, SECOND_INDICATOR);
	SubField[] subFields = {
			new SubField('a', "data"),
			new SubField('b', "moredata")
	};
	
	public void testGrantedConstructedWithIndicatorsThenIsShownAsNonControlField() throws Exception {
		assertFalse(field.isControlField());
	}

	public void testGrantedConstructedWithoutIndicatorsThenIsShownAsControlField() throws Exception {
		assertTrue(new Field(ID).isControlField());
	}
	
	public void testIdentityCanBeGet() throws Exception {
		assertEquals(ID, field.getId());
	}
	
	public void testIndicatorCanBeGet() throws Exception {
		assertEquals(SECOND_INDICATOR, field.getSecondIndicator());
	}

	public void testSpaceIndicatorIsConvertedToUnderscore() throws Exception {
		assertEquals(' ', field.getFirstIndicator());
	}
	
	public void testReferenceToSubFieldListIsAccessible() throws Exception {
		SubField subField = new SubField('a', "value");
		field.addSubFields(subField);
		assertSame(subField, field.getSubFields().get(0));
	}

	public void testToString() throws Exception {
		field.addSubFields(subFields);
		assertEquals("001._1 " + subFields[0].toString() + "\n       " + subFields[1] + "\n", field.toString());
	}
	
	public void testToStringInCaseOfNoIndicators() throws Exception {
		Field field = new Field(ID);
		SubField subField = new SubField("data");
		field.addSubFields(subField);
		assertEquals("001    " + subField.toString() + "\n", field.toString());
	}
	
	public void testIdAndIndicatorsAreSettable() throws Exception {
		Field otherField = new Field(900, '2', '3');
		otherField.addSubFields(field.getSubFields().toArray(new SubField[0]));
		field.setId(otherField.getId());
		field.setFirstIndicator(otherField.getFirstIndicator());
		field.setSecondIndicator(otherField.getSecondIndicator());
		assertEquals(otherField, field);
	}
	
	public void testIndicatorsAreSetToUnassignedIfUnset() throws Exception {
		field = new Field(1);
		assertTrue(Character.UNASSIGNED == field.getFirstIndicator());
		assertTrue(Character.UNASSIGNED == field.getSecondIndicator());
	}

	public void testGrantedControlFieldStatusIsSetThenGetterReturnsCorrectStatus() throws Exception {
		field.setIsControlField(true);
		assertTrue(field.isControlField());
	}
	
	public void testEquals() {
		new EqualsMethodTester().testEqualsMethod(Field.class);
	}
	
}
