# 🔐 Admin Access Guide - F1RSTLOOK Digital

## Admin Login Information

### 🔗 Admin Login URL
```
http://localhost:8080/admin/login
```

### 🔑 Default Credentials
```
Username: admin
Password: admin123
```

⚠️ **IMPORTANT**: Change these credentials before deploying to production!

---

## 📍 Access Points

### Public (No Login Required)
- **Website Homepage**: http://localhost:8080
- **Contact Form**: http://localhost:8080 (scroll to bottom)
- **WhatsApp Button**: Visible on all pages

### Protected (Login Required)
- **Admin Dashboard**: http://localhost:8080/admin/dashboard
- **Analytics**: http://localhost:8080/admin/dashboard

---

## 🚀 How to Access Admin Dashboard

### Step 1: Start the Application
```bash
./run.sh
# OR
mvn spring-boot:run
```

### Step 2: Open Browser
Navigate to:
```
http://localhost:8080/admin/login
```

### Step 3: Login
- Enter username: `admin`
- Enter password: `admin123`
- Click "🔐 Login to Dashboard"

### Step 4: View Dashboard
You'll be redirected to:
```
http://localhost:8080/admin/dashboard
```

---

## 📊 What You Can See in Dashboard

### Overview Metrics
- **Total Visitors**: All page visits
- **Unique Visitors**: Unique IP addresses
- **WhatsApp Clicks**: Times WhatsApp button clicked
- **Contact Messages**: Form submissions

### Recent Visitors Table
- IP Address
- Device Type (Mobile/Tablet/Desktop)
- Browser
- Page Visited
- Visit Time

### Contact Messages
- Name
- Email
- Subject
- Message
- Status
- Submission Time

### Device Analytics
- Breakdown by device type
- Mobile vs Desktop statistics

---

## 🔒 Security Features

### What's Protected
✅ Admin dashboard requires login
✅ Password is encrypted (BCrypt)
✅ Session management
✅ Automatic logout on browser close
✅ CSRF protection

### What's Public
✅ Homepage and all content
✅ Contact form submission
✅ WhatsApp button
✅ All APIs for tracking

---

## 🔄 Logout

### From Dashboard
Click the **"🚪 Logout"** button in the top-right corner

### Manual Logout
Navigate to:
```
http://localhost:8080/admin/logout
```

---

## 🔧 Changing Admin Credentials

### Method 1: Edit SecurityConfig.java

**File**: `src/main/java/com/firstlook/config/SecurityConfig.java`

Find this section:
```java
@Bean
public UserDetailsService userDetailsService() {
    UserDetails admin = User.builder()
        .username("admin")              // ← Change username here
        .password(passwordEncoder().encode("admin123"))  // ← Change password here
        .roles("ADMIN")
        .build();
    
    return new InMemoryUserDetailsManager(admin);
}
```

**Change to:**
```java
@Bean
public UserDetailsService userDetailsService() {
    UserDetails admin = User.builder()
        .username("yourname")           // Your new username
        .password(passwordEncoder().encode("YourSecurePassword123!"))  // Your new password
        .roles("ADMIN")
        .build();
    
    return new InMemoryUserDetailsManager(admin);
}
```

**Restart the application** after making changes.

### Method 2: Use Environment Variables (Recommended for Production)

**Update SecurityConfig.java:**
```java
@Value("${admin.username:admin}")
private String adminUsername;

@Value("${admin.password:admin123}")
private String adminPassword;

@Bean
public UserDetailsService userDetailsService() {
    UserDetails admin = User.builder()
        .username(adminUsername)
        .password(passwordEncoder().encode(adminPassword))
        .roles("ADMIN")
        .build();
    
    return new InMemoryUserDetailsManager(admin);
}
```

**Set environment variables:**
```bash
export ADMIN_USERNAME=yourname
export ADMIN_PASSWORD=YourSecurePassword123!
```

**Or in application.properties:**
```properties
admin.username=yourname
admin.password=YourSecurePassword123!
```

---

## 👥 Adding Multiple Admin Users

**Update SecurityConfig.java:**
```java
@Bean
public UserDetailsService userDetailsService() {
    UserDetails admin1 = User.builder()
        .username("admin")
        .password(passwordEncoder().encode("admin123"))
        .roles("ADMIN")
        .build();
    
    UserDetails admin2 = User.builder()
        .username("manager")
        .password(passwordEncoder().encode("manager123"))
        .roles("ADMIN")
        .build();
    
    return new InMemoryUserDetailsManager(admin1, admin2);
}
```

---

## 🐛 Troubleshooting

### Problem: Can't access admin dashboard
**Solution**: Make sure you're logged in first at `/admin/login`

### Problem: Invalid credentials error
**Solution**: 
- Check username is exactly: `admin`
- Check password is exactly: `admin123`
- Case-sensitive!

### Problem: Redirected to login after accessing dashboard
**Solution**: Your session expired. Login again.

### Problem: Changes to credentials not working
**Solution**: 
1. Stop the application (Ctrl+C)
2. Clean and rebuild: `mvn clean install`
3. Restart: `./run.sh`

### Problem: Forgot admin password
**Solution**: 
1. Edit `SecurityConfig.java`
2. Change password in the code
3. Rebuild and restart

---

## 🌐 Production Deployment

### Before Going Live

1. **Change Default Credentials**
   ```java
   .username("your-secure-username")
   .password(passwordEncoder().encode("YourVerySecurePassword123!@#"))
   ```

2. **Use Environment Variables**
   ```bash
   heroku config:set ADMIN_USERNAME=yourname
   heroku config:set ADMIN_PASSWORD=SecurePass123
   ```

3. **Enable HTTPS**
   - Use SSL certificate
   - Force HTTPS redirect

4. **Add Rate Limiting**
   - Prevent brute force attacks
   - Limit login attempts

5. **Consider Database-backed Users**
   - Store users in database
   - Allow password reset
   - Add user management

---

## 📱 Mobile Access

### Access from Phone
1. Find your computer's IP:
   ```bash
   ifconfig | grep inet
   # Example: 192.168.1.100
   ```

2. On phone browser:
   ```
   http://192.168.1.100:8080/admin/login
   ```

3. Login with same credentials

---

## 🔐 Security Best Practices

### DO ✅
- Change default credentials immediately
- Use strong passwords (12+ characters)
- Use HTTPS in production
- Logout when done
- Keep credentials secret
- Use environment variables
- Enable two-factor authentication (future)

### DON'T ❌
- Share admin credentials
- Use simple passwords
- Leave logged in on public computers
- Hardcode passwords in code
- Commit credentials to Git
- Use same password for multiple services

---

## 📊 Session Management

### Session Duration
- Default: 30 minutes of inactivity
- Closes on browser close
- Can be extended in configuration

### Extending Session
**In application.properties:**
```properties
server.servlet.session.timeout=60m
```

---

## 🆘 Emergency Access

### If Locked Out
1. Stop the application
2. Edit `SecurityConfig.java`
3. Reset credentials to known values
4. Rebuild: `mvn clean install`
5. Restart: `./run.sh`
6. Login with new credentials

### Disable Authentication (Emergency Only)
**Comment out in SecurityConfig.java:**
```java
// .authorizeHttpRequests(auth -> auth
//     .requestMatchers("/admin/**").authenticated()
//     .requestMatchers("/api/**", "/", "/**").permitAll()
// )
```

⚠️ **Only for emergency! Re-enable immediately!**

---

## 📝 Quick Reference

| Item | Value |
|------|-------|
| Login URL | http://localhost:8080/admin/login |
| Dashboard URL | http://localhost:8080/admin/dashboard |
| Default Username | admin |
| Default Password | admin123 |
| Logout URL | http://localhost:8080/admin/logout |
| Session Timeout | 30 minutes |

---

## 🎓 Training for Team

### For New Admins
1. Provide login URL
2. Give credentials securely (not via email!)
3. Show dashboard features
4. Explain logout procedure
5. Emphasize security importance

### For Developers
1. Never commit credentials
2. Use environment variables
3. Test authentication locally
4. Document any changes
5. Follow security guidelines

---

## 📞 Support

If you have issues accessing the admin panel:
- Check this guide first
- Verify application is running
- Try clearing browser cache
- Restart the application
- Contact technical support

---

## ✅ Admin Access Checklist

Before using admin panel:
- [ ] Application is running
- [ ] Know the login URL
- [ ] Have correct credentials
- [ ] Browser is up to date
- [ ] Network connection is stable

After logging in:
- [ ] Dashboard loads correctly
- [ ] Can see visitor data
- [ ] Can view contact messages
- [ ] Logout button works
- [ ] Remember to logout when done

---

**🎉 You're ready to access the admin dashboard!**

Navigate to: **http://localhost:8080/admin/login**

---

© 2025 F1RSTLOOK Digital - Secure Admin Access
