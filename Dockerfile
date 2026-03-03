FROM eclipse-temurin:17.0.2_8-jdk AS build

WORKDIR /app

COPY build.gradle settings.gradle ./

COPY gradle ./gradle

COPY gradlew ./

RUN chmod +x ./gradlew

COPY src ./src

# Build the application
RUN ./gradlew build --no-daemon -x test

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=build /app/build/libs/app.jar app.jar

# Add health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=5 \
CMD wget --quiet --tries=1 --spider http://localhost:8090/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-Duser.timezone=America/Sao_Paulo", "-jar", "app.jar"]