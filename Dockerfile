FROM eclipse-temurin:21-jre-alpine AS runner
COPY app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
