package ppa.marc.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class FieldTest {

	private static final char SECOND_INDICATOR = '1';
	private static final char FIRST_INDICATOR = ' ';
	private static final int ID = 1;
	Field field = new Field(ID, FIRST_INDICATOR, SECOND_INDICATOR);
	SubField[] subFields = {
			new SubField('a', "data"),
			new SubField('b', "moredata")
	};
	
	@Test
	
	public void testGrantedConstructedWithIndicatorsThenIsShownAsNonControlField() throws Exception {
		assertFalse(field.isControlField());
	}

	@Test

	public void testGrantedConstructedWithoutIndicatorsThenIsShownAsControlField() throws Exception {
		assertTrue(new Field(ID).isControlField());
	}
	
	@Test
	
	public void testIdentityCanBeGet() throws Exception {
		assertEquals(ID, field.getId());
	}
	
	@Test
	
	public void testIndicatorCanBeGet() throws Exception {
		assertEquals(SECOND_INDICATOR, field.getSecondIndicator());
	}

	@Test

	public void testSpaceIndicatorIsConvertedToUnderscore() throws Exception {
		assertEquals(' ', field.getFirstIndicator());
	}
	
	@Test
	
	public void testReferenceToSubFieldListIsAccessible() throws Exception {
		SubField subField = new SubField('a', "value");
		field.getSubFields().add(subField);
		assertSame(subField, field.getSubFields().get(0));
	}

	@Test

	public void testEquality() throws Exception {
		assertEquals(new Field(100, '0', '1'), new Field(100, '0', '1'));
	}

	@Test

	public void testEqualityWithoutIndicators() throws Exception {
		assertEquals(new Field(100), new Field(100));
	}

	@Test

	public void testToString() throws Exception {
		field.getSubFields().add(subFields[0]);
		field.getSubFields().add(subFields[1]);
		assertEquals("001._1 " + subFields[0].toString() + "\n       " + subFields[1] + "\n", field.toString());
	}
	
	@Test
	
	public void testToStringInCaseOfNoIndicators() throws Exception {
		Field field = new Field(ID);
		SubField subField = new SubField("data");
		field.getSubFields().add(subField);
		assertEquals("001    " + subField.toString() + "\n", field.toString());
	}
	
	@Test
	
	public void testIdAndIndicatorsAreSettable() throws Exception {
		Field otherField = new Field(900, '2', '3');
		otherField.getSubFields().addAll(field.getSubFields());
		field.setId(otherField.getId());
		field.setFirstIndicator(otherField.getFirstIndicator());
		field.setSecondIndicator(otherField.getSecondIndicator());
		assertEquals(otherField, field);
	}
	
	@Test
	
	public void testIndicatorsAreSetToUnassignedIfUnset() throws Exception {
		field = new Field(1);
		assertTrue(Character.UNASSIGNED == field.getFirstIndicator());
		assertTrue(Character.UNASSIGNED == field.getSecondIndicator());
	}

	@Test

	public void testGrantedControlFieldStatusIsSetThenGetterReturnsCorrectStatus() throws Exception {
		field.setIsControlField(true);
		assertTrue(field.isControlField);
	}
	
}
