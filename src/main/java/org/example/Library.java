package org.example;

import java.util.Scanner;

public class Library {
    static User sessionOwner = new User("default","default");
    public static void main(String[] args) {
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        Scanner userInput = new Scanner(System.in);
        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);

        initializeSessionHolder(userInput, uiHandler, userList);

        while (true) {
            uiHandler.dispUserHolds(sessionOwner);
            int choice = systemOperationInPrompt(userInput, uiHandler, userList);
            switchOperationState(choice, uiHandler, sessionOwner, catalogue, userInput);

            if (sessionOwner.getUsername().equals("default")) {
                System.out.println("Logged out successfully.");
                System.out.println("Returning to login screen...\n");
                boolean cont = initializeSessionHolder(userInput, uiHandler, userList);

            }

        }
    }
    public static void switchOperationState(int userPick, UserIOHandler userIOHandler, User user, Catalogue catalogue, Scanner userInput){
        if(userPick == 1){
            borrowState(userIOHandler,user,catalogue, userInput);
        }
        if(userPick == 2){
            returnState(userIOHandler,user,catalogue, userInput);
        }
        if(userPick == 3){
            logoutState(userIOHandler,user,catalogue, userInput);
        }
    }
    public static Boolean initializeSessionHolder(Scanner userInput, UserIOHandler uiHandler, UserList userList){
        String tempUsername = "";
        String tempPassword = "";

        while (true) {
            User currentUserLogin = uiHandler.getUserLogin(userInput);
            String username = currentUserLogin.getUsername();
            String password = currentUserLogin.getPassword();
            //check if user already in syst
            User existingUser = null;
            for (int i = 0; i < userList.getNumberOfUsers(); i++) {
                User user = userList.getUser(i);
                if (user.getUsername().equals(username)) {
                    existingUser = user;
                    break;
                }
            }

            if (existingUser != null) {
                if (existingUser.getPassword().equals(password)) {
                    sessionOwner = existingUser;
                    System.out.println("User Validated: welcome...");
                    return true;
                } else {
                    System.out.println("ERROR: incorrect password");
                    continue; // ask again
                }
            }

            boolean validUser = Validator.validateUsername(username, userList);
            boolean validPass = Validator.validatePassword(password);

            if (validUser && validPass) {
                tempUsername = username;
                tempPassword = password;
                sessionOwner = new User(tempUsername, tempPassword);
                userList.addUser(sessionOwner);
                System.out.println("User Validated: welcome...");
                return true;
            }

            System.out.println("Please try again...");
        }
    }

    public static User getCurrentSessionHolder(){
        return sessionOwner;
    }

    public static int systemOperationInPrompt(Scanner userInput, UserIOHandler uiHandler, UserList userList) {
        String userChoice = "";
        boolean validChoice = false;
        while (!validChoice) {
            userChoice = uiHandler.reqSystemOperation(userInput);

            if (Validator.validateUserOperationChoice(userChoice)) {
                validChoice = true;
                break;
            } else {
                System.out.println("invalid selection please try again...");
            }
        }
        return Integer.parseInt(userChoice);
    }

    public static void borrowState(UserIOHandler userIOHandler, User user, Catalogue catalogue, Scanner userInput){
        userIOHandler.dispNumBorrowedBooks(user);
        int borrowStatus = 0;

        while (true) {
            userIOHandler.dispAllBooks(catalogue, user);
            int borrowSelection = userIOHandler.getBorrowSelection(userInput);

            if (borrowSelection == -1) {
                System.out.println("Invalid selection please try again...");
                continue; // go back to selection
            }

            Book selectedBook = catalogue.getBook(borrowSelection - 1);
            userIOHandler.dispBook(selectedBook);

            int borrowConfirm = userIOHandler.reqBorrowConfirm(userInput);
            if (borrowConfirm == -1) {
                continue;
            }
            if(borrowConfirm == 1){

                borrowStatus = user.borrowBook(selectedBook, userInput);
            }
            if(borrowStatus == 1){
                System.out.println("You have successfully borrowed: " + catalogue.getBook(borrowSelection - 1).getTitle());
                System.out.println("It is due: " + selectedBook.getDueDate());
            }
            if(borrowStatus == 0){
                System.out.println("You have successfully been added to the hold queue");
            }
            if(borrowStatus == -1){
                System.out.println("You cannot place a hold on this book");
                continue;
            }
            break;
        }

    }
    public static void returnState(UserIOHandler userIOHandler, User user, Catalogue catalogue, Scanner userInput){
        int returnStatus = 0;

        while (true) {
            userIOHandler.dispUserBorrows(user);
            if(user.getNumberOfBorrowedBooks() == 0){
                break;
            }
            int returnSelection = userIOHandler.getReturnSelection(userInput, user);

            if (returnSelection == -1) {
                System.out.println("Invalid selection please try again...");
                continue; // go back to selection
            }

            Book selectedBook = user.getBorrowedBook(returnSelection - 1);
            userIOHandler.dispBook(selectedBook);

            int returnConfirm = userIOHandler.reqReturnConfirm(userInput);
            if (returnConfirm == -1) {
                continue;
            }
            if(returnConfirm == 1){
                System.out.println("You have successfully returned " + catalogue.getBook(returnSelection - 1).getTitle());
                user.returnBook(selectedBook);
            }

            break;
        }


    }
    public static void logoutState(UserIOHandler userIOHandler, User user, Catalogue catalogue, Scanner userInput){
        userIOHandler.dispLogOutConfirm();
        sessionOwner = new User("default", "default");
    }


}

