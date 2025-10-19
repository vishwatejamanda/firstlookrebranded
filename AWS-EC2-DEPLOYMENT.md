# 🖥️ AWS EC2 Deployment Guide (Simple & FREE)

## ✅ **Deploy to EC2 - No Docker, No EBS, Just Simple!**

**Perfect for beginners! Just run your JAR on a virtual server.**

---

## 💰 **What You Get FREE (12 Months)**

- ✅ **EC2 t2.micro** - 750 hours/month (1 server 24/7)
- ✅ **RDS MySQL db.t3.micro** - 750 hours/month
- ✅ **30 GB storage**
- ✅ **15 GB data transfer/month**

**Total Cost: $0 for 12 months**

---

## 📋 **What You Need**

1. ✅ AWS Account
2. ✅ JAR file (already built)
3. ✅ 30 minutes of your time

---

## 🚀 **Step-by-Step Deployment**

---

## **STEP 1: Build Your JAR File**

```bash
cd /home/vishwatejamanda/Pictures/DOC-20251014-WA0006/firstlookver2-master
mvn clean package -DskipTests
```

✅ **JAR created:** `target/firstlook-digital-1.0.0.jar`

---

## **STEP 2: Create RDS MySQL Database**

### **2.1 Go to RDS Console**

1. Login to [AWS Console](https://console.aws.amazon.com)
2. Search **"RDS"**
3. Click **"Create database"**

### **2.2 Configure Database**

1. **Engine:** ✅ **MySQL 8.0**
2. **Template:** ✅ **Free tier** ⭐
3. **Settings:**
   - DB identifier: `firstlook-db`
   - Username: `admin`
   - Password: `FirstLook2024!` (remember this!)
4. **Instance:** ✅ **db.t3.micro**
5. **Storage:** ✅ **20 GB**
6. **Connectivity:**
   - Public access: ✅ **Yes**
   - VPC: **Default VPC**
7. **Additional configuration:**
   - Initial database name: `firstlook`
   - ❌ Uncheck automated backups
8. Click **"Create database"**

### **2.3 Wait & Get Endpoint**

- Wait **5-10 minutes**
- Status: ✅ **Available**
- Copy **Endpoint**: 
  - Example: `firstlook-db.xxxxx.us-east-1.rds.amazonaws.com`
  - ⚠️ **Save this!**

### **2.4 Configure Security Group**

1. Click database → **Connectivity & security**
2. Click the **VPC security group** link
3. Click **"Edit inbound rules"**
4. Click **"Add rule"**
5. Configure:
   - Type: **MySQL/Aurora**
   - Port: **3306**
   - Source: **Anywhere-IPv4** (0.0.0.0/0)
6. Click **"Save rules"**

✅ **Database ready!**

---

## **STEP 3: Launch EC2 Instance**

### **3.1 Go to EC2 Console**

1. Search **"EC2"**
2. Click **"Launch instance"**

### **3.2 Configure Instance**

**Name and tags:**
- Name: `firstlook-server`

**Application and OS Images:**
- Quick Start: ✅ **Ubuntu**
- AMI: ✅ **Ubuntu Server 24.04 LTS (Free tier eligible)**

**Instance type:**
- ✅ **t2.micro** (Free tier eligible) ⭐

**Key pair (login):**
- Click **"Create new key pair"**
- Key pair name: `firstlook-key`
- Key pair type: **RSA**
- Private key format: **.pem**
- Click **"Create key pair"**
- ⚠️ **Save the .pem file!** You'll need it to connect

**Network settings:**
- Click **"Edit"**
- Auto-assign public IP: ✅ **Enable**
- Firewall (security groups): **Create security group**
- Security group name: `firstlook-sg`
- Description: `Security group for F1RSTLOOK app`

**Add security group rules:**

| Type | Protocol | Port | Source | Description |
|------|----------|------|--------|-------------|
| SSH | TCP | 22 | My IP | SSH access |
| HTTP | TCP | 80 | Anywhere | HTTP access |
| HTTPS | TCP | 443 | Anywhere | HTTPS access |
| Custom TCP | TCP | 8080 | Anywhere | Application port |

**Configure storage:**
- ✅ **30 GB** gp3 (Free tier eligible)

### **3.3 Launch Instance**

1. Review settings
2. Click **"Launch instance"**
3. Wait **2-3 minutes**
4. Status: ✅ **Running**

### **3.4 Get Instance Details**

1. Click on your instance: **firstlook-server**
2. Copy **Public IPv4 address**
   - Example: `54.123.45.67`
   - ⚠️ **Save this!**

✅ **EC2 instance running!**

---

## **STEP 4: Connect to EC2 & Install Java**

### **4.1 Connect via SSH**

**On Linux/Mac:**
```bash
# Make key file secure
chmod 400 ~/Downloads/firstlook-key.pem

# Connect to EC2
ssh -i ~/Downloads/firstlook-key.pem ubuntu@YOUR-EC2-IP

# Replace YOUR-EC2-IP with your actual IP
# Example: ssh -i ~/Downloads/firstlook-key.pem ubuntu@54.123.45.67
```

**On Windows:**
- Use PuTTY or Windows Terminal
- Or use AWS Console → Connect → EC2 Instance Connect

### **4.2 Update System**

```bash
# Update package list
sudo apt update

# Upgrade packages
sudo apt upgrade -y
```

### **4.3 Install Java 17**

```bash
# Install Java 17
sudo apt install openjdk-17-jdk -y

# Verify installation
java -version

# Should show: openjdk version "17.x.x"
```

✅ **Java 17 installed!**

---

## **STEP 5: Upload & Run Your Application**

### **5.1 Upload JAR File**

**From your computer (new terminal, not SSH):**

```bash
# Upload JAR to EC2
scp -i ~/Downloads/firstlook-key.pem \
  /home/vishwatejamanda/Pictures/DOC-20251014-WA0006/firstlookver2-master/target/firstlook-digital-1.0.0.jar \
  ubuntu@YOUR-EC2-IP:/home/ubuntu/

# Replace YOUR-EC2-IP with your actual IP
```

### **5.2 Create Application Directory**

**Back in SSH session:**

```bash
# Create app directory
sudo mkdir -p /opt/firstlook
sudo chown ubuntu:ubuntu /opt/firstlook

# Move JAR to app directory
mv /home/ubuntu/firstlook-digital-1.0.0.jar /opt/firstlook/

# Verify
ls -lh /opt/firstlook/
```

### **5.3 Create Configuration File**

```bash
# Create application properties
nano /opt/firstlook/application.properties
```

**Paste this content:**

```properties
# Server Configuration
server.port=8080
spring.application.name=F1RSTLOOK Digital

# MySQL Database Configuration
spring.datasource.url=jdbc:mysql://YOUR-RDS-ENDPOINT:3306/firstlook
spring.datasource.username=admin
spring.datasource.password=FirstLook2024!
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# Disable H2 Console
spring.h2.console.enabled=false

# Thymeleaf
spring.thymeleaf.cache=true

# WhatsApp Configuration
whatsapp.phone.number=919494385675

# Logging
logging.level.com.firstlook=INFO
logging.level.org.springframework.web=WARN
```

**Replace `YOUR-RDS-ENDPOINT`** with your actual RDS endpoint!

**Save:** Press `Ctrl+X`, then `Y`, then `Enter`

### **5.4 Test Run Application**

```bash
# Run application
cd /opt/firstlook
java -jar firstlook-digital-1.0.0.jar --spring.config.location=application.properties

# You should see:
# Started FirstLookApplication in X seconds
```

**Test in browser:**
```
http://YOUR-EC2-IP:8080
```

If it works, press `Ctrl+C` to stop, then continue to make it run permanently.

✅ **Application works!**

---

## **STEP 6: Run Application as Service (Auto-start)**

### **6.1 Create Systemd Service**

```bash
# Create service file
sudo nano /etc/systemd/system/firstlook.service
```

**Paste this content:**

```ini
[Unit]
Description=F1RSTLOOK Digital Application
After=syslog.target network.target

[Service]
User=ubuntu
WorkingDirectory=/opt/firstlook
ExecStart=/usr/bin/java -Xmx512m -jar /opt/firstlook/firstlook-digital-1.0.0.jar --spring.config.location=/opt/firstlook/application.properties
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

**Save:** `Ctrl+X`, `Y`, `Enter`

### **6.2 Start Service**

```bash
# Reload systemd
sudo systemctl daemon-reload

# Enable service (auto-start on boot)
sudo systemctl enable firstlook

# Start service
sudo systemctl start firstlook

# Check status
sudo systemctl status firstlook

# Should show: active (running) ✅
```

### **6.3 View Logs**

```bash
# View live logs
sudo journalctl -u firstlook -f

# Press Ctrl+C to exit
```

✅ **Application running as service!**

---

## **STEP 7: Install Nginx (Reverse Proxy)**

### **7.1 Install Nginx**

```bash
# Install Nginx
sudo apt install nginx -y

# Start Nginx
sudo systemctl start nginx
sudo systemctl enable nginx
```

### **7.2 Configure Nginx**

```bash
# Create Nginx configuration
sudo nano /etc/nginx/sites-available/firstlook
```

**Paste this content:**

```nginx
server {
    listen 80;
    server_name YOUR-EC2-IP;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

**Replace `YOUR-EC2-IP`** with your actual EC2 IP!

**Save:** `Ctrl+X`, `Y`, `Enter`

### **7.3 Enable Configuration**

```bash
# Enable site
sudo ln -s /etc/nginx/sites-available/firstlook /etc/nginx/sites-enabled/

# Remove default site
sudo rm /etc/nginx/sites-enabled/default

# Test configuration
sudo nginx -t

# Reload Nginx
sudo systemctl reload nginx
```

✅ **Nginx configured!**

---

## **STEP 8: Access Your Application**

### **8.1 Test Application**

**Homepage:**
```
http://YOUR-EC2-IP
```

**Admin Panel:**
```
http://YOUR-EC2-IP/admin/login
Username: admin
Password: admin123
```

✅ **Your app is LIVE on AWS EC2!** 🎉

---

## 🔒 **STEP 9: Secure Your Application (Optional)**

### **9.1 Change Admin Password**

1. Login to admin panel
2. Change password from `admin123`

### **9.2 Restrict SSH Access**

```bash
# Edit security group in AWS Console
# Change SSH source from "Anywhere" to "My IP"
```

### **9.3 Set Up SSL (Optional - Requires Domain)**

```bash
# Install Certbot
sudo apt install certbot python3-certbot-nginx -y

# Get SSL certificate (requires domain)
sudo certbot --nginx -d yourdomain.com

# Auto-renewal
sudo certbot renew --dry-run
```

---

## 🔄 **Update Your Application**

### **When You Make Changes:**

**1. Build new JAR:**
```bash
cd /home/vishwatejamanda/Pictures/DOC-20251014-WA0006/firstlookver2-master
mvn clean package -DskipTests
```

**2. Upload to EC2:**
```bash
scp -i ~/Downloads/firstlook-key.pem \
  target/firstlook-digital-1.0.0.jar \
  ubuntu@YOUR-EC2-IP:/opt/firstlook/
```

**3. Restart service:**
```bash
# SSH into EC2
ssh -i ~/Downloads/firstlook-key.pem ubuntu@YOUR-EC2-IP

# Restart application
sudo systemctl restart firstlook

# Check status
sudo systemctl status firstlook
```

✅ **Application updated!**

---

## 📊 **Monitoring & Maintenance**

### **View Application Logs:**

```bash
# SSH into EC2
ssh -i ~/Downloads/firstlook-key.pem ubuntu@YOUR-EC2-IP

# View logs
sudo journalctl -u firstlook -f

# View last 100 lines
sudo journalctl -u firstlook -n 100
```

### **Check Application Status:**

```bash
# Service status
sudo systemctl status firstlook

# Check if running
ps aux | grep java

# Check port
sudo netstat -tlnp | grep 8080
```

### **Restart Services:**

```bash
# Restart application
sudo systemctl restart firstlook

# Restart Nginx
sudo systemctl restart nginx

# Reboot server (if needed)
sudo reboot
```

---

## 💰 **Cost Monitoring**

### **Stay Within Free Tier:**

✅ **EC2:** 1 t2.micro = 750 hours/month (FREE)
✅ **RDS:** 1 db.t3.micro = 750 hours/month (FREE)
✅ **Storage:** 30 GB (FREE)
✅ **Data transfer:** 15 GB/month (FREE)

### **Set Billing Alert:**

1. AWS Billing Dashboard
2. Create budget
3. Set alert at $1
4. Get email if charges occur

---

## 🛑 **Terminate Resources (When Done)**

### **Stop Charges:**

**1. Terminate EC2 Instance:**
- EC2 Console → Instances
- Select instance
- Instance state → Terminate instance

**2. Delete RDS Database:**
- RDS Console → Databases
- Select database
- Actions → Delete
- ❌ Uncheck final snapshot (or create if needed)

⚠️ **This deletes everything!**

---

## 🆘 **Troubleshooting**

### **Issue: Can't Connect via SSH**

**Check:**
1. Security group allows port 22
2. Using correct key file
3. Key file has correct permissions (chmod 400)
4. Using correct username (ubuntu)

**Solution:**
```bash
chmod 400 firstlook-key.pem
ssh -i firstlook-key.pem ubuntu@YOUR-EC2-IP
```

### **Issue: Application Won't Start**

**Check logs:**
```bash
sudo journalctl -u firstlook -n 50
```

**Common issues:**
- Wrong database endpoint
- Wrong database password
- Port 8080 already in use
- Java not installed

### **Issue: Can't Access Website**

**Check:**
1. Security group allows port 80 and 8080
2. Application is running: `sudo systemctl status firstlook`
3. Nginx is running: `sudo systemctl status nginx`
4. Using correct IP address

**Test directly:**
```
http://YOUR-EC2-IP:8080
```

### **Issue: Database Connection Failed**

**Verify:**
1. RDS endpoint is correct in application.properties
2. RDS security group allows port 3306
3. Database is running (status: Available)
4. Username/password are correct

**Test connection:**
```bash
# Install MySQL client
sudo apt install mysql-client -y

# Test connection
mysql -h YOUR-RDS-ENDPOINT -u admin -p
# Enter password
```

---

## 📋 **Quick Command Reference**

```bash
# Connect to EC2
ssh -i firstlook-key.pem ubuntu@YOUR-EC2-IP

# View logs
sudo journalctl -u firstlook -f

# Restart application
sudo systemctl restart firstlook

# Check status
sudo systemctl status firstlook

# Upload new JAR
scp -i firstlook-key.pem target/*.jar ubuntu@YOUR-EC2-IP:/opt/firstlook/

# Reboot server
sudo reboot
```

---

## 🎉 **Summary**

### **What You Did:**

1. ✅ Created RDS MySQL database (FREE)
2. ✅ Launched EC2 instance (FREE)
3. ✅ Installed Java 17
4. ✅ Uploaded and ran your JAR
5. ✅ Set up auto-start service
6. ✅ Configured Nginx reverse proxy
7. ✅ Application is LIVE!

### **Your Setup:**

- **Server:** EC2 t2.micro (Ubuntu)
- **Runtime:** Java 17
- **Database:** MySQL on RDS
- **Web server:** Nginx
- **Cost:** $0 for 12 months (free tier)
- **URL:** `http://YOUR-EC2-IP`

---

## 🎊 **Congratulations!**

**Your application is running on AWS EC2!**

- ✅ Simple deployment (no Docker, no EBS)
- ✅ Full control
- ✅ Production-ready
- ✅ FREE for 12 months

**Enjoy your deployed application!** 🚀

---

© 2025 F1RSTLOOK Digital - AWS EC2 Deployment Guide
**Simple & FREE!** ✅
