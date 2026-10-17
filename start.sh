#!/bin/bash
echo "========================================================"
echo "Starting CTH TI Plus Trade Finance Profitability Engine"
echo "Configured Memory Settings: Min 2GB (-Xms2g), Max 8GB (-Xmx8g)"
echo "========================================================"

# Kill any existing instance
kill $(lsof -t -i:8080) 2>/dev/null || true

nohup java -Xms2g -Xmx8g -XX:+UseG1GC -jar target/trade-finance-profitability-1.0.0-SNAPSHOT.jar > app.log 2>&1 &
PID=$!
echo "Application started in background. PID: $PID"
