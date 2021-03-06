# HTTP/2 Brain Upgrade Demo Server Implementation

A demonstration implementation of an [HTTP/2][1] server (using [Vert.x][2]) for the [Luminis HTTP/2 Brain Upgrade][3].

## Build status

[![Build Status](https://travis-ci.org/pietvandongen/http2-brain-upgrade.svg?branch=master)](https://travis-ci.org/pietvandongen/http2-brain-upgrade)
[![Codecov](https://img.shields.io/codecov/c/github/pietvandongen/http2-brain-upgrade.svg)](https://codecov.io/gh/pietvandongen/http2-brain-upgrade)
[![Docker Pulls](https://img.shields.io/docker/pulls/pietvandongen/http2-demo-server.svg)](https://hub.docker.com/r/pietvandongen/http2-demo-server/)

## See it in action

The quickest way to see the server in action is to pull its Docker container image, run it and go to your browser:
 
 ```bash
 docker run -p 8080:8080 pietvandongen/http2-demo-server
 ```
 
Then visit [https://localhost:8080/](https://localhost:8080/) and get glorious HTTP/2 responses!

## Building

You can build the server either as a JAR with dependencies (using [Gradle][4]), or as a container (using [Docker][5]). 

### Using Gradle

In your terminal, go to this project's directory and type:
 
```bash
./gradlew build
```

This will build a JAR with dependencies, which can be found in `/build/libs/server.jar`.


### Using Docker

Build a Docker container tagged `http2-demo-server` like this:

```bash
docker build -t http2-demo-server .
```

## How to run

Depending on your build artifact, you can either run the server on the JVM or use Docker. 

When it is running, you can go to [https://localhost:8080/](https://localhost:8080/) to access the server!

### As a JAR

Using the artifact resulting from the Gradle build step, run:

```bash
java -jar build/libs/server.jar
```

### As a Docker container
 
Using the previously built container image, run:

```bash
docker run -p 8080:8080 http2-demo-server
```

[1]: https://http2.github.io/
[2]: http://vertx.io/
[3]: https://academy.luminis.eu/en/event/brain-upgrade-http2/
[4]: https://gradle.org/
[5]: https://www.docker.com/
