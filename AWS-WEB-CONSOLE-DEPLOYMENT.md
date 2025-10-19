# 🌐 AWS Web Console Deployment (No CLI Required)

## ✅ **Deploy Using AWS Website Only**

**No command line needed!** Everything through the AWS web interface.

---

## 💰 **What You Get FREE (12 Months)**

- ✅ **EC2 t2.micro** - 750 hours/month
- ✅ **RDS MySQL db.t3.micro** - 750 hours/month
- ✅ **20 GB storage**
- ✅ **15 GB data transfer/month**

**Total Cost: $0 for 12 months**

---

## 📋 **Prerequisites**

1. ✅ **AWS Account** - [Sign up here](https://aws.amazon.com/free/)
2. ✅ **JAR file** - Already built in `target/` folder
3. ✅ **10-15 minutes** of your time

---

## 🚀 **Step-by-Step Deployment**

---

## **STEP 1: Build Your Application**

### **On Your Computer:**

```bash
cd /home/vishwatejamanda/Pictures/DOC-20251014-WA0006/firstlookver2-master
mvn clean package -DskipTests
```

This creates: `target/firstlook-digital-1.0.0.jar`

✅ **JAR file is ready!**

---

## **STEP 2: Create RDS MySQL Database**

### **2.1 Go to RDS Console**

1. Login to [AWS Console](https://console.aws.amazon.com)
2. Search for **"RDS"** in the top search bar
3. Click **"RDS"** (Relational Database Service)

### **2.2 Create Database**

1. Click **"Create database"** (orange button)

2. **Choose a database creation method:**
   - Select: ✅ **Standard create**

3. **Engine options:**
   - Engine type: ✅ **MySQL**
   - Version: ✅ **MySQL 8.0.35** (or latest 8.0.x)

4. **Templates:**
   - Select: ✅ **Free tier** ⭐ (IMPORTANT!)

5. **Settings:**
   - DB instance identifier: `firstlook-db`
   - Master username: `admin`
   - Master password: `YourStrongPassword123`
   - Confirm password: `YourStrongPassword123`
   
   ⚠️ **Remember this password!**

6. **DB instance class:**
   - Should auto-select: ✅ **db.t3.micro** (free tier)

7. **Storage:**
   - Storage type: ✅ **General Purpose SSD (gp2)**
   - Allocated storage: ✅ **20 GB** (free tier limit)
   - ❌ Uncheck "Enable storage autoscaling"

8. **Connectivity:**
   - Virtual private cloud (VPC): ✅ **Default VPC**
   - Public access: ✅ **Yes** (for now, we'll secure it later)
   - VPC security group: ✅ **Create new**
   - New VPC security group name: `firstlook-db-sg`

9. **Database authentication:**
   - ✅ **Password authentication**

10. **Additional configuration** (expand this section):
    - Initial database name: `firstlook`
    - ❌ Uncheck "Enable automated backups" (to stay free)
    - ❌ Uncheck "Enable encryption"

11. **Click "Create database"** (orange button at bottom)

### **2.3 Wait for Database Creation**

- Status will show: "Creating..."
- Wait **5-10 minutes**
- Refresh page until status shows: ✅ **Available**

### **2.4 Get Database Endpoint**

1. Click on your database: **firstlook-db**
2. In **Connectivity & security** tab
3. Copy the **Endpoint**: 
   - Example: `firstlook-db.xxxxx.us-east-1.rds.amazonaws.com`
   - ⚠️ **Save this!** You'll need it later

### **2.5 Configure Security Group**

1. Scroll down to **Security** section
2. Click on the security group (e.g., `firstlook-db-sg`)
3. Click **"Edit inbound rules"**
4. Click **"Add rule"**
5. Configure:
   - Type: ✅ **MySQL/Aurora**
   - Port: ✅ **3306**
   - Source: ✅ **Anywhere-IPv4** (0.0.0.0/0)
   - Description: `Allow MySQL access`
6. Click **"Save rules"**

✅ **Database is ready!**

---

## **STEP 3: Create Elastic Beanstalk Application**

### **3.1 Go to Elastic Beanstalk Console**

1. Search for **"Elastic Beanstalk"** in top search bar
2. Click **"Elastic Beanstalk"**

### **3.2 Create Application**

1. Click **"Create application"** (orange button)

2. **Configure environment:**
   - Environment tier: ✅ **Web server environment**

3. **Application information:**
   - Application name: `firstlook-digital`
   - Environment name: `firstlook-prod-env`
   - Domain: (leave auto-generated or customize)

4. **Platform:**
   - Platform: ✅ **Corretto**
   - Platform branch: ✅ **Corretto 17 running on 64bit Amazon Linux 2023**
   - Platform version: ✅ (latest recommended)

5. **Application code:**
   - Select: ✅ **Upload your code**
   - Version label: `v1.0`
   - Click **"Choose file"**
   - Select: `target/firstlook-digital-1.0.0.jar`
   - ⏳ Wait for upload (may take 1-2 minutes)

6. **Presets:**
   - Select: ✅ **Single instance (free tier eligible)** ⭐

7. Click **"Next"**

### **3.3 Configure Service Access**

1. **Service role:**
   - Select: ✅ **Create and use new service role**
   - Service role name: `aws-elasticbeanstalk-service-role`

2. **EC2 key pair:**
   - Select existing or: **Create new key pair** (optional, for SSH)

3. **EC2 instance profile:**
   - Select: ✅ **Create and use new instance profile**
   - Or use: `aws-elasticbeanstalk-ec2-role`

4. Click **"Skip to review"** (we'll configure more later)

### **3.4 Review and Create**

1. Review all settings
2. Click **"Submit"** (orange button)
3. Wait **5-10 minutes** for environment creation

You'll see:
```
Creating environment...
├── Creating resources
├── Launching EC2 instance
├── Deploying application
└── Environment health: Ok ✅
```

✅ **Application is deploying!**

---

## **STEP 4: Configure Environment Variables**

### **4.1 Go to Configuration**

1. In Elastic Beanstalk console
2. Click on your environment: **firstlook-prod-env**
3. Click **"Configuration"** (left sidebar)

### **4.2 Edit Software Settings**

1. Find **"Updates, monitoring, and logging"** section
2. Click **"Edit"**
3. Scroll to **"Environment properties"**

### **4.3 Add Environment Variables**

Add these properties (click "Add environment property" for each):

| Name | Value |
|------|-------|
| `SERVER_PORT` | `5000` |
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `RDS_DB_URL` | `jdbc:mysql://YOUR-RDS-ENDPOINT:3306/firstlook` |
| `RDS_USERNAME` | `admin` |
| `RDS_PASSWORD` | `YourStrongPassword123` |

**Replace:**
- `YOUR-RDS-ENDPOINT` with your actual RDS endpoint from Step 2.4
- `YourStrongPassword123` with your actual password

**Example:**
```
RDS_DB_URL: jdbc:mysql://firstlook-db.xxxxx.us-east-1.rds.amazonaws.com:3306/firstlook
```

### **4.4 Save Configuration**

1. Click **"Apply"** (bottom right)
2. Wait 2-3 minutes for environment update
3. Environment will restart with new configuration

✅ **Environment variables configured!**

---

## **STEP 5: Access Your Application**

### **5.1 Get Application URL**

1. In Elastic Beanstalk console
2. Your environment: **firstlook-prod-env**
3. At the top, you'll see the URL:
   - Example: `firstlook-prod-env.elasticbeanstalk.com`

### **5.2 Test Your Application**

**Homepage:**
```
http://firstlook-prod-env.elasticbeanstalk.com
```

**Admin Panel:**
```
http://firstlook-prod-env.elasticbeanstalk.com/admin/login
Username: admin
Password: admin123
```

✅ **Your app is LIVE on AWS!** 🎉

---

## **STEP 6: Monitor Your Application**

### **6.1 View Logs**

1. In Elastic Beanstalk console
2. Click **"Logs"** (left sidebar)
3. Click **"Request Logs"** → **"Last 100 Lines"**
4. Wait a moment, then click **"Download"**

### **6.2 Check Health**

1. Click **"Health"** (left sidebar)
2. Should show: ✅ **Ok** (green)
3. View metrics: CPU, Memory, Network

### **6.3 Monitor Costs**

1. Go to **AWS Billing Dashboard**
2. Click **"Bills"**
3. Check current month charges
4. Should show: **$0.00** (free tier)

---

## 🔄 **Update Your Application**

### **When You Make Changes:**

1. **Build new JAR:**
   ```bash
   mvn clean package -DskipTests
   ```

2. **Upload to Elastic Beanstalk:**
   - Go to EB console
   - Click **"Upload and deploy"**
   - Choose file: `target/firstlook-digital-1.0.0.jar`
   - Version label: `v1.1` (increment version)
   - Click **"Deploy"**

3. **Wait 2-3 minutes** for deployment

✅ **Application updated!**

---

## 🔒 **Security Best Practices**

### **After Deployment:**

1. **Change Admin Password:**
   - Login to admin panel
   - Change default password from `admin123`

2. **Restrict Database Access:**
   - Go to RDS security group
   - Change source from `0.0.0.0/0` to EB security group only

3. **Enable HTTPS:**
   - Request SSL certificate in AWS Certificate Manager
   - Add to load balancer (costs extra, not free tier)

---

## 💰 **Cost Monitoring**

### **Stay Within Free Tier:**

1. **Set Up Billing Alert:**
   - Go to **AWS Billing Dashboard**
   - Click **"Budgets"**
   - Click **"Create budget"**
   - Choose **"Zero spend budget"**
   - Enter email for alerts
   - Click **"Create budget"**

2. **Monitor Usage:**
   - Check **AWS Free Tier** page
   - Shows usage vs. limits
   - Alerts when approaching limits

### **Free Tier Limits:**

✅ **EC2:** 750 hours/month (1 instance 24/7)
✅ **RDS:** 750 hours/month (1 instance 24/7)
✅ **Storage:** 20 GB
✅ **Data transfer:** 15 GB/month

### **What to Avoid (Costs Money):**

❌ **Load Balancer** - ~$16/month
❌ **Multiple instances** - Uses more hours
❌ **Snapshots** - $0.05/GB/month
❌ **Elastic IP** (if not attached) - $3.60/month

---

## 🛑 **Terminate Resources (When Done)**

### **To Stop All Charges:**

1. **Terminate Elastic Beanstalk Environment:**
   - Go to EB console
   - Select environment
   - Click **"Actions"** → **"Terminate environment"**
   - Type environment name to confirm
   - Click **"Terminate"**

2. **Delete RDS Database:**
   - Go to RDS console
   - Select database
   - Click **"Actions"** → **"Delete"**
   - ❌ Uncheck "Create final snapshot" (or create if you want backup)
   - Type `delete me` to confirm
   - Click **"Delete"**

⚠️ **This will delete everything!** Backup first if needed.

---

## 🆘 **Troubleshooting**

### **Issue: Application Not Starting**

1. **Check Logs:**
   - EB Console → Logs → Request Logs
   - Look for errors

2. **Common Issues:**
   - Wrong database endpoint
   - Wrong database password
   - Database security group not allowing connections

### **Issue: Can't Connect to Database**

1. **Verify RDS Endpoint:**
   - Copy exact endpoint from RDS console
   - Include `:3306` port

2. **Check Security Group:**
   - RDS security group must allow port 3306
   - Source: 0.0.0.0/0 or EB security group

3. **Test Connection:**
   - Use MySQL Workbench or similar tool
   - Host: RDS endpoint
   - Port: 3306
   - Username: admin
   - Password: your password

### **Issue: Environment Health is "Degraded"**

1. **Check Logs** for errors
2. **Verify Environment Variables** are correct
3. **Check Database** is running
4. **Restart Environment:**
   - Actions → Restart app server(s)

---

## 📋 **Quick Checklist**

### **Before Deployment:**
- [ ] Build JAR file (`mvn clean package -DskipTests`)
- [ ] Have AWS account ready
- [ ] Know your database password

### **During Deployment:**
- [ ] Create RDS MySQL database (db.t3.micro, free tier)
- [ ] Note RDS endpoint
- [ ] Create EB environment (t2.micro, single instance)
- [ ] Upload JAR file
- [ ] Configure environment variables
- [ ] Wait for deployment

### **After Deployment:**
- [ ] Test homepage
- [ ] Test admin login
- [ ] Change admin password
- [ ] Set up billing alerts
- [ ] Monitor free tier usage

---

## 🎉 **Summary**

### **What You Did:**

1. ✅ Created RDS MySQL database (FREE)
2. ✅ Created Elastic Beanstalk environment (FREE)
3. ✅ Deployed Spring Boot application
4. ✅ Configured database connection
5. ✅ Application is LIVE on AWS!

### **Your Application:**

- **URL:** `http://firstlook-prod-env.elasticbeanstalk.com`
- **Admin:** `http://firstlook-prod-env.elasticbeanstalk.com/admin/login`
- **Database:** MySQL on RDS
- **Cost:** $0 for 12 months (free tier)

---

## 📞 **Need Help?**

### **AWS Support:**
- [AWS Documentation](https://docs.aws.amazon.com/elasticbeanstalk/)
- [AWS Free Tier FAQ](https://aws.amazon.com/free/free-tier-faqs/)

### **Check These Files:**
- `AWS-FREE-TIER-DEPLOYMENT.md` - CLI version
- `AWS-DEPLOYMENT.md` - Detailed AWS guide
- `DEPLOYMENT-READY.md` - Pre-deployment checklist

---

## 🎊 **Congratulations!**

**Your application is now running on AWS!**

- ✅ Production-ready
- ✅ MySQL database
- ✅ Auto-scaling ready
- ✅ FREE for 12 months

**Enjoy your deployed application!** 🚀

---

© 2025 F1RSTLOOK Digital - AWS Web Console Deployment Guide
**No CLI Required!** ✅
