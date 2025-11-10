package org.example;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class AcceptanceTest{
    @Test
    @DisplayName("Multi-User Borrow and Return with Availability Validated")
    void A_TEST_01() {
        //this simulates the required A_TEST_01 exactly as described
        Library lib = new Library();
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();

        //user1 logs in
        String user1Login = "austin\nP@55word\n";
        Scanner user1Scanner = new Scanner(new ByteArrayInputStream(user1Login.getBytes()));
        UserIOHandler uiHandler1 = new UserIOHandler(user1Scanner, userList);
        lib.initializeSessionHolder(user1Scanner, uiHandler1, userList);

        //user 1 borrows book 20 and logs out
        String user1BorrowInput = "20\nY\n";
        Scanner borrowScanner1 = new Scanner(new ByteArrayInputStream(user1BorrowInput.getBytes()));
        lib.borrowState(uiHandler1, userList.getUser(3), catalogue, borrowScanner1);

        lib.logoutState(uiHandler1, userList.getUser(3), catalogue, user1Scanner);

        //user 2 logs in, sees book 20 is unavailable, and logs out
        String user2Login = "newUser\np@55word\n";
        Scanner user2Scanner = new Scanner(new ByteArrayInputStream(user2Login.getBytes()));
        UserIOHandler uiHandler2 = new UserIOHandler(user2Scanner, userList);
        lib.initializeSessionHolder(user2Scanner, uiHandler2, userList);

        //disp all books to see availability
        uiHandler2.dispAllBooks(catalogue, userList.getUser(4));

        //check book isnt avail
        Book book = catalogue.getBook(19); // index 19 = book 20
        assertEquals(0, book.getAvailablity(), "book should be unavailable after user1 borrows it.");

        lib.logoutState(uiHandler2, userList.getUser(4), catalogue, user2Scanner);

        //user 1 logs back in and returns book
        Scanner user1Scanner2 = new Scanner(new ByteArrayInputStream(user1Login.getBytes()));
        UserIOHandler uiHandler1b = new UserIOHandler(user1Scanner2, userList);
        lib.initializeSessionHolder(user1Scanner2, uiHandler1b, userList);

        String user1ReturnInput = "1\nY\n";
        Scanner returnScanner = new Scanner(new ByteArrayInputStream(user1ReturnInput.getBytes()));
        lib.returnState(uiHandler1b, userList.getUser(3), catalogue, returnScanner);

        lib.logoutState(uiHandler1b, userList.getUser(3), catalogue, user1Scanner2);

        //user2 logs back in and sees book 20 is avail
        Scanner user2Scanner2 = new Scanner(new ByteArrayInputStream(user2Login.getBytes()));
        UserIOHandler uiHandler2b = new UserIOHandler(user2Scanner2, userList);
        lib.initializeSessionHolder(user2Scanner2, uiHandler2b, userList);

        uiHandler2b.dispAllBooks(catalogue, userList.getUser(4));

        int finalAvail = catalogue.getBook(19).getAvailablity();
        assertEquals(1, finalAvail, "book should be available again after user1 returns it.");

        lib.logoutState(uiHandler2b, userList.getUser(4), catalogue, user2Scanner2);
    }
    //changed as per A2 specs
    @Test
    @DisplayName("Initialization and Authentication with Error Handling")
    void A_TEST_02(){
        Library lib = new Library();
        InitializeLibrary initLib = new InitializeLibrary();
        Catalogue catalogue = initLib.initializeLibrary();
        InitializeUserList initUserList = new InitializeUserList();
        UserList userList = initUserList.initializeUserList();

        //precons
        assertEquals(20, catalogue.getCatalogueSize(), "system should start with 20 books");
        assertEquals(3, userList.getNumberOfUsers(), "system should start with 3 users.");

        //valid login
        String validLoginInput = "austin\nP@55word\n";
        Scanner validLoginScanner = new Scanner(new ByteArrayInputStream(validLoginInput.getBytes()));
        UserIOHandler uiHandler = new UserIOHandler(validLoginScanner, userList);

        //init session
        lib.initializeSessionHolder(validLoginScanner, uiHandler, userList);

        //logout
        String logoutInput = "";
        Scanner logoutScanner = new Scanner(new ByteArrayInputStream(logoutInput.getBytes()));
        lib.logoutState(uiHandler, userList.getUser(3), catalogue, logoutScanner);

        //invalid login, followed by correct stuff entered on reprompt
        String invalidLoginInput = "wayTooLongOfAUserName\nnoNumbers\nnewUser\np@55word\n";
        Scanner invalidLoginScanner = new Scanner(new ByteArrayInputStream(invalidLoginInput.getBytes()));
        UserIOHandler uiHandlerInvalid = new UserIOHandler(invalidLoginScanner, userList);

        ByteArrayOutputStream systemOut = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(systemOut));

        //init session
        lib.initializeSessionHolder(invalidLoginScanner, uiHandlerInvalid, userList);

        //restore
        System.setOut(originalOut);

        String output = systemOut.toString();
        //testing outs, you wont be able to see them
        assertTrue(output.contains("ERROR: username is too long") && output.contains("ERROR: password does not contain any digits") && output.contains("Please try again..."),
                "system displays an error message when invalid credentials are entered");

        assertTrue(output.contains("User Validated: welcome...") , "System should allow retry and succeed after valid credentials are entered.");
    }
}




