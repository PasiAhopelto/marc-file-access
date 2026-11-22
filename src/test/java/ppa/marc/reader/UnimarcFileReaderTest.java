package ppa.marc.reader;

import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

import ppa.marc.RecordReader;
import ppa.marc.common.UnimarcConstants;
import ppa.marc.domain.Record;
import ppa.marc.reader.UnimarcFileReader;


import static org.easymock.EasyMock.*;

public class UnimarcFileReaderTest {

	UnimarcRecordConverter recordConverter = createStrictMock(UnimarcRecordConverter.class);
	Record[] records = new Record[] {
			new Record("id"),
			new Record("id")
	};
	RecordReader fileReader = new UnimarcFileReader(recordConverter);
	
	@Test
	public void testReturnsEmptyListIfInputStreamIsEmpty() throws Exception {
		List<Record> recordsAsStrings = fileReader.read(new ByteArrayInputStream(new byte[0]));
		assertEquals(0, recordsAsStrings.size());
	}
	
	@Test
	public void testReturnsTwoRecordsWithTrailingRecordTerminators() throws Exception {
		expectTwoRecords();
		List<Record> recordsAsStrings = fileReader.read(new ByteArrayInputStream(new byte[] { 'a', UnimarcConstants.RECORD_TERMINATOR, 'b', UnimarcConstants.RECORD_TERMINATOR }));
		assertTwoRecords(recordsAsStrings);
	}

	@Test
	public void testSkipsLineBreaksBetweenRecords() throws Exception {
		expectTwoRecords();
		List<Record> recordsAsStrings = fileReader.read(new ByteArrayInputStream(new byte[] { '\n', 'a', UnimarcConstants.RECORD_TERMINATOR, '\r', '\n', 'b', UnimarcConstants.RECORD_TERMINATOR, '\n' }));
		assertTwoRecords(recordsAsStrings);
	}

	@Test
	public void testKeepsLineBreaksInsideRecord() throws Exception {
		expect(recordConverter.convert("a\nb" + UnimarcConstants.RECORD_TERMINATOR)).andReturn(records[0]);
		replay(recordConverter);
		assertEquals(List.of(records[0]), fileReader.read(new ByteArrayInputStream(("a\nb" + UnimarcConstants.RECORD_TERMINATOR).getBytes(StandardCharsets.UTF_8))));
		verify(recordConverter);
	}

	@Test
	public void testGrantedLastRecordIsNotTerminatedThenThrowsException() throws Exception {
		expect(recordConverter.convert("a" + UnimarcConstants.RECORD_TERMINATOR)).andReturn(records[0]);
		replay(recordConverter);
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> fileReader.read(new ByteArrayInputStream(new byte[] { 'a', UnimarcConstants.RECORD_TERMINATOR, 'b', 'c' })));
		assertEquals("Record 2 is not terminated: 2 bytes after the last complete record.", exception.getMessage());
		verify(recordConverter);
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

	@Test
	public void testDecodesRecordAsUtf8() throws Exception {
		String recordAsString = "Mäkinen €🎉" + UnimarcConstants.RECORD_TERMINATOR;
		expect(recordConverter.convert(recordAsString)).andReturn(records[0]);
		replay(recordConverter);
		assertEquals(List.of(records[0]), fileReader.read(new ByteArrayInputStream(recordAsString.getBytes(StandardCharsets.UTF_8))));
		verify(recordConverter);
	}
}
