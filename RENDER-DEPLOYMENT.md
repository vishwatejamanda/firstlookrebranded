# 🚀 Deploy to Render.com (FREE Testing)

## ✅ **Best for Testing Before AWS**

---

## 🎯 **Why Render.com?**

- ✅ **FREE** - No credit card required
- ✅ **Easy** - Deploy in 5 minutes
- ✅ **HTTPS** - Free SSL certificate
- ✅ **Auto-deploy** - Push to Git, auto-deploys
- ✅ **Perfect for testing** - Before AWS production

---

## 📋 **Prerequisites**

- [ ] GitHub account
- [ ] Render.com account (free)
- [ ] Your code pushed to GitHub

---

## 🚀 **Deployment Steps**

### **Step 1: Prepare Your Code**

Create `render.yaml` in project root:

```yaml
services:
  - type: web
    name: firstlook-digital
    env: java
    buildCommand: mvn clean package -DskipTests
    startCommand: java -Xmx512m -jar target/firstlook-digital-0.0.1-SNAPSHOT.jar
    envVars:
      - key: SERVER_PORT
        value: 10000
      - key: SPRING_PROFILES_ACTIVE
        value: prod
```

Update `application.properties`:

```properties
# Add this for Render
server.port=${PORT:8080}
```

### **Step 2: Push to GitHub**

```bash
# Initialize git (if not already)
git init
git add .
git commit -m "Prepare for Render deployment"

# Create GitHub repo and push
git remote add origin https://github.com/yourusername/firstlook-digital.git
git branch -M main
git push -u origin main
```

### **Step 3: Deploy on Render**

1. Go to [render.com](https://render.com)
2. Sign up / Login (free)
3. Click "New +" → "Web Service"
4. Connect your GitHub repository
5. Select "firstlook-digital" repo
6. Configure:
   - **Name:** firstlook-digital
   - **Environment:** Java
   - **Build Command:** `mvn clean package -DskipTests`
   - **Start Command:** `java -jar target/firstlook-digital-0.0.1-SNAPSHOT.jar`
   - **Plan:** Free
7. Click "Create Web Service"
8. Wait 5-10 minutes for build

### **Step 4: Access Your App**

Your app will be at:
```
https://firstlook-digital.onrender.com
```

Admin panel:
```
https://firstlook-digital.onrender.com/admin/login
```

---

## 🎉 **That's It!**

Your app is now live and accessible worldwide!

---

## 📊 **Free Tier Limits**

- ✅ 750 hours/month (enough for testing)
- ✅ Sleeps after 15 min inactivity
- ✅ Wakes up on first request (takes 30 seconds)
- ✅ Perfect for testing and demos

---

## 🔄 **Auto-Deploy**

Every time you push to GitHub:
```bash
git add .
git commit -m "Update feature"
git push
```

Render automatically:
1. Detects changes
2. Builds application
3. Deploys new version
4. Your app is updated!

---

## 🗄️ **Add Database (Optional)**

### **Add PostgreSQL:**

1. In Render dashboard, click "New +" → "PostgreSQL"
2. Name: `firstlook-db`
3. Plan: Free
4. Click "Create Database"
5. Copy "Internal Database URL"
6. In your web service, add environment variable:
   - Key: `DATABASE_URL`
   - Value: (paste database URL)

Update `application.properties`:
```properties
spring.datasource.url=${DATABASE_URL}
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

---

## 📧 **Configure Email (Optional)**

Add environment variables in Render:
- `MAIL_HOST`: smtp.gmail.com
- `MAIL_PORT`: 587
- `MAIL_USERNAME`: your-email@gmail.com
- `MAIL_PASSWORD`: your-app-password

---

## 🔍 **Monitor Your App**

### **View Logs:**
- Go to Render dashboard
- Click your service
- Click "Logs" tab
- See real-time logs

### **Check Status:**
- Dashboard shows if app is running
- Shows last deploy time
- Shows build status

---

## 💰 **Cost**

- **Free tier:** Perfect for testing
- **Paid plans:** Start at $7/month (if you need always-on)

---

## 🎯 **Next Steps**

After testing on Render:
1. ✅ Test all features
2. ✅ Share with team/client
3. ✅ Get feedback
4. ✅ Fix any issues
5. ✅ Then deploy to AWS for production

---

## 🚀 **Quick Commands**

```bash
# 1. Create render.yaml
cat > render.yaml << 'EOF'
services:
  - type: web
    name: firstlook-digital
    env: java
    buildCommand: mvn clean package -DskipTests
    startCommand: java -jar target/firstlook-digital-0.0.1-SNAPSHOT.jar
EOF

# 2. Update application.properties
echo "server.port=\${PORT:8080}" >> src/main/resources/application.properties

# 3. Commit and push
git add .
git commit -m "Add Render config"
git push

# 4. Deploy on render.com (via dashboard)
```

---

## ✅ **Testing Checklist**

After deployment, test:
- [ ] Homepage loads
- [ ] Contact form works
- [ ] WhatsApp button works
- [ ] Admin login works
- [ ] All admin pages load
- [ ] Bulk WhatsApp send works
- [ ] Database persists data
- [ ] Mobile responsive

---

## 🎉 **Perfect for Testing!**

Render.com is ideal for:
- ✅ Testing before AWS
- ✅ Client demos
- ✅ Team collaboration
- ✅ Getting feedback
- ✅ No cost during testing

Then move to AWS for production! 🚀

---

© 2025 F1RSTLOOK Digital - Render Deployment Guide
