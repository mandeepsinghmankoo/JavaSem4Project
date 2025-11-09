@echo off
cd /d "%~dp0"
java -cp "WeatherApp.jar;json-20240303.jar" weatherApp
pause