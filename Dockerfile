FROM openjdk:8-alpine
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN javac -cp ./jar/* Main.java
CMD ["java", "-cp", "./jar/*:.", "Main"]
