REM wymagane: 
REM - http://www.graphviz.org/Download_windows.php  
REM - http://schemaspy.sourceforge.net/

java -jar schemaSpy_5.0.0.jar -t pgsql -db IO -host 127.0.0.1 -u user -p password -o ./schema -dp postgresql-9.4-1201-jdbc41.jar -s public -noads -gv "C:\Program Files (x86)\Graphviz2.38"