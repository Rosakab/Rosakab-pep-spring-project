package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    
    // new register

        public Account register(Account account) {
            
            if (account.getUsername() == null || account.getUsername().isBlank()) {
                throw new IllegalArgumentException("Username shoudl not be empty");
            }
     
            if (account.getPassword() == null || account.getPassword().length() < 4) {
                throw new IllegalArgumentException("Password must be at least 4 characters");
            }
            // check to see if username already exists
            Account existingAccount = accountRepository.findByUsername(account.getUsername());
            if (existingAccount != null) {
                throw new IllegalArgumentException("Username already exists");
            }
            
            return accountRepository.save(account);
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    // login
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Account login(String username, String password) {
        Account account = getAccountByUsername(username);
        // ckeck to see if username already exists and if entered password is matched with the existing one!
        if (account != null && account.getPassword().equals(password)) {
            return account;
        }
        return null;
    }

}