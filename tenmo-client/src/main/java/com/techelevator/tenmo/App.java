package com.techelevator.tenmo;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;

public class   App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private  AccountService accountService;
    private AuthenticatedUser currentUser;
    private Account account;
    private TransferService transferService;


    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            accountService = new AccountService(API_BASE_URL, currentUser.getToken());
            account = accountService.getAccountMatchingUsername(currentUser.getUser().getId());
            transferService = new TransferService(API_BASE_URL, currentUser.getToken());
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        System.out.println("Your current account balance is : $" + account.getBalance());

	}

	private void viewTransferHistory() {
        Transfer[] transfers = transferService.getAllTransfers(account);

        System.out.printf("-------------------------------------------%n");
        System.out.printf("Transfers%n");
        System.out.printf("%-10s %-10s %10s%n","ID","From/To", "Amount");
        System.out.printf("-------------------------------------------%n");
        for( Transfer transfer : transfers){
            String amount ="$" + transfer.getAmountForTransfer();
            if(transfer.getFromAccount().getAccount_id()== account.getAccount_id() && transfer.getTransferStatus().getTransferStatusId()==1){

            System.out.printf("%-10s %-10s %-10s%n",String.valueOf(transfer.getTransferId()),"to " + transfer.getToAccount().getUser().getUsername(), amount);}
            else {System.out.printf("%-10s %-10s %-10s%n",String.valueOf(transfer.getTransferId()),"from " + transfer.getFromAccount().getUser().getUsername(), amount);}
        }
        System.out.printf("-------------------------------------------%n");
		
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
		
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

}
