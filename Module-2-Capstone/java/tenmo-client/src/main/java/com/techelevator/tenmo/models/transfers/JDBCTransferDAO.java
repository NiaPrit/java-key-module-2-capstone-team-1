package com.techelevator.tenmo.models.transfers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JDBCTransferDAO implements TransferDAO{

    private JdbcTemplate dao;

    public JDBCTransferDAO(DataSource ourData){
        this.dao = new JdbcTemplate(ourData);
    }


    @Override
    public List<Transfer> allTransfer(long accountFromId) {
        List<Transfer> allTransfers = new ArrayList<>();
        String searchString = "SELECT * from transfers where account_from=? or account_to = ?";

        SqlRowSet results = dao.queryForRowSet(searchString, accountFromId, accountFromId);
        while (results.next()) {
            Transfer transferResult = mapRowToTransfer(results);
            allTransfers.add(transferResult);
        }
        return allTransfers;
    }

    @Override
    public void saveTransfer (Transfer newTransfer) {
        String searchString = "INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?, ?)";
        newTransfer.setTransferId(getNextTransferId());

        dao.update(searchString,newTransfer.getTransferId(), newTransfer.getTransferTypeId(), newTransfer.getTransferStatusId(), newTransfer.getAccountFrom(), newTransfer.getAccountTo(), newTransfer.getAmount());

    }

    @Override
    public Transfer getTransferById(long transferId) {
        Transfer transferResult = new Transfer();
        String searchString = "SELECT * from transfers where transfer_id = ?";
        SqlRowSet results = dao.queryForRowSet(searchString, transferId);
        while (results.next()) {
         return transferResult = mapRowToTransfer(results);
        }
        return null;
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

    private long getNextTransferId() {
        SqlRowSet nextIdResult = dao.queryForRowSet("SELECT nextval('seq_transfer_id')");
        if (nextIdResult.next()){
            return nextIdResult.getLong(1);
        } else {
            throw new RuntimeException("Uh OH");
        }
    }
}
