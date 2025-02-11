package com.bank.controller;

import com.bank.entity.Transaction;
import com.bank.entity.User;
import com.bank.repository.UserRepository;
import com.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final UserRepository userRepository;
    private final TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAllTransactions(@RequestParam String page, Authentication auth){
        Object principal = auth.getPrincipal();
        if(principal instanceof User){
            User user= (User) principal;
            return transactionService.getAllTransactions(page,user);
        }else if(principal instanceof  String){
            String username = (String) principal;
            User user = userRepository.findByUsernameIgnoreCase(username);
            return transactionService.getAllTransactions(page,user);
        }else{
            throw new IllegalArgumentException("User not found: "+auth);
        }
    }

    @GetMapping("/c/{cardId}")
    public List<Transaction> getTransactionsByCardId(@PathVariable String cardId, @RequestParam String page, Authentication auth){
        Object principal = auth.getPrincipal();
        if(principal instanceof User){
            User user= (User) principal;
            return transactionService.getTransactionsByCardId(cardId, page, user);
        } else if(principal instanceof  String){
            String username = (String) principal;
            User user = userRepository.findByUsernameIgnoreCase(username);
            return transactionService.getTransactionsByCardId(cardId, page, user);
        } else{
            throw new IllegalArgumentException("Card not found: "+auth);
        }
    }

    @GetMapping("/a/{accountId}")
    public List<Transaction> getTransactionsByAccountId(@PathVariable String accountId, @RequestParam String page, Authentication auth){
        Object principal = auth.getPrincipal();
        if(principal instanceof User){
            User user= (User) principal;
            return transactionService.getTransactionsByAccountId(accountId, page, user);
        } else if(principal instanceof  String){
            String username = (String) principal;
            User user = userRepository.findByUsernameIgnoreCase(username);
            return transactionService.getTransactionsByAccountId(accountId, page, user);
        } else{
            throw new IllegalArgumentException("Account not found: "+auth);
        }
    }
}
