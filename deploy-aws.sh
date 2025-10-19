#!/bin/bash

echo "🚀 AWS Elastic Beanstalk Deployment Script"
echo "=========================================="
echo ""

# Check if AWS CLI is configured
if ! aws sts get-caller-identity &> /dev/null; then
    echo "❌ AWS CLI not configured!"
    echo "Run: aws configure"
    exit 1
fi

echo "✅ AWS CLI configured"
echo ""

# Check if EB CLI is installed
if ! command -v eb &> /dev/null; then
    echo "📦 Installing Elastic Beanstalk CLI..."
    pip install awsebcli --upgrade --user
fi

echo "✅ EB CLI installed"
echo ""

# Build application
echo "📦 Building application..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

echo "✅ Build successful"
echo ""

# Check if EB is initialized
if [ ! -d ".elasticbeanstalk" ]; then
    echo "🔧 Initializing Elastic Beanstalk..."
    echo ""
    echo "Please answer the following questions:"
    echo "1. Select region: us-east-1 (recommended for free tier)"
    echo "2. Application name: firstlook-digital"
    echo "3. Platform: Corretto 17"
    echo "4. SSH: Yes"
    echo ""
    eb init
else
    echo "✅ EB already initialized"
fi

echo ""
echo "🎯 Ready to deploy!"
echo ""
echo "Next steps:"
echo "1. Create environment: eb create firstlook-prod-env --instance-type t2.micro --single --database"
echo "2. Set environment variables: eb setenv RDS_DB_URL=... RDS_USERNAME=... RDS_PASSWORD=..."
echo "3. Deploy: eb deploy"
echo "4. Open: eb open"
echo ""
echo "Or run these commands manually (see AWS-FREE-TIER-DEPLOYMENT.md)"
