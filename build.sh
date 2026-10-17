#!/bin/bash
echo "========================================================"
echo "Building CTH TI Plus Trade Finance Profitability Engine"
echo "========================================================"

mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    echo ""
    echo "BUILD SUCCESSFUL! Package generated at target/trade-finance-profitability-1.0.0-SNAPSHOT.jar"
else
    echo ""
    echo "BUILD FAILED! Please check error output."
    exit 1
fi
