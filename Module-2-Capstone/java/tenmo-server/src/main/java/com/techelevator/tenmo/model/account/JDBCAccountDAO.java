package com.techelevator.tenmo.model.account;

import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
@Component
public class JDBCAccountDAO implements AccountDAO{

   private JdbcTemplate dao;

   public JDBCAccountDAO(DataSource ourData){
       this.dao = new JdbcTemplate(ourData);
   }

    @Override
    public Account currentAccount(long accountId) {
     Account currentAccount =  new Account();
      String searchString = "select * from accounts where account_id = ?";
        SqlRowSet results = dao.queryForRowSet(searchString,accountId);
        if(results.next()){

            return currentAccount = mapRowToAccount(results);
        }
        return null;
    }

    @Override
    public void updateSenderAccount(long accountId, double amountToUpdate) {
       String searchString = "UPDATE accounts SET balance =? WHERE account_id = ?";
           dao.update(searchString, currentAccount(accountId).getBalance() - amountToUpdate, accountId);
   }

    @Override
    public void updateReceiverAccount(long accountId, double amountToUpdate) {
        String searchString = "UPDATE accounts SET balance =? WHERE account_id = ?";
        dao.update(searchString, currentAccount(accountId).getBalance() + amountToUpdate, accountId);
    }

    private Account mapRowToAccount (SqlRowSet results){
       Account account = new Account();
       account.setAccountId(results.getLong("account_id"));
       account.setUserId(results.getLong("user_id"));
       account.setBalance(results.getDouble("balance"));
       return account;
    }
}
