package org.example;
import java.util.ArrayList;
import java.util.Scanner;

public class UserIOHandler {
    Scanner inputScanner;
    UserList userList;
    public UserIOHandler(Scanner userInput, UserList userList){
        this.inputScanner = userInput;
        this.userList = userList;
    }
    public UserIOHandler(Scanner userInput){
        this.inputScanner = userInput;
    }
    public User getUserLogin(Scanner usrIn){
        String username = reqUsername(usrIn);
        String password = reqPassword(usrIn);
        return new User(username, password);
    }
    public String reqUsername(Scanner usrIn){
        System.out.println("Enter username:");
        String username = usrIn.nextLine();
        return username;
    }
    public String reqHoldChoice(Scanner usrIn){
        String choice = usrIn.nextLine();
        return choice;
    }
    public String reqPassword(Scanner usrIn){
        System.out.println("Enter password");
        String password = usrIn.nextLine();
        return password;
    }
    public void dispUserHolds(User user){
        ArrayList<Integer> userBookAvails = user.getHeldBookAvailability();
        if(userBookAvails.get(0) == -3){
            System.out.println("No User Holds");
        }
        for(int i = 0; i < userBookAvails.size(); i++){
            if(userBookAvails.get(i) == 1 || (userBookAvails.get(i) == -1 && user.getUsername().equals(user.getBooksOnHold().get(i).getHolder(0))) ){
                System.out.println("<=====------<NOTIFICATION>------=====>");
                System.out.println("The book: " + user.getBooksOnHold().get(i).getTitle() +"\n" + "By: " + user.getBooksOnHold().get(i).getAuthor() + "\nIs now available...");
            }
        }
    }
    public String reqSystemOperation(Scanner usrIn){
        System.out.println("-----:=|{[SYSTEM OPERATIONS]}|=:------");
        System.out.println("(1) borrow a book");
        System.out.println("(2) return a book");
        System.out.println("(3) logout");
        System.out.println("<=====------------<>------------=====>");
        return usrIn.nextLine();
    }
    public void dispNumBorrowedBooks(User user){
        System.out.println("");
    }

}