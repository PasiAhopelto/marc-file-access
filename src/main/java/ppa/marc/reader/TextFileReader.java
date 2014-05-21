package ppa.marc.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ppa.marc.RecordReader;
import ppa.marc.domain.Record;

public class TextFileReader implements RecordReader {

	private final RecordConverter recordConverter;
	private final SpecialLineChecker commentLineChecker;

	public TextFileReader(RecordConverter recordConverter, SpecialLineChecker commentLineChecker) {
		this.recordConverter = recordConverter;
		this.commentLineChecker = commentLineChecker;
	}

	public List<Record> read(InputStream inputStream) throws IOException {
		return convertInputToRecords(new BufferedReader(new InputStreamReader(inputStream)));
	}

	private List<Record> convertInputToRecords(BufferedReader reader) throws IOException {
		List<Record> records = new ArrayList<Record>();
		StringBuffer inputAsString = new StringBuffer();
		String line = null;
		while((line = reader.readLine()) != null) {
			if(commentLineChecker.isRecordSeparator(line)) {
				addRecordIfThereWasInput(records, inputAsString);
			}
			else if(!commentLineChecker.isCommentLine(line)) {
				if(inputAsString.length() > 0) inputAsString.append('\n');
				inputAsString.append(line);
			}
		}
		addRecordIfThereWasInput(records, inputAsString);
		return records;
	}

	private void addRecordIfThereWasInput(List<Record> records, StringBuffer inputAsString) {
		if(inputAsString.length() > 0) {
			records.add(recordConverter.convert(inputAsString.toString()));
			inputAsString.delete(0, inputAsString.length());
		}
	}
}
