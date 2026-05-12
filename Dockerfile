FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# instala store:order:1.0.0
COPY api/order/order/pom.xml ./api/order/order/pom.xml
COPY api/order/order/src     ./api/order/order/src
RUN cd api/order/order && mvn install -DskipTests -q

# instala store:product:1.0.0 (necessário para ProductClient)
COPY api/product/product/pom.xml ./api/product/product/pom.xml
COPY api/product/product/src     ./api/product/product/src
RUN cd api/product/product && mvn install -DskipTests -q

# instala store:exchange:1.0.0 (necessário para ExchangeController)
COPY api/exchange/exchange-interface/pom.xml ./api/exchange/exchange-interface/pom.xml
COPY api/exchange/exchange-interface/src     ./api/exchange/exchange-interface/src
RUN cd api/exchange/exchange-interface && mvn install -DskipTests -q

# builda o order-service
COPY api/order/order-service/pom.xml ./api/order/order-service/pom.xml
COPY api/order/order-service/src     ./api/order/order-service/src
RUN cd api/order/order-service && mvn clean package -DskipTests -q

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/api/order/order-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
