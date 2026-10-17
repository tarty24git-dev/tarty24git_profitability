FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/trade-finance-profitability-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENV JAVA_OPTS="-Xms2g -Xmx8g -XX:+UseG1GC"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
