# Movie Watchlist Application - Student 711189

A Spring Boot RESTful web application for managing a personal movie watchlist. This application integrates with external movie APIs (OMDb and TMDb), downloads movie images, and stores movie details in an H2 database.

## Features

- **Add Movies**: Search and add movies to your watchlist using OMDb API
- **Image Downloads**: Automatically downloads 3 movie images per movie from TMDb API
- **Pagination**: Efficient pagination support for large movie lists
- **Update Status**: Mark movies as watched and rate them (1-5 scale)
- **Concurrent Processing**: Multi-threaded image downloading for better performance
- **RESTful API**: Clean REST endpoints for all operations

## Technology Stack

- **Java 21**: Latest LTS version
- **Spring Boot 3.2.0**: Framework for rapid application development
- **Spring Data JPA**: Database operations and pagination
- **H2 Database**: In-memory database for development
- **Maven**: Build and dependency management
- **JUnit 5 & Mockito**: Testing framework
- **Lombok**: Reduces boilerplate code

## Project Structure

```
src/
├── main/
│   ├── java/com/student711189/moviewatchlist/
│   │   ├── MovieWatchlistApplication.java     # Main application class
│   │   ├── controller/                        # REST controllers
│   │   │   └── MovieController.java
│   │   ├── model/                            # Domain models and DTOs
│   │   │   ├── Movie.java
│   │   │   ├── MovieDto.java
│   │   │   ├── AddMovieRequest.java
│   │   │   └── UpdateMovieRequest.java
│   │   ├── repository/                       # Data access layer
│   │   │   └── MovieRepository.java
│   │   └── service/                          # Business logic
│   │       └── MovieService.java
│   └── resources/
│       └── application.properties            # Configuration
└── test/                                     # Unit tests
    └── java/com/student711189/moviewatchlist/
        ├── controller/
        │   └── MovieControllerTest.java
        └── service/
            └── MovieServiceTest.java
```

## API Endpoints

### Movies

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/movies` | Add a new movie to watchlist |
| GET | `/api/movies` | Get paginated list of movies |
| GET | `/api/movies/{id}` | Get specific movie by ID |
| PUT | `/api/movies/{id}` | Update movie watched status/rating |
| DELETE | `/api/movies/{id}` | Delete movie from watchlist |

### Example Requests

#### Add Movie
```bash
curl -X POST http://localhost:8080/api/movies \
  -H "Content-Type: application/json" \
  -d '{"title": "Inception", "year": "2010"}'
```

#### Get Movies (Paginated)
```bash
curl "http://localhost:8080/api/movies?page=0&size=10"
```

#### Update Movie
```bash
curl -X PUT http://localhost:8080/api/movies/1 \
  -H "Content-Type: application/json" \
  -d '{"watched": true, "rating": 5}'
```

#### Delete Movie
```bash
curl -X DELETE http://localhost:8080/api/movies/1
```

## Setup Instructions

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- API Keys (see below)

### API Keys Setup

1. **OMDb API Key**: Register at [omdbapi.com](http://omdbapi.com/apikey.aspx)
2. **TMDb API Key**: Register at [themoviedb.org](https://www.themoviedb.org/settings/api)

### Configuration

1. Clone the repository
2. Navigate to the project directory
3. Update `src/main/resources/application.properties` with your API keys:

```properties
omdb.api.key=YOUR_OMDB_KEY_HERE
tmdb.api.key=YOUR_TMDB_KEY_HERE
```

**⚠️ Important**: Never commit real API keys to version control!

### Running the Application

```bash
# Using Maven
mvn spring-boot:run

# Or build and run JAR
mvn clean package
java -jar target/movie-watchlist-711189-1.0.0.jar
```

The application will start on `http://localhost:8080`

### Running Tests

```bash
# Run all tests
mvn test

# Run with coverage report
mvn test jacoco:report
```

Coverage reports are generated in `target/site/jacoco/index.html`

### Database Console

H2 console is available at: `http://localhost:8080/h2-console`

**Connection details:**
- JDBC URL: `jdbc:h2:mem:watchlistdb`
- Username: `sa`
- Password: (empty)

## Development Notes

### Multi-threading
The application uses Java 21 virtual threads for concurrent image downloading, improving performance when adding movies with multiple images.

### Java Streams
Stream operations are used throughout the service layer for data processing, filtering, and transformations.

### Error Handling
The application includes comprehensive error handling:
- 404 Not Found for missing movies
- 409 Conflict for duplicate movies
- 400 Bad Request for invalid data
- 500 Internal Server Error for unexpected issues

### Testing Strategy
- Unit tests for service layer with mocked dependencies
- Controller tests using MockMvc
- Aim for >90% code coverage

## TODO Items

The following features are planned for future implementation:

1. **External API Integration**: Complete OMDb and TMDb API calls
2. **Image Download**: Implement concurrent image downloading
3. **Movie Search**: Add movie search by ID functionality
4. **Error Handling**: Global exception handler
5. **Validation**: Input validation annotations
6. **Documentation**: API documentation with Swagger

## Contributing

This is a student project for OOP3 course. Please follow the coding standards and commit message conventions outlined in the project plan.

## License

This project is for educational purposes only.

---

**Student ID**: 711189  
**Course**: OOP3  
**Project**: Movie Watchlist Application 