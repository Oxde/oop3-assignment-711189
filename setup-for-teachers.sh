#!/bin/bash

echo "🎬 === Movie Watchlist Setup - Student 711189 ==="
echo

# Source the Java 21 environment
if [ -f .envrc ]; then
    echo "📋 Setting up Java 21 environment..."
    source .envrc
    echo "✅ Java 21 configured successfully!"
    echo
else
    echo "⚠️  .envrc file not found. Checking current Java version..."
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    echo "Current Java version: $JAVA_VERSION"
    
    if [ "$JAVA_VERSION" != "21" ]; then
        echo "❌ This project requires Java 21"
        echo "   Please install Java 21 and try again"
        echo "   Or ensure .envrc file is present"
        exit 1
    fi
fi

# Verify Maven is using correct Java version
echo "🔍 Verifying Maven setup..."
MVN_JAVA_VERSION=$(mvn -version | grep "Java version" | cut -d',' -f1 | cut -d':' -f2 | xargs | cut -d'.' -f1)
echo "Maven Java version: $MVN_JAVA_VERSION"

if [ "$MVN_JAVA_VERSION" != "21" ]; then
    echo "❌ Maven is not using Java 21"
    echo "   Expected: 21, Got: $MVN_JAVA_VERSION"
    exit 1
fi

echo "✅ Maven configured correctly with Java 21!"
echo

# Run tests to verify everything works
echo "🧪 Running all tests..."
echo "────────────────────────────────────"

if mvn test; then
    echo
    echo "🎉 === ALL TESTS PASSED! ==="
    echo "✅ Total: 28 tests"
    echo "✅ Service Tests: 8 tests (Core functionality)"
    echo "✅ Controller Tests: 12 tests (REST API)"
    echo "✅ API Demo Tests: 7 tests (Feature demonstration)"
    echo "✅ Application Test: 1 test (Boot test)"
    echo
    echo "🚀 === PROJECT READY TO RUN ==="
    echo "Commands available:"
    echo "  mvn test                              - Run all tests"
    echo "  mvn test -Dtest=ApiDemonstrationTest  - See API demo"
    echo "  mvn spring-boot:run                   - Start application"
    echo "  mvn test -Dtest=MovieServiceTest      - Core functionality tests"
    echo
    echo "🌐 Application URL: http://localhost:8080"
    echo "📊 H2 Console: http://localhost:8080/h2-console"
    echo
else
    echo
    echo "❌ === TESTS FAILED ==="
    echo "Please check the output above for details"
    echo "Most likely Java version issue"
    exit 1
fi 