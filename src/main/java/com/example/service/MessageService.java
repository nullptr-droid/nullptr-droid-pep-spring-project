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
    public ResponseEntity<Void> deleteMessage(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.noContent().build();
    }

    // Update message text by ID
    public ResponseEntity<Void> updateMessage(Integer messageId, Message updatedMessage) {
        Optional<Message> existingMessage = messageRepository.findById(messageId);
        if (existingMessage.isPresent()) {
            Message message = existingMessage.get();
            message.setMessageText(updatedMessage.getMessageText());
            if (message.getMessageText().length() > 255 || message.getMessageText().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            messageRepository.save(message);
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Get all messages for a specific user
    public List<Message> getMessagesByUser(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
