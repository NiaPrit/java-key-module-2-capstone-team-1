package com.techelevator.tenmo.models.transfers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JDBCTransferDAO implements TransferDAO{

    private JdbcTemplate dao;

    public JDBCTransferDAO(DataSource ourData){
        this.dao = new JdbcTemplate(ourData);
    }


    @Override
    public List<Transfer> allTransfer(long accountFromId) {
        List<Transfer> allTransfers = new ArrayList<>();
        String searchString = "SELECT * from transfers";

        SqlRowSet results = dao.queryForRowSet(searchString);
        while (results.next()) {
            Transfer transferResult = mapRowToTransfer(results);
            allTransfers.add(transferResult);
        }
        return allTransfers;
    }


    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer newTransfer = new Transfer();
        newTransfer.setTransferId(results.getLong("transfer_id"));
        newTransfer.setTransferTypeId(results.getLong("transfer_type_id"));
        newTransfer.setTransferStatusId(results.getLong("transfer_status_id"));
        newTransfer.setAccountFrom(results.getLong("account_from"));
        newTransfer.setAccountTo(results.getLong("account_to"));
        newTransfer.setAmount(results.getDouble("amount"));
        return newTransfer;
    }
}
