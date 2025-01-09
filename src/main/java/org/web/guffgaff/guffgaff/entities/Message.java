package org.web.guffgaff.guffgaff.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
public class Message {
    @Column(name = "encrypted_message", columnDefinition = "text")
    private String message;
    private String sender;
    private String receiver;
    private LocalDateTime dateAndTime;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    // No-args constructor
    public Message() {
    }

    // All-args constructor
    public Message(String message, String sender, String receiver, LocalDateTime dateAndTime, long id) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.dateAndTime = dateAndTime;
        this.id = id;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDate(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

