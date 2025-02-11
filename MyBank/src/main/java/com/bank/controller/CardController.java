package com.bank.controller;

import com.bank.entity.Card;
import com.bank.entity.Transaction;
import com.bank.entity.User;
import com.bank.repository.UserRepository;
import com.bank.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Card> getCard(Authentication authentication){
        Object principal = authentication.getPrincipal();
        if(principal instanceof User){
            User user = (User) principal;
            return ResponseEntity.ok(cardService.getCard(user));
        }else if(principal instanceof String){
            String username = (String) principal;
            User user = userRepository.findByUsernameIgnoreCase(username);
            return ResponseEntity.ok(cardService.getCard(user));
        }else{
            throw new IllegalArgumentException("Card user not found");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Card> createCard(@RequestParam double amount, Authentication authentication) throws Exception {
        Object principal = authentication.getPrincipal();
        if(principal instanceof User){
            User user = (User) principal;
            return ResponseEntity.ok(cardService.createCard(amount, user));
        } else if(principal instanceof String){
            String username = (String) principal;
            User user = userRepository.findByUsernameIgnoreCase(username);
            return ResponseEntity.ok(cardService.createCard(amount, user));
        } else{
            throw new IllegalArgumentException("Card user not found");
        }
    }
    @PostMapping("/credit")
    public ResponseEntity<Transaction> creditCard(@RequestParam double amount, Authentication authentication){
        Object principal = authentication.getPrincipal();
        if(principal instanceof User){
            User user = (User) principal;
            return ResponseEntity.ok(cardService.creditCard(amount, user));
        } else if(principal instanceof String){
            String username = (String) principal;
            User user = userRepository.findByUsernameIgnoreCase(username);
            return ResponseEntity.ok(cardService.creditCard(amount, user));
        } else{
            throw new IllegalArgumentException("Card user not found");
        }
    }

    @PostMapping("/debit")
    public ResponseEntity<Transaction> debitCard(@RequestParam double amount, Authentication authentication){
        Object principal = authentication.getPrincipal();
        if(principal instanceof User){
            User user = (User) principal;
            return ResponseEntity.ok(cardService.debitCard(amount, user));
        } else if(principal instanceof String){
            String username = (String) principal;
            User user = userRepository.findByUsernameIgnoreCase(username);
            return ResponseEntity.ok(cardService.debitCard(amount, user));
        } else{
            throw new IllegalArgumentException("Card user not found");
        }
    }

}
