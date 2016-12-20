@echo off
rem this is a hacky technique to set command result to a variable
for /f %%i in ('cd') do set CURRENT_DIR=%%i
echo %CURRENT_DIR%
start javaw -jar im-main-all.jar org.tanuneko.im.AppRunner "%CURRENT_DIR%"