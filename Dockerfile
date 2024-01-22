FROM --platform=linux/amd64 amazoncorretto:20-alpine3.17-jdk as correto-jdk
ADD . /src
WORKDIR /src
RUN ./mvnw package -DskipTests
#WORKDIR /app

#COPY ./target/sales_loader-0.0.1-SNAPSHOT.jar /app

ENV TZ=Europe/Bucharest
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8090

ENTRYPOINT ["java","-jar","target/loyality_cms-1.0.0-ALFA.jar"]