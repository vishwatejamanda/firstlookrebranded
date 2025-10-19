# 🚀 Quick Start Guide - F1RSTLOOK Digital Spring Boot

## ⚡ 3-Minute Setup

### Step 1: Install Prerequisites
```bash
# Check Java (need 17+)
java -version

# Check Maven (need 3.6+)
mvn -version
```

**Don't have them?**
- Java: https://adoptium.net/
- Maven: https://maven.apache.org/download.cgi

---

### Step 2: Run the Application
```bash
# Navigate to project directory
cd /home/vishwatejamanda/Pictures/DOC-20251014-WA0006/firstlookver2-master

# Option A: Use the run script (easiest)
./run.sh

# Option B: Manual Maven command
mvn spring-boot:run
```

---

### Step 3: Open Your Browser
```
Website:          http://localhost:8080
Admin Dashboard:  http://localhost:8080/admin/dashboard
Database Console: http://localhost:8080/h2-console
```

---

## 🎯 What You'll See

### 1. Homepage (http://localhost:8080)
- ✅ Same beautiful design as before
- ✅ **NEW**: Green WhatsApp button (bottom-right)
- ✅ **NEW**: Contact form with backend

### 2. WhatsApp Button
- Click it → Opens WhatsApp with pre-filled message
- Works on mobile and desktop
- Automatically tracked in database

### 3. Contact Form (scroll to bottom)
- Fill out name, email, subject, message
- Click "Work With F1RSTLOOK"
- See success message
- Check admin dashboard to see submission

### 4. Admin Dashboard (http://localhost:8080/admin/dashboard)
- See visitor statistics
- View contact form submissions
- Track WhatsApp clicks
- Monitor device types

---

## 📧 Configure Email (Optional but Recommended)

### Edit: `src/main/resources/application.properties`

```properties
# Replace with your Gmail
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

### Get Gmail App Password:
1. Go to https://myaccount.google.com/security
2. Enable 2-Step Verification
3. Go to "App passwords"
4. Generate password for "Mail"
5. Copy the 16-character password
6. Paste in application.properties

**Restart application after changes!**

---

## 🧪 Test Everything

### Test 1: Visitor Tracking
1. Visit http://localhost:8080
2. Go to http://localhost:8080/admin/dashboard
3. See your visit in "Recent Visitors"

### Test 2: WhatsApp Button
1. Click green WhatsApp button
2. WhatsApp opens with message
3. Check dashboard → "WhatsApp Clicks" increased

### Test 3: Contact Form
1. Scroll to contact section
2. Fill out form
3. Submit
4. See success message
5. Check dashboard → New message appears
6. Check email (if configured)

---

## 🎨 Customization

### Change WhatsApp Number
**File**: `src/main/resources/application.properties`
```properties
whatsapp.phone.number=919494385675
```

### Change WhatsApp Message
**File**: `src/main/resources/application.properties`
```properties
whatsapp.default.message=Your custom message here
```

### Change Port
**File**: `src/main/resources/application.properties`
```properties
server.port=8081
```

**Restart after changes!**

---

## 🐛 Troubleshooting

### Problem: Port 8080 already in use
**Solution**:
```bash
# Option 1: Kill process on port 8080
sudo lsof -ti:8080 | xargs kill -9

# Option 2: Change port (see Customization above)
```

### Problem: Maven not found
**Solution**:
```bash
# Install Maven
sudo apt install maven  # Ubuntu/Debian
brew install maven      # macOS
```

### Problem: Java version too old
**Solution**:
```bash
# Install Java 17
sudo apt install openjdk-17-jdk  # Ubuntu/Debian
brew install openjdk@17          # macOS
```

### Problem: Email not sending
**Solution**:
1. Check Gmail settings
2. Use App Password (not regular password)
3. Enable 2-Step Verification first
4. Check spam folder

### Problem: Database locked
**Solution**:
```bash
# Stop application (Ctrl+C)
# Delete database
rm -rf data/
# Restart application
./run.sh
```

---

## 📱 Mobile Testing

### Test on Your Phone
1. Find your computer's IP address:
   ```bash
   # Linux/Mac
   ifconfig | grep inet
   
   # Windows
   ipconfig
   ```

2. On phone, visit: `http://YOUR_IP:8080`
   Example: `http://192.168.1.100:8080`

3. Test WhatsApp button → Should open WhatsApp app

---

## 🔒 Security Notes

### Before Production:
- [ ] Change default database password
- [ ] Add authentication to admin dashboard
- [ ] Enable HTTPS
- [ ] Configure firewall
- [ ] Set up backup strategy
- [ ] Use environment variables for secrets

---

## 📊 Understanding the Dashboard

### Metrics Explained

**Total Visitors**: Every page load
**Unique Visitors**: Unique IP addresses
**WhatsApp Clicks**: Times button was clicked
**Contact Messages**: Form submissions

### Color Badges
- 🔵 Blue = Mobile device
- ⚫ Gray = Desktop device
- 🟡 Yellow = Tablet device
- 🟢 Green = New message

---

## 🎓 Next Steps

### For Development
1. Read `README-SPRINGBOOT.md` for detailed docs
2. Read `FEATURES.md` for feature explanations
3. Explore the code in `src/main/java/com/firstlook/`
4. Customize templates in `src/main/resources/templates/`

### For Production
1. Configure production database (MySQL/PostgreSQL)
2. Set up email service
3. Add authentication
4. Deploy to cloud (Heroku, AWS, etc.)
5. Set up monitoring

### For Marketing
1. Monitor dashboard daily
2. Respond to WhatsApp messages quickly
3. Reply to contact form within 24 hours
4. Analyze traffic patterns
5. Optimize based on data

---

## 🆘 Getting Help

### Resources
- **Documentation**: See README-SPRINGBOOT.md
- **Features**: See FEATURES.md
- **Code**: Well-commented in src/

### Contact
- **WhatsApp**: +919494385675
- **Test Form**: Use the contact form on the site

---

## ✅ Success Checklist

After setup, you should have:
- [x] Application running on http://localhost:8080
- [x] WhatsApp button visible and working
- [x] Contact form submitting successfully
- [x] Admin dashboard showing data
- [x] Email notifications (if configured)
- [x] Database storing information

---

## 🎉 You're Ready!

Your F1RSTLOOK Digital website is now a powerful Spring Boot application with:
- ✅ Visitor tracking
- ✅ WhatsApp integration
- ✅ Contact form backend
- ✅ Analytics dashboard
- ✅ Database persistence

**Start tracking your customers today!** 🚀

---

## 💡 Pro Tips

1. **Check dashboard daily** to monitor engagement
2. **Respond to WhatsApp within 5 minutes** for best conversion
3. **Reply to contact forms within 24 hours**
4. **Export data weekly** for analysis
5. **Test on mobile devices** regularly

---

© 2025 F1RSTLOOK Digital
