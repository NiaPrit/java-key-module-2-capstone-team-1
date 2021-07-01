package com.techelevator.tenmo.model.account;

import com.techelevator.tenmo.model.User;

public interface AccountDAO {

     Account currentAccount(long accountId);

     void updateSenderAccount(long accountId, double amountToUpdate);

     void updateReceiverAccount(long accountId, double amountToUpdate);

}
