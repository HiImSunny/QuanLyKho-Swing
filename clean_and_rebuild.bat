@echo off
echo ========================================
echo Clean VSCode Java Workspace
echo ========================================
echo.

echo [1/3] Deleting bin folder...
if exist bin (
    rmdir /s /q bin
    echo Done!
) else (
    echo Bin folder not found, skipping...
)

echo.
echo [2/3] Deleting VSCode Java cache...
if exist .vscode\.factorypath (
    del /q .vscode\.factorypath
    echo Done!
) else (
    echo No cache found, skipping...
)

echo.
echo [3/3] Recompiling project...
call compile.bat

echo.
echo ========================================
echo Clean completed! 
echo Now restart VSCode or run "Java: Clean Java Language Server Workspace"
echo ========================================
pause
