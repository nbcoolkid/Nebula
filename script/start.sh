#!/bin/bash

#############################################
# Nebula Server - Start All Services Script
# This script starts all Spring Boot services
# in the server directory using Maven
#############################################

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Server directory (parent of script directory)
SERVER_DIR="$(cd "$SCRIPT_DIR/../server" && pwd)"

# Log directory
LOG_DIR="$SCRIPT_DIR/logs"

# Function to print colored messages
print_message() {
    local color=$1
    local message=$2
    echo -e "${color}${message}${NC}"
}

# Function to check if Maven is installed
check_maven() {
    if ! command -v mvn &> /dev/null; then
        print_message "$RED" "Error: Maven is not installed or not in PATH"
        exit 1
    fi
    print_message "$GREEN" "✓ Maven is installed"
}

# Function to check if port is in use
is_port_in_use() {
    local port=$1
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; then
        return 0
    else
        return 1
    fi
}

# Function to wait for service to start
wait_for_service() {
    local service_name=$1
    local port=$2
    local max_wait=60
    local count=0

    print_message "$YELLOW" "Waiting for $service_name to start (port: $port)..."

    while [ $count -lt $max_wait ]; do
        if is_port_in_use $port; then
            print_message "$GREEN" "✓ $service_name is now running on port $port"
            return 0
        fi
        sleep 1
        count=$((count + 1))
        echo -n "."
    done

    echo ""
    print_message "$RED" "✗ $service_name failed to start within ${max_wait}s"
    return 1
}

# Function to start a service
start_service() {
    local service_name=$1
    local module_name=$2
    local port=$3
    local log_file="$LOG_DIR/${module_name}.log"

    print_message "$BLUE" "----------------------------------------"
    print_message "$BLUE" "Starting $service_name"
    print_message "$BLUE" "----------------------------------------"

    # Check if service is already running
    if is_port_in_use $port; then
        print_message "$YELLOW" "○ $service_name is already running on port $port"
        return 0
    fi

    # Create log directory if it doesn't exist
    mkdir -p "$LOG_DIR"

    # Start the service
    print_message "$YELLOW" "Starting $service_name in background..."
    print_message "$YELLOW" "  Module: $module_name"
    print_message "$YELLOW" "  Port: $port"
    print_message "$YELLOW" "  Log: $log_file"

    cd "$SERVER_DIR/$module_name"
    nohup mvn spring-boot:run > "$log_file" 2>&1 &

    # Save PID for later use
    local pid=$!
    echo $pid > "$LOG_DIR/${module_name}.pid"

    print_message "$GREEN" "✓ $service_name started with PID: $pid"

    # Wait for service to be ready
    if wait_for_service "$service_name" $port; then
        return 0
    else
        print_message "$RED" "✗ Failed to start $service_name"
        print_message "$YELLOW" "Check log file: $log_file"
        return 1
    fi
}

# Main execution
echo ""
print_message "$GREEN" "========================================"
print_message "$GREEN"  "  Starting Nebula Server Services"
print_message "$GREEN"  "========================================"
echo ""

# Check prerequisites
print_message "$BLUE" "Checking prerequisites..."
check_maven
echo ""

# Check if server directory exists
if [ ! -d "$SERVER_DIR" ]; then
    print_message "$RED" "Error: Server directory not found: $SERVER_DIR"
    exit 1
fi

print_message "$GREEN" "✓ Server directory: $SERVER_DIR"
echo ""

# Start services
# Note: Common module is not a service, so we only start auth and gateway

# 1. Start Auth Service first
print_message "$YELLOW" "Starting services in order:"
print_message "$YELLOW" "  1. Auth Service (dependency for others)"
print_message "$YELLOW" "  2. Gateway Service (entry point)"
echo ""

start_service "Auth Service" "auth" 8081
if [ $? -ne 0 ]; then
    print_message "$RED" "Failed to start Auth Service. Aborting..."
    exit 1
fi
echo ""

# 2. Start Gateway Service
start_service "Gateway Service" "gateway" 8080
if [ $? -ne 0 ]; then
    print_message "$RED" "Failed to start Gateway Service"
    exit 1
fi
echo ""

# Summary
print_message "$GREEN" "========================================"
print_message "$GREEN"  "  All Services Started Successfully"
print_message "$GREEN"  "========================================"
echo ""
print_message "$BLUE" "Service Status:"
echo "  - Auth Service:  http://localhost:8081"
echo "  - Gateway:       http://localhost:8080"
echo ""
print_message "$BLUE" "API Endpoints:"
echo "  - Health (Auth):     http://localhost:8081/actuator/health"
echo "  - Health (Gateway):  http://localhost:8080/actuator/health"
echo "  - Hello (Direct):    http://localhost:8081/hello"
echo "  - Hello (Gateway):   http://localhost:8080/api/auth/hello"
echo ""
print_message "$YELLOW" "Log files: $LOG_DIR/"
print_message "$YELLOW" "To stop all services, run: ./stop.sh"
echo ""
