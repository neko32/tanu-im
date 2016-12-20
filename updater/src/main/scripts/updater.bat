@echo off
rem this is a hacky technique to set command result to a variable
for /f %%i in ('cd') do set CURRENT_DIR=%%i
echo %CURRENT_DIR%
rem start javaw -jar updater-all.jar org.tanuneko.im.updater.Updater "%CURRENT_DIR%" "%1" "%2"
start javaw -jar updater-all.jar org.tanuneko.im.updater.Updater "%CURRENT_DIR%" "JAR_ONLY" "en"