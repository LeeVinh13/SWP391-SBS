#nay la Dockerfile de build image
#Stage 1: Use Maven for building
#maven:3.8.5-openjdk-17-slim: là image Maven đã cài sẵn Maven 3.8.5 + JDK17, phiên bản slim
FROM maven:3.9.8-amazoncorretto-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests && rm -rf /root/.m2/repository
#COPY . ./SWP391-SBS
#WORKDIR /app/SWP391-SBS
#RUN mvn clean package -DskipTests

#Stage 2: use OpenJDK for running
FROM  amazoncorretto:21.0.4
WORKDIR /app
#COPY --from=build /app/SWP391-SBS/target/*.jar SWP391-SBS.jar
COPY --from=build /app/target/*.jar SWP391-SBS.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","SWP391-SBS.jar"]
