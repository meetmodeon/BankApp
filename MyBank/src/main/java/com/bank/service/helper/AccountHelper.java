package com.bank.service.helper;

import com.bank.dto.AccountDto;
import com.bank.dto.ConvertDto;
import com.bank.entity.*;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import com.bank.service.ExchangeRateService;
import com.bank.service.TransactionService;
import com.bank.util.RandomUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Data
@RequiredArgsConstructor
public class AccountHelper {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ExchangeRateService exchangeRateService;
    private final TransactionService transactionService;

    private final Map<String,String> CURRENCIES = Map.of(
            "USD","United States",
            "EUR","European Union",
            "GBP","Great Britain",
            "JPY","Japan",
            "AUD","Australia",
            "CAD","Canada",
            "INR","India Rupee",
            "NGN","Nigerian Naira"
    );


    public Account createAccount(AccountDto accountDto, User user) throws Exception {
        long accountNumber;
        validateAccountNotExistsForUser(accountDto.getCode(), user.getUid());
        do {
            accountNumber= new RandomUtil().generateRandom(10);
        }while(accountRepository.existsByAccountNumber(accountNumber));
        var account= Account.builder()
                .accountNumber(accountNumber)
                .accountName(user.getFirstName() + " "+user.getLastName())
                .balance(1000)
                .owner(user)
                .code(accountDto.getCode())
                .symbol(accountDto.getSymbol())
                .label(CURRENCIES.get(accountDto.getCode()))
                .build();
       return accountRepository.save(account);
    }

    public void validateAccountNotExistsForUser(String code, String uid) throws Exception {
        if(accountRepository.existsByCodeAndOwnerUid(code,uid)){
            throw new Exception("Account of this type already exists for this user");
        }
    }

    public Transaction performTransfer(Account senderAccount, Account receiverAccount, double amount,User user) throws Exception {
        validateSufficientFunds(senderAccount, amount * 1.01);
        senderAccount.setBalance(senderAccount.getAccountNumber() - amount * 1.01);
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);
        accountRepository.saveAll(List.of(senderAccount, receiverAccount));
        var senderTransaction = Transaction.builder()
                .account(senderAccount)
                .status(Status.COMPLETED)
                .type(Type.WITHDRAW)
                .txFee(amount * 0.01)
                .amount(amount)
                .owner(user)
                .build();

        var recipientTransaction = Transaction.builder()
                .account(receiverAccount)
                .status(Status.COMPLETED)
                .type(Type.DEPOSIT)
                .amount(amount)
                .owner(receiverAccount.getOwner())
                .build();
        return transactionRepository.saveAll(List.of(senderTransaction,recipientTransaction)).get(0);
    }

    public void validateAccountOwner(Account account, User user) throws OperationNotSupportedException {
        if(!account.getOwner().getUid().equals(user.getUid())){
            throw new OperationNotSupportedException("Invalid account owner");
        }
    }

    public void validateSufficientFunds(Account account, double amount) throws OperationNotSupportedException {
        if(account.getBalance() <= amount){
            throw new OperationNotSupportedException("Insufficient funds in the account");
        }
    }

    public void validateAmount(double amount) throws Exception{
        if(amount <= 0){
            throw new IllegalArgumentException("Invalid amount");
        }
    }

    public void validateDifferentCurrencyType(ConvertDto convertDto) throws Exception{

        if(convertDto.getToCurrency().equals(convertDto.getFromCurrency())){
            throw new IllegalArgumentException("Conversion between the same currency type is not allowed");
        }

    }

    public void validateAccountOwnership(ConvertDto convertDto,String uid) throws Exception{
        accountRepository.findByCodeAndOwnerUid(convertDto.getFromCurrency(), uid).orElseThrow();
        accountRepository.findByCodeAndOwnerUid(convertDto.getToCurrency(), uid).orElseThrow();
    }

    public void validateConversion(ConvertDto convertDto,String uid) throws Exception{
        validateDifferentCurrencyType(convertDto);
        validateAccountOwnership(convertDto,uid);
        validateAmount(convertDto.getAmount());
        validateSufficientFunds(accountRepository.findByCodeAndOwnerUid(convertDto.getFromCurrency(), uid).get(), convertDto.getAmount());

    }

    public Transaction convertCurrency(ConvertDto convertDto,User user) throws Exception{
        validateConversion(convertDto, user.getUid());
       var rates= exchangeRateService.getRates();
       var sendingRates = rates.get(convertDto.getFromCurrency());
       var receivingRates = rates.get(convertDto.getToCurrency());
       var computedAmount = (receivingRates/sendingRates) * convertDto.getAmount();
       Account fromAccount=accountRepository.findByCodeAndOwnerUid(convertDto.getFromCurrency(),user.getUid()).orElseThrow();
       Account toAccount = accountRepository.findByCodeAndOwnerUid(convertDto.getToCurrency(),user.getUid()).orElseThrow();
       fromAccount.setBalance(toAccount.getBalance()-(convertDto.getAmount() * 1.01));
       toAccount.setBalance(toAccount.getBalance()+ computedAmount);
       accountRepository.saveAll(List.of(fromAccount,toAccount));

       var fromAccountTransaction = transactionService.createAccountTransaction(convertDto.getAmount(), Type.CONVERSION,convertDto.getAmount()*0.01, user,fromAccount);
       var toAccountTransaction = transactionService.createAccountTransaction(computedAmount,Type.DEPOSIT, convertDto.getAmount()*0.00,user,toAccount);

       return fromAccountTransaction;

    }

    public boolean existsByCodeAndOwnerUid(String code, String uid){
        return accountRepository.existsByCodeAndOwnerUid(code, uid);
    }

    public Optional<Account> findByCodeAndOwnerUid(String code, String uid){
        return accountRepository.findByCodeAndOwnerUid(code, uid);
    }

    public Account save(Account usdAccount){
        return accountRepository.save(usdAccount);
    }
}
