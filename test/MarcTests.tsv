*Setting*	*Value*	*Value*	*Value*
Library	OperatingSystem
Library	ppa.marc.robot.RecordAccessLibrary	marcFileReader	marcFileWriter

*Variable*	*Value* *Value*
${testDataDirectory}	test/data
${record}	${testDataDirectory}/record.marc
${twoRecords}	${testDataDirectory}/two_records.marc

*Test case*	*Value*	*Value*
Writes idential MARC record compared to what was read	[Tags]	regression
	Assert that writes identical record compares to what was read	${record}

Writes two idential MARC records compared to what was read	[Tags]	regression
	Assert that writes identical record compares to what was read	${twoRecords}

Prints out MARC record with complete information	[Tags]	regression
	Assert that toString prints complete information	${twoRecords}

Prints out two MARC records with complete information	[Tags]	regression
	Assert that toString prints complete information	${twoRecords}

*Keyword*	*Action*	*Argument*	*Argument*
Assert that writes identical record compares to what was read	[Arguments]	${filename}
	${expectedOutput}=	Get file	${filename}
	${actualOutput}=	Read record	${filename}
	Should be equal	${expectedOutput}	${actualOutput}	

Assert that toString prints complete information 	[Arguments]	${filename}
	${expectedOutput}=	Get file	${filename}.toString
	${actualOutput}=	Print record with toString	${filename}
	Should be equal	${expectedOutput}	${actualOutput}	
