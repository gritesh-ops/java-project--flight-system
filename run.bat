@echo off
setlocal
cd /d "%~dp0"
java -jar Airport_Flight_Board_Scheduler.jar
if errorlevel 1 echo Java failed. Install Java 8 or later and check PATH.
pause
