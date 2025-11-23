# EquipTrack - Modern Equipment Rental Management System

![EquipTrack Logo](https://img.shields.io/badge/EquipTrack-Equipment%20Rental-blue?style=for-the-badge)
![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green?style=for-the-badge&logo=spring)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)

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
- **Spring Data JPA**: Database access
- **Spring Security**: Authentication and authorization
- **MySQL**: Database
- **Hibernate**: ORM
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
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

### Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd rental
```

2. **Configure Database**

Create a MySQL database:
```sql
CREATE DATABASE equiptrack_db;
```

Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/equiptrack_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. **Build the project**
```bash
mvn clean install
```

4. **Run the application**
```bash
mvn spring-boot:run
```

Or use your IDE to run `EquipTrackApplication.java`

5. **Access the application**
- **URL**: http://localhost:8080
- **Admin Login**: admin@equiptrack.com / admin123
- **Customer Login**: customer@test.com / customer123

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

### Main Entities
- **User**: Customer and admin accounts
- **Equipment**: Rental equipment items
- **Booking**: Rental bookings
- **Category**: Equipment categories
- **Location**: Storage/pickup locations
- **Payment**: Payment transactions
- **Maintenance Record**: Equipment maintenance tracking
- **Notification**: User notifications

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
- Verify MySQL is running
- Check credentials in `application.properties`
- Ensure database exists

### Port Already in Use
- Change port in `application.properties`:
```properties
server.port=8081
```

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
