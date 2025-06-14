# Movie Watchlist - Student 711189

A Spring Boot application for managing a personal movie watchlist with external API integration (OMDb + TMDb), multi-threading, and REST APIs.

## For Teachers: Quick Start

### Option 1: Automatic Setup (Recommended)
```bash
git clone <repository-url>
cd oop3-assignment-711189
./setup-for-teachers.sh
```
This script will:
- ✅ Configure Java 21 automatically
- ✅ Run all 28 tests
- ✅ Verify everything works
- ✅ Show you available commands

### Option 2: Manual Setup
**Requirements:** Java 21 and Maven 3.6+

1. **Set Java 21:**
   ```bash
   source .envrc    # Sets Java 21 for this project
   ```

2. **Run tests:**
   ```bash
   mvn test         # All 28 tests
   ```
   
3. **See API demo:**
   ```bash
   mvn test -Dtest=ApiDemonstrationTest
   ```

4. **Run application:**
   ```bash
   mvn spring-boot:run
   ```

## What This Project Demonstrates

✅ **External API Integration** - OMDb + TMDb APIs  
✅ **Multi-threading** - Java 21 virtual threads for parallel image downloads  
✅ **REST API** - Complete CRUD operations with pagination  
✅ **Database** - JPA/Hibernate with H2 in-memory database  
✅ **File Operations** - Automatic image download and storage  
✅ **Error Handling** - Comprehensive validation and exception handling  
✅ **Testing** - 28 tests covering all functionality  

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/movies` | Add movie (calls external APIs, downloads images) |
| GET | `/api/movies` | List movies (paginated) |
| GET | `/api/movies/{id}` | Get specific movie |
| PUT | `/api/movies/{id}` | Update movie (watched status, rating) |
| DELETE | `/api/movies/{id}` | Delete movie |

## URLs When Running

- **API Base:** http://localhost:8080/api/movies
- **H2 Database Console:** http://localhost:8080/h2-console
- **Health Check:** http://localhost:8080/actuator/health

## Test Summary

- **28 tests total** - All passing ✅
- **MovieServiceTest** (8 tests) - Core business logic
- **MovieControllerTest** (12 tests) - REST API endpoints  
- **ApiDemonstrationTest** (7 tests) - Feature showcase for teachers
- **ApplicationTest** (1 test) - Spring Boot integration

---

**Note:** This project requires Java 21. The setup script handles this automatically. 