package org.example;

import java.util.Scanner;

public class Validator {

    public static boolean validateUsername(String username, UserList userList) {
        for (int i = 0; i < userList.getNumberOfUsers(); i++) {
            if (username.equals(userList.getUser(i).getUsername())) {
                System.out.println("ERROR: user already exists with this name");
                return false;
            }
        }
        if (username.length() > 12) {
            System.out.println("ERROR: username is too long");
            return false;
        }
        return username.matches("[a-zA-Z]+");
    }

    public static boolean validatePassword(String password) {
        //must be > 4 chars, must have one special char, one number, and at least 1 letter
        boolean hasSpecialChar = false;
        boolean hasLetter = false;
        boolean hasNumber = false;
        if (password.length() < 5) {
            System.out.println("ERROR: password is too short");
            return false;
        }
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else {
                hasSpecialChar = true;
            }
        }
        if (!hasLetter) {
            System.out.println("ERROR: password does not contain any letters");
        }
        if (!hasNumber) {
            System.out.println("ERROR: password does not contain any digits");
        }
        if (!hasSpecialChar) {
            System.out.println("ERROR: password does not contain any special characters");
        }
        return hasLetter && hasNumber && hasSpecialChar;
    }

    public static boolean validateUserOperationChoice(String userInput){
        if(userInput.equals("1") || userInput.equals("2") || userInput.equals("3")){
            return true;
        }
        return false;
    }
    //1     = can borrow
    //0     = can place hold
    //-1    = cant place hold
    public static int validateBorrow(Book book, User user, Scanner userInput){
        return -10;
    }
    //1 = yes
    //0 = no
    public static int validateHoldChoice(Book book, User user, Scanner userInput){
        UserIOHandler uiHandler = new UserIOHandler(userInput);
        String userResp = uiHandler.reqHoldChoice(userInput);
        if(userResp.equals("Y")){
            return 1;
        }
        else{
            return 0;
        }
    }
}
