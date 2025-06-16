# E-Commerce Console Application

A comprehensive Java console-based e-commerce application with user management, product catalog, order processing, and secure authentication system.

## Features

### 🔐 Authentication & Session Management
- User registration with secure password hashing
- User login with session persistence
- Automatic session restoration on application restart
- Secure logout functionality
- Password validation and strength requirements

### 👥 User Management
- Create new user accounts
- Update user profiles (name, email, address, phone)
- View user account details
- Role-based access (Customer, Admin)
- User account status management (Active/Inactive)

### 📦 Product Management
- Add new products to catalog
- Update existing product information
- Delete products from inventory
- Search products by name, category, or price range
- Manage product stock levels
- Category-based product organization
- Product rating and review system

### 🛒 Order Management
- Create new orders from shopping cart
- View order history for users
- Update order status (Pending, Processing, Shipped, Delivered, Cancelled)
- Order tracking with timestamps
- Calculate order totals with tax and shipping
- Generate order receipts
- Admin order management dashboard

## Technology Stack

- **Language**: Java
- **Database**: PostgreSQL (deployed on Render)
- **JDBC Driver**: PostgreSQL JDBC Driver
- **Build Tool**: Gradle
- **Authentication**: BCrypt for password hashing
- **Session Management**: File-based session storage

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    role VARCHAR(20) DEFAULT 'CUSTOMER',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Products Table
```sql
CREATE TABLE products (
    product_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    category VARCHAR(50),
    image_url VARCHAR(255),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Orders Table
```sql
CREATE TABLE orders (
    order_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id),
    total_amount DECIMAL(10,2) NOT NULL,
    tax_amount DECIMAL(10,2) DEFAULT 0,
    shipping_amount DECIMAL(10,2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'PENDING',
    shipping_address TEXT,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Order Items Table
```sql
CREATE TABLE order_items (
    item_id SERIAL PRIMARY KEY,
    order_id INTEGER REFERENCES orders(order_id),
    product_id INTEGER REFERENCES products(product_id),
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL
);
```

## Project Structure

```
e-commerce/
└── src/
    └── main/
        └── java/
            ├── controller/
            │   ├── CartController.java
            │   ├── OrderController.java
            │   ├── ProductController.java
            │   └── UserController.java
            │
            ├── debug/
            │   └── ProductDebugHelper.java
            │
            ├── mapper/
            │   ├── OrderItemMapper.java
            │   ├── OrderMapper.java
            │   ├── ProductMapper.java
            │   └── UserMapper.java
            │
            ├── model/
            │   ├── dto/
            │   │   ├── order/
            │   │   ├── product/
            │   │   ├── user/
            │   │   └── CartItemCreateDto.java
            │   │
            │   ├── entities/
            │   │   ├── Cart.java
            │   │   ├── CartItem.java
            │   │   ├── Order.java
            │   │   ├── OrderItem.java
            │   │   ├── Product.java
            │   │   └── Users.java
            │   │
            │   ├── repositories/
            │   │   ├── CartItemRepository.java
            │   │   ├── CartItemRepositoryImpl.java
            │   │   ├── CartRepository.java
            │   │   ├── CartRepositoryImpl.java
            │   │   ├── OrderRepository.java
            │   │   ├── OrderRepositoryImpl.java
            │   │   ├── ProductRepository.java
            │   │   ├── Repository.java
            │   │   └── UserRepositoryImpl.java
            │   │
            │   └── service/
            │       ├── CartItemService.java
            │       ├── CartItemServiceImpl.java
            │       ├── OrderService.java
            │       ├── OrderServiceImpl.java
            │       ├── ProductService.java
            │       ├── ProductServiceImpl.java
            │       ├── UserService.java
            │       └── UserServiceImpl.java
            │
            ├── utils/
            │   ├── DatabaseConfig.java
            │   └── PasswordHash.java
            │   
            │
            ├── view/
            │   ├── ModernUIComponents.java
            │   ├── OrderUI.java
            │   ├── ProductServer.java
            │   ├── TableUI.java
            │   ├── UIComponents.java
            │   └── UserUI.java
            │
            └── Application.java

```

## Installation & Setup

### Prerequisites
- Java 21
- Gradle
- PostgreSQL database (deployed on Render) or using my script to create your local database

### Database Setup on Render

1. **Create PostgreSQL Database on Render**:
    - Go to [Render Dashboard](https://dashboard.render.com)
    - Click "New" → "PostgreSQL"
    - Choose your database name and region
    - Select a plan (Free tier available)
    - Click "Create Database"

2. **Get Database Connection Details**:
    - Once created, go to your database dashboard
    - Copy the connection details:
        - Hostname
        - Port
        - Database name
        - Username
        - Password
        - Connection string

3. **Configure Application**:
   Update `src/main/resources/application.properties`:
   ```properties
   # Database Configuration
   db.url=jdbc:postgresql://[hostname]:[port]/[database_name]
   db.username=[username]
   db.password=[password]
   db.driver=org.postgresql.Driver
   
   # Application Configuration
   app.session.timeout=3600000
   app.session.directory=sessions/
   ```

### Local Setup

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd ecommerce-console
   ```

2. **Install dependencies**:
   ```bash
   ./gradlew clean build
   ```

3. **Run database migrations**:
   ```bash
   # Execute the SQL scripts in src/main/resources/database.sql
   # against your Render PostgreSQL database
   ```


## Usage

### First Time Setup
1. Run the application
2. Choose "Register" to create a new account
3. Enter your details and create a secure password
4. Login with your credentials

### Main Menu Options
1. **User Management**:
    - View profile
    - Update profile information
    - Change password

2. **Product Catalog**:
    - Browse all products
    - Search products by category
    - View product details
    - Add products to cart (for customers)
    - Manage inventory (for admins)

3. **Order Management**:
    - View shopping cart
    - Place orders
    - View order history
    - Track order status
    - Admin order dashboard

### Session Management
- Sessions are automatically saved when you login
- On restart, the application will restore your session if still valid
- Sessions expire after 1 hour of inactivity (configurable)
- Use "Logout" to clear your session manually

## Configuration

### Database Configuration
Edit `application.properties` to configure database settings:
```properties
db.url=your_render_database_url
db.username=your_db_username
db.password=your_db_password
```

### Session Configuration
```properties
app.session.timeout=3600000  # 1 hour in milliseconds
app.session.directory=sessions/
```

## API Reference

### User Operations
- Register new user
- Authenticate user
- Update user profile
- Get user details
- Manage user sessions

### Product Operations
- Create product (Admin only)
- Read product details
- Update product information (Admin only)
- Delete product (Admin only)
- Search products

### Order Operations
- Create new order
- View order details
- Update order status (Admin only)
- Get order history
- Cancel order

## Security Features

- Password hashing using BCrypt
- Session-based authentication
- Role-based access control
- SQL injection prevention using PreparedStatements
- Input validation and sanitization
- Secure session file storage

## Error Handling

The application includes comprehensive error handling for:
- Database connection issues
- Invalid user inputs
- Authentication failures
- Session management errors
- Network connectivity problems

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Create a Pull Request

## Troubleshooting

### Common Issues

1. **Database Connection Failed**:
    - Verify your Render database credentials
    - Check if your IP is whitelisted (if applicable)
    - Ensure database is running and accessible

2. **Session Not Restored**:
    - Check if sessions directory exists and has write permissions
    - Verify session timeout configuration
    - Clear session files if corrupted

3. **Gradle Build Issues**:
    - Ensure Java 21 is installed
    - Update Gradle to latest version
    - Clear Gradle cache: `./gradlew clean`

## License

គ្មានទេ ចង់បានយកទៅ😂

---

> **Note**: This application is designed Java mini project that I have learned in course Full-Stack web development 
> at [ISTAD](https://cstad.edu.kh/).