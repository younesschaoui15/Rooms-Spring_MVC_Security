# Build stage (1st stage)
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

#Copy .mvn folder to /app
COPY .mvn .mvn
#Copy Maven Wrapper mvnw and pom.xml file to /app
COPY mvnw pom.xml ./

#Add execution permission to mvnw (Make Maven Wrapper executable)
RUN chmod +x mvnw
#Download dependencies in "pom.xml"
RUN ./mvnw dependency:go-offline

#Copy source code folder "src" to /app/src
COPY src src

#Build the Spring Boot app
RUN ./mvnw clean package -DskipTests


# Runtime stage (2nd stage)
FROM eclipse-temurin:25-jre

#We build the image in two stages, why?
#Because we want that the final image contains only the necessery = the .JAR file
#Without the entire JDk, maven, dependencies, build files...

WORKDIR /app

#Copy from the previous Docker stage "build" (as build)
#Copy *.jar to /app/app.jar
COPY --from=build /app/target/*.jar app.jar

#Exposed server port
EXPOSE 8080

#EntryPoint is the command to execute when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]