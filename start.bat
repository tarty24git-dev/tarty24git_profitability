@echo off
echo ========================================================
echo Starting CTH TI Plus Trade Finance Profitability Engine
echo Configured Memory Settings: Min 2GB (-Xms2g), Max 8GB (-Xmx8g)
echo ========================================================

java -Xms2g -Xmx8g -XX:+UseG1GC -jar target/trade-finance-profitability-1.0.0-SNAPSHOT.jar
