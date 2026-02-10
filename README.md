# 🏭 Industrial Inventory Manager

> **A modern, enterprise-grade production planning and inventory management system for industrial manufacturing**

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-6DB33F?style=flat&logo=springboot&logoColor=white)
![React](https://img.shields.io/badge/React-19-61DAFB?style=flat&logo=react&logoColor=black)
![TypeScript](https://img.shields.io/badge/TypeScript-5.6-3178C6?style=flat&logo=typescript&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?style=flat&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=flat&logo=docker&logoColor=white)

## 📋 Overview

The **Industrial Inventory Manager** is a full-stack web application that empowers manufacturers to efficiently manage their production operations. Built with modern technologies and industry best practices, this system provides intelligent production planning capabilities that maximize profitability while optimizing resource utilization.

### 🎯 Key Capabilities

- **📦 Product Catalog Management** - Comprehensive CRUD operations for product inventory with pricing and material composition
- **🔧 Raw Material Control** - Real-time stock monitoring and management of manufacturing materials
- **🤖 Intelligent Production Planning** - AI-driven suggestions for optimal production based on available inventory
- **💰 Profit Maximization** - Automatic prioritization of high-value products to maximize revenue
- **📊 Dashboard Analytics** - Real-time insights into production capacity and financial forecasts
- **🔄 Material Association** - Flexible product-material relationship management with quantity tracking

### ✨ What Makes This Special

This application implements an **intelligent production optimization algorithm** that:

- Analyzes current raw material stock levels
- Calculates all possible products that can be manufactured
- Prioritizes production by highest value products first
- Handles shared materials across multiple products
- Provides accurate revenue forecasts for production runs
- Prevents over-allocation of limited resources

## 🏗️ Architecture

This project follows a **modern microservices architecture** with clear separation of concerns:

```
┌─────────────────┐      ┌─────────────────┐      ┌─────────────────┐
│                 │      │                 │      │                 │
│  React Frontend │─────▶│  Spring Backend │─────▶│   PostgreSQL    │
│  (Port 5173)    │      │  (Port 8080)    │      │  (Port 5432)    │
│                 │      │                 │      │                 │
└─────────────────┘      └─────────────────┘      └─────────────────┘
      Vite + Nginx           REST API              Persistent Storage
```

### 🔧 Backend Stack

| Component             | Technology                  | Version  |
| --------------------- | --------------------------- | -------- |
| **Framework**         | Spring Boot                 | 3.5.9    |
| **Language**          | Java                        | 21 (LTS) |
| **Database**          | PostgreSQL                  | 16       |
| **ORM**               | Spring Data JPA + Hibernate | 6.6      |
| **Object Mapping**    | MapStruct                   | Latest   |
| **API Documentation** | Swagger/OpenAPI             | 3.0      |
| **Build Tool**        | Maven                       | 3.9      |
| **Testing**           | JUnit 5 + Mockito           | Latest   |

### 🎨 Frontend Stack

| Component            | Technology               | Version |
| -------------------- | ------------------------ | ------- |
| **Framework**        | React                    | 19.2    |
| **Language**         | TypeScript               | 5.6     |
| **State Management** | Redux Toolkit            | 2.11    |
| **Build Tool**       | Vite                     | 7.0     |
| **Styling**          | Tailwind CSS             | 4.0     |
| **UI Components**    | Custom Component Library | -       |
| **Icons**            | Lucide React             | 0.563   |
| **HTTP Client**      | Axios                    | 1.13    |
| **Routing**          | React Router             | 7.13    |

### 🐳 Infrastructure

- **Containerization**: Docker & Docker Compose
- **Reverse Proxy**: Nginx (production-ready configuration)
- **Database**: PostgreSQL with health checks and persistent volumes
- **Orchestration**: Multi-container setup with automatic service dependencies

## 🚀 Quick Start

### Prerequisites

Before you begin, ensure you have the following installed:

- ✅ **Docker** (20.10 or higher) and **Docker Compose** (2.0 or higher)
- ✅ **Java 21** (for local backend development)
- ✅ **Node.js 18+** and **npm** (for local frontend development)
- ✅ **Git** (for cloning the repository)

**Required Ports**: `5173` (frontend), `8080` (backend), `5436` (database)

---

### 🐳 Option 1: Docker Compose (Recommended for Quick Demo)

The fastest way to run the entire application with all services:

```bash
# Clone the repository
git clone <repository-url>
cd desafio-tecnico

# Start all services (database, backend, frontend)
docker-compose up -d

# View logs (optional)
docker-compose logs -f

# Stop all services
docker-compose down
```

**Access Points:**

- 🌐 **Frontend Application**: http://localhost:5173
- 🔌 **Backend API**: http://localhost:8080
- 📚 **API Documentation**: http://localhost:8080/swagger-ui/index.html
- 🗄️ **Database**: `localhost:5436` (postgres/postgres)

**First Time Setup:**
The database will automatically initialize with the required schema on first run thanks to Hibernate's DDL auto-update feature.

---

### 💻 Option 2: Local Development Setup

For development with hot-reload and debugging capabilities:

#### Step 1: Start the Database

```bash
# Start only the PostgreSQL database
docker-compose up db -d

# Verify database is healthy
docker-compose ps
```

#### Step 2: Run the Backend

```bash
# Navigate to backend directory
cd backend

# Run with Maven wrapper (Unix/Mac)
./mvnw spring-boot:run

# Or on Windows
mvnw.cmd spring-boot:run

# Or if you have Maven installed globally
mvn spring-boot:run
```

The backend will start on **http://localhost:8080**

**Backend Features:**

- 🔄 Spring DevTools for hot-reload
- 📊 Swagger UI at `/swagger-ui/index.html`
- 🔍 H2 Console (if enabled) at `/h2-console`
- 📝 Detailed logging for debugging

#### Step 3: Run the Frontend

```bash
# Open a new terminal
cd frontend

# Install dependencies (first time only)
npm install

# Start development server
npm run dev
```

The frontend will start on **http://localhost:5173**

**Frontend Features:**

- ⚡ Vite HMR (Hot Module Replacement)
- 🎨 Tailwind CSS with JIT compilation
- 🔍 React DevTools compatible
- 📱 Fully responsive design

---

### 🏗️ Building for Production

#### Backend JAR

```bash
cd backend
./mvnw clean package -DskipTests
# Output: target/inventory-manager-0.0.1-SNAPSHOT.jar

# Run the JAR
java -jar target/inventory-manager-0.0.1-SNAPSHOT.jar
```

#### Frontend Static Files

```bash
cd frontend
npm run build
# Output: dist/ directory

# Preview production build
npm run preview
```

#### Docker Images

```bash
# Build backend image
docker build -t inventory-backend:latest ./backend

# Build frontend image
docker build -t inventory-frontend:latest ./frontend

# Run with custom images
docker-compose up -d
```

---

### 🔧 Troubleshooting

<details>
<summary><b>Port Already in Use</b></summary>

If ports are already occupied, modify `docker-compose.yml`:

```yaml
ports:
  - "5173:80" # Change 5173 to another port
  - "8080:8080" # Change 8080 to another port
  - "5436:5432" # Change 5436 to another port
```

</details>

<details>
<summary><b>Database Connection Failed</b></summary>

Ensure PostgreSQL is running and healthy:

```bash
docker-compose ps
docker-compose logs db
```

Check `application.properties` for correct credentials.

</details>

<details>
<summary><b>Frontend Can't Connect to Backend</b></summary>

Verify CORS configuration in `CorsConfig.java` and ensure backend is running on port 8080.

</details>

## 📚 API Documentation

The backend exposes a RESTful API following industry standards with comprehensive Swagger documentation.

**Interactive API Docs**: http://localhost:8080/swagger-ui/index.html

### 🔧 Raw Materials Endpoints

Manage raw material inventory and stock levels.

| Method   | Endpoint                 | Description               | Request Body            |
| -------- | ------------------------ | ------------------------- | ----------------------- |
| `GET`    | `/api/rawMaterials`      | List all raw materials    | -                       |
| `GET`    | `/api/rawMaterials/{id}` | Get specific raw material | -                       |
| `POST`   | `/api/rawMaterials`      | Create new raw material   | `RawMaterialRequestDTO` |
| `PUT`    | `/api/rawMaterials/{id}` | Update raw material       | `RawMaterialRequestDTO` |
| `DELETE` | `/api/rawMaterials/{id}` | Delete raw material       | -                       |

**Example Request** - Create Raw Material:

```json
POST /api/rawMaterials
{
  "name": "Steel Sheet",
  "stockQuantity": 100
}
```

**Example Response**:

```json
{
  "id": 1,
  "name": "Steel Sheet",
  "stockQuantity": 100
}
```

---

### 📦 Products Endpoints

Manage product catalog, pricing, and material associations.

| Method   | Endpoint                               | Description                         | Request Body        |
| -------- | -------------------------------------- | ----------------------------------- | ------------------- |
| `GET`    | `/api/products`                        | List all products                   | -                   |
| `GET`    | `/api/products/{id}`                   | Get specific product with materials | -                   |
| `POST`   | `/api/products`                        | Create new product                  | `ProductRequestDTO` |
| `PUT`    | `/api/products/{id}`                   | Update product                      | `ProductRequestDTO` |
| `DELETE` | `/api/products/{id}`                   | Delete product                      | -                   |
| `GET`    | `/api/products/producible`             | Get producible products list        | -                   |
| `GET`    | `/api/products/production-suggestions` | Get optimized production plan       | -                   |

**Example Request** - Create Product with Materials:

```json
POST /api/products
{
  "name": "Industrial Chair",
  "price": 150.00,
  "materials": [
    {
      "materialId": 1,
      "requiredQuantity": 2
    },
    {
      "materialId": 2,
      "requiredQuantity": 4
    }
  ]
}
```

**Example Response**:

```json
{
  "id": 1,
  "name": "Industrial Chair",
  "price": 150.0,
  "materials": [
    {
      "id": 1,
      "materialId": 1,
      "materialName": "Steel Sheet",
      "requiredQuantity": 2,
      "stockQuantity": 50
    },
    {
      "id": 2,
      "materialId": 2,
      "materialName": "Wood Plank",
      "requiredQuantity": 4,
      "stockQuantity": 100
    }
  ],
  "producible": true
}
```

---

### 🤖 Production Planning Endpoints

#### Get Producible Products

```
GET /api/products/producible
```

Returns all products that can be manufactured with current stock levels. Each product includes the `producible` field set to `true` and shows current material stock availability.

**Response**:

```json
[
  {
    "id": 1,
    "name": "Industrial Chair",
    "price": 150.0,
    "materials": [
      {
        "id": 1,
        "materialId": 1,
        "materialName": "Steel Sheet",
        "requiredQuantity": 2,
        "stockQuantity": 50
      },
      {
        "id": 2,
        "materialId": 2,
        "materialName": "Wood Plank",
        "requiredQuantity": 4,
        "stockQuantity": 100
      }
    ],
    "producible": true
  }
]
```

#### Get Production Suggestions

```
GET /api/products/production-suggestions
```

Returns **optimized production plan** prioritized by product value. This endpoint implements the core algorithm that maximizes revenue while respecting material constraints.

**Algorithm Features**:

- ✅ Prioritizes high-value products
- ✅ Handles shared materials across products
- ✅ Prevents over-allocation of stock
- ✅ Calculates maximum producible quantities
- ✅ Provides total revenue forecast

**Response**:

```json
[
  {
    "id": 2,
    "name": "Premium Desk",
    "price": 450.0,
    "maxQuantity": 5,
    "totalValue": 2250.0
  },
  {
    "id": 1,
    "name": "Industrial Chair",
    "price": 150.0,
    "maxQuantity": 8,
    "totalValue": 1200.0
  }
]
```

---

### 🔗 Product-Material Association Endpoints

Manage many-to-many relationships between products and materials.

| Method   | Endpoint                                           | Description                  | Request Body                |
| -------- | -------------------------------------------------- | ---------------------------- | --------------------------- |
| `POST`   | `/api/products/{productId}/materials`              | Add material to product      | `ProductMaterialRequestDTO` |
| `DELETE` | `/api/products/{productId}/materials/{materialId}` | Remove material from product | -                           |

**Example Request** - Add Material to Product:

```json
POST /api/products/1/materials
{
  "materialId": 3,
  "requiredQuantity": 5
}
```

---

### 🔍 Error Responses

All endpoints follow consistent error response format:

```json
{
  "timestamp": "2026-02-09T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with id: 999",
  "path": "/api/products/999"
}
```

**Common HTTP Status Codes**:

- `200 OK` - Successful GET/PUT request
- `201 Created` - Successful POST request
- `204 No Content` - Successful DELETE request
- `400 Bad Request` - Invalid request data
- `404 Not Found` - Resource not found
- `409 Conflict` - Duplicate material in product
- `500 Internal Server Error` - Server error

## 🎯 Core Features

### 1. 🔧 Raw Material Management

Complete lifecycle management for manufacturing materials.

**Capabilities**:

- ✅ Track material inventory with real-time stock quantities
- ✅ Full CRUD operations (Create, Read, Update, Delete)
- ✅ Stock level monitoring and alerts
- ✅ Material utilization tracking
- ✅ Bulk import/export capabilities

**Business Value**: Maintain accurate inventory records to prevent production delays and optimize storage costs.

---

### 2. 📦 Product Management

Comprehensive product catalog with material composition tracking.

**Capabilities**:

- ✅ Maintain product catalog with pricing
- ✅ Associate multiple materials to each product
- ✅ Define precise required quantities for production
- ✅ Update product specifications dynamically
- ✅ Product-material relationship management

**Business Value**: Create detailed product recipes to ensure consistent manufacturing quality and accurate cost calculations.

---

### 3. 🤖 Intelligent Production Planning ⭐

**The core differentiator** - Advanced production optimization algorithm.

**How It Works**:

```
1. Analyze Stock → 2. Calculate Feasibility → 3. Prioritize by Value → 4. Optimize Allocation
     ↓                       ↓                         ↓                      ↓
  Check all            Determine which          Sort products by        Allocate materials
  material stock       products can be           highest price          without conflicts
  levels               manufactured
```

**Key Features**:

#### 📊 Producible Products Analysis

- Scans current inventory against all product recipes
- Identifies which products **can** be manufactured right now
- Calculates **maximum quantity** for each producible product
- Estimates **total revenue** potential per product

#### 💡 Optimized Production Suggestions

- **Profit Maximization**: Prioritizes high-value products first
- **Smart Material Allocation**: Handles shared materials across multiple products
- **Conflict Resolution**: Prevents over-allocation of limited resources
- **Dynamic Recalculation**: Adjusts suggestions as materials are allocated
- **Revenue Forecasting**: Provides accurate total value projections

**Example Scenario**:

```
Available Materials:
- Steel Sheet: 50 units
- Wood Plank: 100 units

Products:
1. Premium Desk ($500) - Needs: 10 Steel, 20 Wood
2. Office Chair ($150) - Needs: 2 Steel, 5 Wood

Algorithm Output:
✅ Priority 1: Premium Desk × 5 units = $2,500 (uses 50 Steel, 100 Wood)
❌ Priority 2: Office Chair × 0 units = $0 (insufficient materials remaining)

Total Projected Revenue: $2,500
```

**Business Value**: Maximize profitability by automatically suggesting the most valuable production mix while respecting material constraints.

---

### 4. 📊 Dashboard Analytics

Real-time business intelligence and operational insights.

**Metrics Displayed**:

- 💰 **Total Production Revenue**: Estimated value from suggested production
- 📦 **Producible Products**: Count of items that can be manufactured
- 🔧 **Raw Materials in Stock**: Total material types available
- 📈 **Production Capacity**: Visual indicators of manufacturing potential

**Interactive Features**:

- Quick navigation to management pages
- Real-time data updates
- Responsive design for mobile access
- Visual charts and statistics

**Business Value**: Gain instant visibility into production capacity and financial forecasts for informed decision-making.

---

### 5. 🔄 Material-Product Association

Flexible relationship management between materials and products.

**Features**:

- Many-to-many relationships between products and materials
- Dynamic quantity adjustments
- Duplicate prevention mechanisms
- Cascading updates and deletes
- Audit trail for changes

**Business Value**: Maintain accurate Bills of Materials (BOM) for each product to ensure manufacturing consistency.

## 🗄️ Database Schema

The system uses a **normalized relational database** design optimized for performance and data integrity.

### Entity-Relationship Diagram

```
┌─────────────────┐         ┌──────────────────────┐         ┌─────────────────┐
│   raw_material  │         │  product_material    │         │     product     │
├─────────────────┤         ├──────────────────────┤         ├─────────────────┤
│ id (PK)         │◄────────│ raw_material_id (FK) │         │ id (PK)         │
│ name            │         │ product_id (FK)      │────────►│ name            │
│ stock_quantity  │         │ required_quantity    │         │ price           │
└─────────────────┘         │ id (PK)              │         └─────────────────┘
                            └──────────────────────┘
```

### Table: `raw_material`

Stores all manufacturing materials with their current stock levels.

```sql
CREATE TABLE raw_material (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    stock_quantity INTEGER NOT NULL CHECK (stock_quantity >= 0)
);

CREATE INDEX idx_raw_material_name ON raw_material(name);
```

**Columns**:

- `id`: Auto-incrementing primary key
- `name`: Unique material identifier (e.g., "Steel Sheet", "Plastic Resin")
- `stock_quantity`: Current inventory count (must be non-negative)

**Business Rules**:

- Material names must be unique
- Stock quantity cannot be negative
- Indexed for fast name lookups

---

### Table: `product`

Maintains the product catalog with pricing information.

```sql
CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0)
);

CREATE INDEX idx_product_name ON product(name);
CREATE INDEX idx_product_price ON product(price DESC);
```

**Columns**:

- `id`: Auto-incrementing primary key
- `name`: Product name (e.g., "Office Chair", "Premium Desk")
- `price`: Product selling price (2 decimal precision)

**Business Rules**:

- Price must be non-negative
- Indexed by name for searches
- Indexed by price descending for optimization algorithm

---

### Table: `product_material` (Join Table)

Manages many-to-many relationships between products and materials with quantity requirements.

```sql
CREATE TABLE product_material (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
    raw_material_id BIGINT NOT NULL REFERENCES raw_material(id) ON DELETE CASCADE,
    required_quantity INTEGER NOT NULL CHECK (required_quantity > 0),
    UNIQUE (product_id, raw_material_id)
);

CREATE INDEX idx_pm_product ON product_material(product_id);
CREATE INDEX idx_pm_material ON product_material(raw_material_id);
```

**Columns**:

- `id`: Auto-incrementing primary key
- `product_id`: Foreign key to product table
- `raw_material_id`: Foreign key to raw_material table
- `required_quantity`: Amount of material needed to produce one unit

**Business Rules**:

- Composite unique constraint prevents duplicate material assignments
- Required quantity must be positive
- Cascading deletes maintain referential integrity
- Indexed for efficient joins and lookups

---

### Database Features

✅ **ACID Compliance**: Full transaction support for data integrity  
✅ **Foreign Key Constraints**: Enforced referential integrity  
✅ **Check Constraints**: Business rule validation at database level  
✅ **Indexes**: Optimized for common query patterns  
✅ **Cascading Deletes**: Automatic cleanup of dependent records  
✅ **Connection Pooling**: HikariCP for high-performance connections

### Sample Data

```sql
-- Raw Materials
INSERT INTO raw_material (name, stock_quantity) VALUES
('Steel Sheet', 100),
('Wood Plank', 200),
('Plastic Resin', 50);

-- Products
INSERT INTO product (name, price) VALUES
('Office Chair', 150.00),
('Premium Desk', 450.00);

-- Product Materials (Bill of Materials)
INSERT INTO product_material (product_id, raw_material_id, required_quantity) VALUES
(1, 1, 2),  -- Office Chair needs 2 Steel Sheets
(1, 2, 4),  -- Office Chair needs 4 Wood Planks
(2, 1, 10), -- Premium Desk needs 10 Steel Sheets
(2, 2, 20); -- Premium Desk needs 20 Wood Planks
```

## 🐳 Docker Configuration

### Building Individual Images

Backend:

```bash
cd backend
docker build -t inventory-backend .
```

Frontend:

```bash
cd frontend
docker build -t inventory-frontend .
```

### Environment Variables

#### Backend

- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SPRING_JPA_HIBERNATE_DDL_AUTO`: Hibernate DDL mode (update/create-drop)

#### Frontend

- `VITE_API_URL`: Backend API base URL (build-time variable)

## 📦 Project Structure

```
.
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/projedata/inventory_manager/
│   │   │   │   ├── controller/      # REST endpoints
│   │   │   │   ├── service/         # Business logic
│   │   │   │   ├── repository/      # Data access
│   │   │   │   ├── model/           # JPA entities
│   │   │   │   ├── dto/             # Data transfer objects
│   │   │   │   ├── mapper/          # MapStruct mappers
│   │   │   │   └── config/          # Configuration classes
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── components/          # Reusable UI components
│   │   ├── pages/               # Page components
│   │   ├── services/            # API client
│   │   ├── store/               # Redux state management
│   │   ├── types/               # TypeScript types
│   │   └── hooks/               # Custom React hooks
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
│
└── docker-compose.yml
```

## 🧪 Testing

### Backend Tests

```bash
cd backend
./mvnw test
```

### Frontend Tests

```bash
cd frontend
npm test
```

## 🔒 Security Features

- CORS configuration for frontend-backend communication
- Nginx security headers (X-Frame-Options, X-Content-Type-Options, X-XSS-Protection)
- Non-root Docker containers for enhanced security
- Environment-based configuration for sensitive data

## 🛠️ Technology Stack

| Layer            | Technologies                                         |
| ---------------- | ---------------------------------------------------- |
| Frontend         | React, TypeScript, Redux Toolkit, Vite, Tailwind CSS |
| Backend          | Spring Boot, Java 21, Spring Data JPA, MapStruct     |
| Database         | PostgreSQL 16                                        |
| API Docs         | Swagger/OpenAPI                                      |
| Containerization | Docker, Docker Compose, Nginx                        |

## 📈 Performance

- Multi-stage Docker builds for optimized image sizes
- Nginx static asset caching with 1-year expiration
- Gzip compression enabled for text resources
- Database connection pooling via HikariCP
- JPA query optimization with proper indexing

## 📋 Requirements Coverage

### Functional Requirements

- ✅ **RF001** - Backend CRUD for products
- ✅ **RF002** - Backend CRUD for raw materials
- ✅ **RF003** - Backend CRUD for product-material associations
- ✅ **RF004** - Backend query for producible products
- ✅ **RF005** - Frontend interface for product management
- ✅ **RF006** - Frontend interface for raw material management
- ✅ **RF007** - Frontend interface for product-material associations
- ✅ **RF008** - Frontend interface for production suggestions

### Non-Functional Requirements

- ✅ **RNF001** - Web platform (Chrome, Firefox, Edge compatible)
- ✅ **RNF002** - API architecture (separated backend/frontend)
- ✅ **RNF003** - Responsive design
- ✅ **RNF004** - PostgreSQL database
- ✅ **RNF005** - Spring Boot framework
- ✅ **RNF006** - React & Redux
- ✅ **RNF007** - English codebase

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project was developed as a technical challenge and is available for educational purposes.

## 👤 Author

Developed as part of a technical assessment for demonstrating full-stack development capabilities with Spring Boot and React.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- React team for the powerful UI library
- PostgreSQL community for the robust database system
- Material design principles for UI/UX inspiration

---

**Note**: This system demonstrates enterprise-level application development with modern best practices including containerization, RESTful API design, responsive UI, and production-ready deployment configurations.
