#!/bin/bash

#############################################
# Nebula Server - Stop All Services Script
# This script stops all running Spring Boot
# services in the server directory
#############################################

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Server directory (parent of script directory)
SERVER_DIR="$(cd "$SCRIPT_DIR/../server" && pwd)"

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  Stopping Nebula Server Services${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# Function to check if a process is running
is_process_running() {
    local pid=$1
    if ps -p "$pid" > /dev/null 2>&1; then
        return 0
    else
        return 1
    fi
}

# Function to gracefully stop a service
graceful_stop() {
    local service_name=$1
    local port=$2
    local pid=$(lsof -ti:$port 2>/dev/null)

    if [ -n "$pid" ]; then
        echo -e "${YELLOW}Stopping ${service_name} (PID: $pid, Port: $port)...${NC}"
        kill "$pid" 2>/dev/null

        # Wait for process to stop (max 10 seconds)
        local count=0
        while is_process_running "$pid" && [ $count -lt 10 ]; do
            sleep 1
            count=$((count + 1))
        done

        # Force kill if still running
        if is_process_running "$pid"; then
            echo -e "${RED}Force killing ${service_name}...${NC}"
            kill -9 "$pid" 2>/dev/null
            sleep 1
        fi

        if ! is_process_running "$pid"; then
            echo -e "${GREEN}✓ ${service_name} stopped successfully${NC}"
        else
            echo -e "${RED}✗ Failed to stop ${service_name}${NC}"
        fi
    else
        echo -e "${YELLOW}○ ${service_name} is not running (port $port)${NC}"
    fi
}

# Stop services by port
echo "Stopping services by port..."
echo ""

# Stop Gateway Service (Port 8080)
graceful_stop "Gateway Service" 8080

# Stop Auth Service (Port 8081)
graceful_stop "Auth Service" 8081

echo ""
echo "Checking for remaining Spring Boot processes..."

# Kill any remaining Maven Spring Boot processes
MAVEN_PIDS=$(ps aux | grep -E 'spring-boot:run|maven.*spring-boot' | grep -v grep | awk '{print $2}')

if [ -n "$MAVEN_PIDS" ]; then
    echo -e "${YELLOW}Found remaining Maven processes, terminating...${NC}"
    for pid in $MAVEN_PIDS; do
        echo -e "${YELLOW}Killing Maven process (PID: $pid)${NC}"
        kill -9 "$pid" 2>/dev/null
    done
    echo -e "${GREEN}✓ All Maven processes terminated${NC}"
else
    echo -e "${GREEN}✓ No remaining Maven processes found${NC}"
fi

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  All services stopped${NC}"
echo -e "${GREEN}========================================${NC}"
