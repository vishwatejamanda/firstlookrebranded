#!/bin/bash

echo "📦 Creating AWS Elastic Beanstalk Deployment Package"
echo "===================================================="
echo ""

# Check if we're in the right directory
if [ ! -f "pom.xml" ]; then
    echo "❌ Error: pom.xml not found!"
    echo "Please run this script from the project root directory"
    exit 1
fi

# Create deployment ZIP
echo "Creating firstlook-docker.zip..."
zip -r firstlook-docker.zip \
    Dockerfile \
    Dockerrun.aws.json \
    pom.xml \
    src/ \
    .ebextensions/ \
    -x "*.git*" "target/*" "*.DS_Store"

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Deployment package created successfully!"
    echo ""
    echo "📦 File: firstlook-docker.zip"
    echo "📊 Size: $(du -h firstlook-docker.zip | cut -f1)"
    echo ""
    echo "🚀 Next steps:"
    echo "1. Go to AWS Elastic Beanstalk Console"
    echo "2. Create new application with Docker platform"
    echo "3. Upload firstlook-docker.zip"
    echo "4. Configure environment variables"
    echo ""
    echo "📚 See AWS-DOCKER-DEPLOYMENT.md for detailed instructions"
else
    echo ""
    echo "❌ Error creating deployment package"
    exit 1
fi
