package com.techelevator.tenmo;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.models.transfers.Transfer;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TenmoApplicationServices;
import com.techelevator.view.ConsoleService;
import org.springframework.web.client.RestClientResponseException;

import java.util.Arrays;
import java.util.List;

public class TenmoApplicationProgram {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
	private TenmoApplicationServices services = new TenmoApplicationServices(API_BASE_URL);


    public static void main(String[] args) {
    	TenmoApplicationProgram app = new TenmoApplicationProgram(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public TenmoApplicationProgram(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;

	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		// TODO - Put code for this process here

		console.viewCurrentBalFromUser(services.getCurrentBal(currentUser.getUser().getId()));
    }

	private void viewTransferHistory() {
		// TODO - Put code for this process here
		List<Transfer> theTransferList = Arrays.asList(services.listAllTransfers(currentUser.getUser().getId()));
		console.transferMainMenu();
		console.showTransfersFromUser(currentUser, theTransferList);
		int transferId = console.getUserInputInteger("Enter ID of the transfer you wish to view (0 to cancel)");
		if (transferId == 0) {
			mainMenu();
		} else {
			try{Transfer theTransfer = services.listTransferById(transferId);
			console.transferDetailMenu();
			if (currentUser.getUser().getId() == theTransfer.getAccountFrom()) {
				console.eachTransferMenuOptionsOne(theTransfer);
			} else if (currentUser.getUser().getId() == theTransfer.getAccountTo()){
				console.eachTransferMenuOptionsTwo(theTransfer);
			} }
			catch(Exception e){
				System.out.println("Transfer Id Invalid Please try Again");
				mainMenu();
			}
		}
		}


	private void viewPendingRequests() {
		// TODO - Put code for this process here
			double currentBalance = console.userCurrentBalance(services.getCurrentBal(currentUser.getUser().getId()));
			List<Transfer> thePendingList = Arrays.asList(services.listAllFromPendingTransfers(currentUser.getUser().getId()));
			console.pendingFromMainMenu();
			console.showTransfersFromUser(currentUser, thePendingList);
			int pendingId = console.getUserInputInteger("-".repeat(10) + "\nEnter ID of the Transfer you wish to Approve/Deny (0 to cancel)");
			if (pendingId == 0) {
				mainMenu();
			} else {
				console.approvalRejectTransfer();
				int decisionChoice = console.getUserInputInteger("-".repeat(10) + "\nPlease choose an option");
				if (decisionChoice == 0) {
					mainMenu();
				} else if (decisionChoice == 1) {
					Transfer newTransfer = services.listTransferById(pendingId);
					if (newTransfer.getAmount() > currentBalance) {
						console.amountInsufficientApprovalMessage();
						newTransfer.setTransferStatusId(3);
						services.updateTransfer(newTransfer);
					} else {
						newTransfer.setTransferStatusId(2);
						services.updateTransfer(newTransfer);
						services.sendAmountFromUser(currentUser.getUser().getId(), newTransfer.getAmount());
						services.receiveAmountFromUser((int) newTransfer.getAccountTo(), newTransfer.getAmount());
					}
				} else if (decisionChoice == 2) {
					Transfer newTransfer = services.listTransferById(pendingId);
					newTransfer.setTransferStatusId(3);
					services.updateTransfer(newTransfer);

				}
			}
	}

	private void sendBucks() {
		// TODO - Put code for this process here
		console.getAllUsers(Arrays.asList(services.listUsers()));
		int userId = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)");
		if (userId == 0) {
			mainMenu();
		} else {
			double amountToTransfer = console.getUserInputDouble("Enter amount ($)");
			if (amountToTransfer<=0 || amountToTransfer > console.userCurrentBalance(services.getCurrentBal(currentUser.getUser().getId()))) {
				console.errorAmountMessage();
				mainMenu();
			} else {
				services.sendAmountFromUser(currentUser.getUser().getId(), amountToTransfer);
				try{services.receiveAmountFromUser(userId, amountToTransfer);}
				catch(Exception e){
					System.out.println("User Id for account sent does not exist");
					mainMenu();
				}
				console.transferAmount();
			Transfer newTransfer = new Transfer();
				newTransfer.setTransferTypeId(2L);
				newTransfer.setTransferStatusId(2L);
				newTransfer.setAccountFrom(currentUser.getUser().getId());
				newTransfer.setAccountTo(userId);
				newTransfer.setAmount(amountToTransfer);
				services.createTransfer(newTransfer);


			}
		}
	}

	private void requestBucks() {
		// TODO - Put code for this process here
		console.getAllUsers(Arrays.asList(services.listUsers()));
		int userId = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)");
		if (userId == 0) {
			mainMenu();
		} else {
			double amountToTransfer = console.getUserInputDouble("Enter amount ($)");
			if (amountToTransfer<=0) {
				console.errorAmountMessage();
				mainMenu();
			} else {
				Transfer newTransfer = new Transfer();
				newTransfer.setTransferTypeId(1L);
				newTransfer.setTransferStatusId(1L);
				newTransfer.setAccountFrom(userId);
				newTransfer.setAccountTo(currentUser.getUser().getId());
				newTransfer.setAmount(amountToTransfer);
				services.createTransfer(newTransfer);}}
		console.transferPending();
	}
	
	private void exitProgram() {
		System.exit(0);
	}
	/***********************************************************************************
	 * The code below works and should NOT need to be changed
	 * 
	 * It contains methods you may use in your processing
	 * 
	 * Consider reviewing it to at least understand it's general functionality
	 *     (what it returns, receives and does) in case your need it.
	 * 
	 * Remember: DRY - Don't Repeat Yourself 
	 *                (Don't write new code if functionality already exists)
	 ************************************************************************************/
	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
