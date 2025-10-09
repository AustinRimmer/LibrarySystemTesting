package org.example;
import java.util.ArrayList;
import java.util.Scanner;

public class UserIOHandler {
    Scanner inputScanner;
    UserList userList;
    UserIOHandler(Scanner userInput, UserList userList){
        this.inputScanner = userInput;
        this.userList = userList;
    }
    User getUserLogin(Scanner usrIn){
        String username = reqUsername(usrIn);
        String password = reqPassword(usrIn);
        return new User(username, password);
    }
    String reqUsername(Scanner usrIn){
        System.out.println("Enter username:");
        String username = usrIn.nextLine();
        return username;

    }
    String reqPassword(Scanner usrIn){
        System.out.println("Enter password");
        String password = usrIn.nextLine();
        return password;
    }
    void dispUserHolds(User user){
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
}