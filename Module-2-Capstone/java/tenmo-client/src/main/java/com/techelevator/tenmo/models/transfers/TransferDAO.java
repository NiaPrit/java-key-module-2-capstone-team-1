package com.techelevator.tenmo.models.transfers;

import java.util.List;

public interface TransferDAO {

    List<Transfer> allTransfer(long accountFromId);

}
