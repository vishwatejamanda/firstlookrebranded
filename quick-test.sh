#!/bin/bash

echo "🧪 F1RSTLOOK Digital - Quick Test Suite"
echo "========================================"
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test counter
PASSED=0
FAILED=0

# Function to test
test_feature() {
    local name=$1
    local command=$2
    local expected=$3
    
    echo -n "Testing $name... "
    
    result=$(eval $command 2>&1)
    
    if echo "$result" | grep -q "$expected"; then
        echo -e "${GREEN}✅ PASSED${NC}"
        ((PASSED++))
    else
        echo -e "${RED}❌ FAILED${NC}"
        echo "  Expected: $expected"
        echo "  Got: $result"
        ((FAILED++))
    fi
}

echo "1️⃣  Building Application..."
mvn clean package -DskipTests > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Build successful${NC}"
    ((PASSED++))
else
    echo -e "${RED}❌ Build failed${NC}"
    ((FAILED++))
    exit 1
fi
echo ""

echo "2️⃣  Starting Application..."
java -jar target/firstlook-digital-0.0.1-SNAPSHOT.jar > /dev/null 2>&1 &
APP_PID=$!
echo "   PID: $APP_PID"
echo "   Waiting 15 seconds for startup..."
sleep 15
echo ""

echo "3️⃣  Testing Endpoints..."
echo ""

# Test homepage
test_feature "Homepage" \
    "curl -s http://localhost:8080" \
    "F1RSTLOOK"

# Test admin login page
test_feature "Admin Login Page" \
    "curl -s http://localhost:8080/admin/login" \
    "login"

# Test contact form API
test_feature "Contact Form API" \
    "curl -s -X POST http://localhost:8080/api/contact/submit -H 'Content-Type: application/json' -d '{\"name\":\"Test\",\"email\":\"test@test.com\",\"mobile\":\"9876543210\",\"subject\":\"Test\",\"message\":\"Test\"}'" \
    "success"

# Test WhatsApp tracking
test_feature "WhatsApp Tracking API" \
    "curl -s -X POST http://localhost:8080/api/whatsapp/track" \
    "success"

# Test form analytics
test_feature "Form Analytics API" \
    "curl -s -X POST http://localhost:8080/api/form-analytics/start" \
    "sessionId"

echo ""
echo "4️⃣  Testing Database..."
echo ""

# Check if data directory exists
if [ -d "data" ]; then
    echo -e "${GREEN}✅ Database directory exists${NC}"
    ((PASSED++))
else
    echo -e "${RED}❌ Database directory missing${NC}"
    ((FAILED++))
fi

# Check if database file exists
if [ -f "data/firstlook.mv.db" ]; then
    echo -e "${GREEN}✅ Database file exists${NC}"
    ((PASSED++))
else
    echo -e "${YELLOW}⚠️  Database file not found (may be first run)${NC}"
fi

echo ""
echo "5️⃣  Stopping Application..."
kill $APP_PID
wait $APP_PID 2>/dev/null
echo -e "${GREEN}✅ Application stopped${NC}"
echo ""

echo "========================================"
echo "📊 Test Results:"
echo "   Passed: $PASSED"
echo "   Failed: $FAILED"
echo ""

if [ $FAILED -eq 0 ]; then
    echo -e "${GREEN}🎉 All tests passed! Ready for deployment!${NC}"
    exit 0
else
    echo -e "${RED}❌ Some tests failed. Please review.${NC}"
    exit 1
fi
