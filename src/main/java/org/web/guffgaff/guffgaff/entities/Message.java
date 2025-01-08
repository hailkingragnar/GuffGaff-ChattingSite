package org.web.guffgaff.guffgaff.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String Message;
    private String Sender;
    private String Receiver;
    private String Date;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
}
