#!/bin/bash

echo "=== Movie Watchlist Setup Script - Student 711189 ==="
echo

# Check if Java 21 is available
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    echo "Current Java version: $JAVA_VERSION"
    
    if [ "$JAVA_VERSION" != "21" ]; then
        echo "⚠️  Warning: This project requires Java 21"
        echo "   Current version: $JAVA_VERSION"
        echo "   Please install Java 21 or set JAVA_HOME to Java 21"
        echo
        
        # Try to find Java 21 on common paths
        if [ -d "/opt/homebrew/Cellar/openjdk@21" ]; then
            echo "🔍 Found Java 21 via Homebrew!"
            echo "   Run: export JAVA_HOME=/opt/homebrew/Cellar/openjdk@21/21.0.7/libexec/openjdk.jdk/Contents/Home"
        fi
        
        if [ -d "/usr/lib/jvm/java-21-openjdk" ]; then
            echo "🔍 Found Java 21 on Linux!"
            echo "   Run: export JAVA_HOME=/usr/lib/jvm/java-21-openjdk"
        fi
        echo
    else
        echo "✅ Java 21 detected - ready to go!"
    fi
else
    echo "❌ Java not found. Please install Java 21"
    exit 1
fi

echo "=== Running Tests ==="
mvn test

if [ $? -eq 0 ]; then
    echo
    echo "✅ All tests passed!"
    echo
    echo "=== Ready to run! ==="
    echo "To start the application:"
    echo "  mvn spring-boot:run"
    echo
    echo "Then access:"
    echo "  API: http://localhost:8080/api/movies"
    echo "  Database: http://localhost:8080/h2-console"
    echo
else
    echo "❌ Tests failed. Please check Java version (needs Java 21)"
    exit 1
fi 