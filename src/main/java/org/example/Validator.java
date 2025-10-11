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

        boolean alreadyHolding  = false;
        boolean alreadyBorrowed = false;

        for(int i = 0; i < book.getNumberOfHolders(); i ++){
            if(book.getHolder(i).equals(user.getUsername())){
                alreadyHolding = true;
            }
        }
        for(int i = 0; i < user.getNumberOfBorrowedBooks(); i ++){
            if(user.getBorrowedBook(i).equals(book)){
                alreadyBorrowed = true;
            }
        }
        if(alreadyBorrowed){
            System.out.println("You already have this book checked out");
            return -1;
        }
        //can borrow avail books when avail and not 3 already borrowed
        if(book.getAvailablity() == 1 && user.getNumberOfBorrowedBooks() < 3){
            return 1;
        }
        //if book was returned and current user is first in queue they are able to check it out
        if(book.getAvailablity() == -1 && book.getHolder(0).equals(user.getUsername())){
            return 1;
        }
        else if(book.getAvailablity() == -1 && !book.getHolder(0).equals(user.getUsername())){
            System.out.println("Would you like to place a hold for position ("+ (book.getNumberOfHolders() + 1)+") (Y/N): " );
            validateHoldChoice(book,user, userInput);
            //ask if they want to place a hold
            return 0;
        }
        //cases where no hold is possible
        if(alreadyHolding){
            System.out.println("You already have this book on hold");
            return -1;
        }

        if(book.getAvailablity() == -1){
            System.out.println("Would you like to place a hold for position ("+ (book.getNumberOfHolders() + 1)+") (Y/N): " );
            int choice = validateHoldChoice(book,user, userInput);
            if(choice == 1){
                return 0;
            }
            return - 1;
        }

        if(book.getAvailablity() == 0){
            System.out.println("Would you like to place a hold for position ("+ (book.getNumberOfHolders() + 1)+") (Y/N): " );
            int choice = validateHoldChoice(book,user,userInput);
            if(choice == 1){
                return 0;
            }
            return - 1;
        }

        //never able to borrow when held books = 3
        if(user.getNumberOfBorrowedBooks() == 3){
            System.out.println("The maximum number of borrowed books has been reached");
            System.out.println("Would you like to place a hold for position ("+ (book.getNumberOfHolders() + 1)+") (Y/N): " );
            int choice = validateHoldChoice(book,user,userInput);
            if(choice == 1){
                return 0;
            }
            return - 1;
        }
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
