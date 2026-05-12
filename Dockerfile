FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# instala store:order:1.0.0
COPY api/order/pom.xml ./api/order/pom.xml
COPY api/order/src     ./api/order/src
RUN cd api/order && mvn install -DskipTests -q

# instala store:product:1.0.0 (necessário para ProductClient)
COPY api/product/pom.xml ./api/product/pom.xml
COPY api/product/src     ./api/product/src
RUN cd api/product && mvn install -DskipTests -q

# instala store:exchange:1.0.0 (necessário para ExchangeController)
COPY api/exchange-interface/pom.xml ./api/exchange-interface/pom.xml
COPY api/exchange-interface/src     ./api/exchange-interface/src
RUN cd api/exchange-interface && mvn install -DskipTests -q

# builda o order-service
COPY api/order-service/pom.xml ./api/order-service/pom.xml
COPY api/order-service/src     ./api/order-service/src
RUN cd api/order-service && mvn clean package -DskipTests -q

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/api/order-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
