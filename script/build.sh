#!/bin/bash

################################################################################
# Nebula Server Docker Build Script
################################################################################

set -e  # Exit immediately if a command exits with a non-zero status

# Color output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 本次构建的镜像版本
IMAGE_VERSION=2

# Project root directory
# 脚本所在路径
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# 项目根路径，SCRIPT_DIR的上级路径
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
# server后台项目路径
SERVER_DIR="$PROJECT_ROOT/server"
# ui前端项目路径
UI_DIR="$PROJECT_ROOT/ui"
# dockerfile脚本路径
DOCKERFILE_DIR="$SCRIPT_DIR/dockerfile"

# Registry and version configuration
REGISTRY="${DOCKER_REGISTRY:-registry.cn-hangzhou.aliyuncs.com/sherry}"
VERSION="${IMAGE_VERSION:-latest}"

# Services to build
BACKEND_SERVICES=("auth" "gateway")

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  Nebula Server Docker Build Script${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

################################################################################
# Step 1: Build Maven project
################################################################################
echo -e "${YELLOW}[Step 1/4] Building Maven project...${NC}"
cd "$SERVER_DIR"
mvn clean package -DskipTests -Dmaven.test.skip=true

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Maven build successful${NC}"
else
    echo -e "${RED}✗ Maven build failed${NC}"
    exit 1
fi

echo ""

################################################################################
# Step 2: Build UI project
################################################################################
echo -e "${YELLOW}[Step 2/4] Building UI project...${NC}"

# Check if pnpm is installed
if ! command -v pnpm &> /dev/null; then
    echo -e "${RED}✗ pnpm is not installed${NC}"
    exit 1
fi

cd "$UI_DIR"
echo -e "${YELLOW}Installing dependencies with pnpm...${NC}"
pnpm install

if [ $? -ne 0 ]; then
    echo -e "${RED}✗ pnpm install failed${NC}"
    exit 1
fi

echo -e "${YELLOW}Building UI with pnpm...${NC}"
pnpm build

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ UI build successful${NC}"
else
    echo -e "${RED}✗ UI build failed${NC}"
    exit 1
fi

echo ""

################################################################################
# Step 3: Build Docker images for backend services
################################################################################
echo -e "${YELLOW}[Step 3/4] Building Docker images for backend services...${NC}"

for SERVICE in "${BACKEND_SERVICES[@]}"; do
    echo -e "${YELLOW}Building image for $SERVICE...${NC}"

    SERVICE_DIR="$SERVER_DIR/$SERVICE"
    DOCKERFILE="$DOCKERFILE_DIR/Dockerfile-$SERVICE"
    IMAGE_NAME="$REGISTRY/nebula-$SERVICE:$VERSION"

    # Check if Dockerfile exists
    if [ ! -f "$DOCKERFILE" ]; then
        echo -e "${RED}✗ Dockerfile not found: $DOCKERFILE${NC}"
        exit 1
    fi

    # Build Docker image
    docker build \
        -f "$DOCKERFILE" \
        -t "$IMAGE_NAME" \
        "$SERVICE_DIR"

    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Built $IMAGE_NAME${NC}"
    else
        echo -e "${RED}✗ Failed to build $SERVICE${NC}"
        exit 1
    fi

    # Tag as latest if VERSION is not 'latest'
    if [ "$VERSION" != "latest" ]; then
        docker tag "$IMAGE_NAME" "$REGISTRY/nebula-$SERVICE:latest"
        echo -e "${GREEN}  Tagged as $REGISTRY/nebula-$SERVICE:latest${NC}"
    fi

    echo ""
done

################################################################################
# Step 4: Build Docker image for UI service
################################################################################
echo -e "${YELLOW}[Step 4/4] Building Docker image for UI service...${NC}"

UI_DOCKERFILE="$DOCKERFILE_DIR/Dockerfile-ui"
UI_IMAGE_NAME="$REGISTRY/nebula-ui:$VERSION"

# Check if Dockerfile exists
if [ ! -f "$UI_DOCKERFILE" ]; then
    echo -e "${RED}✗ Dockerfile not found: $UI_DOCKERFILE${NC}"
    exit 1
fi

# Build Docker image
docker build \
    -f "$UI_DOCKERFILE" \
    -t "$UI_IMAGE_NAME" \
    "$UI_DIR"

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Built $UI_IMAGE_NAME${NC}"
else
    echo -e "${RED}✗ Failed to build UI${NC}"
    exit 1
fi

# Tag as latest if VERSION is not 'latest'
if [ "$VERSION" != "latest" ]; then
    docker tag "$UI_IMAGE_NAME" "$REGISTRY/nebula-ui:latest"
    echo -e "${GREEN}  Tagged as $REGISTRY/nebula-ui:latest${NC}"
fi

echo ""

################################################################################
# Step 5: Display build summary
################################################################################
echo -e "${YELLOW}[Step 5/5] Build Summary${NC}"
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Successfully built images:${NC}"
docker images | grep "nebula-" | grep "$VERSION" | awk '{print "  - " $1 ":" $2}'
echo -e "${GREEN}========================================${NC}"
echo ""

echo -e "${GREEN}✓ All builds completed successfully!${NC}"
echo ""
echo -e "${YELLOW}You can now run the services using:${NC}"
for SERVICE in "${BACKEND_SERVICES[@]}"; do
    echo -e "  docker run -d -p 8080:8080 --name nebula-$SERVICE $REGISTRY/nebula-$SERVICE:$VERSION"
done
echo -e "  docker run -d -p 3000:8080 --name nebula-ui $REGISTRY/nebula-ui:$VERSION"
echo ""
