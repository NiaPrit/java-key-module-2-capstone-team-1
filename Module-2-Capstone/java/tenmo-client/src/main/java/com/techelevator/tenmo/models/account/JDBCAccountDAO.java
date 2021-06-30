package com.techelevator.tenmo.models.account;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;

public class JDBCAccountDAO implements AccountDAO{

   private JdbcTemplate dao;

   public JDBCAccountDAO(DataSource ourData){
       this.dao = new JdbcTemplate(ourData);
   }

    @Override
    public double getCurrentBal(long accountId) {
      double currentBal = 0;
      String searchString = "select * from accounts where account_id = ?";
        SqlRowSet results = dao.queryForRowSet(searchString,accountId);
        if(results.next()){
          Account accountResult = mapRowToAccount(results);
           currentBal = accountResult.getBalance();
        }
        return currentBal;
    }

    private Account mapRowToAccount (SqlRowSet results){
       Account account = new Account();
       account.setAccountId(results.getLong("account_id"));
       account.setUserId(results.getLong("user_id"));
       account.setBalance(results.getDouble("balance"));
       return account;
    }
}
