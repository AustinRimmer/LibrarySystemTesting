package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
public class LibraryTest {
    @Test
    @DisplayName("Check library catalogue size is 20")
    void RESP_01_test_01(){
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();

        int size = catalogue.getCatalogueSize();

        assertEquals(20, size);


    }
    @Test
    @DisplayName ("Boundary Testing book names at 1st, 10th, and 20th positions")
    void RESP_01_test_02() {
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();

        Book book1 = catalogue.getBook(0);
        String title1 = book1.getTitle();
        Book book2 = catalogue.getBook(9);
        String title2 = book2.getTitle();
        Book book3 = catalogue.getBook(19);
        String title3 = book3.getTitle();
        assertEquals("The Miscellaneous Mis-adventures of Captain Borqueefious",title1);
        assertEquals("The Murderizing Mutilator Strikes back",title2);
        assertEquals("How to code in scheme",title3);

    }
    @Test
    @DisplayName("Check that 3 users are initialized")
    void RESP_02_test_01(){
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userlist = initializeUserList.initializeUserList();

        int numberOfUsers = userlist.getNumberOfUsers();

        assertEquals(3, numberOfUsers);


    }

    @Test
    @DisplayName("Ensure that all pre initialized users have valid usernames and passwords")
    void RESP_03_test_01(){
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        boolean user1ValidUser = Validator.validateUsername(userList.getUser(0).getUsername(),userList);
        boolean user2ValidUser = Validator.validateUsername(userList.getUser(1).getUsername(),userList);
        boolean user3ValidUser = Validator.validateUsername(userList.getUser(2).getUsername(), userList);


        boolean user1ValidPass = Validator.validatePassword(userList.getUser(0).getPassword());
        boolean user2ValidPass = Validator.validatePassword(userList.getUser(1).getPassword());
        boolean user3ValidPass = Validator.validatePassword(userList.getUser(2).getPassword());


        assertEquals(false, user1ValidUser);
        assertEquals(false, user2ValidUser);
        assertEquals(false, user3ValidUser);
        //need to be false since users are already initalized at start of test,
        //thus comparing their username string and checking if valid will always
        //be false since its technically a duplicate
        assertEquals(true, user1ValidPass);
        assertEquals(true, user2ValidPass);
        assertEquals(true, user3ValidPass);


    }
    @Test
    @DisplayName("Boundary Testing acceptable passwords")
    void RESP_03_test_02(){
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();
        boolean tooShort = Validator.validatePassword("12@a");
        boolean noNumbers = Validator.validatePassword("ab@aazay");
        boolean noSpecial = Validator.validatePassword("ab1aazay");
        boolean noLetters = Validator.validatePassword("12@12345");


        boolean justRight = Validator.validatePassword("p@55w0rd");
        assertEquals(false,tooShort);
        assertEquals(false,noNumbers);
        assertEquals(false,noSpecial);
        assertEquals(false,noLetters);
        assertEquals(true,justRight);
    }
    @Test
    @DisplayName("Testing adding a book to hold")
    void RESP_05_test_01(){
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();

        userList.getUser(0).addBookToOnHoldBooks(catalogue.getBook(0));

        assertEquals(userList.getUser(0).getUsername(), catalogue.getBook(0).getHolder(0));
    }
    @Test
    @DisplayName("Testing multiple holds by same user")
    void RESP_05_test_02(){
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();

        userList.getUser(0).addBookToOnHoldBooks(catalogue.getBook(0));
        userList.getUser(0).addBookToOnHoldBooks(catalogue.getBook(1));
        userList.getUser(0).addBookToOnHoldBooks(catalogue.getBook(2));

        assertEquals(userList.getUser(0).getUsername(), catalogue.getBook(0).getHolder(0));
        assertEquals(userList.getUser(0).getUsername(), catalogue.getBook(1).getHolder(0));
        assertEquals(userList.getUser(0).getUsername(), catalogue.getBook(2).getHolder(0));
    }
    @Test
    @DisplayName("Testing multiple users with same held book/multiple borrows by same user")
    void RESP_05_test_03(){
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();



        userList.getUser(1).addBookToOnHoldBooks(catalogue.getBook(0));
        userList.getUser(2).addBookToOnHoldBooks(catalogue.getBook(0));

        assertEquals(userList.getUser(1).getUsername(), catalogue.getBook(0).getHolder(0));
        assertEquals(userList.getUser(2).getUsername(), catalogue.getBook(0).getHolder(1));
    }
    @Test
    @DisplayName("Fetching user holds availability when book is on hold but current user is the first is the hold queue (should show as available)")
    void RESP_06_test_01(){
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();



        userList.getUser(1).addBookToOnHoldBooks(catalogue.getBook(0));
        userList.getUser(2).addBookToOnHoldBooks(catalogue.getBook(0));
        ArrayList<Integer> testAvail = userList.getUser(1).getHeldBookAvailability();
        assertEquals(1,testAvail.get(0));
    }
    @Test
    @DisplayName("Fetching user holds availability on unavailable books")
    void RESP_06_test_02(){
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();


        catalogue.getBook(0).setAvailablity(0);
        catalogue.getBook(1).setAvailablity(0);
        userList.getUser(1).addBookToOnHoldBooks(catalogue.getBook(0));
        userList.getUser(1).addBookToOnHoldBooks(catalogue.getBook(1));
        ArrayList<Integer> testAvail = userList.getUser(1).getHeldBookAvailability();
        assertEquals(0,testAvail.get(0));
        assertEquals(0,testAvail.get(1));
    }
    @Test
    @DisplayName("Fetching user holds availability on available books (wouldnt happen in normal use) since book would always be on hold they would just be first)")
    void RESP_06_test_03(){
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();


        catalogue.getBook(0).setAvailablity(1);
        catalogue.getBook(1).setAvailablity(1);
        userList.getUser(1).addBookToOnHoldBooks(catalogue.getBook(0));
        userList.getUser(1).addBookToOnHoldBooks(catalogue.getBook(1));
        ArrayList<Integer> testAvail = userList.getUser(1).getHeldBookAvailability();
        assertEquals(1,testAvail.get(0));
        assertEquals(1,testAvail.get(1));
    }
    @Test
    @DisplayName("Fetching user holds availability on assortment of different book availabilities")
    void RESP_06_test_04(){
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();


        catalogue.getBook(0).setAvailablity(0);
        catalogue.getBook(1).setAvailablity(1);
        catalogue.getBook(2).setAvailablity(-1);
        catalogue.getBook(3).setAvailablity(-1);
        userList.getUser(1).addBookToOnHoldBooks(catalogue.getBook(0));
        userList.getUser(1).addBookToOnHoldBooks(catalogue.getBook(1));

        //user is first in hold queue
        userList.getUser(1).addBookToOnHoldBooks(catalogue.getBook(2));
        //user isnt first in hold queue
        userList.getUser(2).addBookToOnHoldBooks(catalogue.getBook(3));
        userList.getUser(1).addBookToOnHoldBooks(catalogue.getBook(3));
        ArrayList<Integer> testAvail = userList.getUser(1).getHeldBookAvailability();
        assertEquals(0,testAvail.get(0));
        assertEquals(1,testAvail.get(1));
        assertEquals(1,testAvail.get(2));
        assertEquals(-1,testAvail.get(3));
    }
    @Test
    @DisplayName("Fetching user holds availability when they have no held books")
    void RESP_06_test_05(){
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        InitializeUserList initializeUserList = new InitializeUserList();
        UserList userList = initializeUserList.initializeUserList();



        ArrayList<Integer> testAvail = userList.getUser(1).getHeldBookAvailability();
        assertEquals(-2,testAvail.get(0));
    }
    @Test
    @DisplayName("Fetching book due dates when not updating due date")
    void RESP_09_test_01(){
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        String testDueDate = catalogue.getBook(0).getDueDate();
        String expectedDueDate = "NOT CHECKED OUT";
        assertEquals(expectedDueDate,testDueDate);
    }
    @Test
    @DisplayName("Fetching book due dates when updating due date")
    void RESP_09_test_02(){
        //for ease of testing and since i want less headaches, im just always going to test the current system date
        InitializeLibrary library = new InitializeLibrary();
        Catalogue catalogue = library.initializeLibrary();
        //this is the line im planning to call once borrowing is implemented
        catalogue.getBook(0).calculateDueDate();
        String testDueDate = catalogue.getBook(0).getDueDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime currentDate = LocalDateTime.now();
        String expectedDueDate = currentDate.plusDays(14).format(formatter);
        assertEquals(expectedDueDate,testDueDate);
    }

}




