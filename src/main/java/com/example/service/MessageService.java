
package com.example.service;

import com.example.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.repository.MessageRepository;
import java.util.Optional;
import java.util.List;

@Service
public class MessageService {

@Autowired
private MessageRepository messageRepository;



// creation of new messages


public Message createMessage(Message message) {
    return messageRepository.save(message);
  
}



// retrieve all messages

public List<Message> getAllMessages() {
    return messageRepository.findAll();
}

//retrieve a message by its ID
public Message getMessageById(Integer id) {
    Optional<Message> optionalMessage = messageRepository.findById(id);
    return optionalMessage.orElse(null);
}

//update a message text identified by a message ID

public Message updateMessage(Integer id, Message message) {
    if (messageRepository.existsById(id)) {

        if (message.getMessage_text().isEmpty()) {
            throw new IllegalArgumentException("Text filed cannot be blank.");
        }

        if (message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Message characters cannot be greater than 255.");
        }

        return messageRepository.save(message);
    }
    return null; 
}


//retrieve all messages written by a particular user

public List<Message> getAllMessagesForParticularUser(Integer userId) {
    return messageRepository.findAllByPosted_by(userId);
}




//delete a message identified by a message ID

public boolean deleteMessageById(int messageId) {
    if(messageRepository.existsById(messageId)){
        messageRepository.deleteMessageById(messageId);
        return true;
    }
    return false;
}


}






















