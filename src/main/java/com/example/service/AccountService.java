package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Register new user
    public ResponseEntity<Account> register(Account account) {
        // Validate account information
        if (account.getUsername().isEmpty() || account.getPassword().length() < 4) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Check if username already exists
        Optional<Account> existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        // Save new account
        accountRepository.save(account);
        return ResponseEntity.ok(account);
    }

    // Login existing user
    public ResponseEntity<Account> login(Account account) {
        Optional<Account> existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount.isPresent() && existingAccount.get().getPassword().equals(account.getPassword())) {
            return ResponseEntity.ok(existingAccount.get());
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
