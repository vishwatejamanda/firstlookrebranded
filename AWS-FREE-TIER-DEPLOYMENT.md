# ☁️ AWS Free Tier Deployment Guide (12 Months FREE)

## ✅ **What You Get FREE for 12 Months**

- ✅ **EC2 t2.micro** - 750 hours/month (1 instance 24/7)
- ✅ **RDS MySQL db.t3.micro** - 750 hours/month  
- ✅ **20 GB SSD storage**
- ✅ **15 GB data transfer/month**
- ✅ **Elastic Beanstalk** - No additional charge

**Total Cost: $0 for first 12 months** (if you stay within limits)

---

## 🚀 **Deployment Steps**

### **Prerequisites:**

1. **AWS Account** (new account gets 12 months free)
2. **AWS CLI installed** ✅ (already installed)
3. **EB CLI** (we'll install it)

---

## 📋 **Step 1: Configure AWS Credentials**

### **Get Your AWS Credentials:**

1. Go to [AWS Console](https://console.aws.amazon.com)
2. Click your name (top right) → **Security Credentials**
3. Scroll to **Access Keys**
4. Click **Create access key**
5. Select **Command Line Interface (CLI)**
6. Check "I understand" → **Create access key**
7. **Download** or copy:
   - Access Key ID
   - Secret Access Key

### **Configure AWS CLI:**

```bash
aws configure

# Enter when prompted:
AWS Access Key ID: YOUR_ACCESS_KEY_ID
AWS Secret Access Key: YOUR_SECRET_ACCESS_KEY
Default region name: us-east-1
Default output format: json
```

---

## 📋 **Step 2: Install Elastic Beanstalk CLI**

```bash
# Install EB CLI
pip install awsebcli --upgrade --user

# Verify installation
eb --version
```

---

## 📋 **Step 3: Initialize Elastic Beanstalk**

```bash
# Navigate to your project
cd /home/vishwatejamanda/Pictures/DOC-20251014-WA0006/firstlookver2-master

# Initialize EB
eb init

# Answer the prompts:
# 1. Select region: us-east-1 (or your preferred region)
# 2. Application name: firstlook-digital
# 3. Platform: Corretto 17 (Java 17)
# 4. Platform branch: Corretto 17 running on 64bit Amazon Linux 2023
# 5. CodeCommit: No
# 6. SSH: Yes (for debugging)
# 7. Keypair: Create new or select existing
```

---

## 📋 **Step 4: Create RDS MySQL Database (FREE TIER)**

### **Option A: Via EB CLI (Recommended)**

```bash
# Create environment with RDS
eb create firstlook-prod-env \
  --instance-type t2.micro \
  --database \
  --database.engine mysql \
  --database.instance db.t3.micro \
  --database.username admin \
  --database.password YourStrongPassword123 \
  --single

# This creates:
# - EC2 t2.micro instance (FREE)
# - RDS MySQL db.t3.micro (FREE)
# - Security groups
# - Load balancer (optional, costs extra)
```

### **Option B: Via AWS Console**

1. **Go to RDS Console**
2. **Create database**
3. **Choose:**
   - Engine: MySQL
   - Version: 8.0
   - Template: **Free tier**
   - DB instance: **db.t3.micro**
   - Storage: 20 GB (free tier limit)
   - Username: admin
   - Password: YourStrongPassword123
   - VPC: Same as EB environment
4. **Create database**
5. **Copy endpoint** (e.g., `firstlook-db.xxxxx.us-east-1.rds.amazonaws.com`)

---

## 📋 **Step 5: Set Environment Variables**

```bash
# Set database connection
eb setenv \
  RDS_DB_URL="jdbc:mysql://your-rds-endpoint:3306/ebdb" \
  RDS_USERNAME="admin" \
  RDS_PASSWORD="YourStrongPassword123" \
  SPRING_PROFILES_ACTIVE="prod"

# Optional: Set email configuration
eb setenv \
  MAIL_HOST="smtp.gmail.com" \
  MAIL_PORT="587" \
  MAIL_USERNAME="your-email@gmail.com" \
  MAIL_PASSWORD="your-app-password"
```

---

## 📋 **Step 6: Deploy Application**

```bash
# Build JAR
mvn clean package -DskipTests

# Deploy to EB
eb deploy

# Wait 5-10 minutes for deployment

# Open in browser
eb open
```

---

## 📋 **Step 7: Verify Deployment**

### **Check Application:**

```bash
# Get environment URL
eb status

# Check logs
eb logs

# SSH into instance (if needed)
eb ssh
```

### **Test Your App:**

```
Homepage: http://firstlook-prod-env.elasticbeanstalk.com
Admin: http://firstlook-prod-env.elasticbeanstalk.com/admin/login
```

---

## 🔧 **Configuration Files Created**

### **1. `.ebextensions/01-java.config`**
- Sets Java memory (512 MB)
- Sets Spring profile to `prod`
- Configures server port

### **2. `.ebextensions/02-cloudwatch.config`**
- Enables CloudWatch logs
- 7-day log retention

### **3. `application-prod.properties`**
- MySQL database configuration
- Production settings
- Environment variable support

---

## 💰 **Cost Monitoring**

### **Stay Within Free Tier:**

1. **Monitor usage:**
   ```bash
   # Check running instances
   aws ec2 describe-instances --query 'Reservations[].Instances[].[InstanceId,InstanceType,State.Name]'
   
   # Check RDS instances
   aws rds describe-db-instances --query 'DBInstances[].[DBInstanceIdentifier,DBInstanceClass,DBInstanceStatus]'
   ```

2. **Set up billing alerts:**
   - Go to AWS Billing Dashboard
   - Create budget alert for $1
   - Get email when approaching limit

### **Free Tier Limits:**

- ✅ **EC2:** 750 hours/month (1 t2.micro instance)
- ✅ **RDS:** 750 hours/month (1 db.t3.micro instance)
- ✅ **Storage:** 20 GB
- ✅ **Data transfer:** 15 GB/month out

### **What Costs Extra:**

- ❌ **Load Balancer:** ~$16/month (avoid for testing)
- ❌ **Elastic IP:** Free if attached, $3.60/month if not
- ❌ **Snapshots:** $0.05/GB/month
- ❌ **Data transfer:** Over 15 GB/month

---

## 🎯 **Recommended Setup for FREE Tier**

### **Use Single Instance (No Load Balancer):**

```bash
# Create environment without load balancer
eb create firstlook-prod-env \
  --instance-type t2.micro \
  --single \
  --database \
  --database.engine mysql \
  --database.instance db.t3.micro
```

This keeps you 100% within free tier!

---

## 📊 **Architecture**

### **Free Tier Setup:**

```
Internet
    ↓
EC2 t2.micro (FREE)
    ↓
RDS MySQL db.t3.micro (FREE)
```

**Cost: $0/month** (within free tier)

### **Production Setup (After 12 months):**

```
Internet
    ↓
Load Balancer ($16/mo)
    ↓
EC2 t2.small ($17/mo) × 2
    ↓
RDS MySQL db.t3.small ($25/mo)
```

**Cost: ~$75/month** (after free tier)

---

## 🔒 **Security Best Practices**

### **1. Change Admin Password:**

After deployment, change default admin password:
- Login: http://your-app.elasticbeanstalk.com/admin/login
- Username: admin
- Password: admin123 (CHANGE THIS!)

### **2. Configure Security Groups:**

```bash
# Allow only HTTPS (recommended for production)
# This is configured automatically by EB
```

### **3. Enable HTTPS:**

```bash
# Request SSL certificate (FREE with AWS Certificate Manager)
# Then configure in EB console
```

---

## 🔄 **Update & Maintenance**

### **Deploy Updates:**

```bash
# Make changes to code
git add .
git commit -m "Update feature"

# Build
mvn clean package -DskipTests

# Deploy
eb deploy
```

### **View Logs:**

```bash
# Stream logs
eb logs --stream

# Download logs
eb logs --all
```

### **Scale (if needed):**

```bash
# Scale up
eb scale 2

# Scale down
eb scale 1
```

---

## 🛑 **Terminate Environment (When Done Testing)**

### **To Stop Charges:**

```bash
# Terminate environment
eb terminate firstlook-prod-env

# This will:
# - Stop EC2 instance
# - Delete RDS database (backup first!)
# - Remove all resources
```

### **Backup Database First:**

```bash
# Create RDS snapshot before terminating
aws rds create-db-snapshot \
  --db-instance-identifier your-db-instance \
  --db-snapshot-identifier firstlook-backup-$(date +%Y%m%d)
```

---

## 📝 **Quick Command Reference**

```bash
# Initialize
eb init

# Create environment
eb create firstlook-prod-env --instance-type t2.micro --single --database

# Set environment variables
eb setenv KEY=VALUE

# Deploy
eb deploy

# Open in browser
eb open

# Check status
eb status

# View logs
eb logs

# SSH into instance
eb ssh

# Terminate
eb terminate firstlook-prod-env
```

---

## 🎉 **You're Ready to Deploy!**

### **Next Steps:**

1. ✅ **Configure AWS CLI** (Step 1)
2. ✅ **Install EB CLI** (Step 2)
3. ✅ **Initialize EB** (Step 3)
4. ✅ **Create environment with RDS** (Step 4)
5. ✅ **Deploy** (Step 6)
6. ✅ **Test** (Step 7)

### **Total Time:** 30-45 minutes
### **Total Cost:** $0 (free tier)

---

## 💡 **Tips**

- ✅ Use **single instance** to stay free
- ✅ Monitor usage in AWS Billing Dashboard
- ✅ Set up billing alerts
- ✅ Terminate when not in use (testing)
- ✅ Backup database before terminating

---

## 🆘 **Troubleshooting**

### **Issue: Deployment Failed**

```bash
# Check logs
eb logs

# Common issues:
# 1. Wrong Java version → Use Corretto 17
# 2. Database connection → Check RDS endpoint
# 3. Memory issues → Increase instance size
```

### **Issue: Can't Connect to Database**

```bash
# Check environment variables
eb printenv

# Verify RDS endpoint
aws rds describe-db-instances

# Check security groups
# RDS security group must allow EC2 security group
```

### **Issue: Application Won't Start**

```bash
# SSH into instance
eb ssh

# Check application logs
sudo tail -f /var/log/web.stdout.log

# Check Java process
ps aux | grep java
```

---

## 🎊 **Ready to Deploy to AWS FREE Tier!**

**Everything is configured and ready!**

**Start with Step 1: Configure AWS CLI** 🚀

---

© 2025 F1RSTLOOK Digital - AWS Free Tier Deployment Guide
