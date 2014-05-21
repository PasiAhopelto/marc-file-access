package ppa.marc.reader;

import org.apache.commons.lang.StringUtils;

import ppa.marc.common.UnimarcConstants;

public class UnimarcRecordSplitter implements RecordSplitter {

	public String extractDirectoryBlock(String recordAsString) {
		return splitRecord(recordAsString)[0].substring(24);
	}

	public String extractFieldBlock(String recordAsString) {
		return extractVariableData(splitRecord(recordAsString));
	}

	public String extractRecordLabel(String recordAsString) {
		return splitRecord(recordAsString)[0].substring(0, 24);
	}
	
	private String[] splitRecord(String recordAsString) {
		return StringUtils.split(recordAsString, UnimarcConstants.FIELD_TERMINATOR);
	}

	private String extractVariableData(String[] fields) {
		StringBuffer variableData = new StringBuffer();
		for(int i = 1; i < fields.length; ++i) {
			if(isEndOfRecord(fields, i)) break;
			variableData.append(fields[i] + UnimarcConstants.FIELD_TERMINATOR);
		}
		return variableData.toString();
	}

	private boolean isEndOfRecord(String[] fields, int i) {
		return fields[i].indexOf(UnimarcConstants.RECORD_TERMINATOR) > -1;
	}

}
