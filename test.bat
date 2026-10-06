@echo off
setlocal
cd /d "%~dp0"
if not exist test-build mkdir test-build
javac --release 8 -encoding UTF-8 -d test-build src\*.java tests\*.java
if errorlevel 1 exit /b 1
java -cp test-build FlightSchedulerTests
if errorlevel 1 exit /b 1
