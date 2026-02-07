@echo off
chcp 65001 > nul
if not exist bin mkdir bin

echo Compiling sources...
REM Use PowerShell to list files without quotes and ASCII encoding to avoid BOM issues
powershell -Command "Get-ChildItem -Recurse -Filter *.java 'src' | ForEach-Object { $_.FullName } | Out-File -Encoding ASCII sources.txt"

javac -encoding UTF-8 -source 17 -target 17 -d bin -cp "lib/*" @sources.txt
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

echo Compilation successful!
del sources.txt
pause

