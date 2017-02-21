import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.net.JksOptions;

public class Example {

    private static final String HTML_FILENAME = "index.html";
    private static final String STYLESHEET_FILENAME = "style.css";
    private static final String IMAGE_FILENAME = "test.png";
    private static final String JAVASCRIPT_FILENAME = "application.js";

    private static final String BASE_PATH = "/";
    private static final String STYLESHEET_PATH = BASE_PATH + STYLESHEET_FILENAME;
    private static final String IMAGE_PATH = BASE_PATH + IMAGE_FILENAME;
    private static final String JAVASCRIPT_PATH = BASE_PATH + JAVASCRIPT_FILENAME;

    public static void main(String[] args) {
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
                .requestHandler(request -> {
                    HttpServerResponse response = request.response();

                    switch (request.path()) {
                        case BASE_PATH:
                            response
                                    .push(HttpMethod.GET, STYLESHEET_PATH, asyncResult -> {
                                        if (asyncResult.succeeded()) {
                                            HttpServerResponse pushedResp = asyncResult.result();
                                            pushedResp.sendFile(STYLESHEET_FILENAME);
                                        } else {
                                            System.err.println(asyncResult.cause().getMessage());
                                        }
                                    })
                                    .push(HttpMethod.GET, IMAGE_PATH, asyncResult -> {
                                        if (asyncResult.succeeded()) {
                                            HttpServerResponse pushedResp = asyncResult.result();
                                            pushedResp.sendFile(IMAGE_FILENAME);
                                        } else {
                                            System.err.println(asyncResult.cause().getMessage());
                                        }
                                    })
                                    .push(HttpMethod.GET, JAVASCRIPT_PATH, asyncResult -> {
                                        if (asyncResult.succeeded()) {
                                            HttpServerResponse pushedResp = asyncResult.result();
                                            pushedResp.sendFile(JAVASCRIPT_FILENAME);
                                        } else {
                                            System.err.println(asyncResult.cause().getMessage());
                                        }
                                    })
                                    .sendFile(HTML_FILENAME);
                            break;

                        case STYLESHEET_PATH:
                            response.sendFile(STYLESHEET_FILENAME);
                            break;

                        case IMAGE_PATH:
                            response.sendFile(IMAGE_FILENAME);
                            break;

                        case JAVASCRIPT_PATH:
                            response.sendFile(JAVASCRIPT_FILENAME);
                            break;

                        default:
                            System.out.println("Not found: " + request.path());
                            response.setStatusCode(404).end();
                            break;
                    }
                })
                .listen(8080);
    }
}
