#!/bin/bash
echo "Stopping CTH TI Plus Trade Finance Profitability Engine..."
PID=$(lsof -t -i:8080 2>/dev/null)
if [ -n "$PID" ]; then
    kill -9 $PID
    echo "Process $PID terminated."
else
    echo "No running process found on port 8080."
fi
