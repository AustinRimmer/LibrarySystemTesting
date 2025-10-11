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
    @Test
    @DisplayName("Testing display of user borrowed book number with no books borrowed")
    void RESP_11_test_01(){
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();

        User user = userList.getUser(0);
        Book book = catalogue.getBook(0);

        String SimUserIn = "Y";
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));
        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);

        uiHandler.dispNumBorrowedBooks(user);
        String testOut = "[NUMBER OF BORROWED BOOKS 0]" + System.lineSeparator();
        assertEquals(systemOutStream.toString(), testOut);
    }
    @Test
    @DisplayName("Testing display of user borrowed book number with 3 books borrowed")
    void RESP_11_test_02(){
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();

        User user = userList.getUser(0);
        Book book1 = catalogue.getBook(0);
        Book book2 = catalogue.getBook(9);
        Book book3 = catalogue.getBook(19);


        String SimUserIn = "";
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));
        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);

        user.borrowBook(book1, userInput);
        user.borrowBook(book2, userInput);
        user.borrowBook(book3, userInput);

        uiHandler.dispNumBorrowedBooks(user);
        String testOut = "[NUMBER OF BORROWED BOOKS 3]" + System.lineSeparator();
        assertEquals(systemOutStream.toString(), testOut);
    }
    @Test
    @DisplayName("Testing display of books and their information when some books are borrowed ")
    void RESP_12_test_01(){
        InitializeLibrary iLibrary = new InitializeLibrary();
        Catalogue catalogue = iLibrary.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        String simUserIn = "";
        Scanner userInput = new Scanner(new ByteArrayInputStream(simUserIn.getBytes()));
        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);
        User user = userList.getUser(0);
        user.borrowBook(catalogue.getBook(0), userInput);
        user.borrowBook(catalogue.getBook(1), userInput);
        user.borrowBook(catalogue.getBook(2), userInput);
        uiHandler.dispAllBooks( catalogue, user);
        assertEquals(
                System.lineSeparator() +
                        "_------:=|{[BOOK SELECTION]}|=:------_" +System.lineSeparator() +
                        "(1)" + System.lineSeparator() +
                        "Title: The Miscellaneous Mis-adventures of Captain Borqueefious" +System.lineSeparator() +
                        "Author: Dennis Bartholomew III" +System.lineSeparator() +
                        "Status: {Checked Out}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(2)" + System.lineSeparator() +
                        "Title: How to train Pigeons: A guide to flight" +System.lineSeparator() +
                        "Author: Birdy McBorderman" +System.lineSeparator() +
                        "Status: {Checked Out}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(3)" + System.lineSeparator() +
                        "Title: Day Drinking: How to do it & How not to stop" +System.lineSeparator() +
                        "Author: Ann Alkelhoul Adick" +System.lineSeparator() +
                        "Status: {Checked Out}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(4)" + System.lineSeparator() +
                        "Title: Sacrificial Birds and how to raise them" +System.lineSeparator() +
                        "Author: Birdy McBorderman" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(5)" + System.lineSeparator() +
                        "Title: Summoning Rituals: Beginner Edition" +System.lineSeparator() +
                        "Author: Alvin Evil" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(6)" + System.lineSeparator() +
                        "Title: How to program Tron & Make it a movie" +System.lineSeparator() +
                        "Author: Bill Adams & Steven Lisberger" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(7)" + System.lineSeparator() +
                        "Title: How to enter old arcade machines" +System.lineSeparator() +
                        "Author: Sam Flynn" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(8)" + System.lineSeparator() +
                        "Title: The cunning and witty emaculations of sophisticated Gentlemen" +System.lineSeparator() +
                        "Author: Sir Tophattington of Florence the III" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(9)" + System.lineSeparator() +
                        "Title: How to train dragons: a guide to owning house geckos" +System.lineSeparator() +
                        "Author: Lisa Lizard" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(10)" + System.lineSeparator() +
                        "Title: The Murderizing Mutilator Strikes back" +System.lineSeparator() +
                        "Author: Stephan Queen" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(11)" + System.lineSeparator() +
                        "Title: A sudsy and cleaning story" +System.lineSeparator() +
                        "Author: Mr. Clean" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(12)" + System.lineSeparator() +
                        "Title: Trains and sewing machines" +System.lineSeparator() +
                        "Author: Imogen Heap" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(13)" + System.lineSeparator() +
                        "Title: How to sample someone else's song" +System.lineSeparator() +
                        "Author: Jason D" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(14)" + System.lineSeparator() +
                        "Title: Burning Bridges: a guide to messy relationships" +System.lineSeparator() +
                        "Author: alisha McHomewrecker" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(15)" + System.lineSeparator() +
                        "Title: 10 dos and donts of eating burritos" +System.lineSeparator() +
                        "Author: Ieym Phatt" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(16)" + System.lineSeparator() +
                        "Title: A Guide on Exterminating Gnomes" +System.lineSeparator() +
                        "Author: A.R Jr. Mcdavid" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(17)" + System.lineSeparator() +
                        "Title: Trains and their inner workings" +System.lineSeparator() +
                        "Author: Thomas D.A. Tank" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(18)" + System.lineSeparator() +
                        "Title: How to download RAM" +System.lineSeparator() +
                        "Author: Eugine McGullible" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(19)" + System.lineSeparator() +
                        "Title: Wizard Spells for beginners: pyrokinesis edition" +System.lineSeparator() +
                        "Author: G. Grey" +System.lineSeparator() +
                        "Status: {Available}" + System.lineSeparator() +
                        "<=====------------<>------------=====>" + System.lineSeparator() +
                        "(20)" + System.lineSeparator()+
                        "Title: How to code in scheme"+ System.lineSeparator()+
                        "Author: Lord Sean Benjamin XII"+ System.lineSeparator()+
                        "Status: {Available}"+ System.lineSeparator()+
                        "<=====------------<>------------=====>" + System.lineSeparator(),systemOutStream.toString());
    }
    @Test
    @DisplayName("Testing display of books and their information when no books are borrowed ")
    void RESP_12_test_02() {

        InitializeLibrary iLibrary = new InitializeLibrary();
        Catalogue catalogue = iLibrary.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        String simUserIn = "";
        Scanner userInput = new Scanner(new ByteArrayInputStream(simUserIn.getBytes()));
        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);
        User user = userList.getUser(0);
        uiHandler.dispAllBooks( catalogue, user);
        assertEquals(
                System.lineSeparator() +
                        "_------:=|{[BOOK SELECTION]}|=:------_" +System.lineSeparator() +
                        "(1)" + System.lineSeparator() +
                        "Title: The Miscellaneous Mis-adventures of Captain Borqueefious" +System.lineSeparator() +
                        "Author: Dennis Bartholomew III" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(2)" + System.lineSeparator() +
                        "Title: How to train Pigeons: A guide to flight" +System.lineSeparator() +
                        "Author: Birdy McBorderman" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(3)" + System.lineSeparator() +
                        "Title: Day Drinking: How to do it & How not to stop" +System.lineSeparator() +
                        "Author: Ann Alkelhoul Adick" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(4)" + System.lineSeparator() +
                        "Title: Sacrificial Birds and how to raise them" +System.lineSeparator() +
                        "Author: Birdy McBorderman" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(5)" + System.lineSeparator() +
                        "Title: Summoning Rituals: Beginner Edition" +System.lineSeparator() +
                        "Author: Alvin Evil" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(6)" + System.lineSeparator() +
                        "Title: How to program Tron & Make it a movie" +System.lineSeparator() +
                        "Author: Bill Adams & Steven Lisberger" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(7)" + System.lineSeparator() +
                        "Title: How to enter old arcade machines" +System.lineSeparator() +
                        "Author: Sam Flynn" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(8)" + System.lineSeparator() +
                        "Title: The cunning and witty emaculations of sophisticated Gentlemen" +System.lineSeparator() +
                        "Author: Sir Tophattington of Florence the III" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(9)" + System.lineSeparator() +
                        "Title: How to train dragons: a guide to owning house geckos" +System.lineSeparator() +
                        "Author: Lisa Lizard" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(10)" + System.lineSeparator() +
                        "Title: The Murderizing Mutilator Strikes back" +System.lineSeparator() +
                        "Author: Stephan Queen" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(11)" + System.lineSeparator() +
                        "Title: A sudsy and cleaning story" +System.lineSeparator() +
                        "Author: Mr. Clean" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(12)" + System.lineSeparator() +
                        "Title: Trains and sewing machines" +System.lineSeparator() +
                        "Author: Imogen Heap" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(13)" + System.lineSeparator() +
                        "Title: How to sample someone else's song" +System.lineSeparator() +
                        "Author: Jason D" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(14)" + System.lineSeparator() +
                        "Title: Burning Bridges: a guide to messy relationships" +System.lineSeparator() +
                        "Author: alisha McHomewrecker" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(15)" + System.lineSeparator() +
                        "Title: 10 dos and donts of eating burritos" +System.lineSeparator() +
                        "Author: Ieym Phatt" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(16)" + System.lineSeparator() +
                        "Title: A Guide on Exterminating Gnomes" +System.lineSeparator() +
                        "Author: A.R Jr. Mcdavid" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(17)" + System.lineSeparator() +
                        "Title: Trains and their inner workings" +System.lineSeparator() +
                        "Author: Thomas D.A. Tank" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(18)" + System.lineSeparator() +
                        "Title: How to download RAM" +System.lineSeparator() +
                        "Author: Eugine McGullible" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(19)" + System.lineSeparator() +
                        "Title: Wizard Spells for beginners: pyrokinesis edition" +System.lineSeparator() +
                        "Author: G. Grey" +System.lineSeparator() +
                        "Status: {Available}" + System.lineSeparator() +
                        "<=====------------<>------------=====>" + System.lineSeparator() +
                        "(20)" + System.lineSeparator()+
                        "Title: How to code in scheme"+ System.lineSeparator()+
                        "Author: Lord Sean Benjamin XII"+ System.lineSeparator()+
                        "Status: {Available}"+ System.lineSeparator()+
                        "<=====------------<>------------=====>" + System.lineSeparator(),systemOutStream.toString());

    }

    @Test
    @DisplayName("Testing display of books and their information when some books have been returned (ie. they are no longer checked out but are on hold) by the current user ")
    void RESP_12_test_03() {

        InitializeLibrary iLibrary = new InitializeLibrary();
        Catalogue catalogue = iLibrary.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        String simUserIn = "";
        Scanner userInput = new Scanner(new ByteArrayInputStream(simUserIn.getBytes()));
        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);
        User user = userList.getUser(0);
        user.addBookToOnHoldBooks(catalogue.getBook(0));
        catalogue.getBook(0).setAvailablity(-1);
        user.addBookToOnHoldBooks(catalogue.getBook(9));
        catalogue.getBook(9).setAvailablity(-1);
        user.addBookToOnHoldBooks(catalogue.getBook(19));
        catalogue.getBook(19).setAvailablity(-1);
        uiHandler.dispAllBooks( catalogue, user);
        assertEquals(
                System.lineSeparator() +
                        "_------:=|{[BOOK SELECTION]}|=:------_" +System.lineSeparator() +
                        "(1)" + System.lineSeparator() +
                        "Title: The Miscellaneous Mis-adventures of Captain Borqueefious" +System.lineSeparator() +
                        "Author: Dennis Bartholomew III" +System.lineSeparator() +
                        "Status: {On Hold}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(2)" + System.lineSeparator() +
                        "Title: How to train Pigeons: A guide to flight" +System.lineSeparator() +
                        "Author: Birdy McBorderman" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(3)" + System.lineSeparator() +
                        "Title: Day Drinking: How to do it & How not to stop" +System.lineSeparator() +
                        "Author: Ann Alkelhoul Adick" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(4)" + System.lineSeparator() +
                        "Title: Sacrificial Birds and how to raise them" +System.lineSeparator() +
                        "Author: Birdy McBorderman" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(5)" + System.lineSeparator() +
                        "Title: Summoning Rituals: Beginner Edition" +System.lineSeparator() +
                        "Author: Alvin Evil" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(6)" + System.lineSeparator() +
                        "Title: How to program Tron & Make it a movie" +System.lineSeparator() +
                        "Author: Bill Adams & Steven Lisberger" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(7)" + System.lineSeparator() +
                        "Title: How to enter old arcade machines" +System.lineSeparator() +
                        "Author: Sam Flynn" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(8)" + System.lineSeparator() +
                        "Title: The cunning and witty emaculations of sophisticated Gentlemen" +System.lineSeparator() +
                        "Author: Sir Tophattington of Florence the III" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(9)" + System.lineSeparator() +
                        "Title: How to train dragons: a guide to owning house geckos" +System.lineSeparator() +
                        "Author: Lisa Lizard" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(10)" + System.lineSeparator() +
                        "Title: The Murderizing Mutilator Strikes back" +System.lineSeparator() +
                        "Author: Stephan Queen" +System.lineSeparator() +
                        "Status: {On Hold}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(11)" + System.lineSeparator() +
                        "Title: A sudsy and cleaning story" +System.lineSeparator() +
                        "Author: Mr. Clean" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(12)" + System.lineSeparator() +
                        "Title: Trains and sewing machines" +System.lineSeparator() +
                        "Author: Imogen Heap" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(13)" + System.lineSeparator() +
                        "Title: How to sample someone else's song" +System.lineSeparator() +
                        "Author: Jason D" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(14)" + System.lineSeparator() +
                        "Title: Burning Bridges: a guide to messy relationships" +System.lineSeparator() +
                        "Author: alisha McHomewrecker" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(15)" + System.lineSeparator() +
                        "Title: 10 dos and donts of eating burritos" +System.lineSeparator() +
                        "Author: Ieym Phatt" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(16)" + System.lineSeparator() +
                        "Title: A Guide on Exterminating Gnomes" +System.lineSeparator() +
                        "Author: A.R Jr. Mcdavid" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(17)" + System.lineSeparator() +
                        "Title: Trains and their inner workings" +System.lineSeparator() +
                        "Author: Thomas D.A. Tank" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(18)" + System.lineSeparator() +
                        "Title: How to download RAM" +System.lineSeparator() +
                        "Author: Eugine McGullible" +System.lineSeparator() +
                        "Status: {Available}" +System.lineSeparator() +
                        "<=====------------<>------------=====>" +System.lineSeparator() +
                        "(19)" + System.lineSeparator() +
                        "Title: Wizard Spells for beginners: pyrokinesis edition" +System.lineSeparator() +
                        "Author: G. Grey" +System.lineSeparator() +
                        "Status: {Available}" + System.lineSeparator() +
                        "<=====------------<>------------=====>" + System.lineSeparator() +
                        "(20)" + System.lineSeparator()+
                        "Title: How to code in scheme"+ System.lineSeparator()+
                        "Author: Lord Sean Benjamin XII"+ System.lineSeparator()+
                        "Status: {On Hold}"+ System.lineSeparator()+
                        "<=====------------<>------------=====>" + System.lineSeparator(),systemOutStream.toString());

    }
    @Test
    @DisplayName("Testing positive flow with valid borrow selection and confirmation")
    void RESP_13_test_01() {
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        User user = userList.getUser(0);

        String SimUserIn = "1\nY\n";
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));

        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);
        Library.borrowState(uiHandler,user,catalogue,userInput); //1 here means user selected borrow operation
        String expectedOut = "[NUMBER OF BORROWED BOOKS 0]" + System.lineSeparator()
                + System.lineSeparator() +
                "_------:=|{[BOOK SELECTION]}|=:------_" +System.lineSeparator() +
                "(1)" + System.lineSeparator() +
                "Title: The Miscellaneous Mis-adventures of Captain Borqueefious" +System.lineSeparator() +
                "Author: Dennis Bartholomew III" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(2)" + System.lineSeparator() +
                "Title: How to train Pigeons: A guide to flight" +System.lineSeparator() +
                "Author: Birdy McBorderman" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(3)" + System.lineSeparator() +
                "Title: Day Drinking: How to do it & How not to stop" +System.lineSeparator() +
                "Author: Ann Alkelhoul Adick" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(4)" + System.lineSeparator() +
                "Title: Sacrificial Birds and how to raise them" +System.lineSeparator() +
                "Author: Birdy McBorderman" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(5)" + System.lineSeparator() +
                "Title: Summoning Rituals: Beginner Edition" +System.lineSeparator() +
                "Author: Alvin Evil" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(6)" + System.lineSeparator() +
                "Title: How to program Tron & Make it a movie" +System.lineSeparator() +
                "Author: Bill Adams & Steven Lisberger" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(7)" + System.lineSeparator() +
                "Title: How to enter old arcade machines" +System.lineSeparator() +
                "Author: Sam Flynn" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(8)" + System.lineSeparator() +
                "Title: The cunning and witty emaculations of sophisticated Gentlemen" +System.lineSeparator() +
                "Author: Sir Tophattington of Florence the III" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(9)" + System.lineSeparator() +
                "Title: How to train dragons: a guide to owning house geckos" +System.lineSeparator() +
                "Author: Lisa Lizard" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(10)" + System.lineSeparator() +
                "Title: The Murderizing Mutilator Strikes back" +System.lineSeparator() +
                "Author: Stephan Queen" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(11)" + System.lineSeparator() +
                "Title: A sudsy and cleaning story" +System.lineSeparator() +
                "Author: Mr. Clean" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(12)" + System.lineSeparator() +
                "Title: Trains and sewing machines" +System.lineSeparator() +
                "Author: Imogen Heap" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(13)" + System.lineSeparator() +
                "Title: How to sample someone else's song" +System.lineSeparator() +
                "Author: Jason D" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(14)" + System.lineSeparator() +
                "Title: Burning Bridges: a guide to messy relationships" +System.lineSeparator() +
                "Author: alisha McHomewrecker" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(15)" + System.lineSeparator() +
                "Title: 10 dos and donts of eating burritos" +System.lineSeparator() +
                "Author: Ieym Phatt" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(16)" + System.lineSeparator() +
                "Title: A Guide on Exterminating Gnomes" +System.lineSeparator() +
                "Author: A.R Jr. Mcdavid" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(17)" + System.lineSeparator() +
                "Title: Trains and their inner workings" +System.lineSeparator() +
                "Author: Thomas D.A. Tank" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(18)" + System.lineSeparator() +
                "Title: How to download RAM" +System.lineSeparator() +
                "Author: Eugine McGullible" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(19)" + System.lineSeparator() +
                "Title: Wizard Spells for beginners: pyrokinesis edition" +System.lineSeparator() +
                "Author: G. Grey" +System.lineSeparator() +
                "Status: {Available}" + System.lineSeparator() +
                "<=====------------<>------------=====>" + System.lineSeparator() +
                "(20)" + System.lineSeparator()+
                "Title: How to code in scheme"+ System.lineSeparator()+
                "Author: Lord Sean Benjamin XII"+ System.lineSeparator()+
                "Status: {Available}"+ System.lineSeparator()+
                "<=====------------<>------------=====>" + System.lineSeparator() + System.lineSeparator() +
                "What book would you like to borrow?" + System.lineSeparator() +
                "_-------:=|{[BOOK DETAILS]}|=:-------_" + System.lineSeparator() +
                "Title: The Miscellaneous Mis-adventures of Captain Borqueefious" + System.lineSeparator() +
                "Author: Dennis Bartholomew III" + System.lineSeparator() +
                "Status: {Available}" + System.lineSeparator() +
                "<=====------------<>------------=====>" + System.lineSeparator() +
                "Confirm Borrow (Y/N)" + System.lineSeparator();
        assertTrue(systemOutStream.toString().startsWith(expectedOut), "");
    }
    @Test
    @DisplayName("Testing negative flow with valid borrow selection and rejection (then acceptance so I can show out put")
    void RESP_13_test_02() {
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        User user = userList.getUser(0);

        String SimUserIn = "1\nN\n1\nY";
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));

        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);
        Library.borrowState(uiHandler,user,catalogue,userInput); //1 here means user selected borrow operation
        String expectedOut = "[NUMBER OF BORROWED BOOKS 0]" + System.lineSeparator()
                + System.lineSeparator() +
                "_------:=|{[BOOK SELECTION]}|=:------_" +System.lineSeparator() +
                "(1)" + System.lineSeparator() +
                "Title: The Miscellaneous Mis-adventures of Captain Borqueefious" +System.lineSeparator() +
                "Author: Dennis Bartholomew III" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(2)" + System.lineSeparator() +
                "Title: How to train Pigeons: A guide to flight" +System.lineSeparator() +
                "Author: Birdy McBorderman" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(3)" + System.lineSeparator() +
                "Title: Day Drinking: How to do it & How not to stop" +System.lineSeparator() +
                "Author: Ann Alkelhoul Adick" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(4)" + System.lineSeparator() +
                "Title: Sacrificial Birds and how to raise them" +System.lineSeparator() +
                "Author: Birdy McBorderman" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(5)" + System.lineSeparator() +
                "Title: Summoning Rituals: Beginner Edition" +System.lineSeparator() +
                "Author: Alvin Evil" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(6)" + System.lineSeparator() +
                "Title: How to program Tron & Make it a movie" +System.lineSeparator() +
                "Author: Bill Adams & Steven Lisberger" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(7)" + System.lineSeparator() +
                "Title: How to enter old arcade machines" +System.lineSeparator() +
                "Author: Sam Flynn" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(8)" + System.lineSeparator() +
                "Title: The cunning and witty emaculations of sophisticated Gentlemen" +System.lineSeparator() +
                "Author: Sir Tophattington of Florence the III" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(9)" + System.lineSeparator() +
                "Title: How to train dragons: a guide to owning house geckos" +System.lineSeparator() +
                "Author: Lisa Lizard" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(10)" + System.lineSeparator() +
                "Title: The Murderizing Mutilator Strikes back" +System.lineSeparator() +
                "Author: Stephan Queen" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(11)" + System.lineSeparator() +
                "Title: A sudsy and cleaning story" +System.lineSeparator() +
                "Author: Mr. Clean" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(12)" + System.lineSeparator() +
                "Title: Trains and sewing machines" +System.lineSeparator() +
                "Author: Imogen Heap" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(13)" + System.lineSeparator() +
                "Title: How to sample someone else's song" +System.lineSeparator() +
                "Author: Jason D" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(14)" + System.lineSeparator() +
                "Title: Burning Bridges: a guide to messy relationships" +System.lineSeparator() +
                "Author: alisha McHomewrecker" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(15)" + System.lineSeparator() +
                "Title: 10 dos and donts of eating burritos" +System.lineSeparator() +
                "Author: Ieym Phatt" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(16)" + System.lineSeparator() +
                "Title: A Guide on Exterminating Gnomes" +System.lineSeparator() +
                "Author: A.R Jr. Mcdavid" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(17)" + System.lineSeparator() +
                "Title: Trains and their inner workings" +System.lineSeparator() +
                "Author: Thomas D.A. Tank" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(18)" + System.lineSeparator() +
                "Title: How to download RAM" +System.lineSeparator() +
                "Author: Eugine McGullible" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(19)" + System.lineSeparator() +
                "Title: Wizard Spells for beginners: pyrokinesis edition" +System.lineSeparator() +
                "Author: G. Grey" +System.lineSeparator() +
                "Status: {Available}" + System.lineSeparator() +
                "<=====------------<>------------=====>" + System.lineSeparator() +
                "(20)" + System.lineSeparator()+
                "Title: How to code in scheme"+ System.lineSeparator()+
                "Author: Lord Sean Benjamin XII"+ System.lineSeparator()+
                "Status: {Available}"+ System.lineSeparator()+
                "<=====------------<>------------=====>" + System.lineSeparator() + System.lineSeparator() +
                "What book would you like to borrow?" + System.lineSeparator() +
                "_-------:=|{[BOOK DETAILS]}|=:-------_" + System.lineSeparator() +
                "Title: The Miscellaneous Mis-adventures of Captain Borqueefious" + System.lineSeparator() +
                "Author: Dennis Bartholomew III" + System.lineSeparator() +
                "Status: {Available}" + System.lineSeparator() +
                "<=====------------<>------------=====>" + System.lineSeparator() +
                "Confirm Borrow (Y/N)" + System.lineSeparator() + System.lineSeparator() +
                "_------:=|{[BOOK SELECTION]}|=:------_" +System.lineSeparator() +
                "(1)" + System.lineSeparator() +
                "Title: The Miscellaneous Mis-adventures of Captain Borqueefious" +System.lineSeparator() +
                "Author: Dennis Bartholomew III" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(2)" + System.lineSeparator() +
                "Title: How to train Pigeons: A guide to flight" +System.lineSeparator() +
                "Author: Birdy McBorderman" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(3)" + System.lineSeparator() +
                "Title: Day Drinking: How to do it & How not to stop" +System.lineSeparator() +
                "Author: Ann Alkelhoul Adick" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(4)" + System.lineSeparator() +
                "Title: Sacrificial Birds and how to raise them" +System.lineSeparator() +
                "Author: Birdy McBorderman" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(5)" + System.lineSeparator() +
                "Title: Summoning Rituals: Beginner Edition" +System.lineSeparator() +
                "Author: Alvin Evil" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(6)" + System.lineSeparator() +
                "Title: How to program Tron & Make it a movie" +System.lineSeparator() +
                "Author: Bill Adams & Steven Lisberger" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(7)" + System.lineSeparator() +
                "Title: How to enter old arcade machines" +System.lineSeparator() +
                "Author: Sam Flynn" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(8)" + System.lineSeparator() +
                "Title: The cunning and witty emaculations of sophisticated Gentlemen" +System.lineSeparator() +
                "Author: Sir Tophattington of Florence the III" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(9)" + System.lineSeparator() +
                "Title: How to train dragons: a guide to owning house geckos" +System.lineSeparator() +
                "Author: Lisa Lizard" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(10)" + System.lineSeparator() +
                "Title: The Murderizing Mutilator Strikes back" +System.lineSeparator() +
                "Author: Stephan Queen" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(11)" + System.lineSeparator() +
                "Title: A sudsy and cleaning story" +System.lineSeparator() +
                "Author: Mr. Clean" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(12)" + System.lineSeparator() +
                "Title: Trains and sewing machines" +System.lineSeparator() +
                "Author: Imogen Heap" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(13)" + System.lineSeparator() +
                "Title: How to sample someone else's song" +System.lineSeparator() +
                "Author: Jason D" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(14)" + System.lineSeparator() +
                "Title: Burning Bridges: a guide to messy relationships" +System.lineSeparator() +
                "Author: alisha McHomewrecker" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(15)" + System.lineSeparator() +
                "Title: 10 dos and donts of eating burritos" +System.lineSeparator() +
                "Author: Ieym Phatt" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(16)" + System.lineSeparator() +
                "Title: A Guide on Exterminating Gnomes" +System.lineSeparator() +
                "Author: A.R Jr. Mcdavid" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(17)" + System.lineSeparator() +
                "Title: Trains and their inner workings" +System.lineSeparator() +
                "Author: Thomas D.A. Tank" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(18)" + System.lineSeparator() +
                "Title: How to download RAM" +System.lineSeparator() +
                "Author: Eugine McGullible" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(19)" + System.lineSeparator() +
                "Title: Wizard Spells for beginners: pyrokinesis edition" +System.lineSeparator() +
                "Author: G. Grey" +System.lineSeparator() +
                "Status: {Available}" + System.lineSeparator() +
                "<=====------------<>------------=====>" + System.lineSeparator() +
                "(20)" + System.lineSeparator()+
                "Title: How to code in scheme"+ System.lineSeparator()+
                "Author: Lord Sean Benjamin XII"+ System.lineSeparator()+
                "Status: {Available}"+ System.lineSeparator()+
                "<=====------------<>------------=====>" + System.lineSeparator() + System.lineSeparator() +
                "What book would you like to borrow?" + System.lineSeparator() +
                "_-------:=|{[BOOK DETAILS]}|=:-------_" + System.lineSeparator() +
                "Title: The Miscellaneous Mis-adventures of Captain Borqueefious" + System.lineSeparator() +
                "Author: Dennis Bartholomew III" + System.lineSeparator() +
                "Status: {Available}" + System.lineSeparator() +
                "<=====------------<>------------=====>" + System.lineSeparator() +
                "Confirm Borrow (Y/N)" + System.lineSeparator();
        assertTrue(systemOutStream.toString().startsWith(expectedOut), "");
    }
    @Test
    @DisplayName("Testing negative flow with invalid borrow selection (then acceptance so I can show output")
    void RESP_13_test_03() {
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        User user = userList.getUser(0);

        String SimUserIn = "21\n1\nY";
        Scanner userInput = new Scanner(new ByteArrayInputStream(SimUserIn.getBytes()));

        UserIOHandler uiHandler = new UserIOHandler(userInput, userList);
        Library.borrowState(uiHandler,user,catalogue,userInput); //1 here means user selected borrow operation
        String expectedOut = "[NUMBER OF BORROWED BOOKS 0]" + System.lineSeparator()
                + System.lineSeparator() +
                "_------:=|{[BOOK SELECTION]}|=:------_" +System.lineSeparator() +
                "(1)" + System.lineSeparator() +
                "Title: The Miscellaneous Mis-adventures of Captain Borqueefious" +System.lineSeparator() +
                "Author: Dennis Bartholomew III" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(2)" + System.lineSeparator() +
                "Title: How to train Pigeons: A guide to flight" +System.lineSeparator() +
                "Author: Birdy McBorderman" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(3)" + System.lineSeparator() +
                "Title: Day Drinking: How to do it & How not to stop" +System.lineSeparator() +
                "Author: Ann Alkelhoul Adick" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(4)" + System.lineSeparator() +
                "Title: Sacrificial Birds and how to raise them" +System.lineSeparator() +
                "Author: Birdy McBorderman" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(5)" + System.lineSeparator() +
                "Title: Summoning Rituals: Beginner Edition" +System.lineSeparator() +
                "Author: Alvin Evil" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(6)" + System.lineSeparator() +
                "Title: How to program Tron & Make it a movie" +System.lineSeparator() +
                "Author: Bill Adams & Steven Lisberger" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(7)" + System.lineSeparator() +
                "Title: How to enter old arcade machines" +System.lineSeparator() +
                "Author: Sam Flynn" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(8)" + System.lineSeparator() +
                "Title: The cunning and witty emaculations of sophisticated Gentlemen" +System.lineSeparator() +
                "Author: Sir Tophattington of Florence the III" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(9)" + System.lineSeparator() +
                "Title: How to train dragons: a guide to owning house geckos" +System.lineSeparator() +
                "Author: Lisa Lizard" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(10)" + System.lineSeparator() +
                "Title: The Murderizing Mutilator Strikes back" +System.lineSeparator() +
                "Author: Stephan Queen" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(11)" + System.lineSeparator() +
                "Title: A sudsy and cleaning story" +System.lineSeparator() +
                "Author: Mr. Clean" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(12)" + System.lineSeparator() +
                "Title: Trains and sewing machines" +System.lineSeparator() +
                "Author: Imogen Heap" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(13)" + System.lineSeparator() +
                "Title: How to sample someone else's song" +System.lineSeparator() +
                "Author: Jason D" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(14)" + System.lineSeparator() +
                "Title: Burning Bridges: a guide to messy relationships" +System.lineSeparator() +
                "Author: alisha McHomewrecker" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(15)" + System.lineSeparator() +
                "Title: 10 dos and donts of eating burritos" +System.lineSeparator() +
                "Author: Ieym Phatt" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(16)" + System.lineSeparator() +
                "Title: A Guide on Exterminating Gnomes" +System.lineSeparator() +
                "Author: A.R Jr. Mcdavid" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(17)" + System.lineSeparator() +
                "Title: Trains and their inner workings" +System.lineSeparator() +
                "Author: Thomas D.A. Tank" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(18)" + System.lineSeparator() +
                "Title: How to download RAM" +System.lineSeparator() +
                "Author: Eugine McGullible" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(19)" + System.lineSeparator() +
                "Title: Wizard Spells for beginners: pyrokinesis edition" +System.lineSeparator() +
                "Author: G. Grey" +System.lineSeparator() +
                "Status: {Available}" + System.lineSeparator() +
                "<=====------------<>------------=====>" + System.lineSeparator() +
                "(20)" + System.lineSeparator()+
                "Title: How to code in scheme"+ System.lineSeparator()+
                "Author: Lord Sean Benjamin XII"+ System.lineSeparator()+
                "Status: {Available}"+ System.lineSeparator()+
                "<=====------------<>------------=====>" + System.lineSeparator() + System.lineSeparator() +
                "What book would you like to borrow?" + System.lineSeparator() +
                "Invalid selection please try again..."+ System.lineSeparator() + System.lineSeparator() +
                "_------:=|{[BOOK SELECTION]}|=:------_" +System.lineSeparator() +
                "(1)" + System.lineSeparator() +
                "Title: The Miscellaneous Mis-adventures of Captain Borqueefious" +System.lineSeparator() +
                "Author: Dennis Bartholomew III" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(2)" + System.lineSeparator() +
                "Title: How to train Pigeons: A guide to flight" +System.lineSeparator() +
                "Author: Birdy McBorderman" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(3)" + System.lineSeparator() +
                "Title: Day Drinking: How to do it & How not to stop" +System.lineSeparator() +
                "Author: Ann Alkelhoul Adick" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(4)" + System.lineSeparator() +
                "Title: Sacrificial Birds and how to raise them" +System.lineSeparator() +
                "Author: Birdy McBorderman" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(5)" + System.lineSeparator() +
                "Title: Summoning Rituals: Beginner Edition" +System.lineSeparator() +
                "Author: Alvin Evil" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(6)" + System.lineSeparator() +
                "Title: How to program Tron & Make it a movie" +System.lineSeparator() +
                "Author: Bill Adams & Steven Lisberger" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(7)" + System.lineSeparator() +
                "Title: How to enter old arcade machines" +System.lineSeparator() +
                "Author: Sam Flynn" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(8)" + System.lineSeparator() +
                "Title: The cunning and witty emaculations of sophisticated Gentlemen" +System.lineSeparator() +
                "Author: Sir Tophattington of Florence the III" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(9)" + System.lineSeparator() +
                "Title: How to train dragons: a guide to owning house geckos" +System.lineSeparator() +
                "Author: Lisa Lizard" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(10)" + System.lineSeparator() +
                "Title: The Murderizing Mutilator Strikes back" +System.lineSeparator() +
                "Author: Stephan Queen" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(11)" + System.lineSeparator() +
                "Title: A sudsy and cleaning story" +System.lineSeparator() +
                "Author: Mr. Clean" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(12)" + System.lineSeparator() +
                "Title: Trains and sewing machines" +System.lineSeparator() +
                "Author: Imogen Heap" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(13)" + System.lineSeparator() +
                "Title: How to sample someone else's song" +System.lineSeparator() +
                "Author: Jason D" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(14)" + System.lineSeparator() +
                "Title: Burning Bridges: a guide to messy relationships" +System.lineSeparator() +
                "Author: alisha McHomewrecker" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(15)" + System.lineSeparator() +
                "Title: 10 dos and donts of eating burritos" +System.lineSeparator() +
                "Author: Ieym Phatt" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(16)" + System.lineSeparator() +
                "Title: A Guide on Exterminating Gnomes" +System.lineSeparator() +
                "Author: A.R Jr. Mcdavid" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(17)" + System.lineSeparator() +
                "Title: Trains and their inner workings" +System.lineSeparator() +
                "Author: Thomas D.A. Tank" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(18)" + System.lineSeparator() +
                "Title: How to download RAM" +System.lineSeparator() +
                "Author: Eugine McGullible" +System.lineSeparator() +
                "Status: {Available}" +System.lineSeparator() +
                "<=====------------<>------------=====>" +System.lineSeparator() +
                "(19)" + System.lineSeparator() +
                "Title: Wizard Spells for beginners: pyrokinesis edition" +System.lineSeparator() +
                "Author: G. Grey" +System.lineSeparator() +
                "Status: {Available}" + System.lineSeparator() +
                "<=====------------<>------------=====>" + System.lineSeparator() +
                "(20)" + System.lineSeparator()+
                "Title: How to code in scheme"+ System.lineSeparator()+
                "Author: Lord Sean Benjamin XII"+ System.lineSeparator()+
                "Status: {Available}"+ System.lineSeparator()+
                "<=====------------<>------------=====>" + System.lineSeparator() + System.lineSeparator() +
                "What book would you like to borrow?" + System.lineSeparator() +
                "_-------:=|{[BOOK DETAILS]}|=:-------_" + System.lineSeparator() +
                "Title: The Miscellaneous Mis-adventures of Captain Borqueefious" + System.lineSeparator() +
                "Author: Dennis Bartholomew III" + System.lineSeparator() +
                "Status: {Available}" + System.lineSeparator() +
                "<=====------------<>------------=====>" + System.lineSeparator() +
                "Confirm Borrow (Y/N)" + System.lineSeparator();
        assertTrue(systemOutStream.toString().startsWith(expectedOut), "");
        //note: i just want to make it clear how horrible coding these tests first is, like this sucks to make code for
    }



}


