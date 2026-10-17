@echo off
echo Stopping CTH TI Plus Trade Finance Profitability Engine...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080') do taskkill /F /PID %%a 2>nul
echo Application stopped.
