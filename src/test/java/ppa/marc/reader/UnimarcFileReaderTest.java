package ppa.marc.reader;

import java.io.ByteArrayInputStream;
import java.util.List;

import ppa.marc.RecordReader;
import ppa.marc.common.UnimarcConstants;
import ppa.marc.domain.Record;
import ppa.marc.reader.UnimarcFileReader;

import junit.framework.TestCase;

import static org.easymock.EasyMock.*;

public class UnimarcFileReaderTest extends TestCase {

	RecordConverter recordConverter = createStrictMock(RecordConverter.class);
	Record[] records = new Record[] {
			new Record("id"),
			new Record("id")
	};
	RecordReader fileReader = new UnimarcFileReader(recordConverter);
	
	public void testReturnsEmptyListIfInputStreamIsEmpty() throws Exception {
		List<Record> recordsAsStrings = fileReader.read(new ByteArrayInputStream(new byte[0]));
		assertEquals(0, recordsAsStrings.size());
	}
	
	public void testReturnsTwoRecordsWithTrailingRecordTerminators() throws Exception {
		expectTwoRecords();
		List<Record> recordsAsStrings = fileReader.read(new ByteArrayInputStream(new byte[] { 'a', UnimarcConstants.RECORD_TERMINATOR, 'b', UnimarcConstants.RECORD_TERMINATOR }));
		assertTwoRecords(recordsAsStrings);
	}

	private void assertTwoRecords(List<Record> recordsAsStrings) {
		assertEquals(2, recordsAsStrings.size());
		assertEquals(records[0], recordsAsStrings.get(0));
		assertEquals(records[1], recordsAsStrings.get(1));
		verify(recordConverter);
	}

	private void expectTwoRecords() {
		expect(recordConverter.convert("a" + UnimarcConstants.RECORD_TERMINATOR)).andReturn(records[0]);
		expect(recordConverter.convert("b" + UnimarcConstants.RECORD_TERMINATOR)).andReturn(records[1]);
		replay(recordConverter);
	}

}
