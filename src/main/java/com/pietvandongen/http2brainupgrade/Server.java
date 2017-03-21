package com.pietvandongen.http2brainupgrade;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.net.JksOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Server {

    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private static final JksOptions KEYSTORE_OPTIONS = new JksOptions()
            .setPath("keystore.jks")
            .setPassword("secret");

    private static final HttpServerOptions SERVER_OPTIONS = new HttpServerOptions()
            .setUseAlpn(true)
            .setSsl(true)
            .setKeyStoreOptions(KEYSTORE_OPTIONS);

    static final Map<String, Resource> resources = new HashMap<>();
    static final Resource HTML = Resource.create("/", "index.html");
    static final Resource STYLESHEET = Resource.create("/style.css", "style.css");
    static final Resource IMAGE = Resource.create("/photo.jpg", "photo.jpg");
    static final Resource JAVASCRIPT = Resource.create("/application.js", "application.js");

    static {
        resources.put(HTML.getPath(), HTML);
        resources.put(STYLESHEET.getPath(), STYLESHEET);
        resources.put(IMAGE.getPath(), IMAGE);
        resources.put(JAVASCRIPT.getPath(), JAVASCRIPT);
    }

    public static void main(String[] args) {
        LOGGER.info("Starting server...");

        Vertx.vertx()
                .createHttpServer(SERVER_OPTIONS)
                .requestHandler(Server::handleRequest)
                .listen(8080);
    }

    static void handleRequest(HttpServerRequest request) {
        LOGGER.info("Handling incoming request...");

        Resource requestedResource = resources.get(request.path());
        HttpServerResponse response = request.response();

        if (requestedResource == null) {
            LOGGER.info("Resource not found: " + request.path());
            response.setStatusCode(404).end();
            return;
        }

        LOGGER.info("Sending resource " + requestedResource.getFileName() + " ...");

        if (requestedResource.equals(HTML)) {
            pushResource(response, STYLESHEET);
            pushResource(response, IMAGE);
            pushResource(response, JAVASCRIPT);
        }

        response.sendFile(requestedResource.getFileName());
    }

    static void pushResource(HttpServerResponse response, Resource resource) {
        response.push(HttpMethod.GET, resource.getPath(), asyncResult -> handlePush(asyncResult, resource));
    }

    static void handlePush(AsyncResult<HttpServerResponse> asyncResult, Resource resource) {
        if (asyncResult.succeeded()) {
            LOGGER.info("Pushing resource " + resource.getFileName() + " ...");
            HttpServerResponse pushedResponse = asyncResult.result();
            pushedResponse.sendFile(resource.getFileName());
        } else {
            LOGGER.info(asyncResult.cause().getMessage());
        }
    }
}
