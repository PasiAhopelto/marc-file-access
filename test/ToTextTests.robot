*Setting*	*Value*	*Value*	*Value*
Library	OperatingSystem
Library	ppa.marc.robot.RecordAccessLibrary	marcFileReader	textFileWriter

*Variable*	*Value* *Value*
${testDataDirectory}	test/data
${record}	${testDataDirectory}/record.marc
${twoRecords}	${testDataDirectory}/two_records.marc
${utf8Record}	${testDataDirectory}/utf8_record.marc
${twoRecordsWithLineBreaks}	${testDataDirectory}/two_records_line_breaks.marc
${twoRecordsTruncated}	${testDataDirectory}/two_records_truncated.marc

*Test case*	*Value*	*Value*
Writes what was read as binary as a text record	[Tags]	regression
	Assert that writes identical record compares to what was read	${record}

Writes two records read as binary as a text record	[Tags]	regression
	Assert that writes identical record compares to what was read	${twoRecords}

Writes UTF-8 record read as binary as a text record	[Tags]	regression
	Assert that writes identical record compares to what was read	${utf8Record}

Writes two records read as binary with line breaks between records as text records	[Tags]	regression
	Assert that converts to text	${twoRecordsWithLineBreaks}	${twoRecords}.toString

Fails to read binary records when the last record is truncated	[Tags]	regression
	Run keyword and expect error	*Record 2 is not terminated*	Read record	${twoRecordsTruncated}

*Keyword*	*Action*	*Argument*	*Argument*
Assert that writes identical record compares to what was read	[Arguments]	${filename}
	${expectedOutput}=	Get file	${filename}.toString
	${actualOutput}=	Read record	${filename}
	Should be equal	${expectedOutput}	${actualOutput}	

Assert that converts to text	[Arguments]	${filename}	${expectedFilename}
	${expectedOutput}=	Get file	${expectedFilename}
	${actualOutput}=	Read record	${filename}
	Should be equal	${expectedOutput}	${actualOutput}
