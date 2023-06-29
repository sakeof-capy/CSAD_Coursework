package org.example.server.contexts.dispatching;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.utilities.http.HttpUtils;

import java.util.function.Consumer;

public class DispatchingHttpHandler implements HttpHandler {
    public DispatchingHttpHandler(EndpointDispatcher<Consumer<HttpExchange>> dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void handle(HttpExchange exchange) {
        final var requestType = exchange.getRequestMethod();
        final var uri = exchange.getRequestURI();
        final var processor = dispatcher.dispatch(requestType, uri);
        if(processor.isPresent())
            processor.get().accept(exchange);
        else
            HttpUtils.sendResponse(exchange, 404);
    }

    private final EndpointDispatcher<Consumer<HttpExchange>> dispatcher;
}
