package org.web.guffgaff.guffgaff.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .setHandshakeHandler(new DefaultHandshakeHandler() {

                    @Override
                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler webSocketHandler, Map<String, Object> attributes) {
                        // Assign a Principal using the username from attributes
                        String username = (String) attributes.get("username");
                        System.out.println("Frm Class websocketconfig"+username);
                        return () -> username;

                        // Create a Principal with the username
                    }
                })
                .addInterceptors(new CustomHandshakeInterceptor())
                .withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue/");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
        // Enable debugging
        config.setPreservePublishOrder(true);
    }

}