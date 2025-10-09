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
}

