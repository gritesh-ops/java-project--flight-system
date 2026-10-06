@echo off
setlocal
cd /d "%~dp0"
where javac >nul 2>nul
if errorlevel 1 (
    echo Install a JDK and add its bin folder to PATH.
    exit /b 1
)
if not exist build mkdir build
javac --release 8 -encoding UTF-8 -d build src\*.java
if errorlevel 1 (
    echo Compile failed. This script requires JDK 9 or later. See README for JDK 8.
    exit /b 1
)
jar cfe Airport_Flight_Board_Scheduler.jar AirportApp -C build .
if errorlevel 1 exit /b 1
echo Built Airport_Flight_Board_Scheduler.jar
