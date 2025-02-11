package com.bank.controller;

import com.bank.dto.AccountDto;
import com.bank.dto.ConvertDto;
import com.bank.dto.TransferDto;
import com.bank.entity.Account;
import com.bank.entity.Transaction;
import com.bank.entity.User;
import com.bank.repository.UserRepository;
import com.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto accountDto, Authentication authentication) throws Exception {
       Object principal = authentication.getPrincipal();
       if(principal instanceof User){
           User user = (User) principal;
           return ResponseEntity.ok(accountService.createAccount(accountDto, user));
       }else if(principal instanceof  String) {
           String username= (String) principal;

           User user = userRepository.findByUsernameIgnoreCase(username);
           return ResponseEntity.ok(accountService.createAccount(accountDto,user));
       }else {
           throw new ClassNotFoundException("Unexpected principal type: "+ principal.getClass().getName());
       }
    }

    @GetMapping
    public ResponseEntity<List<Account>> getUserAccounts(Authentication authentication) throws ClassNotFoundException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            return ResponseEntity.ok(accountService.getUserAccounts(user.getUid()));
        }else if (principal instanceof  String){
            String username = (String) principal;
            User user = userRepository.findByUsernameIgnoreCase(username);
            return ResponseEntity.ok(accountService.getUserAccounts(user.getUid()));
        }else {
            throw new ClassNotFoundException("Unexpected principal type: " + principal.getClass().getName());
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferFunds(@RequestBody TransferDto transferDto,Authentication authentication) throws Exception {
        Object principal = authentication.getPrincipal();
        if(principal instanceof  User){
            User user = (User) principal;
            return ResponseEntity.ok(accountService.transferFunds(transferDto,user));
        } else if (principal instanceof  String) {
            String username = (String) principal;
            User user = userRepository.findByUsernameIgnoreCase(username);
            return ResponseEntity.ok(accountService.transferFunds(transferDto,user));

        }else {
            throw new ClassNotFoundException("Unexpected principal type: " + principal.getClass().getName());
        }
    }

    @GetMapping("/rates")
    public ResponseEntity<Map<String, Double>> getExchangeRate(){
        return ResponseEntity.ok(accountService.getExchangeRate());
    }
    @PostMapping("/find")
    public ResponseEntity<Account> findAccount(@RequestBody TransferDto transferDto){
        return ResponseEntity.ok(accountService.findAccount(transferDto.getCode(), transferDto.getRecipientAccountNumber()));
    }

    @PostMapping("/convert")
    public ResponseEntity<Transaction> convertCurrency(@RequestBody ConvertDto convertDto,Authentication authentication) throws Exception {
        Object principal = authentication.getPrincipal();
        if(principal instanceof  User){
            User user = (User) principal;
            return ResponseEntity.ok(accountService.convertCurrency(convertDto, user));
        } else if (principal instanceof  String) {
            String username = (String) principal;
            User user = userRepository.findByUsernameIgnoreCase(username);
            return ResponseEntity.ok(accountService.convertCurrency(convertDto, user));
        } else {
            throw new ClassNotFoundException("Unexpected principal type: " + principal.getClass().getName());
        }
    }

}
