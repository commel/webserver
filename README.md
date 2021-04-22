# Simple multi-threaded Webserver 

A working multithreaded static file webserver in Java. Inspired by the idea of https://devopsdirective.com/posts/2021/04/tiny-container-image/, but I am too lazy to build containers.

Bytecode with JDK 11 uses around 5.6 kB.

## Run
Run with
```
java SmallJavaWebServer 
```

## Containers
Okay, now I got some containers. 

|Image|Size|
|-|-|
|openjdk:11|647MB|
|openjdk:11-slim|421MB|
|opensuse/leap:15.2|378MB|
|openjdk:16-alpine|324mb
|adoptopenjdk/openjdk11:alpine-jre|148 MB|


