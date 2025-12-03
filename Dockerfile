FROM gradle:8.13-jdk21 as builder
WORKDIR /app
COPY . .
RUN gradle bootJar

FROM eclipse-temurin:21-jdk-alpine as prod
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/app.jar
EXPOSE 8084
CMD ["java", "-jar", "/app/app.jar"]
