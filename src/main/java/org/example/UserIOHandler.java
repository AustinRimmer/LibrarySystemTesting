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
}