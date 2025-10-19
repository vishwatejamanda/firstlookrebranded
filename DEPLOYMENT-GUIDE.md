# 🚀 Deployment Guide - F1RSTLOOK Digital

## Production Deployment Options

---

## Option 1: Heroku (Easiest) ☁️

### Prerequisites
- Heroku account (free tier available)
- Heroku CLI installed

### Steps

1. **Install Heroku CLI**
```bash
# Ubuntu/Debian
curl https://cli-assets.heroku.com/install.sh | sh

# macOS
brew tap heroku/brew && brew install heroku
```

2. **Login to Heroku**
```bash
heroku login
```

3. **Create Heroku App**
```bash
cd /home/vishwatejamanda/Pictures/DOC-20251014-WA0006/firstlookver2-master
heroku create firstlook-digital
```

4. **Add PostgreSQL Database**
```bash
heroku addons:create heroku-postgresql:mini
```

5. **Configure Environment Variables**
```bash
heroku config:set SPRING_PROFILES_ACTIVE=prod
heroku config:set SPRING_MAIL_USERNAME=your-email@gmail.com
heroku config:set SPRING_MAIL_PASSWORD=your-app-password
```

6. **Create Procfile**
```bash
echo "web: java -jar target/firstlook-digital-1.0.0.jar" > Procfile
```

7. **Deploy**
```bash
git init
git add .
git commit -m "Initial commit"
git push heroku master
```

8. **Open Your App**
```bash
heroku open
```

**Your site is live!** 🎉

---

## Option 2: AWS Elastic Beanstalk 🌐

### Prerequisites
- AWS account
- AWS CLI installed

### Steps

1. **Install AWS CLI**
```bash
pip install awscli awsebcli
```

2. **Configure AWS**
```bash
aws configure
```

3. **Initialize EB**
```bash
cd /home/vishwatejamanda/Pictures/DOC-20251014-WA0006/firstlookver2-master
eb init -p java-17 firstlook-digital
```

4. **Create Environment**
```bash
eb create firstlook-production
```

5. **Set Environment Variables**
```bash
eb setenv SPRING_PROFILES_ACTIVE=prod
eb setenv SPRING_MAIL_USERNAME=your-email@gmail.com
eb setenv SPRING_MAIL_PASSWORD=your-app-password
```

6. **Deploy**
```bash
mvn clean package
eb deploy
```

7. **Open Application**
```bash
eb open
```

---

## Option 3: DigitalOcean Droplet 💧

### Prerequisites
- DigitalOcean account
- SSH access to droplet

### Steps

1. **Create Droplet**
- Choose Ubuntu 22.04
- Select plan ($6/month minimum)
- Add SSH key

2. **Connect to Droplet**
```bash
ssh root@your-droplet-ip
```

3. **Install Java**
```bash
apt update
apt install openjdk-17-jdk -y
```

4. **Upload Application**
```bash
# On your local machine
scp target/firstlook-digital-1.0.0.jar root@your-droplet-ip:/opt/
```

5. **Create Systemd Service**
```bash
# On droplet
cat > /etc/systemd/system/firstlook.service << EOF
[Unit]
Description=F1RSTLOOK Digital
After=syslog.target

[Service]
User=root
ExecStart=/usr/bin/java -jar /opt/firstlook-digital-1.0.0.jar
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
EOF
```

6. **Start Service**
```bash
systemctl enable firstlook
systemctl start firstlook
```

7. **Install Nginx**
```bash
apt install nginx -y
```

8. **Configure Nginx**
```bash
cat > /etc/nginx/sites-available/firstlook << EOF
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
    }
}
EOF

ln -s /etc/nginx/sites-available/firstlook /etc/nginx/sites-enabled/
systemctl restart nginx
```

---

## Option 4: Docker Container 🐳

### Prerequisites
- Docker installed

### Steps

1. **Create Dockerfile**
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/firstlook-digital-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

2. **Build Image**
```bash
mvn clean package
docker build -t firstlook-digital .
```

3. **Run Container**
```bash
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_MAIL_USERNAME=your-email@gmail.com \
  -e SPRING_MAIL_PASSWORD=your-app-password \
  --name firstlook \
  firstlook-digital
```

4. **Push to Docker Hub (Optional)**
```bash
docker tag firstlook-digital yourusername/firstlook-digital
docker push yourusername/firstlook-digital
```

---

## Production Configuration

### 1. Database Configuration

**For MySQL:**
```properties
# application-prod.properties
spring.datasource.url=jdbc:mysql://localhost:3306/firstlook
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
```

**For PostgreSQL:**
```properties
# application-prod.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/firstlook
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

### 2. Security Configuration

**Add to pom.xml:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

**Create SecurityConfig.java:**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/admin/dashboard")
            );
        return http.build();
    }
}
```

### 3. Environment Variables

**Never hardcode sensitive data!**

```bash
# Set environment variables
export SPRING_MAIL_USERNAME=your-email@gmail.com
export SPRING_MAIL_PASSWORD=your-app-password
export DB_PASSWORD=your-db-password
```

**In application.properties:**
```properties
spring.mail.username=${SPRING_MAIL_USERNAME}
spring.mail.password=${SPRING_MAIL_PASSWORD}
spring.datasource.password=${DB_PASSWORD}
```

---

## SSL/HTTPS Setup

### Using Let's Encrypt (Free)

1. **Install Certbot**
```bash
apt install certbot python3-certbot-nginx -y
```

2. **Get Certificate**
```bash
certbot --nginx -d your-domain.com
```

3. **Auto-renewal**
```bash
certbot renew --dry-run
```

---

## Monitoring & Logging

### 1. Application Logs

**View logs:**
```bash
# Heroku
heroku logs --tail

# Systemd
journalctl -u firstlook -f

# Docker
docker logs -f firstlook
```

### 2. Add Spring Boot Actuator

**pom.xml:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

**application.properties:**
```properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
```

**Access:**
- Health: http://your-domain.com/actuator/health
- Metrics: http://your-domain.com/actuator/metrics

---

## Backup Strategy

### 1. Database Backup

**MySQL:**
```bash
# Backup
mysqldump -u username -p firstlook > backup.sql

# Restore
mysql -u username -p firstlook < backup.sql
```

**PostgreSQL:**
```bash
# Backup
pg_dump firstlook > backup.sql

# Restore
psql firstlook < backup.sql
```

### 2. Automated Backups

**Cron job (daily at 2 AM):**
```bash
crontab -e
# Add:
0 2 * * * /usr/bin/mysqldump -u username -p'password' firstlook > /backups/firstlook-$(date +\%Y\%m\%d).sql
```

---

## Performance Optimization

### 1. JVM Options

```bash
java -Xms512m -Xmx1024m -jar firstlook-digital-1.0.0.jar
```

### 2. Database Connection Pool

**application.properties:**
```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

### 3. Caching

**Add to pom.xml:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

---

## Domain Setup

### 1. Buy Domain
- Namecheap
- GoDaddy
- Google Domains

### 2. Configure DNS

**A Record:**
```
Type: A
Host: @
Value: your-server-ip
TTL: 3600
```

**CNAME Record:**
```
Type: CNAME
Host: www
Value: your-domain.com
TTL: 3600
```

---

## Scaling

### Horizontal Scaling

**Load Balancer + Multiple Instances:**
```
                    ┌─────────────┐
Internet ──────────►│Load Balancer│
                    └──────┬──────┘
                           │
              ┌────────────┼────────────┐
              │            │            │
         ┌────▼───┐   ┌────▼───┐   ┌────▼───┐
         │Instance│   │Instance│   │Instance│
         │   1    │   │   2    │   │   3    │
         └────────┘   └────────┘   └────────┘
```

### Vertical Scaling

**Increase server resources:**
- More RAM
- More CPU cores
- Faster disk (SSD)

---

## Troubleshooting Production

### Issue: Application won't start

**Check:**
```bash
# Logs
journalctl -u firstlook -n 100

# Port availability
netstat -tulpn | grep 8080

# Java version
java -version
```

### Issue: Database connection failed

**Check:**
```bash
# Database running
systemctl status mysql

# Connection string
echo $DATABASE_URL

# Firewall
ufw status
```

### Issue: High memory usage

**Solution:**
```bash
# Limit JVM memory
java -Xmx512m -jar app.jar

# Monitor
top
htop
```

---

## Security Checklist

- [ ] Change default passwords
- [ ] Enable HTTPS
- [ ] Add authentication to admin
- [ ] Use environment variables
- [ ] Enable firewall
- [ ] Regular updates
- [ ] Backup strategy
- [ ] Monitor logs
- [ ] Rate limiting
- [ ] Input validation

---

## Cost Estimates

### Heroku
- Free tier: $0/month (limited)
- Hobby: $7/month
- Standard: $25/month

### AWS
- t2.micro: $8-10/month
- t2.small: $17-20/month
- RDS: $15-30/month

### DigitalOcean
- Basic: $6/month
- Standard: $12/month
- Premium: $24/month

### Domain
- .com: $10-15/year
- .digital: $30-40/year

---

## Support Resources

- Spring Boot Docs: https://spring.io/projects/spring-boot
- Heroku Docs: https://devcenter.heroku.com/
- AWS Docs: https://docs.aws.amazon.com/
- DigitalOcean Tutorials: https://www.digitalocean.com/community/tutorials

---

## Final Checklist

Before going live:
- [ ] Test all features
- [ ] Configure production database
- [ ] Set up email service
- [ ] Enable HTTPS
- [ ] Add authentication
- [ ] Set up monitoring
- [ ] Configure backups
- [ ] Test on mobile
- [ ] Load testing
- [ ] Security audit

---

**Ready to deploy? Choose your platform and follow the steps above!** 🚀

© 2025 F1RSTLOOK Digital
