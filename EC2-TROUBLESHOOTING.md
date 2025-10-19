# 🔧 EC2 Troubleshooting Guide

## Common Issues and Solutions

---

## ❌ **Issue: Database Lock Error**

### **Error Message:**
```
Database may be already in use: "data/firstlook.mv.db"
The file is locked
```

### **Cause:**
H2 database file is locked by another process or wasn't closed properly.

---

## ✅ **Solution 1: Use In-Memory H2 (Quick Fix)**

### **What Changed:**
The application now uses **in-memory H2** by default instead of file-based.

**Benefits:**
- ✅ No file locks
- ✅ No database files to manage
- ✅ Fresh start every time
- ✅ Perfect for testing

**Drawback:**
- ⚠️ Data is lost when app restarts

### **This is Already Fixed!**
The new JAR file uses in-memory H2 by default. Just upload and run!

---

## ✅ **Solution 2: Use MySQL (Production)**

### **For Production with Persistent Data:**

**On EC2, set environment variables:**

```bash
# Edit the application.properties file
sudo nano /opt/firstlook/application.properties
```

**Add these lines:**

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://YOUR-RDS-ENDPOINT:3306/firstlook
spring.datasource.username=admin
spring.datasource.password=YourPassword123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.h2.console.enabled=false
```

**Or use environment variables:**

```bash
# Create environment file
sudo nano /etc/systemd/system/firstlook.service
```

**Add environment variables:**

```ini
[Service]
Environment="DATABASE_URL=jdbc:mysql://YOUR-RDS-ENDPOINT:3306/firstlook"
Environment="DATABASE_USERNAME=admin"
Environment="DATABASE_PASSWORD=YourPassword123"
Environment="H2_CONSOLE_ENABLED=false"
```

**Reload and restart:**

```bash
sudo systemctl daemon-reload
sudo systemctl restart firstlook
```

---

## ✅ **Solution 3: Clear Lock Files (If Using File-Based H2)**

### **SSH into EC2:**

```bash
ssh -i ~/Downloads/firstlook-key.pem ubuntu@YOUR-EC2-IP
```

### **Stop Application:**

```bash
# Stop service
sudo systemctl stop firstlook

# Kill any remaining processes
sudo pkill -f firstlook-digital

# Verify nothing is running
ps aux | grep java
```

### **Remove Lock Files:**

```bash
# Remove H2 database files
sudo rm -rf /opt/firstlook/data/

# Or just remove lock files
sudo rm -f /opt/firstlook/data/*.lock
```

### **Restart Application:**

```bash
sudo systemctl start firstlook
sudo systemctl status firstlook
```

---

## 🔄 **How to Properly Restart Application**

### **Always Use Systemctl:**

```bash
# Stop
sudo systemctl stop firstlook

# Start
sudo systemctl start firstlook

# Restart
sudo systemctl restart firstlook

# Check status
sudo systemctl status firstlook
```

### **Never Kill Directly (Unless Emergency):**

```bash
# ❌ Don't do this (can cause locks)
kill -9 <PID>

# ✅ Do this instead
sudo systemctl stop firstlook
```

---

## 📋 **Quick Troubleshooting Commands**

### **Check if Application is Running:**

```bash
# Check service status
sudo systemctl status firstlook

# Check Java processes
ps aux | grep java

# Check port 8080
sudo netstat -tlnp | grep 8080
```

### **View Logs:**

```bash
# Live logs
sudo journalctl -u firstlook -f

# Last 100 lines
sudo journalctl -u firstlook -n 100

# Errors only
sudo journalctl -u firstlook -p err
```

### **Test Application:**

```bash
# Test locally on EC2
curl http://localhost:8080

# Test from outside
curl http://YOUR-EC2-IP
```

---

## 🆘 **Common Errors & Fixes**

### **Error: Port 8080 Already in Use**

```bash
# Find what's using port 8080
sudo lsof -i :8080

# Kill the process
sudo kill <PID>

# Or restart the service
sudo systemctl restart firstlook
```

### **Error: Cannot Connect to Database**

**Check:**
1. RDS endpoint is correct
2. RDS security group allows EC2
3. Database credentials are correct

**Test connection:**
```bash
# Install MySQL client
sudo apt install mysql-client -y

# Test connection
mysql -h YOUR-RDS-ENDPOINT -u admin -p
```

### **Error: Application Won't Start**

**Check logs:**
```bash
sudo journalctl -u firstlook -n 50
```

**Common causes:**
- Wrong database configuration
- Port already in use
- Out of memory
- Missing dependencies

---

## 🔧 **Update Application on EC2**

### **Step 1: Build New JAR**

**On your computer:**
```bash
cd /home/vishwatejamanda/Pictures/DOC-20251014-WA0006/firstlookver2-master
mvn clean package -DskipTests
```

### **Step 2: Upload to EC2**

```bash
scp -i ~/Downloads/firstlook-key.pem \
  target/firstlook-digital-1.0.0.jar \
  ubuntu@YOUR-EC2-IP:/home/ubuntu/
```

### **Step 3: Replace on EC2**

**SSH into EC2:**
```bash
ssh -i ~/Downloads/firstlook-key.pem ubuntu@YOUR-EC2-IP
```

**Replace JAR:**
```bash
# Stop application
sudo systemctl stop firstlook

# Backup old JAR
sudo mv /opt/firstlook/firstlook-digital-1.0.0.jar \
  /opt/firstlook/firstlook-digital-1.0.0.jar.backup

# Move new JAR
sudo mv /home/ubuntu/firstlook-digital-1.0.0.jar \
  /opt/firstlook/

# Start application
sudo systemctl start firstlook

# Check status
sudo systemctl status firstlook
```

---

## 💾 **Database Options Comparison**

| Database | Pros | Cons | Best For |
|----------|------|------|----------|
| **H2 In-Memory** | ✅ No locks<br>✅ Fast<br>✅ Simple | ❌ Data lost on restart | Testing, Demos |
| **H2 File-Based** | ✅ Data persists<br>✅ No external DB | ❌ Can lock<br>❌ Single user | Development |
| **MySQL (RDS)** | ✅ Production-ready<br>✅ Scalable<br>✅ No locks | ⚠️ Costs after free tier | Production |

---

## 🎯 **Recommended Setup**

### **For Testing/Demo:**
- Use **H2 In-Memory** (default in new JAR)
- No database setup needed
- Just run and test

### **For Production:**
- Use **MySQL on RDS**
- Set environment variables
- Data persists across restarts

---

## 📝 **Configuration Examples**

### **Development (H2 In-Memory):**

No configuration needed! Just run:
```bash
java -jar firstlook-digital-1.0.0.jar
```

### **Production (MySQL):**

**Option A: Environment Variables**
```bash
export DATABASE_URL="jdbc:mysql://YOUR-RDS-ENDPOINT:3306/firstlook"
export DATABASE_USERNAME="admin"
export DATABASE_PASSWORD="YourPassword123"
export H2_CONSOLE_ENABLED="false"

java -jar firstlook-digital-1.0.0.jar
```

**Option B: External Properties File**
```bash
java -jar firstlook-digital-1.0.0.jar \
  --spring.datasource.url=jdbc:mysql://YOUR-RDS-ENDPOINT:3306/firstlook \
  --spring.datasource.username=admin \
  --spring.datasource.password=YourPassword123
```

---

## ✅ **Summary**

### **Database Lock Issue - FIXED!**

**What was changed:**
- ✅ H2 now uses **in-memory** mode by default
- ✅ No more file locks
- ✅ Works out of the box
- ✅ Can still use MySQL with environment variables

**New JAR file:**
- ✅ Built and ready: `target/firstlook-digital-1.0.0.jar`
- ✅ Copied to home: `~/firstlook-digital-1.0.0.jar`
- ✅ No lock issues
- ✅ Ready to deploy

**To deploy:**
1. Upload new JAR to EC2
2. Replace old JAR
3. Restart service
4. No more database locks!

---

© 2025 F1RSTLOOK Digital - EC2 Troubleshooting Guide
