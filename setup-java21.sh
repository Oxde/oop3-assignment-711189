#!/bin/bash
# Setup Java 21 environment for Movie Watchlist project

echo "🔧 Setting up Java 21 environment..."

export JAVA_HOME=/opt/homebrew/opt/openjdk@21
export PATH="$JAVA_HOME/bin:$PATH"

echo "✅ Java environment configured:"
java -version

echo ""
echo "🚀 Environment ready! You can now run:"
echo "   mvn clean compile"
echo "   mvn test"
echo "   mvn spring-boot:run" 