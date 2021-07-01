package com.techelevator.tenmo.model.transfers;

import java.util.List;

public interface TransferDAO {

    List<Transfer> allTransfer(long accountFromId);

    void saveTransfer(Transfer newTransfer);

}
