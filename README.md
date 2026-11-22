MARC File Access is a Java library for reading and writing bibliographics 
records stored as UNIMARC or MARC files.  

The software is developed by Pasi Ahopelto (pahopelt@gmail.com) and licensed 
under Apache License, Version 2.0.  See src/main/rescources/META-INF/LICENSE 
for details and copyright.

Please see wiki at https://github.com/PasiAhopelto/marc-file-access/wiki for 
installation and usage

Command line utility
--------------------

The build also produces a self-contained, executable jar,
`target/marc-file-access-<version>-cli.jar`, for converting between binary
MARC and the library's text format.  It takes the conversion direction and the
input file, and writes the result to standard output:

    java -jar marc-file-access-1.0-cli.jar to-text records.marc > records.txt
    java -jar marc-file-access-1.0-cli.jar to-marc records.txt > records.marc

The text format does not contain the record leader, so records converted from
text get a default leader (`nam  22  4500`) with the record length and base
address of data calculated.

Exit status is 0 on success, 1 if the file cannot be read or converted, and 2
for invalid arguments.
