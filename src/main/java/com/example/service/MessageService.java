package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    // Create new message
    public ResponseEntity<Message> createMessage(Message message) {
        // Validate the message
        if (message.getMessageText().isEmpty() || message.getMessageText().length() > 255) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Validate if user exists
        if (!accountRepository.existsById(message.getPostedBy())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Save message
        messageRepository.save(message);
        return ResponseEntity.ok(message);
    }

    // Get all messages
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Get message by ID
    public Optional<Message> getMessageById(Integer messageId) {
        return messageRepository.findById(messageId);
    }

    // Delete message by ID
    public ResponseEntity<Integer> deleteMessage(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return ResponseEntity.status(200).body(1);
        }
        return ResponseEntity.status(200).build();
    }

    // Update message text by ID
    public ResponseEntity<Integer> updateMessage(Integer messageId, Message updatedMessage) {
        Optional<Message> existingMessage = messageRepository.findById(messageId);
        if (existingMessage.isPresent()) {
            Message message = existingMessage.get();
    
            if (updatedMessage.getMessageText().length() > 255 || updatedMessage.getMessageText().isEmpty()) {
                return ResponseEntity.status(400).body(0); // Invalid messageText
            }
    
            message.setMessageText(updatedMessage.getMessageText());
            messageRepository.save(message);
    
            return ResponseEntity.status(200).body(1); // One row modified
        }
    
        return ResponseEntity.status(400).body(0); // No rows updated
    }
    

    // Get all messages for a specific user
    public List<Message> getMessagesByUser(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
