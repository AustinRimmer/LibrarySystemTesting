package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
}

