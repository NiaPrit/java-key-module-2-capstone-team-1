package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.account.Account;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

/*******************************************************************************************************
 * This is where you code Application Services required by your solution
 * 
 * Remember:  theApp ==> ApplicationServices  ==>  Controller  ==>  DAO
********************************************************************************************************/

public class TenmoApplicationServices {
    private final String BASE_URL_API;
    private RestTemplate theApi = new RestTemplate();
    public TenmoApplicationServices (String aUrl){
        this.BASE_URL_API = aUrl;
    }

    public Account getCurentBal(int accountId) {
        return theApi.getForObject(BASE_URL_API + "accounts/" + accountId + "/currentbalance", Account.class);

    }
    public User[] listUsers(){
        return theApi.getForObject(BASE_URL_API + "users",User[].class);
    }


}
