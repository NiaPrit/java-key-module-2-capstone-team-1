package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.account.Account;
import com.techelevator.tenmo.model.account.AccountDAO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*******************************************************************************************************
 * This is where you code any API controllers you may create
 *
 * Feel free to create additional controller classs if you would
 * like to have separate controller classses base on functionality or use
********************************************************************************************************/
@RestController
public class ApiController {
    AccountDAO theAccountData;
    UserDAO userData;
    
    public ApiController (AccountDAO accountMethod) {
        this.theAccountData = accountMethod;
    }


    public ApiController (UserDAO userMethod) {
        this.userData = userMethod;
    }

    @RequestMapping (path = "/users",method = RequestMethod.GET)
    public List<User> getAllUsers (){
        return userData.findAll();
    }
    @RequestMapping (path = "/accounts/{id}/currentbalance",method = RequestMethod.GET)
    public Account getbalance (@PathVariable Long id){
        return  theAccountData.currentAccount(id);

    }
}
