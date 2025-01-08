package org.web.guffgaff.guffgaff.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web.guffgaff.guffgaff.dto.MessageDTO;

import java.security.Principal;

@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(MessageDTO message , Principal principal) {
        messagingTemplate.convertAndSendToUser(message.getReceiver(), "/queue/messages", message);
         System.out.println("Message sent to " + message.getReceiver());
         System.out.println("Message sent to " + message.getSender());
         System.out.println("Message sent to " + message.getMessage());
        System.out.println("Sending to: /queue/messages for user: " + message.getReceiver());
        System.out.println("Message sent successfully to: " + message.getReceiver());
        System.out.println("Authenticated user: " + principal.getName());

    }
    @RequestMapping("/guffgaff")
    public String chat() {
        return "Index";
    }
}
