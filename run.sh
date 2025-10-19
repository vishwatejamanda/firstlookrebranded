#!/bin/bash

echo "======================================"
echo "  F1RSTLOOK Digital - Spring Boot"
echo "======================================"
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "✅ Java version: $(java -version 2>&1 | head -n 1)"
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven 3.6+"
    exit 1
fi

echo "✅ Maven version: $(mvn -version | head -n 1)"
echo ""

# Build the application
echo "🔨 Building application..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

echo ""
echo "✅ Build successful!"
echo ""
echo "🚀 Starting F1RSTLOOK Digital..."
echo ""
echo "📍 Access points:"
echo "   - Website: http://localhost:8080"
echo "   - Admin Dashboard: http://localhost:8080/admin/dashboard"
echo "   - H2 Console: http://localhost:8080/h2-console"
echo ""
echo "Press Ctrl+C to stop the server"
echo ""

# Run the application
java -jar target/firstlook-digital-1.0.0.jar
