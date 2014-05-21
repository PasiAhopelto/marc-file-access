package ppa.marc.reader;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.List;

import ppa.marc.reader.UnimarcRecordConverter;
import ppa.marc.domain.Field;
import ppa.marc.domain.Record;
import junit.framework.TestCase;

public class UnimarcRecordConverterTest extends TestCase {

	private static final String LABEL = "label";
	RecordSplitter recordSplitter = createStrictMock(RecordSplitter.class);
	DirectoryEntryParser directoryEntryParser = createStrictMock(DirectoryEntryParser.class);
	VariableDataSplitter variableDataSplitter = createStrictMock(VariableDataSplitter.class);
	FieldParser fieldParser = createStrictMock(FieldParser.class);
	IdentityExtractor identityExtractor = createStrictMock(IdentityExtractor.class);
	
	String recordAsString = new String();
	String[] splitRecord = new String[] {
			new String(),
			new String()
	};
	List<Integer> directoryEntries = new ArrayList<Integer>();
	List<String> fieldsAsStrings = new ArrayList<String>();
	int id = 0;
	String fieldAsString = new String();
	Field field = new Field(0);
	String recordId = new String();
	
	RecordConverter converter = new UnimarcRecordConverter(recordSplitter, directoryEntryParser, variableDataSplitter, fieldParser, identityExtractor);
	
	protected void setUp() throws Exception {
		directoryEntries.add(new Integer(0));
		directoryEntries.add(new Integer(1));
		fieldsAsStrings.add("");
		fieldsAsStrings.add("");
	}
	
	public void testHappyPath() throws Exception {
		happyPathExpectations();
		happyPathReplay();
		Record record = converter.convert(recordAsString);
		assertEquals(2, record.getFields().size());
		assertEquals(recordId, record.getName());
		assertEquals(LABEL, record.getLabel());
		happyPathVerify();
	}

	private void happyPathVerify() {
		verify(recordSplitter);
		verify(directoryEntryParser);
		verify(variableDataSplitter);
		verify(fieldParser);
		verify(identityExtractor);
	}

	private void happyPathReplay() {
		replay(recordSplitter);
		replay(directoryEntryParser);
		replay(variableDataSplitter);
		replay(fieldParser);
		replay(identityExtractor);
	}

	private void happyPathExpectations() {
		expect(recordSplitter.extractRecordLabel(recordAsString)).andReturn(LABEL);
		expect(recordSplitter.extractDirectoryBlock(recordAsString)).andReturn(splitRecord[0]);
		expect(recordSplitter.extractFieldBlock(recordAsString)).andReturn(splitRecord[1]);
		expect(directoryEntryParser.parseEntries(splitRecord[0])).andReturn(directoryEntries);
		expect(variableDataSplitter.split(directoryEntries, splitRecord[1])).andReturn(fieldsAsStrings);
		expect(fieldParser.parseField(directoryEntries.get(0).intValue(), fieldsAsStrings.get(0))).andReturn(field);
		expect(fieldParser.parseField(directoryEntries.get(1).intValue(), fieldsAsStrings.get(1))).andReturn(field);
		expect(identityExtractor.getIdentity(fieldsAsStrings)).andReturn(recordId);
	}
	
}
