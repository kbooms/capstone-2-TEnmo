package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transfers")
//@PreAuthorize("isAuthenticated()")
public class TransferController {

 TransferDao transferDao;
 AccountDao accountDao;


    public TransferController(TransferDao transferDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    @GetMapping
    public List<Transfer> list(@RequestBody Account account){
    return  transferDao.getTransfersList(account);
    }


    @PostMapping("/listSent")
    public List<Transfer> getListSentTransfers(@RequestBody Account account){
        return  transferDao.approvedTransferList(account);
    }

    @PostMapping("/listRequest")
    public List<Transfer> getListPendingTransfers(@RequestBody Account account){
        return  transferDao.pendingTransferList(account);
    }



    @PostMapping(value = "/send")
    @ResponseStatus(HttpStatus.CREATED)
    public void SendBucks(@RequestBody Transfer transfer){

        if (transferDao.Create(transfer)) {

            accountDao.updateAccount(decreasedBalanceAccount(transfer.getFromAccount(),
                    transfer.getAmountForTransfer()));

            accountDao.updateAccount(increasedBalanceAccount(transfer.getToAccount(),
                                      transfer.getAmountForTransfer()));
        }

    }

   @PostMapping(value = "/request")
   @ResponseStatus(HttpStatus.CREATED)
    public void requestBucks(@RequestBody Transfer transfer){
       transferDao.Create(transfer);
   }

   @PutMapping
   @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@RequestBody Transfer transfer){
        if (transferDao.updateStatus(transfer)){
            accountDao.updateAccount(decreasedBalanceAccount(transfer.getToAccount(),
                    transfer.getAmountForTransfer()));
            accountDao.updateAccount(increasedBalanceAccount(transfer.getFromAccount(),
                    transfer.getAmountForTransfer()));
        }
   }


    private Account increasedBalanceAccount(Account account, BigDecimal gap){
        BigDecimal toAccountBalance = accountDao.getAccount(account.getUser().getId()).getBalance()
                .add(gap);
        account.setBalance(toAccountBalance);
        return account;

    }

    private Account decreasedBalanceAccount(Account account, BigDecimal gap){
        BigDecimal toAccountBalance = accountDao.getAccount(account.getUser().getId()).getBalance()
                .subtract(gap);
        account.setBalance(toAccountBalance);
        return account;

    }

}
