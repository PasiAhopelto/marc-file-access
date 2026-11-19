package ppa.marc.reader;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ppa.marc.domain.Record;

@Component
@RequiredArgsConstructor
public class UnimarcRecordConverter implements RecordConverter {

	private final RecordSplitter recordSplitter;
	private final DirectoryEntryParser directoryEntryParser;
	private final VariableDataSplitter variableDataSplitter;
	private final FieldParser fieldParser;
	private final IdentityExtractor identityExtractor;

	public Record convert(String input) {
		String recordLabel = recordSplitter.extractRecordLabel(input);
		List<Integer> directoryEntries = directoryEntryParser.parseEntries(recordSplitter.extractDirectoryBlock(input));
		List<String> fieldsAsStrings = variableDataSplitter.split(directoryEntries, recordSplitter.extractFieldBlock(input));
		Record record = new Record(identityExtractor.getIdentity(fieldsAsStrings));
		record.setLabel(recordLabel);
		for(int i = 0; i < directoryEntries.size(); ++i) {
			record.getFields().add(fieldParser.parseField(directoryEntries.get(i).intValue(), fieldsAsStrings.get(i)));
		}
		return record;
	}

}
