package com.techelevator.view;


import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.account.Account;
import com.techelevator.tenmo.models.transfers.Transfer;
import com.techelevator.tenmo.services.TenmoApplicationServices;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;
	private AuthenticatedUser currentUser;
	private TenmoApplicationServices services = new TenmoApplicationServices("http://localhost:8080/");

	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	public String getUserInput(String prompt) {
		out.print(prompt + ": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		do {
			out.print(prompt + ": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch (NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while (result == null);
		return result;
	}

	public Double getUserInputDouble(String prompt) {
		Double result = null;
		do {
			out.print(prompt + ": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Double.parseDouble(userInput);

			} catch (NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while (result == null);
		return result;
	}

	public void viewCurrentBalFromUser(Account currentAccount) {
		System.out.println("Your current account balance is: $" + currentAccount.getBalance());
	}

	public void getAllUsers(List<User> users) {
		System.out.println("-".repeat(50));
		System.out.println("User Id - Name");
		if (users.size() > 0) {
			for (User eachUser : users) {
				System.out.println(eachUser.getId() + " - " + eachUser.getUsername());
			}
		} else {
			System.out.println("\n*** No results ***");
		}
		System.out.println("-".repeat(50));
	}

	public double userCurrentBalance(Account currentAccount) {
		return currentAccount.getBalance();
	}

	public void transferAmount() {
		System.out.println("-".repeat(50));
		System.out.println("Thank you for the transfer!");
		System.out.println("-".repeat(50));
	}

	public void transferPending() {
		System.out.println("-".repeat(50));
		System.out.println("Transfer request has been complete. Pending Approval!");
		System.out.println("-".repeat(50));
	}

	public void errorAmountMessage() {
		System.out.println("-".repeat(50));
		System.out.println("Insufficient amount, please try again!");
		System.out.println("-".repeat(50));
	}

	public void transferDetailMenu() {
		System.out.println("-".repeat(50));
		System.out.println("Transfer Details");
		System.out.println("-".repeat(50));
	}

	public void transferMainMenu() {
		System.out.println("-".repeat(50));
		System.out.println("Transfer ID - From/To - Amount");
		System.out.println("-".repeat(50));
	}


	public void pendingFromMainMenu() {
		System.out.println("-".repeat(50));
		System.out.println("Pending ID - Transfers To - Amount");
		System.out.println("-".repeat(50));
	}

	public void approvalRejectTransfer() {
		System.out.println("1: Approve");
		System.out.println("2: Reject");
		System.out.println("0: Don't Approve or Reject");
	}

	public void amountInsufficientApprovalMessage() {
		System.out.println("-".repeat(50));
		System.out.println("Insufficient amount, request will be denied!");
		System.out.println("-".repeat(50));
	}


	public String getTransferTypeDesc(Long typeId){
		String transferDescription = "";
		if (typeId == 1){
			transferDescription = "Request";
		}
		else if (typeId == 2) {
			transferDescription = "Send";
		}
		return transferDescription;
	}

	public String getTransferStatusDesc(Long typeId){
		String transferDescription = "";
		if (typeId == 1){
			transferDescription = "Pending";
		}
		else if (typeId == 2) {
			transferDescription = "Approved";
		}else if (typeId == 3) {
			transferDescription = "Rejected";
		}
		return transferDescription;
	}



	public void eachTransferMenuOptionsOne(Transfer theTransfer) {
		System.out.println("Id: " + theTransfer.getTransferId());
		System.out.println("From: Me, Myself, And I");
		System.out.println("To: " + services.getName((int) theTransfer.getAccountTo()));
		System.out.println("Type: " +  getTransferTypeDesc(theTransfer.getTransferTypeId()));
		System.out.println("Status: " + getTransferStatusDesc(theTransfer.getTransferStatusId()));
		System.out.println("Amount: $" + theTransfer.getAmount());
	}

	public void eachTransferMenuOptionsTwo(Transfer theTransfer) {
		System.out.println("Id: " + theTransfer.getTransferId());
		System.out.println("From : " + services.getName((int) theTransfer.getAccountFrom()));
		System.out.println("To : Me, Myself, And I");
		System.out.println("Type: " + getTransferTypeDesc(theTransfer.getTransferTypeId()));
		System.out.println("Status: " + getTransferStatusDesc(theTransfer.getTransferStatusId()));
		System.out.println("Amount: $" + theTransfer.getAmount());
	}

	public void showTransfersFromUser(AuthenticatedUser currentUser, List<Transfer> theTransferList) {
		User aUser = new User();
		for(Transfer aTransfer: theTransferList){
			if (currentUser.getUser().getId() == aTransfer.getAccountFrom()) {
				aUser.setId((int) aTransfer.getAccountTo());
				System.out.println(aTransfer.getTransferId() + " - To: " + services.getName((int) aTransfer.getAccountTo()) + " - $" + aTransfer.getAmount());
			}
			else if(currentUser.getUser().getId() == aTransfer.getAccountTo()) {
				aUser.setId((int) aTransfer.getAccountFrom());
				System.out.println(aTransfer.getTransferId() + " - From: " + services.getName((int) aTransfer.getAccountFrom()) + " - $" + aTransfer.getAmount());
			}
		}
	}

}
