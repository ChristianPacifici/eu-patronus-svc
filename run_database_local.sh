#!/bin/bash

# Configuration
LOG_FILE="/tmp/postgres-docker.log"
LOG_LEVEL="INFO"

# Colors for console output (optional)
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Logging function
log() {
    local level="$1"
    local message="$2"
    local timestamp=$(date '+%Y-%m-%d %H:%M:%S')

    # Log to file
    echo "[$timestamp] [$level] $message" >> "$LOG_FILE"

    # Log to console based on level
    case "$level" in
        "ERROR")
            echo -e "${RED}[$timestamp] [$level] $message${NC}" >&2
            ;;
        "WARN")
            echo -e "${YELLOW}[$timestamp] [$level] $message${NC}" >&2
            ;;
        "INFO")
            echo -e "${GREEN}[$timestamp] [$level] $message${NC}"
            ;;
        "DEBUG")
            if [ "$LOG_LEVEL" = "DEBUG" ]; then
                echo "[$timestamp] [$level] $message"
            fi
            ;;
    esac
}

# Cleanup function
cleanup() {
    log "INFO" "Script execution completed."
    exit 0
}

# Trap signals for cleanup
trap cleanup EXIT
trap 'log "ERROR" "Script interrupted. Cleaning up..."; docker stop postgres &> /dev/null; docker rm postgres &> /dev/null; exit 1' INT TERM

# Main script starts here
log "INFO" "Starting PostgreSQL Docker container script."

# Check if Docker is installed
log "INFO" "Checking if Docker is installed..."
if ! command -v docker &> /dev/null; then
    log "ERROR" "Docker is not installed. Please install Docker and try again."
    exit 1
fi
log "INFO" "Docker is installed."

# Check if Docker daemon is running
log "INFO" "Checking if Docker daemon is running..."
if ! docker info &> /dev/null; then
    log "ERROR" "Docker daemon is not running. Please start Docker and try again."
    exit 1
fi
log "INFO" "Docker daemon is running."

# Check if the port 5432 is already in use
log "INFO" "Checking if port 5432 is available..."
if netstat -tuln | grep ":5432" &> /dev/null; then
    log "ERROR" "Port 5432 is already in use. Please free the port or use a different one."
    exit 1
fi
log "INFO" "Port 5432 is available."

# Run the Docker container
log "INFO" "Starting PostgreSQL container..."
CONTAINER_ID=$(docker run \
    --name postgres \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_PASSWORD=password \
    -e POSTGRES_DB=eu_patronus_db \
    -p 5432:5432 \
    postgres:14)

if [ $? -ne 0 ]; then
    log "ERROR" "Failed to start the PostgreSQL container. Docker output: $CONTAINER_ID"
    exit 1
fi
log "INFO" "PostgreSQL container started with ID: $CONTAINER_ID"

# Wait for the container to be healthy
log "INFO" "Waiting for PostgreSQL to be ready..."
timeout=60
count=0
while [ $count -lt $timeout ]; do
    if docker ps --filter "name=postgres" --filter "health=healthy" | grep postgres &> /dev/null; then
        log "INFO" "PostgreSQL container is healthy and running!"
        log "INFO" "Connection details: Host=localhost, Port=5432, User=postgres, Password=password, DB=eu_patronus_db"
        exit 0
    fi
    log "DEBUG" "Health check attempt $((count + 1))/$timeout - Container status: $(docker inspect --format='{{.State.Health.Status}}' postgres 2>/dev/null || echo 'unknown')"
    sleep 1
    ((count++))
done

log "ERROR" "PostgreSQL container did not become healthy within $timeout seconds."
docker stop postgres &> /dev/null
docker rm postgres &> /dev/null
log "INFO" "Cleaned up failed container."
exit 1