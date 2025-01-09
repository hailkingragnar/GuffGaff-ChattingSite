package org.web.guffgaff.guffgaff.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thymeleaf.expression.Messages;
import org.web.guffgaff.guffgaff.entities.Message;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message,Long> {

    List<Message> findBySenderAndReceiver(String sender, String receiver);
}
