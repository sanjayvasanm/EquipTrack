# EquipTrack - Modern Equipment Rental Management System

![EquipTrack Logo](https://img.shields.io/badge/EquipTrack-Equipment%20Rental-blue?style=for-the-badge)
![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green?style=for-the-badge&logo=spring)
![MongoDB](https://img.shields.io/badge/MongoDB-7.0-green?style=for-the-badge&logo=mongodb)

## 📋 Overview

EquipTrack is a comprehensive equipment rental management system designed to streamline equipment rental businesses with real-time tracking, automated bookings, and seamless payment processing.

## ✨ Features

### Core Features
- **Equipment Management**: Track and manage equipment inventory with categories, locations, and status
- **Booking System**: Complete booking workflow from creation to completion
- **User Management**: Customer and admin roles with authentication
- **Payment Processing**: Integrated payment handling with transaction tracking
- **Real-time Notifications**: Keep users informed about booking status changes
- **Search & Filter**: Advanced search and filtering capabilities

### Advanced Features
- **Multi-location Support**: Manage equipment across multiple warehouses and service centers
- **Pricing Flexibility**: Daily, weekly, and monthly rental rates
- **Equipment Maintenance Tracking**: Schedule and track maintenance activities
- **Security Deposits**: Handle security deposit management
- **Email Notifications**: Automated email for booking confirmations and updates
- **Responsive Design**: Modern UI that works on all devices
- **Dashboard Analytics**: Track bookings, revenue, and equipment utilization

## 🛠️ Technology Stack

### Backend
- **Java 17**: Core programming language
- **Spring Boot 3.2.0**: Application framework
- **Spring Data MongoDB**: Database access
- **Spring Security**: Authentication and authorization
- **MongoDB**: NoSQL Database
- **Spring Scheduling**: Automated maintenance tasks
- **Lombok**: Reduce boilerplate code
- **JWT**: Token-based authentication

### Frontend
- **Thymeleaf**: Server-side template engine
- **HTML5/CSS3**: Markup and styling
- **JavaScript**: Client-side functionality
- **Responsive Design**: Mobile-first approach

## 📦 Project Structure

```
rental/
├── src/
│   ├── main/
│   │   ├── java/com/equiptrack/
│   │   │   ├── config/              # Configuration classes
│   │   │   ├── controller/          # Web & API controllers
│   │   │   │   └── api/            # REST API endpoints
│   │   │   ├── model/               # Domain entities
│   │   │   ├── repository/          # Data access layer
│   │   │   ├── service/             # Business logic
│   │   │   └── EquipTrackApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/            # Stylesheets
│   │       │   └── js/             # JavaScript files
│   │       ├── templates/          # Thymeleaf templates
│   │       └── application.properties
│   └── test/                        # Test files
└── pom.xml                          # Maven dependencies
```

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MongoDB 7.0+ (or MongoDB Atlas for cloud database)
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/sanjayvasanm/EquipTrack.git
cd rental_equipment_construction
```

2. **Configure MongoDB Database**

**Option A: Local MongoDB**
- Install MongoDB from https://www.mongodb.com/try/download/community
- Start MongoDB service:
  - **Windows**: MongoDB should start automatically, or run `net start MongoDB`
  - **Mac**: `brew services start mongodb-community`
  - **Linux**: `sudo systemctl start mongod`

**Option B: MongoDB Atlas (Cloud)**
- Create a free account at https://www.mongodb.com/cloud/atlas
- Create a cluster and get your connection string

3. **Configure Application Properties**

Update `src/main/resources/application.properties`:

**For Local MongoDB:**
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/equiptrack
```

**For MongoDB Atlas:**
```properties
spring.data.mongodb.uri=mongodb+srv://<username>:<password>@cluster.mongodb.net/equiptrack
```

4. **Build the project**
```bash
mvn clean install
```

5. **Run the application**

**Option 1: Using Maven**
```bash
mvn spring-boot:run
```

**Option 2: Using Java JAR**
```bash
java -jar target/equipment-rental-system-1.0.0.jar
```

**Option 3: Using IDE**
- Open the project in your IDE
- Run `EquipTrackApplication.java` main class

**Option 4: Using Windows Command (mvnw)**
```bash
.\mvnw.cmd spring-boot:run
```

6. **Access the application**
- **URL**: http://localhost:8080
- **Admin Login**: admin@equiptrack.com / admin123
- **Customer Login**: customer@test.com / customer123

### First Time Setup

On first run, the application will automatically:
- Create the MongoDB database and collections
- Initialize sample data (categories, locations, equipment)
- Create default admin and customer accounts
- Set up indexes for optimal performance

## 📱 User Interface

### Home Page
- Hero section with call-to-action
- Featured equipment showcase
- Why Choose EquipTrack section
- Responsive navigation

### Browse Equipment
- Equipment grid with search and filters
- Category and location filtering
- Real-time status indicators
- Detailed equipment cards

### Equipment Details
- Full equipment information
- Booking calendar
- Pricing calculator
- Customer reviews (coming soon)

### My Bookings
- Dashboard with statistics
- Booking history
- Status tracking
- Cancellation management

### Authentication
- Login page
- Registration with email verification
- Password reset functionality
- Secure session management

## 🔑 Default Login Credentials

### Admin Account
- **Email**: admin@equiptrack.com
- **Password**: admin123
- **Role**: ADMIN

### Test Customer Account
- **Email**: customer@test.com
- **Password**: customer123
- **Role**: CUSTOMER

## 📊 Database Schema

### MongoDB Collections
- **users**: Customer and admin accounts with authentication
- **equipment**: Rental equipment items with status tracking
- **bookings**: Rental bookings with lifecycle management
- **categories**: Equipment categories for classification
- **locations**: Storage/pickup warehouse locations
- **payments**: Payment transactions and processing
- **maintenanceRecords**: Equipment maintenance history
- **notifications**: User notifications and alerts

### Key Features
- **Automatic Status Management**: Equipment status automatically changes (AVAILABLE → RENTED → MAINTENANCE → AVAILABLE)
- **Scheduled Maintenance**: Automated maintenance completion after 24 hours
- **Role-based Security**: Method-level authorization for admin operations
- **Location Tracking**: Warehouse addresses visible to customers for pickup

## 🔐 Security

- **Spring Security**: Authentication and authorization
- **Password Encryption**: BCrypt password hashing
- **JWT Tokens**: Secure API authentication
- **Role-based Access Control**: Customer and Admin roles
- **Email Verification**: Verify user email addresses

## 📧 Email Configuration

Update in `application.properties`:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

## 🎨 Customization

### Adding New Equipment
```java
Equipment equipment = new Equipment();
equipment.setName("Equipment Name");
equipment.setDailyRate(new BigDecimal("100.00"));
// ... set other properties
equipmentRepository.save(equipment);
```

### Creating Custom Categories
```java
Category category = new Category();
category.setName("Category Name");
category.setCode("CODE");
categoryRepository.save(category);
```

## 📈 API Endpoints

### Equipment
- `GET /api/equipment` - List all equipment
- `GET /api/equipment/available` - Available equipment
- `GET /api/equipment/{id}` - Get equipment details
- `GET /api/equipment/search?keyword=` - Search equipment
- `POST /api/equipment` - Create equipment (Admin)
- `PUT /api/equipment/{id}` - Update equipment (Admin)

### Bookings
- `GET /api/bookings` - List all bookings
- `GET /api/bookings/customer/{customerId}` - Customer bookings
- `POST /api/bookings` - Create booking
- `PUT /api/bookings/{id}/confirm` - Confirm booking
- `PUT /api/bookings/{id}/cancel` - Cancel booking

### Users
- `POST /api/users/register` - Register new user
- `POST /api/users/verify-email?token=` - Verify email
- `POST /api/users/forgot-password?email=` - Request password reset
- `GET /api/users/me` - Get current user

## 🧪 Testing

Run tests:
```bash
mvn test
```

## 🐛 Troubleshooting

### Database Connection Issues
- **Verify MongoDB is running**:
  - Windows: Check Services or run `mongod --version`
  - Mac/Linux: Run `sudo systemctl status mongod`
- **Check connection string** in `application.properties`
- **MongoDB Atlas**: Ensure your IP is whitelisted in Atlas Network Access
- **Check credentials** if using authentication

### Port Already in Use
- Change server port in `application.properties`:
```properties
server.port=8081
```
- Default MongoDB port is 27017, change if needed

### Application Won't Start
- Ensure Java 17+ is installed: `java -version`
- Clean and rebuild: `mvn clean install`
- Check MongoDB is accessible: `mongosh` or MongoDB Compass
- Review logs for specific error messages

### Email Not Sending
- Enable "Less secure app access" for Gmail
- Use App Password for Gmail accounts with 2FA
- Check firewall settings

## 🔄 Future Enhancements

- [ ] Equipment reviews and ratings
- [ ] Advanced analytics dashboard
- [ ] Mobile app (iOS/Android)
- [ ] Stripe payment integration
- [ ] GPS tracking for equipment
- [ ] Barcode/QR code scanning
- [ ] Multi-language support
- [ ] Automated reminders and notifications
- [ ] Contract generation
- [ ] Insurance management

## 📝 License

This project is developed for educational purposes.

## 👥 Support

For support, email support@equiptrack.com

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Thymeleaf for the template engine
- All open-source contributors

---

**Built with ❤️ using Java and Spring Boot**
