# F1RSTLOOK Digital - Spring Boot Application

## Overview
This is a Spring Boot conversion of the F1RSTLOOK Digital website with enhanced features including:
- ✅ Visitor tracking and analytics
- ✅ WhatsApp integration with click tracking
- ✅ Contact form with email notifications
- ✅ Admin dashboard for monitoring
- ✅ Database persistence (H2)
- ✅ RESTful APIs

## Features

### 1. **WhatsApp Integration**
- Floating WhatsApp button on every page
- One-click contact via WhatsApp
- Automatic tracking of WhatsApp clicks
- Pre-filled message template
- Phone number: +919494385675

### 2. **Visitor Tracking**
- Automatic visitor tracking on page load
- IP address detection
- Device type detection (Mobile/Tablet/Desktop)
- Browser detection
- Referrer tracking
- Timestamp logging

### 3. **Contact Form**
- Modern AJAX-based form submission
- Email notifications
- Form validation
- Success/Error messages
- Database storage

### 4. **Admin Dashboard**
- Real-time analytics at `/admin/dashboard`
- Total and unique visitor counts
- WhatsApp click statistics
- Recent visitors list
- Contact messages inbox
- Device type breakdown

## Prerequisites
- Java 17 or higher
- Maven 3.6+
- (Optional) MySQL/PostgreSQL for production

## Installation & Setup

### 1. Clone and Navigate
```bash
cd /home/vishwatejamanda/Pictures/DOC-20251014-WA0006/firstlookver2-master
```

### 2. Configure Email (Optional)
Edit `src/main/resources/application.properties`:
```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

**To get Gmail App Password:**
1. Go to Google Account Settings
2. Security → 2-Step Verification
3. App passwords → Generate new password
4. Use the generated password in application.properties

### 3. Build the Application
```bash
mvn clean install
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

Or run the JAR:
```bash
java -jar target/firstlook-digital-1.0.0.jar
```

### 5. Access the Application
- **Website**: http://localhost:8080
- **Admin Dashboard**: http://localhost:8080/admin/dashboard
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:./data/firstlook`
  - Username: `sa`
  - Password: (leave empty)

## API Endpoints

### Contact Form
```
POST /api/contact/submit
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "subject": "Inquiry",
  "message": "Hello..."
}
```

### WhatsApp Tracking
```
POST /api/whatsapp/track?sourcePage=home
```

### WhatsApp URL Generator
```
GET /api/whatsapp/url?message=Custom message
```

## Database Schema

### Visitors Table
- `id` - Primary key
- `ip_address` - Visitor IP
- `user_agent` - Browser info
- `visit_time` - Timestamp
- `page_visited` - URL path
- `referrer` - Referring URL
- `country` - Country (future)
- `city` - City (future)
- `device_type` - Mobile/Tablet/Desktop
- `browser` - Browser name

### Contact Messages Table
- `id` - Primary key
- `name` - Contact name
- `email` - Contact email
- `subject` - Message subject
- `message` - Message content
- `submitted_at` - Timestamp
- `status` - NEW/READ/REPLIED
- `ip_address` - Sender IP

### WhatsApp Clicks Table
- `id` - Primary key
- `ip_address` - Clicker IP
- `clicked_at` - Timestamp
- `source_page` - Page where clicked
- `user_agent` - Browser info

## Configuration

### WhatsApp Settings
Edit `application.properties`:
```properties
whatsapp.phone.number=919494385675
whatsapp.default.message=Hi! I'm interested in F1RSTLOOK Digital services.
```

### Database (Production)
For MySQL/PostgreSQL, update:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/firstlook
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

## Deployment

### 1. Package for Production
```bash
mvn clean package -DskipTests
```

### 2. Run with Production Profile
```bash
java -jar target/firstlook-digital-1.0.0.jar --spring.profiles.active=prod
```

### 3. Deploy to Cloud
- **Heroku**: Add `Procfile` with `web: java -jar target/firstlook-digital-1.0.0.jar`
- **AWS**: Use Elastic Beanstalk with Java platform
- **Docker**: Create Dockerfile and deploy to any container platform

## Project Structure
```
src/
├── main/
│   ├── java/com/firstlook/
│   │   ├── FirstLookApplication.java
│   │   ├── controller/
│   │   │   ├── HomeController.java
│   │   │   ├── ContactController.java
│   │   │   ├── WhatsAppController.java
│   │   │   └── AnalyticsController.java
│   │   ├── model/
│   │   │   ├── Visitor.java
│   │   │   ├── ContactMessage.java
│   │   │   └── WhatsAppClick.java
│   │   ├── repository/
│   │   │   ├── VisitorRepository.java
│   │   │   ├── ContactMessageRepository.java
│   │   │   └── WhatsAppClickRepository.java
│   │   └── service/
│   │       ├── VisitorService.java
│   │       ├── ContactService.java
│   │       └── WhatsAppService.java
│   └── resources/
│       ├── application.properties
│       ├── static/
│       │   ├── css/
│       │   ├── js/
│       │   └── images/
│       └── templates/
│           ├── index.html
│           └── admin/
│               └── dashboard.html
```

## Key Features Explained

### 1. Visitor Tracking
Every page visit is automatically tracked with:
- IP address (with proxy support)
- Device type detection from User-Agent
- Browser detection
- Timestamp

### 2. WhatsApp Integration
- Floating button always visible
- Tracks clicks before redirecting
- Opens WhatsApp with pre-filled message
- Works on mobile and desktop

### 3. Contact Form
- AJAX submission (no page reload)
- Real-time validation
- Email notification to admin
- Stores in database for tracking

### 4. Analytics Dashboard
- View all visitor data
- Monitor contact form submissions
- Track WhatsApp engagement
- Device and browser statistics

## Troubleshooting

### Port Already in Use
```bash
# Change port in application.properties
server.port=8081
```

### Email Not Sending
1. Enable "Less secure app access" in Gmail (not recommended)
2. OR use App Password (recommended)
3. Check firewall settings

### Database Issues
```bash
# Delete database and restart
rm -rf data/
mvn spring-boot:run
```

## Future Enhancements
- [ ] Add authentication for admin dashboard
- [ ] Integrate IP geolocation API
- [ ] Add export to CSV functionality
- [ ] Email campaign integration
- [ ] Advanced analytics charts
- [ ] Multi-language support

## Support
For issues or questions, contact:
- **Phone**: +919494385675
- **WhatsApp**: Click the floating button on the website

## License
© 2025 F1RSTLOOK Digital. All rights reserved.
