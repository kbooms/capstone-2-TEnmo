package com.techelevator.tenmo;

import com.techelevator.tenmo.exceptions.InvalidUserChoiceException;
import com.techelevator.tenmo.exceptions.UserNotFoundException;
import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.util.Scanner;

import static com.techelevator.tenmo.model.TypeTransfer.DESC_SEND;

public class   App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private  AccountService accountService;
    private UserService userService;
    private AuthenticatedUser currentUser;
    private Account account;
    private TransferService transferService;


    public App() {
        this.userService = new UserService();
    }
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
            if(transfer.getFromAccount().getAccount_id()== account.getAccount_id() && transfer.getTransferStatus().getTransferStatusId()==StatusTransfer.STATUS_APPROVE ) {
                System.out.printf("%-10s %-10s %-10s%n",String.valueOf(transfer.getTransferId()),"from " + transfer.getFromAccount().getUser().getUsername(), amount);

            }
            else if(transfer.getToAccount().getAccount_id()== account.getAccount_id() && transfer.getTransferStatus().getTransferStatusId()==StatusTransfer.STATUS_APPROVE ){
                System.out.printf("%-10s %-10s %-10s%n",String.valueOf(transfer.getTransferId()),"to " + transfer.getToAccount().getUser().getUsername(), amount);
            }


        }
        System.out.printf("-------------------------------------------%n");
        System.out.println("Please enter transfer ID to view details (0 to cancel): ");
        Scanner scanner = new Scanner(System.in);
        int transferId = scanner.nextInt();
        if (transferId == 0){
            consoleService.printMainMenu();
        }
        else {
            for( Transfer transfer : transfers){
                if(transfer.getTransferId()==transferId){
                    viewTransferDetails(transfer);
                }
                else{
                    System.out.println("Wrong ID");
                }
            }


        }
	}
    private void viewTransferDetails(Transfer transfer){
        System.out.println("-------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("-------------------------------------------");
        System.out.println("ID: " + transfer.getTransferId());
        System.out.println("From: " + transfer.getFromAccount().getUser().getUsername());
        System.out.println("To: " + transfer.getToAccount().getUser().getUsername());
        System.out.println("Type:" + transfer.getTypeTransfer().getTransferTypeDesc());
        System.out.println("Status: " + transfer.getTransferStatus().getTransferStatusDesc());
        System.out.println("Amount: " + transfer.getAmountForTransfer());

    }

	private void viewPendingRequests() {
        Transfer[] transfers = transferService.getAllTransfers(account);

        System.out.printf("-------------------------------------------%n");
        System.out.printf("Transfers%n");
        System.out.printf("%-10s %-10s %10s%n","ID","From" , "Amount");
        System.out.printf("-------------------------------------------%n");
        for( Transfer transfer : transfers){

            String amount ="$" + transfer.getAmountForTransfer();
            if(transfer.getToAccount().getAccount_id()== account.getAccount_id() && transfer.getTransferStatus().getTransferStatusId()==StatusTransfer.STATUS_PENDING && transfer.getTypeTransfer().getTransferTypeId()== TypeTransfer.ID_REQUEST) {
                System.out.printf("%-10s %-10s %-10s%n",String.valueOf(transfer.getTransferId()),"from " + transfer.getFromAccount().getUser().getUsername(), amount);

            }



        }
        System.out.printf("-------------------------------------------%n");
        System.out.println("Please enter transfer ID to approve/reject (0 to cancel):");
        Scanner scanner = new Scanner(System.in);
        int transferId = scanner.nextInt();
        if(transferId == 0){
            consoleService.printMainMenu();
        }
        else{
                consoleService.approveRejectRequest();
                int approvalStatus = scanner.nextInt();
            Transfer transfer =  searchTransferById(transferId,transfers);
                if(approvalStatus == 1){

                   transfer.getTransferStatus().setTransferStatusId(StatusTransfer.STATUS_APPROVE);
                   transfer.getTransferStatus().setTransferStatusDesc(StatusTransfer.DESC_APPROVE);
                   transferService.updateStatus(transfer);
                }
                else if(approvalStatus == 2){
                    transfer.getTransferStatus().setTransferStatusId(StatusTransfer.STATUS_REJECT);
                    transfer.getTransferStatus().setTransferStatusDesc(StatusTransfer.DESC_REJECT);
                    transferService.updateStatus(transfer);
                }
                else if(approvalStatus == 0){

                }
                else{
                    System.out.println("Wrong number");
                   viewPendingRequests();
                }
        }
	}
    private Transfer searchTransferById(int transferId, Transfer[] transferList){
        Transfer result = null;
        for(Transfer transfer: transferList){
            if(transfer.getTransferId()==transferId){
                result = transfer;
            }
        }
        return result;
    }
	private void sendBucks() {
		User[] users = userService.getAllUsers(currentUser);
        Transfer transfer = new Transfer();
        Account toAccount = new Account();
        Account fromAccount = new Account();

        System.out.printf("-------------------------------------------%n");
        System.out.printf("Users%n");
        System.out.printf("%-10s %-10s%n","ID","Name");
        System.out.printf("-------------------------------------------%n");
        for (User user: users) {
            System.out.printf("%-10s %-10s%n", accountService.getAccountMatchingUsername(user.getId()), user.getUsername());
        }
        System.out.printf("--------------------%n");
        int userIdChoice = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel): ");
        if (validateUserChoice(userIdChoice, users, currentUser)) {
            String amountChoice = consoleService.promptForString("Enter amount: ");
//            transferService.sendMoney(transfer.setTypeTransfer(DESC_SEND);)
//            trying to implement the sendBucks() method, but having trouble putting transfer together
//            do we have/need a method that creates an actual "Transfer" ??
        }
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}


    // Validate Users Choice after user ID is entered
    private boolean validateUserChoice(int userIdChoice, User[] users, AuthenticatedUser authenticatedUser) {
        if (userIdChoice != 0) {
            try {
                boolean validUserIdChoice = false;

                for (User user: users) {
                    if (userIdChoice == currentUser.getUser().getId()) {
                        throw new InvalidUserChoiceException(); // this exception was added to the "exceptions" package in the client
                    }
                    if (user.getId() == userIdChoice) {
                        validUserIdChoice = true;
                        break;
                    }
                }
                if (!validUserIdChoice) {
                    throw new UserNotFoundException(); // this exception was added to the "exceptions" package in the client
                }
                return true;
            } catch (UserNotFoundException | InvalidUserChoiceException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }
}
