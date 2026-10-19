@echo off
title EquipTrack Local Platform
cd /d "%~dp0"
cls
echo =======================================================
echo    EquipTrack - Commercial Equipment Fleet Platform
echo =======================================================
echo.
echo Launching local server on http://localhost:5050...
echo.

node server.js
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Node.js was not found or failed to start.
    echo Please ensure Node.js is installed to run the local server.
    pause
)
