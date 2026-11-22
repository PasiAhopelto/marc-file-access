*Setting*	*Value*	*Value*	*Value*
Library	OperatingSystem
Library	ppa.marc.robot.RecordAccessLibrary	marcFileReader	textFileWriter

*Variable*	*Value* *Value*
${testDataDirectory}	test/data
${record}	${testDataDirectory}/record.marc
${twoRecords}	${testDataDirectory}/two_records.marc
${utf8Record}	${testDataDirectory}/utf8_record.marc

*Test case*	*Value*	*Value*
Writes what was read as binary as a text record	[Tags]	regression
	Assert that writes identical record compares to what was read	${record}

Writes two records read as binary as a text record	[Tags]	regression
	Assert that writes identical record compares to what was read	${twoRecords}

Writes UTF-8 record read as binary as a text record	[Tags]	regression
	Assert that writes identical record compares to what was read	${utf8Record}

*Keyword*	*Action*	*Argument*	*Argument*
Assert that writes identical record compares to what was read	[Arguments]	${filename}
	${expectedOutput}=	Get file	${filename}.toString
	${actualOutput}=	Read record	${filename}
	Should be equal	${expectedOutput}	${actualOutput}	
