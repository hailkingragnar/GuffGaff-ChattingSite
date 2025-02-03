package org.web.guffgaff.guffgaff.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.web.guffgaff.guffgaff.utils.JWTUtils;

@Component
public class JWTWebSocketHandler extends TextWebSocketHandler {

    private final JWTUtils jwtUtils;

    public JWTWebSocketHandler(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = (String) session.getAttributes().get("jwtToken");

        if (token != null && jwtUtils.validateToken(token, jwtUtils.extractUsername(token))) {
            System.out.println("WebSocket connection authenticated with JWT: " + token);
        } else {
            session.close(); // Close connection if invalid
        }
    }
}

