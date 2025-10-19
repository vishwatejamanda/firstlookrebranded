# ☁️ AWS Deployment Guide - Complete

## 🎯 **AWS Deployment Options**

---

## **Option 1: AWS Elastic Beanstalk (RECOMMENDED)**

### **Why Elastic Beanstalk:**
- ✅ Easiest AWS deployment
- ✅ Auto-scaling built-in
- ✅ Load balancer included
- ✅ Free tier eligible
- ✅ Perfect for Spring Boot

### **Cost:**
- **Free tier:** 750 hours/month (t2.micro)
- **After free tier:** ~$15-25/month
- **Production:** ~$50-100/month (with RDS, load balancer)

---

## 🚀 **Elastic Beanstalk Deployment**

### **Prerequisites:**
```bash
# Install AWS CLI
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install

# Install EB CLI
pip install awsebcli

# Configure AWS credentials
aws configure
# Enter: Access Key ID, Secret Access Key, Region (us-east-1), Format (json)
```

### **Step 1: Prepare Application**

Create `.ebextensions/01-java.config`:
```yaml
option_settings:
  aws:elasticbeanstalk:container:java:javase:
    Xmx: 512m
    Xms: 256m
  aws:elasticbeanstalk:application:environment:
    SERVER_PORT: 5000
    SPRING_PROFILES_ACTIVE: prod
```

### **Step 2: Initialize Elastic Beanstalk**

```bash
# Navigate to project
cd /home/vishwatejamanda/Pictures/DOC-20251014-WA0006/firstlookver2-master

# Initialize EB
eb init

# Select:
# - Region: us-east-1 (or your preferred region)
# - Application name: firstlook-digital
# - Platform: Java 17 (Corretto 17)
# - SSH: Yes (for debugging)
```

### **Step 3: Create Environment**

```bash
# Create test environment (free tier)
eb create firstlook-test-env \
  --instance-type t2.micro \
  --single

# Or create production environment
eb create firstlook-prod-env \
  --instance-type t2.small \
  --elb-type application \
  --enable-spot
```

### **Step 4: Deploy**

```bash
# Build JAR
mvn clean package -DskipTests

# Deploy to EB
eb deploy

# Open in browser
eb open

# Check status
eb status

# View logs
eb logs
```

### **Step 5: Configure Domain (Optional)**

```bash
# Add custom domain
eb setenv DOMAIN=yourdomain.com

# Configure Route 53 or use EB domain
# EB provides: firstlook-prod-env.elasticbeanstalk.com
```

### **Step 6: Add Database (Production)**

```bash
# Create RDS MySQL database
aws rds create-db-instance \
  --db-instance-identifier firstlook-db \
  --db-instance-class db.t3.micro \
  --engine mysql \
  --master-username admin \
  --master-user-password YourStrongPassword123 \
  --allocated-storage 20

# Set environment variables
eb setenv \
  DB_URL=jdbc:mysql://your-rds-endpoint:3306/firstlook \
  DB_USER=admin \
  DB_PASS=YourStrongPassword123
```

---

## **Option 2: AWS EC2 (More Control)**

### **Why EC2:**
- ✅ Full control
- ✅ Flexible configuration
- ✅ Free tier eligible
- ✅ Learn AWS infrastructure

### **Cost:**
- **Free tier:** 750 hours/month (t2.micro)
- **After:** ~$10-15/month (t2.micro)
- **Production:** ~$30-50/month (t2.medium)

---

## 🚀 **EC2 Deployment**

### **Step 1: Launch EC2 Instance**

```bash
# Via AWS Console:
1. Go to EC2 Dashboard
2. Click "Launch Instance"
3. Choose:
   - Name: firstlook-server
   - AMI: Ubuntu Server 22.04 LTS
   - Instance type: t2.micro (free tier)
   - Key pair: Create new or use existing
   - Security group: Allow HTTP (80), HTTPS (443), SSH (22), Custom TCP (8080)
4. Click "Launch Instance"
```

### **Step 2: Connect to EC2**

```bash
# SSH into instance
ssh -i your-key.pem ubuntu@your-ec2-public-ip

# Update system
sudo apt update && sudo apt upgrade -y
```

### **Step 3: Install Java**

```bash
# Install Java 17
sudo apt install openjdk-17-jdk -y

# Verify
java -version
```

### **Step 4: Install MySQL (Optional)**

```bash
# Install MySQL
sudo apt install mysql-server -y

# Secure installation
sudo mysql_secure_installation

# Create database
sudo mysql
CREATE DATABASE firstlook;
CREATE USER 'firstlook_user'@'localhost' IDENTIFIED BY 'StrongPassword123';
GRANT ALL PRIVILEGES ON firstlook.* TO 'firstlook_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### **Step 5: Deploy Application**

```bash
# Create app directory
sudo mkdir -p /opt/firstlook
sudo chown ubuntu:ubuntu /opt/firstlook

# Upload JAR from local machine
scp -i your-key.pem target/firstlook-digital-0.0.1-SNAPSHOT.jar ubuntu@your-ec2-ip:/opt/firstlook/

# Or build on server
cd /opt/firstlook
git clone your-repo-url
cd firstlook-digital
mvn clean package -DskipTests
```

### **Step 6: Create Systemd Service**

```bash
# Create service file
sudo nano /etc/systemd/system/firstlook.service
```

Add:
```ini
[Unit]
Description=F1RSTLOOK Digital Application
After=syslog.target network.target

[Service]
User=ubuntu
WorkingDirectory=/opt/firstlook
ExecStart=/usr/bin/java -Xmx512m -jar /opt/firstlook/firstlook-digital-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

Environment="SPRING_PROFILES_ACTIVE=prod"
Environment="DB_URL=jdbc:mysql://localhost:3306/firstlook"
Environment="DB_USER=firstlook_user"
Environment="DB_PASS=StrongPassword123"

[Install]
WantedBy=multi-user.target
```

```bash
# Start service
sudo systemctl daemon-reload
sudo systemctl enable firstlook
sudo systemctl start firstlook

# Check status
sudo systemctl status firstlook

# View logs
sudo journalctl -u firstlook -f
```

### **Step 7: Install Nginx (Reverse Proxy)**

```bash
# Install Nginx
sudo apt install nginx -y

# Configure Nginx
sudo nano /etc/nginx/sites-available/firstlook
```

Add:
```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

```bash
# Enable site
sudo ln -s /etc/nginx/sites-available/firstlook /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

### **Step 8: Install SSL Certificate**

```bash
# Install Certbot
sudo apt install certbot python3-certbot-nginx -y

# Get certificate
sudo certbot --nginx -d your-domain.com -d www.your-domain.com

# Auto-renewal
sudo certbot renew --dry-run
```

---

## **Option 3: AWS Lightsail (Simplest)**

### **Why Lightsail:**
- ✅ Simplest AWS option
- ✅ Fixed pricing ($3.50/month)
- ✅ Includes everything
- ✅ Perfect for small apps

### **Cost:**
- **$3.50/month** - 512 MB RAM, 1 vCPU, 20 GB SSD
- **$5/month** - 1 GB RAM, 1 vCPU, 40 GB SSD
- **$10/month** - 2 GB RAM, 1 vCPU, 60 GB SSD

---

## 🚀 **Lightsail Deployment**

### **Step 1: Create Instance**

```bash
# Via AWS Console:
1. Go to Lightsail Dashboard
2. Click "Create Instance"
3. Choose:
   - Location: Your region
   - Platform: Linux/Unix
   - Blueprint: OS Only → Ubuntu 22.04
   - Plan: $5/month (1 GB RAM)
   - Name: firstlook-server
4. Click "Create Instance"
```

### **Step 2: Configure & Deploy**

Same as EC2 steps 2-8 above.

---

## 📊 **Cost Comparison**

| Service | Free Tier | After Free Tier | Production | Best For |
|---------|-----------|-----------------|------------|----------|
| **Elastic Beanstalk** | 750 hrs/mo | ~$15-25/mo | ~$50-100/mo | Recommended |
| **EC2 t2.micro** | 750 hrs/mo | ~$10-15/mo | ~$30-50/mo | Full control |
| **Lightsail** | No | $3.50-10/mo | $10-40/mo | Simplest |
| **ECS/Fargate** | No | ~$20-30/mo | ~$100+/mo | Containers |

---

## 🎯 **Recommended Architecture**

### **Testing/Small Scale:**
```
User → Elastic Beanstalk (t2.micro) → H2 Database
Cost: FREE (free tier) or ~$15/month
```

### **Production/Medium Scale:**
```
User → Application Load Balancer
    → Elastic Beanstalk (t2.small, auto-scaling)
    → RDS MySQL (db.t3.micro)
    → S3 (static files)
Cost: ~$50-100/month
```

### **High Traffic:**
```
User → CloudFront (CDN)
    → Application Load Balancer
    → Elastic Beanstalk (t2.medium, auto-scaling 2-10 instances)
    → RDS MySQL (db.t3.small, Multi-AZ)
    → ElastiCache (Redis)
    → S3 + CloudFront
Cost: ~$200-500/month
```

---

## 🔒 **Security Checklist**

### **Before Production:**
- [ ] Change default admin password
- [ ] Use environment variables for secrets
- [ ] Enable HTTPS/SSL
- [ ] Configure security groups (only allow 80, 443)
- [ ] Enable AWS WAF (Web Application Firewall)
- [ ] Set up CloudWatch alarms
- [ ] Enable RDS encryption
- [ ] Use IAM roles (not access keys)
- [ ] Enable VPC for database
- [ ] Regular backups configured

---

## 📊 **Monitoring & Logging**

### **CloudWatch:**
```bash
# Enable detailed monitoring
eb config

# Add to .ebextensions/cloudwatch.config:
option_settings:
  aws:elasticbeanstalk:cloudwatch:logs:
    StreamLogs: true
    DeleteOnTerminate: false
    RetentionInDays: 7
```

### **Set Up Alarms:**
```bash
# CPU alarm
aws cloudwatch put-metric-alarm \
  --alarm-name firstlook-high-cpu \
  --alarm-description "Alert when CPU exceeds 80%" \
  --metric-name CPUUtilization \
  --namespace AWS/EC2 \
  --statistic Average \
  --period 300 \
  --threshold 80 \
  --comparison-operator GreaterThanThreshold
```

---

## 💾 **Backup Strategy**

### **Database Backups:**
```bash
# RDS automatic backups (enabled by default)
# Retention: 7 days

# Manual snapshot
aws rds create-db-snapshot \
  --db-instance-identifier firstlook-db \
  --db-snapshot-identifier firstlook-backup-$(date +%Y%m%d)
```

### **Application Backups:**
```bash
# S3 backup
aws s3 sync /opt/firstlook s3://firstlook-backups/$(date +%Y%m%d)/
```

---

## 🚀 **Deployment Commands Summary**

### **Elastic Beanstalk:**
```bash
# Initialize
eb init -p java-17 firstlook-digital

# Create environment
eb create firstlook-prod-env --instance-type t2.small

# Deploy
mvn clean package -DskipTests
eb deploy

# Monitor
eb status
eb logs
eb health

# Update
git commit -am "Update"
eb deploy

# Terminate (when done testing)
eb terminate firstlook-test-env
```

### **EC2:**
```bash
# Connect
ssh -i key.pem ubuntu@ec2-ip

# Deploy
scp -i key.pem target/*.jar ubuntu@ec2-ip:/opt/firstlook/
ssh -i key.pem ubuntu@ec2-ip "sudo systemctl restart firstlook"

# Monitor
ssh -i key.pem ubuntu@ec2-ip "sudo journalctl -u firstlook -f"
```

---

## 🎯 **My Recommendation**

### **For Testing:**
1. **Start with Render.com** (FREE, easy)
2. Test all features
3. Get feedback

### **For AWS Testing:**
1. **Use Elastic Beanstalk** (FREE tier)
2. Deploy with t2.micro
3. Test AWS infrastructure

### **For Production:**
1. **Elastic Beanstalk** with:
   - t2.small or t2.medium
   - RDS MySQL database
   - Application Load Balancer
   - Auto-scaling (2-5 instances)
   - CloudWatch monitoring
   - S3 for static files

---

## 📝 **Quick Start**

```bash
# 1. Install tools
pip install awsebcli

# 2. Configure AWS
aws configure

# 3. Initialize
eb init -p java-17 firstlook-digital

# 4. Create test environment
eb create firstlook-test-env --instance-type t2.micro --single

# 5. Deploy
mvn clean package -DskipTests
eb deploy

# 6. Open
eb open

# 7. Monitor
eb logs --stream
```

---

## 🎉 **You're Ready for AWS!**

**Choose your path:**
- 🧪 **Testing:** Elastic Beanstalk (free tier)
- 🚀 **Production:** Elastic Beanstalk (t2.small + RDS)
- 💰 **Budget:** Lightsail ($3.50/month)
- 🎛️ **Control:** EC2 (manual setup)

---

© 2025 F1RSTLOOK Digital - AWS Deployment Guide
