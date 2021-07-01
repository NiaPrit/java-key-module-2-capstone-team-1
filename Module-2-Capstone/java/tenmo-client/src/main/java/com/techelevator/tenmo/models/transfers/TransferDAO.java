package com.techelevator.tenmo.models.transfers;

import java.util.List;

public interface TransferDAO {

    List<Transfer> allTransfer(long accountFromId);

    void saveTransfer(Transfer newTransfer);

    Transfer getTransferById(long transferId);

}
