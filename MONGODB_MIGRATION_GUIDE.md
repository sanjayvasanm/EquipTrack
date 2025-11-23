# MongoDB Migration Guide

## Changes Completed

### 1. Dependencies
✅ Added `spring-boot-starter-data-mongodb` to pom.xml
✅ Kept `spring-boot-starter-data-jpa` (can be removed later)

### 2. Configuration
✅ Updated `application.properties`:
- Added MongoDB connection: `mongodb://localhost:27017/equiptrack_db`
- Disabled H2 and JPA configurations

✅ Created `MongoConfig.java`:
- Enabled MongoDB auditing with `@EnableMongoAuditing`
- Configured MongoDB repositories

### 3. Entities → Documents
✅ Converted all entities to MongoDB documents:
- Changed `@Entity` → `@Document(collection = "...")`
- Changed `@Id` with `@GeneratedValue` → `@Id` (String)
- Changed `Long id` → `String id`
- Removed JPA annotations (`@Table`, `@Column`, `@ManyToOne`, `@OneToMany`, etc.)
- Added `@Indexed(unique = true)` for unique fields
- Changed relationships to use ID references (String IDs)

**Converted Models:**
- ✅ User
- ✅ Category
- ✅ Location
- ✅ Equipment
- ✅ Booking
- ✅ Payment
- ✅ Notification
- ✅ MaintenanceRecord

### 4. Repositories
✅ Converted all repositories from `JpaRepository` to `MongoRepository`:
- Changed `extends JpaRepository<Entity, Long>` → `extends MongoRepository<Document, String>`
- Converted JPQL queries to MongoDB queries
- Updated all ID types from `Long` to `String`

**Converted Repositories:**
- ✅ UserRepository
- ✅ CategoryRepository
- ✅ LocationRepository
- ✅ EquipmentRepository
- ✅ BookingRepository
- ✅ PaymentRepository
- ✅ NotificationRepository
- ✅ MaintenanceRecordRepository

### 5. Controllers
⚠️ **Partially Updated:**
- ✅ BookingApiController - Updated parameter types to String

## Changes Needed

### Service Layer Updates Required
All services need to be updated to use String IDs instead of Long IDs.

**Critical Services to Update:**

#### BookingService.java
```java
// Change from:
public Optional<Booking> getBookingById(Long id)
public List<Booking> getBookingsByCustomer(Long customerId)
public List<Booking> getBookingsByEquipment(Long equipmentId)
public Booking confirmBooking(Long id, User user)
public Booking startBooking(Long id)
public Booking completeBooking(Long id)
public Booking cancelBooking(Long id, String reason, User user)
public boolean isEquipmentAvailableForDates(Long equipmentId, LocalDate startDate, LocalDate endDate)

// Change to:
public Optional<Booking> getBookingById(String id)
public List<Booking> getBookingsByCustomer(String customerId)
public List<Booking> getBookingsByEquipment(String equipmentId)
public Booking confirmBooking(String id, User user)
public Booking startBooking(String id)
public Booking completeBooking(String id)
public Booking cancelBooking(String id, String reason, User user)
public boolean isEquipmentAvailableForDates(String equipmentId, LocalDate startDate, LocalDate endDate)
```

**Also Update:**
- `createBooking()` method to work with String IDs for equipment and customer
- Change `booking.getEquipment().getId()` references to `booking.getEquipmentId()`
- Change `booking.getCustomer().getId()` references to `booking.getCustomerId()`

#### UserService.java
```java
// Change all Long id parameters to String id
public Optional<User> getUserById(String id)
public void deleteUser(String id)
```

#### EquipmentService.java
```java
// Change all Long id parameters to String id
public Optional<Equipment> getEquipmentById(String id)
public void deleteEquipment(String id)
public boolean isEquipmentAvailable(String equipmentId)
```

#### CategoryService.java
```java
public Optional<Category> getCategoryById(String id)
public void deleteCategory(String id)
```

#### LocationService.java
```java
public Optional<Location> getLocationById(String id)
public void deleteLocation(String id)
```

#### PaymentService.java
```java
public Optional<Payment> getPaymentById(String id)
```

#### NotificationService.java
```java
public void deleteNotification(String id)
```

### Controller Updates Required

All controllers need to update `@PathVariable Long id` to `@PathVariable String id`:

- WebController.java
- PaymentController.java
- All API controllers

### DataInitializer.java
Update `DataInitializer.java` to:
1. Remove all cascade save operations (MongoDB doesn't support them)
2. Save each entity individually
3. Use String IDs when referencing related entities

### Important MongoDB Differences

#### No Cascade Operations
MongoDB doesn't support JPA cascade operations. You must:
- Save parent entities first
- Get their generated IDs
- Set those IDs in child entities
- Save child entities separately

#### Relationships
```java
// OLD (JPA):
equipment.setCategory(category);
equipment.setLocation(location);

// NEW (MongoDB):
equipment.setCategoryId(category.getId());
equipment.setLocationId(location.getId());
```

#### Fetching Related Data
```java
// OLD (JPA with lazy loading):
Category category = equipment.getCategory();

// NEW (MongoDB):
String categoryId = equipment.getCategoryId();
Category category = categoryRepository.findById(categoryId).orElse(null);
```

## MongoDB Setup

### Install MongoDB
```powershell
# Using Chocolatey:
choco install mongodb

# Or download from: https://www.mongodb.com/try/download/community
```

### Start MongoDB
```powershell
# Start MongoDB service
net start MongoDB

# Or run mongod directly
mongod --dbpath="C:\data\db"
```

### Verify Connection
```powershell
# Connect with mongo shell
mongo

# Or use MongoDB Compass GUI
```

## Build and Run

After completing all service updates:

```powershell
# Clean and build
.\mvnw.cmd clean package -DskipTests

# Run application
java -jar target\equipment-rental-system-1.0.0.jar
```

## Testing

1. Start MongoDB
2. Run the application
3. Test endpoints:
   - http://localhost:8765
   - Login: customer@test.com / password123
   - Create bookings, test all features

## Common Issues

### Issue 1: Compilation Errors
**Problem:** "incompatible types: String cannot be converted to Long"
**Solution:** Update all method signatures and calls to use String IDs

### Issue 2: MongoDB Connection Failed
**Problem:** "MongoSocketOpenException: Exception opening socket"
**Solution:** Ensure MongoDB is running on localhost:27017

### Issue 3: Data Not Saving
**Problem:** Data not persisting
**Solution:** Check `@CreatedDate` and `@LastModifiedDate` work with MongoDB auditing enabled

### Issue 4: Lazy Loading Errors
**Problem:** LazyInitializationException
**Solution:** MongoDB doesn't use lazy loading. Use ID references instead.

## Next Steps

1. ⚠️ **Update all services** (see "Service Layer Updates Required")
2. ⚠️ **Update all controllers** to use String IDs
3. ⚠️ **Update DataInitializer** for MongoDB
4. ⚠️ **Install and start MongoDB**
5. ✅ **Build and test**
6. ✅ **Remove JPA dependencies** (optional cleanup)
