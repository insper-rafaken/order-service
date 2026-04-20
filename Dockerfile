FROM maven:3.9-eclipse-temurin-24 AS build
WORKDIR /app

# instala a dependência local store:order:1.0.0
COPY api/order/pom.xml ./api/order/pom.xml
COPY api/order/src     ./api/order/src
RUN cd api/order && mvn install -DskipTests -q

# builda o order-service
COPY api/order-service/pom.xml ./api/order-service/pom.xml
COPY api/order-service/src     ./api/order-service/src
RUN cd api/order-service && mvn clean package -DskipTests -q

FROM eclipse-temurin:24-jre
WORKDIR /app
COPY --from=build /app/api/order-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
