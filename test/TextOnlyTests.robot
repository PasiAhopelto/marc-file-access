*Setting*	*Value*	*Value*	*Value*
Library	OperatingSystem
Library	ppa.marc.robot.RecordAccessLibrary	textFileReader	textFileWriter

*Variable*	*Value* *Value*
${testDataDirectory}	test/data
${record}	${testDataDirectory}/record.marc
${two_records}	${testDataDirectory}/two_records.marc

*Test case*	*Value*	*Value*
Writes what was read as text as a text record	
	[Tags]	regression
	Assert that writes identical record compared to what was read	${record}

Writes two identical text records when compared to what was read as text record
	[Tags]	regression
	Assert that writes identical record compared to what was read	${two_records}

*Keyword*	*Action*	*Argument*	*Argument*
Assert that writes identical record compared to what was read	[Arguments]	${filename}
	${expectedOutput}=	Get file	${filename}.toString
	${actualOutput}=	Read record	${filename}.toString
	Should be equal	${expectedOutput}	${actualOutput}	
