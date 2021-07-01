package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.account.Account;
import com.techelevator.tenmo.models.transfers.Transfer;
import io.cucumber.java.bs.A;
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

    public Account getCurrentBal(int accountId) {
        return theApi.getForObject(BASE_URL_API + "accounts/" + accountId + "/currentbalance", Account.class);
    }

    public User[] listUsers(){
        return theApi.getForObject(BASE_URL_API + "users",User[].class);
    }

    public void sendAmountFromUser(int accountId, double amountToSend) {
        theApi.put(BASE_URL_API + "accounts/" + accountId + "/transfer/send?sendAmount=" + amountToSend, Account.class);
    }


    public void receiveAmountFromUser(int accountId, double amountToSend) {
        theApi.put(BASE_URL_API + "accounts/" + accountId + "/transfer/receive?receiveAmount=" + amountToSend, Account.class);
    }

    public Transfer[] listAllTransfers(long accountId) {
        return theApi.getForObject(BASE_URL_API + "transfers/" + accountId, Transfer[].class);
    }
}
