@echo off
setlocal
rem ---------------------------------------------------------------
rem Wild Wind debug export: blocks / items / entities
rem Usage: debug_all.bat [output dir]     (default: build\debug)
rem ---------------------------------------------------------------

set "ROOT=%~dp0"
cd /d "%ROOT%"

set "OUTDIR=%~1"
if "%OUTDIR%"=="" set "OUTDIR=%ROOT%build\debug"

echo ================================================================
echo  Exporting Wild Wind block / item / entity attributes
echo  Output: %OUTDIR%
echo ================================================================
echo.

call "%ROOT%gradlew.bat" :barren:runDebugExport --console=plain "-PwildwindDebugOutput=%OUTDIR%"
set "EXITCODE=%ERRORLEVEL%"

echo.
if not "%EXITCODE%"=="0" (
    echo [FAILED] Gradle exited with code %EXITCODE%.
    echo          If you see "Could not create ZIP", a running Minecraft client or an IDE
    echo          build is holding the jars under modules\*\build\libs. Close it and retry.
    exit /b %EXITCODE%
)

if not exist "%OUTDIR%\debug_all.txt" (
    echo [FAILED] %OUTDIR%\debug_all.txt was not generated. Check the log above.
    exit /b 1
)

echo ================================================================
echo  Done. Files in %OUTDIR%
echo    blocks.txt     blocks only
echo    items.txt      items only
echo    entities.txt   entities only
echo    debug_all.txt  everything combined
echo ================================================================
echo.

findstr /c:"ww_core:" "%OUTDIR%\debug_all.txt" >nul 2>nul || echo [WARN] no ww_core content in the report.
findstr /c:"ww_wilderness:" "%OUTDIR%\debug_all.txt" >nul 2>nul || echo [WARN] no ww_wilderness content in the report.
findstr /c:"ww_barren:" "%OUTDIR%\debug_all.txt" >nul 2>nul || echo [WARN] no ww_barren content in the report.

echo Open %OUTDIR%\debug_all.txt with any text editor.
endlocal
exit /b 0