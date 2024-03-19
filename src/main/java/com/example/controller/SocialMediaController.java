package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.repository.AccountRepository;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 @RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AccountRepository accountRepository;

    // controller for accountService

    @PostMapping("/register")
    public ResponseEntity<Account> registerUser(@RequestBody Account account) {
        Account existingAccount = accountService.getAccountByUsername(account.getUsername());
        if (existingAccount != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Account savedAccount = accountService.saveAccount(account);
        return new ResponseEntity<>(savedAccount, HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<Account> loginUser(@RequestBody Account account) {
        Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());
        if (loggedInAccount != null) {
            return new ResponseEntity<>(loggedInAccount, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


// controller for MessageService


@PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
      
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() ||
            message.getMessage_text().length() > 255) {
            return ResponseEntity.badRequest().build();
        }

        if (message.getPosted_by() == null ||!accountRepository.existsById(message.getPosted_by())) {
            return ResponseEntity.badRequest().build();
        }
        Message createdMessage = messageService.createMessage(message);
        return ResponseEntity.ok(createdMessage);
    }



    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(messages);
    }


    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            return ResponseEntity.ok().build(); // Return 200 OK with an empty body.
        }
        return ResponseEntity.ok(message);
    }


    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable Integer messageId, @RequestBody Message messageUpdate) {
        try {
           
            if (messageUpdate.getMessage_text().trim().isEmpty() || messageUpdate.getMessage_text().length() > 255) {
                return ResponseEntity.badRequest().body("Message text is either empty or exceeds the allowable limit.");
            }

            Message updatedMessage = messageService.updateMessage(messageId, messageUpdate);

            if (updatedMessage == null) {
                return ResponseEntity.badRequest().body("Message with ID " + messageId + " not found.");
            }

            return ResponseEntity.ok(1);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesForParticularUser(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getAllMessagesForParticularUser(accountId);
        if (messages.isEmpty()) {
            return ResponseEntity.ok(messages);
        }

        return ResponseEntity.ok(messages);
    }



    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
        boolean wasDeleted= messageService.deleteMessageById(messageId);

        if(wasDeleted){
            return ResponseEntity.ok().body(1);
        }else{
            return ResponseEntity.ok().body(null);
        }
       
    }

   
   


}
