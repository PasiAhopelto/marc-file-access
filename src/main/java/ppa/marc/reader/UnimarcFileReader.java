package ppa.marc.reader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ppa.marc.RecordReader;
import ppa.marc.common.UnimarcConstants;
import ppa.marc.domain.Record;

@Component("marcFileReader")
@RequiredArgsConstructor
public class UnimarcFileReader implements RecordReader {

	private final UnimarcRecordConverter recordConverter;

	public List<Record> read(InputStream input) throws IOException {
		List<Record> records = new ArrayList<Record>();
		ByteArrayOutputStream content = new ByteArrayOutputStream();
		int nextByte;
		while((nextByte = input.read()) > -1) {
			if(content.size() == 0 && isLineBreak(nextByte)) continue;  // Records are sometimes written one per line.
			content.write(nextByte);
			if(nextByte == UnimarcConstants.RECORD_TERMINATOR) {
				records.add(recordConverter.convert(content.toString(UnimarcConstants.ENCODING)));
				content.reset();
			}
		}
		if(content.size() > 0) {
			throw new IllegalArgumentException("Record " + (records.size() + 1) + " is not terminated: " + content.size() + " bytes after the last complete record.");
		}
		return records;
	}

	private boolean isLineBreak(int nextByte) {
		return nextByte == '\n' || nextByte == '\r';
	}
}
