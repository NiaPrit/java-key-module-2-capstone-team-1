package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.account.Account;
import com.techelevator.tenmo.model.account.AccountDAO;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;

/*******************************************************************************************************
 * This is where you code any API controllers you may create
 *
 * Feel free to create additional controller classs if you would
 * like to have separate controller classses base on functionality or use
********************************************************************************************************/
@RestController
public class AccountController {

    AccountDAO theAccountData;

    public AccountController(AccountDAO accountMethod) {
        this.theAccountData = accountMethod;
    }


    @RequestMapping (path = "/accounts/{id}/currentbalance",method = RequestMethod.GET)
    public Account getBalance (@PathVariable Long id){
        return  theAccountData.currentAccount(id);
    }

    @RequestMapping(path = "/accounts/{id}/transfer/send", method = RequestMethod.PUT)
    public void updateSenderBalance(@PathVariable long id, @RequestParam (value="sendAmount")double sendAmount) {
        theAccountData.updateSenderAccount(id, sendAmount);
    }

    @RequestMapping(path = "/accounts/{id}/transfer/receive", method = RequestMethod.PUT)
    public void updateReceiverBalance(@PathVariable long id, @RequestParam (value="receiveAmount")double receiveAmount) {
        theAccountData.updateReceiverAccount(id, receiveAmount);
    }

}
