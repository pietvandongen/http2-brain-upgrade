package com.pietvandongen.http2brainupgrade;

import io.vertx.core.AsyncResult;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ServerTest {

    @Mock
    private HttpServerRequest request;

    @Mock
    private HttpServerResponse response;

    @Mock
    private AsyncResult<HttpServerResponse> asyncResult;

    @Test
    public void thatHandleRequestReturnsNotFoundStatusWhenPathHasNoRouting() {
        when(request.path()).thenReturn("a-non-existing-path.txt");
        when(request.response()).thenReturn(response);
        when(response.setStatusCode(anyInt())).thenReturn(response);

        Server.handleRequest(request);

        verify(response).setStatusCode(404);
        verify(response).end();
    }

    @Test
    public void thatHandleRequestSendsFileAsResponse() {
        Resource resource = Server.IMAGE;

        when(request.path()).thenReturn(resource.getPath());
        when(request.response()).thenReturn(response);

        Server.handleRequest(request);

        verify(response).sendFile(resource.getFileName());
    }

    @Test
    public void thatHandleRequestPushesFilesWhenHtmlIsRequested() {
        Resource resource = Server.HTML;

        when(request.path()).thenReturn(resource.getPath());
        when(request.response()).thenReturn(response);

        Server.handleRequest(request);

        verify(response).push(eq(HttpMethod.GET), eq(Server.STYLESHEET.getPath()), any());
        verify(response).push(eq(HttpMethod.GET), eq(Server.IMAGE.getPath()), any());
        verify(response).push(eq(HttpMethod.GET), eq(Server.JAVASCRIPT.getPath()), any());
        verify(response).sendFile(resource.getFileName());
    }

    @Test
    public void thatHandlePushDoesNothingWhenResultWasNotSuccessful() {
        when(asyncResult.succeeded()).thenReturn(false);
        when(asyncResult.cause()).thenReturn(new Exception());

        Server.handlePush(asyncResult, Resource.create("/test.txt", "test.txt"));

        verify(asyncResult, never()).result();
    }

    @Test
    public void thatHandlePushSendsFileWhenResultSucceeded() {
        Resource resource = Resource.create("/test.txt", "test.txt");

        when(asyncResult.succeeded()).thenReturn(true);
        when(asyncResult.result()).thenReturn(response);

        Server.handlePush(asyncResult, resource);

        verify(asyncResult).result();
        verify(response).sendFile(resource.getFileName());
    }
}
