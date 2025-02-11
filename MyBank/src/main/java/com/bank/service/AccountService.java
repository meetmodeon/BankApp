package com.bank.service;

import com.bank.dto.AccountDto;
import com.bank.dto.ConvertDto;
import com.bank.dto.TransferDto;
import com.bank.entity.Account;
import com.bank.entity.Transaction;
import com.bank.entity.User;
import com.bank.repository.AccountRepository;
import com.bank.service.helper.AccountHelper;
import com.bank.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountHelper accountHelper;
    private final ExchangeRateService exchangeRateService;

    public Account createAccount(AccountDto accountDto, User user) throws Exception {

        return accountHelper.createAccount(accountDto,user);
    }

    public List<Account> getUserAccounts(String uid) {
        return accountRepository.findAllByOwnerUid(uid);
    }

    public Transaction transferFunds(TransferDto transferDto, User user) throws Exception {
        var senderAccount = accountRepository.findByCodeAndOwnerUid(transferDto.getCode(),user.getUid())
                .orElseThrow(()-> new UnsupportedOperationException("Account of type currency do not exists for user"));
        var receiverAccount = accountRepository.findByAccountNumber(transferDto.getRecipientAccountNumber()).orElseThrow();
       return accountHelper.performTransfer(senderAccount,receiverAccount,transferDto.getAmount(),user);
    }

    public Map<String,Double> getExchangeRate(){
        return exchangeRateService.getRates();
    }

    public Transaction convertCurrency(ConvertDto convertDto, User user) throws Exception {
        return accountHelper.convertCurrency(convertDto,user);
    }

    public Account findAccount(String code, long recipientAccountNumber){
        System.out.println("Account Number: " + recipientAccountNumber);
        System.out.println("Code: " + code);
        return accountRepository.findByCodeAndAccountNumber(code, recipientAccountNumber);
    }
}
