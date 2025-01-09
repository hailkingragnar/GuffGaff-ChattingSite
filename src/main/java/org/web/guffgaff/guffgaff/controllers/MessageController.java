package org.web.guffgaff.guffgaff.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.expression.Messages;
import org.web.guffgaff.guffgaff.dto.MessageDTO;
import org.web.guffgaff.guffgaff.entities.Message;
import org.web.guffgaff.guffgaff.repo.MessageRepo;
import org.web.guffgaff.guffgaff.utils.EncryptionUtil;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MessageController {



@Autowired
    private MessageRepo messageRepo;
    private final SimpMessagingTemplate messagingTemplate;



    public MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(Message message , Principal principal) {

        message.setMessage(EncryptionUtil.encrypt(message.getMessage()));
        message.setDate(LocalDateTime.now());

        messageRepo.save(message);
        message.setMessage(EncryptionUtil.decrypt(message.getMessage()));
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

    @GetMapping("/messages")
    @ResponseBody
    public List<Message> getMessages(@RequestParam String sender, @RequestParam String receiver){
        Message message = new Message();
        String decSender = URLDecoder.decode(sender, StandardCharsets.UTF_8);
        String decReceiver = URLDecoder.decode(receiver, StandardCharsets.UTF_8);
        List<Message> messagesSent = messageRepo.findBySenderAndReceiver(decSender, decReceiver);
           for (Message messageSent : messagesSent) {
               messageSent.setMessage(EncryptionUtil.decrypt(messageSent.getMessage()));
               messageSent.setSender("you");
           }

        List<Message> messagesReceived = messageRepo.findBySenderAndReceiver(decReceiver,decSender);
        for (Message messageReceived : messagesReceived) {
            messageReceived.setMessage(EncryptionUtil.decrypt(messageReceived.getMessage()));

        }
        List<Message> messages = new ArrayList<>();
        messages.addAll(messagesSent);
        messages.addAll(messagesReceived);

        return messages;
    }
}


