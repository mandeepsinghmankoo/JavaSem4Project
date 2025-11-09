@echo off
echo Creating native executable...
jpackage --input . --name "Weather App" --main-jar WeatherApp-standalone.jar --main-class weatherApp --type exe --dest dist
echo Done! Check the 'dist' folder for Weather App.exe