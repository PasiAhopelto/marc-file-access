package ppa.marc.reader;

public interface RecordSplitter {

	String extractDirectoryBlock(String recordAsString);
	String extractFieldBlock(String recordAsString);
	String extractRecordLabel(String string);
	
}
