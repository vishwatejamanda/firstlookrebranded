# 🐳 AWS Elastic Beanstalk with Docker (Web Console)

## ✅ **Deploy Using Docker Platform (No Corretto Needed!)**

**Perfect solution when Corretto is not available!**

---

## 💰 **What You Get FREE (12 Months)**

- ✅ **EC2 t2.micro** - 750 hours/month
- ✅ **RDS MySQL db.t3.micro** - 750 hours/month
- ✅ **Docker platform** - Works everywhere!
- ✅ **20 GB storage**

**Total Cost: $0 for 12 months**

---

## 📋 **Files Ready**

Your project now has:
- ✅ `Dockerfile` - Multi-stage Docker build
- ✅ `Dockerrun.aws.json` - AWS Docker configuration
- ✅ Application JAR will be built inside Docker

---

## 🚀 **Step-by-Step Deployment**

---

## **STEP 1: Create Deployment Package**

### **Option A: Use Pre-built JAR (Faster)**

```bash
# Build JAR first
cd /home/vishwatejamanda/Pictures/DOC-20251014-WA0006/firstlookver2-master
mvn clean package -DskipTests

# Create deployment ZIP
zip -r firstlook-docker.zip Dockerfile Dockerrun.aws.json pom.xml src/ target/firstlook-digital-1.0.0.jar
```

### **Option B: Let AWS Build (Simpler)**

```bash
# Create ZIP without pre-building
cd /home/vishwatejamanda/Pictures/DOC-20251014-WA0006/firstlookver2-master
zip -r firstlook-docker.zip Dockerfile Dockerrun.aws.json pom.xml src/
```

**Recommended: Option B** (AWS will build for you)

✅ **ZIP file created:** `firstlook-docker.zip`

---

## **STEP 2: Create RDS MySQL Database**

### **2.1 Go to RDS Console**

1. Login to [AWS Console](https://console.aws.amazon.com)
2. Search for **"RDS"**
3. Click **"Create database"**

### **2.2 Configure Database**

1. **Engine:** ✅ **MySQL 8.0**
2. **Template:** ✅ **Free tier** ⭐
3. **Settings:**
   - DB identifier: `firstlook-db`
   - Username: `admin`
   - Password: `YourStrongPassword123`
4. **Instance:** ✅ **db.t3.micro** (auto-selected)
5. **Storage:** ✅ **20 GB**
6. **Connectivity:**
   - Public access: ✅ **Yes**
   - VPC security group: ✅ **Create new**
7. **Additional config:**
   - Initial database: `firstlook`
   - ❌ Uncheck backups (to stay free)
8. Click **"Create database"**

### **2.3 Wait & Get Endpoint**

- Wait **5-10 minutes**
- Status: ✅ **Available**
- Copy **Endpoint**: `firstlook-db.xxxxx.us-east-1.rds.amazonaws.com`

### **2.4 Configure Security Group**

1. Click on database → **Security** tab
2. Click security group
3. **Edit inbound rules**
4. **Add rule:**
   - Type: **MySQL/Aurora**
   - Port: **3306**
   - Source: **0.0.0.0/0**
5. **Save rules**

✅ **Database ready!**

---

## **STEP 3: Create Elastic Beanstalk with Docker**

### **3.1 Go to Elastic Beanstalk**

1. Search **"Elastic Beanstalk"**
2. Click **"Create application"**

### **3.2 Configure Application**

1. **Application name:** `firstlook-digital`
2. **Environment name:** `firstlook-prod-env`
3. **Platform:**
   - Platform: ✅ **Docker** ⭐
   - Platform branch: ✅ **Docker running on 64bit Amazon Linux 2023**
   - Platform version: ✅ (latest recommended)

### **3.3 Upload Code**

1. **Application code:** ✅ **Upload your code**
2. **Version label:** `v1.0-docker`
3. Click **"Choose file"**
4. Select: `firstlook-docker.zip`
5. ⏳ Wait for upload

### **3.4 Configure Preset**

1. **Presets:** ✅ **Single instance (free tier eligible)** ⭐
2. Click **"Next"**

### **3.5 Service Access**

1. **Service role:** ✅ **Create and use new service role**
2. **EC2 instance profile:** ✅ **Create new** or use existing
3. Click **"Skip to review"**

### **3.6 Submit**

1. Review settings
2. Click **"Submit"**
3. ⏳ Wait **10-15 minutes** (Docker build takes longer)

You'll see:
```
Creating environment...
├── Launching EC2 instance
├── Building Docker image
│   ├── Downloading Maven
│   ├── Building JAR
│   └── Creating runtime image
├── Deploying container
└── Environment health: Ok ✅
```

✅ **Docker container deployed!**

---

## **STEP 4: Configure Environment Variables**

### **4.1 Go to Configuration**

1. Click your environment: **firstlook-prod-env**
2. Click **"Configuration"** (left sidebar)
3. Find **"Updates, monitoring, and logging"**
4. Click **"Edit"**

### **4.2 Add Environment Properties**

Scroll to **"Environment properties"** and add:

| Name | Value |
|------|-------|
| `PORT` | `5000` |
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `RDS_DB_URL` | `jdbc:mysql://YOUR-RDS-ENDPOINT:3306/firstlook` |
| `RDS_USERNAME` | `admin` |
| `RDS_PASSWORD` | `YourStrongPassword123` |

**Replace `YOUR-RDS-ENDPOINT`** with your actual endpoint!

Example:
```
RDS_DB_URL: jdbc:mysql://firstlook-db.c1a2b3c4.us-east-1.rds.amazonaws.com:3306/firstlook
```

### **4.3 Apply Configuration**

1. Click **"Apply"**
2. ⏳ Wait 2-3 minutes
3. Environment restarts with new config

✅ **Environment configured!**

---

## **STEP 5: Access Your Application**

### **5.1 Get URL**

In EB console, you'll see:
```
http://firstlook-prod-env.elasticbeanstalk.com
```

### **5.2 Test Application**

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

✅ **Your app is LIVE with Docker!** 🎉

---

## 🐳 **How Docker Works on AWS**

### **What Happens:**

1. **Upload ZIP** → AWS receives your code
2. **Build Stage:**
   - AWS runs `Dockerfile`
   - Downloads Maven & Java 17
   - Builds your JAR file
   - Creates optimized runtime image
3. **Deploy Stage:**
   - Starts Docker container
   - Runs your application
   - Maps port 5000

### **Architecture:**

```
AWS Elastic Beanstalk
    ↓
EC2 t2.micro (FREE)
    ↓
Docker Container
    ├── Java 17 Runtime
    ├── Your Application JAR
    └── Port 5000
    ↓
RDS MySQL (FREE)
```

---

## 🔄 **Update Your Application**

### **When You Make Changes:**

1. **Make code changes**

2. **Create new ZIP:**
   ```bash
   cd /home/vishwatejamanda/Pictures/DOC-20251014-WA0006/firstlookver2-master
   zip -r firstlook-docker-v2.zip Dockerfile Dockerrun.aws.json pom.xml src/
   ```

3. **Upload to EB:**
   - Go to EB console
   - Click **"Upload and deploy"**
   - Choose: `firstlook-docker-v2.zip`
   - Version: `v1.1-docker`
   - Click **"Deploy"**

4. ⏳ **Wait 5-10 minutes** (Docker rebuild)

✅ **Application updated!**

---

## 📊 **Advantages of Docker**

### **Why Docker is Better:**

✅ **Works everywhere** - No platform dependency
✅ **Consistent** - Same environment locally & AWS
✅ **Flexible** - Easy to customize
✅ **Portable** - Can move to any cloud
✅ **Isolated** - Container security
✅ **Reproducible** - Same build every time

### **vs Corretto:**

| Feature | Docker | Corretto |
|---------|--------|----------|
| **Availability** | ✅ Always | ❌ Sometimes missing |
| **Flexibility** | ✅ High | ⚠️ Limited |
| **Portability** | ✅ Any cloud | ⚠️ AWS only |
| **Build time** | ⚠️ Slower (first time) | ✅ Faster |
| **Consistency** | ✅ Perfect | ⚠️ Platform dependent |

---

## 🔍 **Monitoring & Logs**

### **View Logs:**

1. EB Console → **"Logs"**
2. **"Request Logs"** → **"Last 100 Lines"**
3. Download and check for errors

### **Docker Logs:**

Logs will show:
```
Building Docker image...
Step 1/8: FROM maven:3.9-eclipse-temurin-17
Step 2/8: WORKDIR /app
...
Successfully built abc123def456
Starting container...
Application started on port 5000
```

### **Application Logs:**

```
Spring Boot starting...
Connected to MySQL database
Application ready!
```

---

## 💰 **Cost Monitoring**

### **Free Tier Usage:**

✅ **EC2:** 1 t2.micro instance = 750 hours/month (FREE)
✅ **RDS:** 1 db.t3.micro = 750 hours/month (FREE)
✅ **Docker:** No additional cost
✅ **Storage:** 20 GB (FREE)

### **Set Billing Alert:**

1. **AWS Billing Dashboard**
2. **Create budget**
3. **Zero spend budget**
4. **Email alerts**

---

## 🛑 **Terminate Resources**

### **When Done Testing:**

1. **Terminate EB Environment:**
   - EB Console → Environment
   - **Actions** → **Terminate environment**
   - Confirm

2. **Delete RDS Database:**
   - RDS Console → Database
   - **Actions** → **Delete**
   - ❌ Uncheck final snapshot (or create if needed)
   - Confirm

⚠️ **This deletes everything!**

---

## 🆘 **Troubleshooting**

### **Issue: Docker Build Failed**

**Check:**
1. ZIP file contains all files
2. Dockerfile syntax is correct
3. pom.xml is valid

**Solution:**
```bash
# Test Docker build locally
docker build -t firstlook-test .
docker run -p 5000:5000 firstlook-test
```

### **Issue: Container Won't Start**

**Check Logs:**
- EB Console → Logs
- Look for Java errors
- Check port configuration

**Common Issues:**
- Wrong port (must be 5000)
- Missing environment variables
- Database connection failed

### **Issue: Can't Connect to Database**

**Verify:**
1. RDS endpoint is correct
2. Security group allows port 3306
3. Database is running (status: Available)
4. Environment variables are set

**Test Connection:**
```bash
# From EB instance (SSH)
mysql -h YOUR-RDS-ENDPOINT -u admin -p
# Enter password
# Should connect successfully
```

---

## 📋 **Quick Checklist**

### **Before Deployment:**
- [ ] Create `firstlook-docker.zip`
- [ ] Have AWS account ready
- [ ] Know database password

### **During Deployment:**
- [ ] Create RDS MySQL (db.t3.micro, free tier)
- [ ] Note RDS endpoint
- [ ] Create EB with **Docker platform**
- [ ] Upload ZIP file
- [ ] Configure environment variables
- [ ] Wait for Docker build (10-15 min)

### **After Deployment:**
- [ ] Test homepage
- [ ] Test admin login
- [ ] Change admin password
- [ ] Set billing alerts
- [ ] Monitor usage

---

## 🎉 **Summary**

### **What You Did:**

1. ✅ Created Docker configuration
2. ✅ Created RDS MySQL database
3. ✅ Deployed with Docker platform
4. ✅ Configured environment variables
5. ✅ Application running in Docker container!

### **Your Setup:**

- **Platform:** Docker on Amazon Linux 2023
- **Runtime:** Java 17 in Docker container
- **Database:** MySQL on RDS
- **Cost:** $0 for 12 months (free tier)
- **URL:** `http://firstlook-prod-env.elasticbeanstalk.com`

---

## 🎊 **Congratulations!**

**Your application is running on AWS with Docker!**

- ✅ No Corretto needed
- ✅ Works reliably
- ✅ Production-ready
- ✅ FREE for 12 months

**Enjoy your deployed application!** 🚀

---

## 📞 **Quick Commands**

```bash
# Create deployment ZIP
zip -r firstlook-docker.zip Dockerfile Dockerrun.aws.json pom.xml src/

# Test Docker locally (optional)
docker build -t firstlook-test .
docker run -p 5000:5000 -e SPRING_PROFILES_ACTIVE=dev firstlook-test

# Update deployment
zip -r firstlook-docker-v2.zip Dockerfile Dockerrun.aws.json pom.xml src/
# Then upload via EB console
```

---

© 2025 F1RSTLOOK Digital - AWS Docker Deployment Guide
**Docker Platform - Works Everywhere!** 🐳
