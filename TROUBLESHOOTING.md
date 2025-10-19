# 🔧 Troubleshooting Guide

## White Label Error on Admin Panel

### Possible Causes & Solutions:

### 1. **Template Not Found**
**Error**: Whitelabel Error Page
**Cause**: Template file doesn't exist or wrong name

**Solution**:
Check if these files exist:
- `src/main/resources/templates/admin/login.html` ✓
- `src/main/resources/templates/admin/dashboard-new.html` ✓
- `src/main/resources/templates/admin/contacts.html` ✓

### 2. **Service Dependency Issue**
**Error**: Bean creation error
**Cause**: Missing or circular dependency

**Solution**:
Make sure all services are available:
- FormAnalyticsService
- LeadScoringService  
- CustomerJourneyService

### 3. **Database Not Initialized**
**Error**: Table doesn't exist
**Cause**: H2 database tables not created

**Solution**:
Check `application.properties`:
```properties
spring.jpa.hibernate.ddl-auto=update
```

### 4. **Security Configuration Issue**
**Error**: 403 Forbidden or redirect loop
**Cause**: Security rules blocking access

**Solution**: Already fixed in SecurityConfig.java

---

## Quick Fix Steps:

### Step 1: Clean and Rebuild
```bash
cd /home/vishwatejamanda/Pictures/DOC-20251014-WA0006/firstlookver2-master
mvn clean install -DskipTests
```

### Step 2: Start Application
```bash
./run.sh
```

### Step 3: Check Logs
Look for errors in console output

### Step 4: Test Login
```
URL: http://localhost:8080/admin/login
Username: admin
Password: admin123
```

---

## Common Errors:

### Error: "Whitelabel Error Page"
**Meaning**: Controller returned a view name that doesn't exist

**Check**:
1. Template file exists
2. File name matches return value
3. File is in correct directory

### Error: "Bean creation failed"
**Meaning**: Spring can't create a required service

**Check**:
1. All @Service classes have @RequiredArgsConstructor
2. No circular dependencies
3. All repositories exist

### Error: "404 Not Found"
**Meaning**: No controller mapping for URL

**Check**:
1. Controller has @RequestMapping
2. Method has @GetMapping
3. URL matches exactly

---

## If Still Not Working:

### Option 1: Use Old Dashboard (Temporary)
Change in `AnalyticsController.java`:
```java
return "admin/dashboard";  // Instead of "admin/dashboard-new"
```

### Option 2: Check Application Logs
```bash
tail -f logs/spring-boot-application.log
```

### Option 3: Test Individual Endpoints
```bash
curl http://localhost:8080/admin/login
curl -u admin:admin123 http://localhost:8080/admin/dashboard
```

---

## Contact
If issue persists, check:
- Console output for stack trace
- Browser console for JavaScript errors
- Network tab for failed requests
