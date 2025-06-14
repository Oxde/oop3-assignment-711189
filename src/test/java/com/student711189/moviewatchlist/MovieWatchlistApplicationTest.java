package com.student711189.moviewatchlist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration test for MovieWatchlistApplication
 * Verifies that the Spring context loads successfully
 */
@SpringBootTest
@ActiveProfiles("test")
class MovieWatchlistApplicationTest {

    @Test
    void contextLoads() {
        // This test will pass if the Spring application context loads successfully
        // It's a basic smoke test to ensure all configurations are correct
    }
} 