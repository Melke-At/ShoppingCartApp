FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/ShoppingCartApp-1.0-SNAPSHOT.jar app.jar

# Install JavaFX (simplified example)
RUN apt-get update && apt-get install -y openjfx

ENTRYPOINT ["java", "--module-path", "/usr/share/openjfx/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "app.jar"]