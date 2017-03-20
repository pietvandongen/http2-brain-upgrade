package com.pietvandongen.http2brainupgrade;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.net.JksOptions;

import java.util.logging.Logger;

public class Server {

    private static final String HTML_FILENAME = "index.html";
    private static final String STYLESHEET_FILENAME = "style.css";
    private static final String IMAGE_FILENAME = "photo.jpg";
    private static final String JAVASCRIPT_FILENAME = "application.js";

    private static final String BASE_PATH = "/";
    private static final String STYLESHEET_PATH = BASE_PATH + STYLESHEET_FILENAME;
    private static final String IMAGE_PATH = BASE_PATH + IMAGE_FILENAME;
    private static final String JAVASCRIPT_PATH = BASE_PATH + JAVASCRIPT_FILENAME;

    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Starting server...");

        JksOptions jksOptions = new JksOptions()
                .setPath("keystore.jks")
                .setPassword("secret");

        HttpServerOptions options = new HttpServerOptions()
                .setUseAlpn(true)
                .setSsl(true)
                .setKeyStoreOptions(jksOptions)
                .setCompressionSupported(true)
                .setLogActivity(true);

        Vertx.vertx()
                .createHttpServer(options)
                .requestHandler(Server::handleRequest)
                .listen(8080);
    }

    private static void handleRequest(HttpServerRequest request) {
        LOGGER.info("Handling incoming request...");

        HttpServerResponse response = request.response();

        switch (request.path()) {
            case BASE_PATH:
                LOGGER.info("Sending resource " + HTML_FILENAME + " ...");
                response
                        .push(HttpMethod.GET, STYLESHEET_PATH, handler -> pushResource(handler, STYLESHEET_FILENAME))
                        .push(HttpMethod.GET, IMAGE_PATH, handler -> pushResource(handler, IMAGE_FILENAME))
                        .push(HttpMethod.GET, JAVASCRIPT_PATH, handler -> pushResource(handler, JAVASCRIPT_FILENAME))
                        .sendFile(HTML_FILENAME);
                break;

            case STYLESHEET_PATH:
                LOGGER.info("Sending resource " + STYLESHEET_FILENAME + " ...");
                response.sendFile(STYLESHEET_FILENAME);
                break;

            case IMAGE_PATH:
                LOGGER.info("Sending resource " + IMAGE_FILENAME + " ...");
                response.sendFile(IMAGE_FILENAME);
                break;

            case JAVASCRIPT_PATH:
                LOGGER.info("Sending resource " + JAVASCRIPT_FILENAME + " ...");
                response.sendFile(JAVASCRIPT_FILENAME);
                break;

            default:
                LOGGER.info("Not found: " + request.path());
                response.setStatusCode(404).end();
                break;
        }
    }

    private static void pushResource(AsyncResult<HttpServerResponse> handler, String fileName) {
        LOGGER.info("Pushing resource " + fileName + " ...");

        if (handler.succeeded()) {
            HttpServerResponse pushedResponse = handler.result();
            pushedResponse.sendFile(fileName);
        } else {
            LOGGER.info(handler.cause().getMessage());
        }
    }
}
