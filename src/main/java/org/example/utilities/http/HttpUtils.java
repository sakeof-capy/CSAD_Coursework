package org.example.utilities.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.example.utilities.dynobjects.DynamicObject;
import org.example.utilities.dynobjects.StandardDynamicObject;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

public class HttpUtils {
    public static Optional<Credentials> credentialsFromBody(HttpExchange exchange) {
        try(var body = exchange.getRequestBody()) {
            var objectMapper = new ObjectMapper();
            return Optional.of(objectMapper.readValue(body, Credentials.class));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public static Optional<DynamicObject> bodyToDynamicObject(HttpExchange exchange) {
        if(hasEmptyBody(exchange))
            return Optional.empty();
        try (var body = exchange.getRequestBody()){
            var objectMapper = new ObjectMapper();
            var readTreeMap = objectMapper.readValue(body, new TypeReference<TreeMap<String, String>>() {});
            return Optional.of(new StandardDynamicObject(readTreeMap));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static void sendResponseOperationResult(HttpExchange exchange, int statusCode, List<DynamicObject> objects) {
        var mappedList = objects.stream()
                .map(DynamicObject::getMap)
                .toList();
        sendResponseObject(exchange, statusCode, mappedList);
    }

    public static Optional<String> extractQueryParam(String query, String paramName) {
        if (query != null) {
            var tokens = query.split("&");
            for (String token : tokens) {
                var params = token.split("=");
                if (params.length == 2 && params[0].equals(paramName)) {
                    return Optional.of(params[1]);
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<DynamicObject> queryParamsToDynamicObject(HttpExchange exchange) {
        if(hasEmptyQueryParams(exchange))
            return Optional.empty();
        final var res = new StandardDynamicObject();
        final var query = exchange.getRequestURI().getQuery();
        var tokens = query.split("&");
        for (String token : tokens) {
            var params = token.split("=");
            if (params.length == 2) {
                res.put(params[0], params[1]);
            }
        }
        return Optional.of(res);
    }

    public static Optional<DynamicObject> extractMergedParamsFromBodyAndQuery(HttpExchange exchange) {
        final var bodyParams = bodyToDynamicObject(exchange);
        final var queryParams = queryParamsToDynamicObject(exchange);
        if(bodyParams.isPresent() && queryParams.isPresent()) {
            final var res = new StandardDynamicObject();
            for(var param : queryParams.get().getMap().entrySet()) {
                res.put(param.getKey(), param.getValue());
            }
            for(var param : bodyParams.get().getMap().entrySet()) {
                if(res.get(param.getKey()).isEmpty())
                    res.put(param.getKey(), param.getValue());
            }
            return Optional.of(res);

        } else if(bodyParams.isPresent()) {
            return bodyParams;
        } else if(queryParams.isPresent()) {
            return queryParams;
        }
        return Optional.empty();
    }

    public static Optional<String> extractQueryParam(HttpExchange exchange, String paramName) {
        final var query = exchange.getRequestURI().getQuery();
        return extractQueryParam(query, paramName);
    }

    public static boolean hasEmptyBody(HttpExchange exchange) {
        var contentLength = exchange.getRequestHeaders().getFirst("Content-Length");
        return contentLength == null ||
                Integer.parseInt(contentLength) == 0;
    }

    public static boolean hasEmptyQueryParams(HttpExchange exchange) {
        var requestQuery = exchange.getRequestURI().getQuery();
        return requestQuery == null || requestQuery.isEmpty();
    }

    public static void writeOptions(final HttpExchange exchange) {
        try {
            Headers headers = exchange.getResponseHeaders();
            headers.set("Access-Control-Allow-Origin", "http://localhost:" + PORT);
            headers.add("Access-Control-Max-Age", "86400");
            headers.set("Access-Control-Allow-Methods", "POST, GET, OPTIONS ");
            headers.set("Access-Control-Allow-Headers", "content-type");
            exchange.sendResponseHeaders(204, -1);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void sendResponse(HttpExchange exchange, int code, String message) {
        sendResponseObject(exchange, code, message);
    }

    public static void sendResponse(HttpExchange exchange, int code) {
        sendResponseObject(exchange, code, null);
    }

    public static void sendResponseObject(HttpExchange exchange, int code, Object obj) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "content-type");
        exchange.getResponseHeaders().add("Content-Type", "application/json");

        try (var responseBody = exchange.getResponseBody()){

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                System.out.println("CORS SENT");
                exchange.getResponseHeaders().add("Access-Control-Max-Age", "86400");
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            byte[] bytes;
            if(obj != null) {
                var objectMapper = new ObjectMapper();
                bytes = objectMapper.writeValueAsBytes(obj);
            } else {
                bytes = new byte[0];
            }
            exchange.sendResponseHeaders(code, bytes.length == 0 ? -1 : bytes.length);
            if(bytes.length != 0) responseBody.write(bytes);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static final int PORT = 8000;
}
