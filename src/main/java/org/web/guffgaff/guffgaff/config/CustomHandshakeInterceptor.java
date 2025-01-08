package org.web.guffgaff.guffgaff.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CustomHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // Extract username from headers (or query params)
        String query = request.getURI().getQuery(); // Get from headers
        String username = null;
        if (query != null) {
            for (String param : query.split("&")) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && "username".equalsIgnoreCase(keyValue[0])) {
                    username = keyValue[1];
                    break;
                }
            }
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }

       username = java.net.URLDecoder.decode(username, StandardCharsets.UTF_8);
        attributes.put("username",username);
        System.out.println("Username decoded and added to attributes: " + username);

        return true;

    }
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
