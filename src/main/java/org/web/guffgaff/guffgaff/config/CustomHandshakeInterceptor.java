package org.web.guffgaff.guffgaff.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.web.guffgaff.guffgaff.utils.JWTUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
@Component
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    private final JWTUtils jwtUtils;

    @Autowired
    public CustomHandshakeInterceptor(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            Cookie[] cookies = servletRequest.getCookies();

            if(cookies != null) {
                for (Cookie cookie : cookies) {
                    if(cookie.getName().equals("jwtToken")) {
                        String userName = null;
                        String token = cookie.getValue();
                        System.out.println("inside interceptor the token is : " + token);
                       try {
                            userName = jwtUtils.extractUsername(token);
                       }catch(Exception e) {
                           System.out.println("inside interceptor the token is : " + e.getMessage());
                       }
                        System.out.println("inside interceptor the name is : " + userName);
                        if(userName != null && jwtUtils.validateToken(token, userName)) {
                            attributes.put("jwtToken", token);
                            return true;
                        }
                    }
                }
            }
        }
            response.setStatusCode(HttpStatus.UNAUTHORIZED);  // Reject connection if not authenticated
            return false;

    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    private boolean isUserAuthenticated(ServerHttpRequest request) {
        // Spring Security provides an authenticated principal
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
