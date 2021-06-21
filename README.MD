# CHAT-APP-PERSONAL

## Description

Personal Messaging and Video Chat App. Text messeging with STOMP websockets. Video Calling with WebRTC. Limited Sessions and "no footprint" user data handling.

## Technologies

CHAT-APP-PERSONAL uses a number of open source projects:

  * [JAVA11] - JAVA 11 SDK
  * [GRADLE] - BUILD AUTOMATION TOOL
  * [SPRING] - JAVA SPRING FRAMEWORK
  * [SPRING-BOOT] - SPRING PROJ BOOTSTRAP
  * [H2] - SQL IN MEMORY DATABASE
  * [LOMBOK] - JAVA SPLICING FRAMEWORK
  * [WEBRTC] - WEB REALTIME COMMUNICATION API
  * [STOMP] -  STREAMING TEXT MESSEAGING PROTOCOL
  * [THYMELEAF] - SERVER SIDE JAVA TEMPLATION ENGINE

## Installation

Download Java dependancies for the project:
```sh
$ cd chat_app_personal
$ gradlew assembleDevelopmentDebug --refresh-dependencies
```

## Running and Building

This application is an **GRADLE APPLICATION USING THE GRADLE WRAPPER**;

* **WITH CLI COMMANDS**

  Open the terminal and navigate to the root project folder.

  ```sh
  $ gradle build
  ```
  or with no gradle
  ```sh
  $ ./gradlew build
  ```

  ## Todo

   - Volume and mic handling on open audio channel

  [JAVA11]: <https://www.oracle.com/java/technologies/javase-jdk11-downloads.html>
  [SPRING]: <https://spring.io>
  [SPRING-BOOT]: <https://spring.io/projects/spring-boot>
  [H2]: <http://www.h2database.com/html/main.html>
  [GRADLE]: <https://gradle.org>
  [LOMBOK]: <https://projectlombok.org>
  [WEBRTC]: <https://en.wikipedia.org/wiki/WebRTC>
  [STOMP]: <https://stomp.github.io>
  [THYMELEAF]: <https://www.thymeleaf.org>