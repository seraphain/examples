@echo off
setlocal enabledelayedexpansion

rem the directory of JAR files
set jar_dir=.\lib

rem get classpath
set classpath=.
for %%c in (%jar_dir%\*.jar) do set classpath=!classpath!;%%c

rem run Java application
java -cp "%classpath%" com.github.seraphain.examples.derby.DerbyExample

pause;
