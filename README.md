# 🏭 Product Inventory System API

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.8-brightgreen?style=for-the-badge&logo=spring)
![Java](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk)
![JUnit5](https://img.shields.io/badge/JUnit-5-green?style=for-the-badge&logo=junit5)
![Mockito](https://img.shields.io/badge/Mockito-5.x-blue?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

**A robust, well-tested Product Inventory Management System API built with Spring Boot, featuring a comprehensive multi-layered testing suite.**

[Features](#-features) •
[Installation](#-installation) •
[API Reference](#-api-reference) •
[Testing](#-testing-suite) •
[License](#-license)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Installation](#-installation)
- [API Reference](#-api-reference)
- [Testing Suite](#-testing-suite)
- [Authentication](#-authentication)
- [License](#-license)

---

## 🎯 Overview

The **Product Inventory System API** is a RESTful web service designed for managing product inventory in an eCommerce platform. It provides endpoints for product management, stock tracking, and utility calculations such as discount pricing and quantity validation.

This project emphasizes **test-driven development (TDD)** with a comprehensive testing suite that demonstrates:

- **Pure JUnit 5** for isolated business logic testing
- **Mockito** for service layer testing with mocked dependencies
- **Spring Boot Test** for full integration testing

---

## ✨ Features

| Feature | Description |
|---------|-------------|
| 📦 **Product Management** | Create, retrieve, and update product inventory |
| 🔄 **Stock Restock** | Add quantity to existing products |
| 💰 **Discount Calculator** | Calculate discounted prices with validation |
| ✅ **Quantity Checker** | Verify if stock is sufficient for orders |
| 🔐 **Secure API** | HTTP Basic Authentication with role-based access |
| 🧪 **Comprehensive Tests** | Multi-layered testing with JUnit 5 & Mockito |

---

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| **Spring Boot 3.5.8** | Application framework |
| **Spring Data JPA** | Data persistence layer |
| **Spring Security** | Authentication & authorization |
| **H2 Database** | In-memory database for development |
| **Lombok** | Boilerplate code reduction |
| **JUnit 5** | Unit testing framework |
| **Mockito** | Mocking framework for isolated testing |
| **Gradle** | Build automation |

---

## 📁 Project Structure

```
product-inventory-system-api/
├── src/
│   ├── main/java/com/example/product_inventory_system_api/
│   │   ├── config/
│   │   │   └── SecurityConfig.java          # Security configuration
│   │   ├── controller/
│   │   │   └── ProductController.java       # REST endpoints
│   │   ├── exception/
│   │   │   ├── GlobalExceptionHandler.java  # Exception handling
│   │   │   └── ProductNotFoundException.java
│   │   ├── model/
│   │   │   └── Product.java                 # Product entity
│   │   ├── repository/
│   │   │   └── ProductRepository.java       # Data access layer
│   │   ├── service/
│   │   │   └── ProductManagerService.java   # Business logic
│   │   ├── util/
│   │   │   └── ProductCalculator.java       # Utility calculations
│   │   └── ProductInventorySystemApiApplication.java
│   │
│   └── test/java/com/example/product_inventory_system_api/
│       ├── service/
│       │   └── ProductManagerServiceTest.java  # Mockito tests
│       ├── util/
│       │   └── ProductCalculatorTest.java      # Pure JUnit tests
│       └── ProductInventorySystemApiApplicationTests.java
│
├── postman/
│   └── Product_Inventory_API.postman_collection.json
├── build.gradle
├── LICENSE
└── README.md
```

---

## 🚀 Installation

### Prerequisites

- **Java 17+** (Java 25 recommended)
- **Gradle 8.x** (or use included Gradle Wrapper)

### Clone the Repository

```bash
git clone https://github.com/nNEWBE/product-inventory-system-api.git
cd product-inventory-system-api
```

### Build the Project

```bash
# Using Gradle Wrapper (Windows)
./gradlew.bat build

# Using Gradle Wrapper (Linux/macOS)
./gradlew build
```

### Run the Application

```bash
# Using Gradle Wrapper
./gradlew bootRun
```

The API will be available at: `http://localhost:8080`

### Run Tests

```bash
# Run all tests
./gradlew test

# Run with detailed output
./gradlew test --info

# Generate test report
./gradlew test jacocoTestReport
```

Test reports are generated at: `build/reports/tests/test/index.html`

---

## 📚 API Reference

### Base URL

```
http://localhost:8080/api/products
```

### Authentication

All endpoints require **HTTP Basic Authentication**.

| Username | Password | Role |
|----------|----------|------|
| `admin` | `admin123` | ADMIN |
| `user` | `user123` | USER |

### Endpoints

#### 1. Create Product

```http
POST /api/products
```

**Request Body:**
```json
{
    "sku": "PROD-001",
    "name": "Wireless Mouse",
    "price": 29.99,
    "quantity": 100,
    "description": "Ergonomic wireless mouse"
}
```

**Response:** `201 Created`

---

#### 2. Get Product by SKU

```http
GET /api/products/{sku}
```

**Example:** `GET /api/products/PROD-001`

**Response:** `200 OK`
```json
{
    "id": 1,
    "sku": "PROD-001",
    "name": "Wireless Mouse",
    "price": 29.99,
    "quantity": 100,
    "description": "Ergonomic wireless mouse"
}
```

---

#### 3. Restock Product

```http
PATCH /api/products/{sku}/restock
```

**Request Body:**
```json
{
    "quantityToAdd": 50
}
```

**Response:** `200 OK`

---

#### 4. Calculate Discounted Price

```http
GET /api/products/calculate/discount?originalPrice=100&discountRate=20
```

**Response:** `200 OK`
```json
{
    "originalPrice": 100.0,
    "discountRate": 20.0,
    "discountedPrice": 80.0
}
```

---

#### 5. Check Quantity Sufficiency

```http
GET /api/products/check/quantity?currentQuantity=50&requiredQuantity=30
```

**Response:** `200 OK`
```json
{
    "currentQuantity": 50,
    "requiredQuantity": 30,
    "isSufficient": true
}
```

---

## 🧪 Testing Suite

This project features a **comprehensive multi-layered testing approach**:

### Test Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Integration Tests                        │
│              (Spring Boot Test + MockMvc)                   │
├─────────────────────────────────────────────────────────────┤
│                    Service Layer Tests                      │
│            (Mockito + @ExtendWith(MockitoExtension))        │
├─────────────────────────────────────────────────────────────┤
│                    Unit Tests                               │
│                  (Pure JUnit 5)                             │
└─────────────────────────────────────────────────────────────┘
```

### 1. Pure JUnit Tests (`ProductCalculatorTest`)

Tests the utility class in complete isolation without any dependencies.

| Test Category | Test Cases |
|---------------|------------|
| **Discount Calculation** | 0%, 50%, 100% discounts, partial discounts, edge cases |
| **Quantity Check** | Sufficient stock, insufficient stock, boundary conditions |
| **Validation** | Negative values, invalid inputs |

### 2. Mockito Tests (`ProductManagerServiceTest`)

Tests the service layer with mocked repository dependencies.

| Test Category | Test Cases |
|---------------|------------|
| **findProductBySku** | Successful retrieval, product not found exception |
| **restockProduct** | Quantity update, save verification, exception handling |
| **Edge Cases** | Zero quantity, large values, property preservation |

### Test Coverage

```
ProductCalculatorTest:     19 tests ✅
ProductManagerServiceTest: 12 tests ✅
Integration Tests:          1 test  ✅
─────────────────────────────────────
Total:                     32 tests ✅
```

### Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "ProductCalculatorTest"
./gradlew test --tests "ProductManagerServiceTest"

# Run with verbose output
./gradlew test --info
```

---

## 🔐 Authentication

The API uses **Spring Security** with HTTP Basic Authentication.

### Default Users

| Role | Username | Password | Permissions |
|------|----------|----------|-------------|
| Admin | `admin` | `admin123` | Full access |
| User | `user` | `user123` | Read access |

### Example cURL Request

```bash
curl -u admin:admin123 http://localhost:8080/api/products/PROD-001
```

---

## 📮 Postman Collection

Import the Postman collection for easy API testing:

📁 `postman/Product_Inventory_API.postman_collection.json`

The collection includes:
- Pre-configured authentication
- All API endpoints
- Sample request bodies
- Environment variables

---

## 📝 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

