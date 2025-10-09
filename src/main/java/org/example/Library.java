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


        return false;
    }

    public User getCurrentSessionHolder(){
        return sessionOwner;
    }


}