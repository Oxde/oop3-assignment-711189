# Movie Watchlist - Student 711189

This project is a Spring Boot application for managing a personal movie watchlist. It integrates with external movie APIs (OMDb and TMDb) to fetch movie details and download images.

## For Teachers: How to Run This Project

### Prerequisites
- **Java 21** (Important: This project requires Java 21 specifically)
- Maven 3.6+

### Quick Setup Instructions

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd oop3-assignment-711189
   ```

2. **Set Java 21 (if needed):**
   - If you have Java 21 installed: `export JAVA_HOME=/path/to/java21`
   - On macOS with Homebrew: `export JAVA_HOME=/opt/homebrew/Cellar/openjdk@21/21.0.7/libexec/openjdk.jdk/Contents/Home`
   - Or use the provided setup script: `source .envrc`

3. **Run tests:**
   ```bash
   mvn test
   ```
   
   To see a complete API demonstration:
   ```bash
   mvn test -Dtest=ApiDemonstrationTest
   ```

4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the API:**
   - Base URL: `http://localhost:8080/api/movies`
   - H2 Database Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:watchlistdb`)

### API Endpoints

- `POST /api/movies` - Add a new movie (body: `{"title": "Inception", "year": "2010"}`)
- `GET /api/movies?page=0&size=10` - List movies (paginated)
- `GET /api/movies/{id}` - Get specific movie
- `PUT /api/movies/{id}` - Update movie (body: `{"watched": true, "rating": 5}`)
- `DELETE /api/movies/{id}` - Delete movie

### What This Project Demonstrates

- **Multi-threading**: Downloads 3 images per movie in parallel using Java 21 virtual threads
- **External API Integration**: Fetches data from OMDb API and images from TMDb API
- **Database Operations**: Uses H2 database with JPA for persistence
- **RESTful API**: Complete CRUD operations with proper HTTP status codes
- **Pagination**: Supports pagination for movie listing
- **File I/O**: Downloads and manages movie images on the file system
- **Java Streams**: Uses Streams API for data processing and filtering
- **Unit Testing**: Comprehensive test coverage with JUnit 5 and Mockito

### Technical Stack

- Java 21
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database
- Maven
- JUnit 5 + Mockito

### Notes

- API keys are already configured for testing
- The application creates an `images/` directory for downloaded movie images
- All tests should pass when using Java 21
- Database is in-memory, so data resets on each restart

### Project Structure

```
src/
├── main/java/com/student711189/moviewatchlist/
│   ├── controller/     # REST endpoints
│   ├── service/        # Business logic
│   ├── model/          # Entities and DTOs
│   └── repository/     # Data access
└── test/               # Unit tests
```

Student ID: 711189 