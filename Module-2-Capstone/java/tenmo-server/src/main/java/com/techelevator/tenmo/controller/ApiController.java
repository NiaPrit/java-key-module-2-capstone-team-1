package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.account.Account;
import com.techelevator.tenmo.model.account.AccountDAO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/*******************************************************************************************************
 * This is where you code any API controllers you may create
 *
 * Feel free to create additional controller classs if you would
 * like to have separate controller classses base on functionality or use
********************************************************************************************************/
@RequestMapping ("/accounts")
@RestController
public class ApiController {
    AccountDAO theAccountData;
    public ApiController (AccountDAO accountMethod) {
        this.theAccountData = accountMethod;
    }

    @RequestMapping (path = "/{id}/currentbalance",method = RequestMethod.GET)
    public Account getbalance (@PathVariable Long id){
        return  theAccountData.currentAccount(id);

    }
}
