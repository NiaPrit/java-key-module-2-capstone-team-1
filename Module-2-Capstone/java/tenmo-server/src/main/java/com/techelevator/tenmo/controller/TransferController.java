package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.transfers.Transfer;
import com.techelevator.tenmo.model.transfers.TransferDAO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TransferController {

    TransferDAO theTransferData;

    public TransferController(TransferDAO theTransferMethods) {
        this.theTransferData = theTransferMethods;
    }

    @RequestMapping(path="/transfers/{id}", method= RequestMethod.GET)
    public List<Transfer> getTransferFromUser(@PathVariable long id) {
        List<Transfer> theTransfers = new ArrayList<>();
        theTransfers = theTransferData.allTransfer(id);
        return theTransfers;
    }


    @RequestMapping(path="/transfers/pending/from/{id}", method=RequestMethod.GET)
    public List<Transfer> getPendingTransfersToUser(@PathVariable long id) {
        List<Transfer> theTransfers = new ArrayList<>();
        theTransfers = theTransferData.getPendingFromTransfersByUser(id);
        return theTransfers;
    }

    @RequestMapping(path="/transfers", method=RequestMethod.PUT)
    public Transfer updateTransfer(@RequestBody Transfer aTransfer) {
        theTransferData.updateTransfer(aTransfer);
        return aTransfer;
    }

    @RequestMapping(path="/transfers", method=RequestMethod.POST)
    public Transfer createTransfer(@RequestBody Transfer aTransfer) {

        theTransferData.saveTransfer(aTransfer);
        return aTransfer;
    }

    @RequestMapping(path="/transfers/user/{tid}", method= RequestMethod.GET)
    public Transfer getTransferFromId(@PathVariable Long tid) {
        return theTransferData.getTransferById(tid);
    }

}
