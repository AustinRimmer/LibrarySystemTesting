package org.example;

import java.util.Scanner;

public class Library {
    static User sessionOwner = new User("default","default");
    public static void main(String[] args) {
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        Scanner userInput = new Scanner(System.in);
        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);

        initializeSessionHolder(userInput,uiHandler,userList);
    }
    public static Boolean initializeSessionHolder(Scanner userInput, UserIOHandler uiHandler, UserList userList){

        String tempUsername = "";
        String tempPassword = "";

        boolean validUser = false;
        boolean validPass = false;

        while (!validUser || !validPass){
            User currentUserLogin = uiHandler.getUserLogin(userInput);

            validUser = false;
            validPass = false;
            if(Validator.validateUsername(currentUserLogin.getUsername(),userList)) {
                validUser = true;
            }
            if(Validator.validatePassword(currentUserLogin.getPassword())){
                validPass = true;

            }
            if(validPass && validUser){
                tempUsername = currentUserLogin.getUsername();
                tempPassword = currentUserLogin.getPassword();
                break;
            }
        }
        sessionOwner = new User(tempUsername,tempPassword);

        userList.addUser(sessionOwner);
        System.out.println("User Validated: welcome...");
        return true;
    }

    public User getCurrentSessionHolder(){
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
        System.out.println("a");



    }

}