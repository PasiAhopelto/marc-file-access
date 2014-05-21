package ppa.marc.reader;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import ppa.marc.domain.Record;

@Named
@Singleton
public class UnimarcRecordConverter implements RecordConverter {

	private final RecordSplitter recordSplitter;
	private final DirectoryEntryParser directoryEntryParser;
	private final VariableDataSplitter variableDataSplitter;
	private final FieldParser fieldParser;
	private final IdentityExtractor identityExtractor;

	@Inject
	public UnimarcRecordConverter(RecordSplitter recordSplitter, DirectoryEntryParser directoryEntryParser, VariableDataSplitter variableDataSplitter, FieldParser fieldParser, IdentityExtractor identityExtractor) {
		this.recordSplitter = recordSplitter;
		this.directoryEntryParser = directoryEntryParser;
		this.variableDataSplitter = variableDataSplitter;
		this.fieldParser = fieldParser;
		this.identityExtractor = identityExtractor;
	}

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
