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

}