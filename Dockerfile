# ---- Stage 1: Maven Dependencies ----
FROM maven:3.9.5-eclipse-temurin-17 AS deps
WORKDIR /app

# Copy only pom.xml to leverage Docker cache
COPY pom.xml .
# Resolve dependencies only
RUN mvn dependency:go-offline

# ---- Stage 2: Build and Test ----
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app

COPY --from=deps /root/.m2 /root/.m2
COPY . .

# Run tests
RUN mvn test

# Package application, skipping tests (rebuild without tests)
RUN mvn package -DskipTests

# ---- Stage 3: Build without dev/test dependencies ----
FROM maven:3.9.5-eclipse-temurin-17 AS build-prod
WORKDIR /app

COPY --from=build /app/pom.xml .
COPY --from=build /app/src ./src
COPY --from=build /root/.m2 /root/.m2

# Build only the app, skipping tests and dev dependencies
RUN mvn package -DskipTests -Pprod

# ---- Stage 4: Production Image ----
FROM eclipse-temurin:17-jre AS prod
WORKDIR /app

# Copy the jar from the previous stage
COPY --from=build-prod /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]