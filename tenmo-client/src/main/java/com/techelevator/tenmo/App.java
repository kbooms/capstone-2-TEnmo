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

        account = accountService.getAccountMatchingUsername(currentUser.getUser().getId());
        System.out.println("Your current account balance is : $" + account.getBalance());

	}

	private void viewTransferHistory() {

        Transfer[] transfers = transferService.getSentTransfers(account);
        if (transfers.length == 0){
            System.out.println("You don't have any transfers yet.");
        }
        else {
        System.out.printf("-------------------------------------------%n");
        System.out.printf("Transfers%n");
        System.out.printf("%-10s %-20s %-10s%n","ID","From/To", "Amount");
        System.out.printf("-------------------------------------------%n");

        for( Transfer transfer : transfers){

            String amount ="$" + transfer.getAmountForTransfer();
            String FromToDetails;
            if(transfer.getFromAccount().getAccount_id()== account.getAccount_id()) {
                FromToDetails = "To: " + transfer.getToAccount().getUser().getUsername();
            }
            else if (transfer.getTypeTransfer().getTransferTypeId() == TypeTransfer.ID_REQUEST){
                FromToDetails = "To: " + transfer.getFromAccount().getUser().getUsername();
            }
            else  {
                FromToDetails = "From: " + transfer.getFromAccount().getUser().getUsername();
            }

            System.out.printf("%-10s %-20s %-10s%n",transfer.getTransferId(),FromToDetails, amount);



        }
        System.out.printf("-------------------------------------------%n");
        System.out.print("Please enter transfer ID to view details (0 to cancel): ");
        Scanner scanner = new Scanner(System.in);

        int transferId = scanner.nextInt();

        if (transferId !=0) {

            Transfer transfer = searchTransferById(transferId, transfers);

            if (transfer == null){
                System.out.println("Wrong ID");

            }
            else  viewTransferDetails(transfer);

            consoleService.pause();
            viewTransferHistory();
        }


        }
	}


	private void viewPendingRequests() {
        Transfer[] transfers = transferService.getRequestTransfers(account);
        if( transfers.length == 0){
            System.out.println("There aren't any pending requests");
        }
        else{
        System.out.printf("-------------------------------------------%n");
        System.out.printf("Transfers%n");
        System.out.printf("%-10s %-20s %-10s%n","ID","From" , "Amount");
        System.out.printf("-------------------------------------------%n");
        for( Transfer transfer : transfers){
            String amount ="$" + transfer.getAmountForTransfer();
              System.out.printf("%-10s %-20s %-10s%n",transfer.getTransferId(),"From: " + transfer.getFromAccount().getUser().getUsername(), amount);

        }

        System.out.printf("-------------------------------------------%n");
        System.out.println("Please enter transfer ID to approve/reject (0 to cancel):");
        Scanner scanner = new Scanner(System.in);
        int transferId = scanner.nextInt();
        if(transferId != 0){
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

                else if (approvalStatus !=0){
                    System.out.println("Wrong number");

                }

            consoleService.pause();
            viewPendingRequests();
        }



        }
	}

	private void sendBucks() {

        Account [] accounts = accountService.getAllAccounts();
        viewListOfAccounts(accounts);

        int userIdChoice = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel): ");

        if (validateAccountChoice(userIdChoice, accounts)) {

            BigDecimal amountChoice = consoleService.promptForBigDecimal("Enter amount: ");

            if (validateBalance(amountChoice)) {
                Transfer transfer = transferSetting(userIdChoice,accounts, amountChoice, StatusTransfer.STATUS_APPROVE, TypeTransfer.ID_SEND);
                transferService.sendMoney(transfer);
                return;
            }
              else System.out.println("You can't send this amount of money.");

        }
         consoleService.pause();
         sendBucks();
	}


	private void requestBucks() {

        Account [] accounts = accountService.getAllAccounts();
        viewListOfAccounts(accounts);
        long userIdChoice = consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel): ");
        if (validateAccountChoice(userIdChoice, accounts)) {
            BigDecimal amountChoice = consoleService.promptForBigDecimal("Enter amount: ");
            Transfer transfer  =  transferSetting(userIdChoice,accounts, amountChoice,StatusTransfer.STATUS_PENDING,TypeTransfer.ID_REQUEST);
            transferService.requestMoney(transfer);
            return;
		
	       }
        consoleService.pause();
        requestBucks();
    }

    private Transfer searchTransferById(int transferId, Transfer[] transferList){
        Transfer result = null;
        for(Transfer transfer: transferList){
            if(transfer.getTransferId()==transferId){
                result = transfer;
                break;
            }
        }
        return result;
    }
    private Account searchAccountByUserId(long userId, Account[] accounts){
        Account result = null;
        for(Account account: accounts){
            if(account.getUser().getId()==userId){
                result = account;
                break;
            }
        }
        return result;
    }

    private void viewTransferDetails(Transfer transfer){


        System.out.println("-------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("-------------------------------------------");
        System.out.println("ID: " + transfer.getTransferId());
        System.out.println("From: " + ((transfer.getFromAccount().getAccount_id() == account.getAccount_id()) ? "Me MyselfAndI": transfer.getFromAccount().getUser().getUsername()));
        System.out.println("To: " +  ((transfer.getToAccount().getAccount_id() == account.getAccount_id()) ? "Me MyselfAndI": transfer.getToAccount().getUser().getUsername()));
        System.out.println("Type:" + transfer.getTypeTransfer().getTransferTypeDesc());
        System.out.println("Status: " + transfer.getTransferStatus().getTransferStatusDesc());
        System.out.println("Amount: " + transfer.getAmountForTransfer());
        System.out.println("-------------------------------------------");

    }

    private boolean validateBalance(BigDecimal amount){
        account = accountService.getAccountMatchingUsername(currentUser.getUser().getId());
       return account.getBalance().doubleValue() >= amount.doubleValue();


    }

    // Validate Users Choice after user ID is entered
    private boolean validateAccountChoice(long userIdChoice, Account[] accounts) {
        if (userIdChoice != 0) {
            try {
                boolean validUserIdChoice = false;

                for (Account account: accounts) {

                    if (account.getUser().getId() == userIdChoice && currentUser.getUser().getId() != userIdChoice ) {
                        validUserIdChoice = true;
                        break;
                    }
                }
                if (!validUserIdChoice) {
                    throw new UserNotFoundException(); // this exception was added to the "exceptions" package in the client
                }
                return validUserIdChoice;
            } catch (UserNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }
    private Transfer transferSetting(long userId, Account[] accounts, BigDecimal amountChoice, int status, int type){
        Transfer transfer = new Transfer();
        transfer.setFromAccount(account);
        transfer.setToAccount(searchAccountByUserId(userId, accounts));


        TypeTransfer typeTransfer = new TypeTransfer();
        typeTransfer.setTransferTypeDesc("");
        typeTransfer.setTransferTypeId(type);
        transfer.setTypeTransfer(typeTransfer);

        StatusTransfer statusTransfer = new StatusTransfer();
        statusTransfer.setTransferStatusDesc("");
        statusTransfer.setTransferStatusId(status);
        transfer.setTransferStatus(statusTransfer);
        transfer.setAmountForTransfer(amountChoice);

        return transfer;
    }


    private void viewListOfAccounts(Account [] accounts){

        System.out.printf("-------------------------------------------%n");
        System.out.printf("Users%n");
        System.out.printf("%-10s %-10s%n","ID","Name");
        System.out.printf("-------------------------------------------%n");
        for (Account account: accounts) {
            if(!account.getUser().getId().equals(currentUser.getUser().getId()))
                System.out.printf("%-10s %-10s%n", account.getUser().getId(),account.getUser().getUsername());
        }
        System.out.printf("--------------------%n");
    }
}
