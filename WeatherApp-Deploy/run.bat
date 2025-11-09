@echo off
cd /d "%~dp0"
java -cp "WeatherApp.jar;json-20240303.jar" weatherApp
if errorlevel 1 (
    echo Error: Java not found or application failed to start
    echo Please ensure Java 11+ is installed
)
pause