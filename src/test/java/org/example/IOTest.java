package org.example;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;


import static org.junit.jupiter.api.Assertions.*;
public class IOTest {
    private final ByteArrayOutputStream systemOutStream = new ByteArrayOutputStream();
    private final PrintStream originalSystemOutStream = System.out;

    @BeforeEach
    public void setUpIOTest() {
        System.setOut(new PrintStream(systemOutStream));
    }

    @AfterEach
    public void restoreSystemOutput() {
        System.setOut(originalSystemOutStream);
    }

    @Test
    @DisplayName("check for correct error messages for password is too short")
    void RESP_03_test_03() {
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        Validator.validatePassword("12@a");
        String expectedOut = "ERROR: password is too short" + System.lineSeparator();
        assertEquals(expectedOut, systemOutStream.toString());
    }

    @Test
    @DisplayName("check for correct error messages for password does not contain any digits")
    void RESP_03_test_04() {
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        Validator.validatePassword("ab@aazay");
        String expectedOut = "ERROR: password does not contain any digits" + System.lineSeparator();
        assertEquals(expectedOut, systemOutStream.toString());
    }

    @Test
    @DisplayName("check for correct error messages for password does not contain any special characters")
    void RESP_03_test_05() {
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        Validator.validatePassword("ab1aazay");
        String expectedOut = "ERROR: password does not contain any special characters" + System.lineSeparator();
        assertEquals(expectedOut, systemOutStream.toString());
    }

    @Test
    @DisplayName("check for correct error messages for password does not contain any letters")
    void RESP_03_test_06() {
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        Validator.validatePassword("12@12345");
        String expectedOut = "ERROR: password does not contain any letters" + System.lineSeparator();
        assertEquals(expectedOut, systemOutStream.toString());
    }

    @Test
    @DisplayName("check for correct error messages for too long username")
    void RESP_03_test_07() {
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        Validator.validateUsername("aaaaaaaaaaaaa", userList);
        String expectedOut = "ERROR: username is too long" + System.lineSeparator();
        assertEquals(expectedOut, systemOutStream.toString());
    }

    @Test
    @DisplayName("check for correct error messages for existing username")
    void RESP_03_test_08() {
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        Validator.validateUsername("MrDavidman", userList);
        String expectedOut = "ERROR: user already exists with this name" + System.lineSeparator();
        assertEquals(expectedOut, systemOutStream.toString());


    }
    @Test
    @DisplayName("Checking if a valid entered user session is established (tests both io and method return)")
    void RESP_04_test_01(){
        Library library = new Library();
        String SimUserIn = "austinrimmer\np@55w0rd\n";
        User expectedSessionUser = new User("austinrimmer","p@55w0rd");
        User actualSessionHolder = new User("","");
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));


        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);
        boolean userSessionSucessfullyInititated = library.initializeSessionHolder(userInput,uiHandler,userList);
        actualSessionHolder = library.getCurrentSessionHolder();
        //checking to see if actual user matches expected
        assertEquals(expectedSessionUser.getUsername(),actualSessionHolder.getUsername());
        assertEquals(expectedSessionUser.getPassword(),actualSessionHolder.getPassword());
        //checking for validation boolean return
        assertEquals(true, userSessionSucessfullyInititated);

        //note im only going to test once as the user input prompt is going to be a while loop using my validate password and username methods
        //and will only exit once both are valid
        //I cant test that without using a mock which is not allowed per assignment rules
    }
    @Test
    @DisplayName("Testing display of available books that are held by the user for 1 available held book")
    void RESP_07_01(){
        //"NOTIFICATION: \n" + "The book: The Miscellaneous Mis-adventures of Captain Borqueefious\n" + "By: Dennis Bartholomew III\n" + "is now available \n";
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        String SimUserIn = "austinrimmer\np@55w0rd\n";
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));
        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);


        User user = userList.getUser(1);
        user.addBookToOnHoldBooks(catalogue.getBook(0));
        uiHandler.dispUserHolds(user);
        String expectedOut = "<=====------<NOTIFICATION>------=====>" + System.lineSeparator() +
                "The book: The Miscellaneous Mis-adventures of Captain Borqueefious\n" + "By: Dennis Bartholomew III\n" + "Is now available..." + System.lineSeparator();
        assertEquals(expectedOut, systemOutStream.toString());

    }
    @Test
    @DisplayName("Testing display of available books that are held by the user for 1 held book that is on hold by a different user")
    void RESP_07_02(){
        //"NOTIFICATION: \n" + "The book: The Miscellaneous Mis-adventures of Captain Borqueefious\n" + "By: Dennis Bartholomew III\n" + "is now available \n";
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        String SimUserIn = "";
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));

        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);
        catalogue.getBook(0).setAvailablity(-1);

        User user = userList.getUser(1);
        userList.getUser(2).addBookToOnHoldBooks(catalogue.getBook(0));
        user.addBookToOnHoldBooks(catalogue.getBook(0));
        uiHandler.dispUserHolds(user);
        String expectedOut = "";
        assertEquals(expectedOut, systemOutStream.toString());

    }
    @Test
    @DisplayName("Testing display of unavailable book ")
    void RESP_07_03(){
        //"NOTIFICATION: \n" + "The book: The Miscellaneous Mis-adventures of Captain Borqueefious\n" + "By: Dennis Bartholomew III\n" + "is now available \n";
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        String SimUserIn = "";
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));

        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);

        catalogue.getBook(0).setAvailablity(0);
        User user = userList.getUser(1);
        user.addBookToOnHoldBooks(catalogue.getBook(0));
        uiHandler.dispUserHolds(user);
        String expectedOut = "";
        assertEquals(expectedOut, systemOutStream.toString());

    }
    @Test
    @DisplayName("Boundary testing display of multiple available books that are held by the user that are available")
    void RESP_07_04(){
        //"NOTIFICATION: \n" + "The book: The Miscellaneous Mis-adventures of Captain Borqueefious\n" + "By: Dennis Bartholomew III\n" + "is now available \n";
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        String SimUserIn = "austinrimmer\np@55w0rd\n";
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));
        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);


        User user = userList.getUser(1);
        user.addBookToOnHoldBooks(catalogue.getBook(0));
        user.addBookToOnHoldBooks(catalogue.getBook(9));
        user.addBookToOnHoldBooks(catalogue.getBook(19));
        uiHandler.dispUserHolds(user);
        String expectedOut = "<=====------<NOTIFICATION>------=====>" + System.lineSeparator() +
                "The book: The Miscellaneous Mis-adventures of Captain Borqueefious\n" + "By: Dennis Bartholomew III\n" + "Is now available..." + System.lineSeparator() + "<=====------<NOTIFICATION>------=====>" + System.lineSeparator() +
                "The book: The Murderizing Mutilator Strikes back\n" + "By: Stephan Queen\n" + "Is now available..." + System.lineSeparator() + "<=====------<NOTIFICATION>------=====>" + System.lineSeparator() +
                "The book: How to code in scheme\n" + "By: Lord Sean Benjamin XII\n" + "Is now available..." + System.lineSeparator();
        assertEquals(expectedOut, systemOutStream.toString());

    }
    @Test
    @DisplayName("Testing user system operation prompt msg for valid choice")
    void RESP_09_test_01(){
        String SimUserIn = "1";
        User expectedSessionUser = new User("austinrimmer","p@55w0rd");
        int returnedInt = 0;
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);

        returnedInt = Library.systemOperationInPrompt(userInput,uiHandler,userList);

        //checking for validation boolean return
        assertEquals( "-----:=|{[SYSTEM OPERATIONS]}|=:------" + System.lineSeparator() + "(1) borrow a book" + System.lineSeparator() + "(2) return a book" + System.lineSeparator() + "(3) logout" + System.lineSeparator() +
                "<=====------------<>------------=====>" + System.lineSeparator() ,systemOutStream.toString());
    }
    @Test
    @DisplayName("Testing that systemOperationPrompt returns user choice correctly")
    void RESP_09_test_05(){
        String SimUserIn = "3";
        User expectedSessionUser = new User("austinrimmer","p@55w0rd");
        int returnedInt = 0;
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);

        returnedInt = Library.systemOperationInPrompt(userInput,uiHandler,userList);

        //checking for validation boolean return
        assertEquals(returnedInt, 3);
    }
    @Test
    @DisplayName("Testing user system operation prompt msg for invalid choices ending with valid (to exit while loop and be able to test)")
    void RESP_09_test_02(){

        String SimUserIn = "a\n a\n@\n4\n1";
        User expectedSessionUser = new User("austinrimmer","p@55w0rd");
        int returnedInt = 0;
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);

        returnedInt = Library.systemOperationInPrompt(userInput,uiHandler,userList);

        //checking for validation boolean return
        assertEquals( "-----:=|{[SYSTEM OPERATIONS]}|=:------" + System.lineSeparator() + "(1) borrow a book" + System.lineSeparator() + "(2) return a book" + System.lineSeparator() + "(3) logout" + System.lineSeparator() +
                "<=====------------<>------------=====>" + System.lineSeparator() + "invalid selection please try again..." + System.lineSeparator() + "-----:=|{[SYSTEM OPERATIONS]}|=:------" + System.lineSeparator() + "(1) borrow a book" + System.lineSeparator() + "(2) return a book" + System.lineSeparator() + "(3) logout" + System.lineSeparator() +
                "<=====------------<>------------=====>" + System.lineSeparator() + "invalid selection please try again..." + System.lineSeparator() + "-----:=|{[SYSTEM OPERATIONS]}|=:------" + System.lineSeparator() + "(1) borrow a book" + System.lineSeparator() + "(2) return a book" + System.lineSeparator() + "(3) logout" + System.lineSeparator() +
                "<=====------------<>------------=====>" + System.lineSeparator() + "invalid selection please try again..." + System.lineSeparator() + "-----:=|{[SYSTEM OPERATIONS]}|=:------" + System.lineSeparator() + "(1) borrow a book" + System.lineSeparator() + "(2) return a book" + System.lineSeparator() + "(3) logout" + System.lineSeparator() +
                "<=====------------<>------------=====>" + System.lineSeparator() + "invalid selection please try again..." + System.lineSeparator() + "-----:=|{[SYSTEM OPERATIONS]}|=:------" + System.lineSeparator() + "(1) borrow a book" + System.lineSeparator() + "(2) return a book" + System.lineSeparator() + "(3) logout" + System.lineSeparator() +
                "<=====------------<>------------=====>" + System.lineSeparator() ,systemOutStream.toString());
    }
    @Test
    @DisplayName("Boundary Testing user system operation prompt msg for valid choice 2 ")
    void RESP_09_test_03(){
        String SimUserIn = "2";
        User expectedSessionUser = new User("austinrimmer","p@55w0rd");
        int returnedInt = 0;
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);

        returnedInt = Library.systemOperationInPrompt(userInput,uiHandler,userList);

        //checking for validation boolean return
        assertEquals( "-----:=|{[SYSTEM OPERATIONS]}|=:------" + System.lineSeparator() + "(1) borrow a book" + System.lineSeparator() + "(2) return a book" + System.lineSeparator() + "(3) logout" + System.lineSeparator() +
                "<=====------------<>------------=====>" + System.lineSeparator() ,systemOutStream.toString());
    }
    @Test
    @DisplayName("Boundary Testing user system operation prompt msg for valid choice 3 ")
    void RESP_09_test_04(){

        String SimUserIn = "3";
        User expectedSessionUser = new User("austinrimmer","p@55w0rd");
        int returnedInt = 0;
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);

        returnedInt = Library.systemOperationInPrompt(userInput,uiHandler,userList);

        //checking for validation boolean return
        assertEquals( "-----:=|{[SYSTEM OPERATIONS]}|=:------" + System.lineSeparator() + "(1) borrow a book" + System.lineSeparator() + "(2) return a book" + System.lineSeparator() + "(3) logout" + System.lineSeparator() +
                "<=====------------<>------------=====>" + System.lineSeparator() ,systemOutStream.toString());
    }
    @Test
    @DisplayName("Testing borrow validation result on available book")
    void RESP_10_test_01(){

        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        String SimUserIn = "Y";
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));

        catalogue.getBook(0).setAvailablity(1);
        User user = userList.getUser(1);
        int testBorrow = user.borrowBook(catalogue.getBook(0),userInput);
        assertEquals(1, testBorrow);
    }
    @Test
    @DisplayName("Testing borrow recording on available book")
    void RESP_10_test_02(){

        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        String SimUserIn = "Y";
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));

        catalogue.getBook(0).setAvailablity(1);
        User user = userList.getUser(1);
        int testBorrow = user.borrowBook(catalogue.getBook(0),userInput);
        assertEquals(catalogue.getBook(0), user.getBorrowedBook(0));
        assertEquals(0, catalogue.getBook(0).getAvailablity());
        assertEquals(1, testBorrow);
    }
    @Test
    @DisplayName("Testing borrow validation result on unavailable book")
    void RESP_10_test_03(){

        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        String SimUserIn = "Y";
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));

        catalogue.getBook(0).setAvailablity(0);
        User user = userList.getUser(1);
        int testBorrow = user.borrowBook(catalogue.getBook(0),userInput);
        assertEquals(0, testBorrow);
    }
    @Test
    @DisplayName("Testing borrow validation user Interaction choice on unavailable book and then checking user holds")
    void RESP_10_test_04(){

        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        String SimUserIn = "N";
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));

        catalogue.getBook(0).setAvailablity(0);
        User user = userList.getUser(1);

        int testBorrow = user.borrowBook(catalogue.getBook(0),userInput);

        assertEquals(-1, testBorrow);
        assertEquals(0,user.getBooksOnHold().size());
    }

    @Test
    @DisplayName("Boundary testing borrow validation result on onHold book when current user isnt already in queue")
    void RESP_10_test_05(){

        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();

        userList.getUser(0).addBookToOnHoldBooks(catalogue.getBook(0));
        catalogue.getBook(0).setAvailablity(-1);

        String SimUserIn = "Y";
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));

        User user = userList.getUser(1);
        int testBorrow = user.borrowBook(catalogue.getBook(0), userInput);
        assertEquals(0, testBorrow);
    }
    @Test
    @DisplayName("Boundary testing borrow validation result on onHold book when current user isnt already in queue")
    void RESP_10_test_06(){

        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();

        userList.getUser(0).addBookToOnHoldBooks(catalogue.getBook(0));
        catalogue.getBook(0).setAvailablity(-1);

        String SimUserIn = "Y";
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));

        User user = userList.getUser(1);
        int testBorrow = user.borrowBook(catalogue.getBook(0), userInput);
        assertEquals(0, testBorrow);
    }
    @Test
    @DisplayName("Boundary testing borrow validation result on already borrowed book")
    void RESP_10_test_07(){

        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        String SimUserIn = "Y";
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));

        User user = userList.getUser(0);
        Book book = catalogue.getBook(0);

        book.setAvailablity(1);
        user.borrowBook(book, userInput);

        int testBorrow = user.borrowBook(book, userInput); // second attempt
        assertEquals(-1, testBorrow);
    }
    @Test
    @DisplayName("Boundary testing borrow validation result on already held book when user is first in queue")
    void RESP_10_test_08(){

        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        String SimUserIn = "Y";
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));
        User user = userList.getUser(0);
        Book book = catalogue.getBook(0);

        book.setAvailablity(-1);
        book.addUserToHoldQueue(user.getUsername());
        int testBorrow = user.borrowBook(book, userInput);

        assertEquals(1, testBorrow);
    }

}


