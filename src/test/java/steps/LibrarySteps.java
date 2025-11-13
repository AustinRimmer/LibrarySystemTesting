package steps;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import org.example.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class LibrarySteps {
    Library library;
    InitializeUserList initializeUserList = new InitializeUserList();
    InitializeLibrary lib = new InitializeLibrary();
    Catalogue catalogue = lib.initializeLibrary();
    UserList userList = initializeUserList.initializeUserList();

    private int getBookIndex(String bookTitle){
        int bookIndex = -1;

        switch (bookTitle) {
            case "The Great Gatsby" -> bookIndex = 0;
            case "To Kill a Mockingbird" -> bookIndex = 1;
            case "1984" -> bookIndex = 2;
            case "Pride and Prejudice" -> bookIndex = 3;
            case "The Hobbit" -> bookIndex = 4;
            case "Harry Potter" -> bookIndex = 5;
            case "The Catcher in the Rye" -> bookIndex = 6;
            case "Animal Farm" -> bookIndex = 7;
            case "Lord of the Flies" -> bookIndex = 8;
            case "Jane Eyre" -> bookIndex = 9;
            case "Wuthering Heights" -> bookIndex = 10;
            case "Moby Dick" -> bookIndex = 11;
            case "The Odyssey" -> bookIndex = 12;
            case "Hamlet" -> bookIndex = 13;
            case "War and Peace" -> bookIndex = 14;
            case "The Divine Comedy" -> bookIndex = 15;
            case "Crime and Punishment" -> bookIndex = 16;
            case "Don Quixote" -> bookIndex = 17;
            case "The Iliad" -> bookIndex = 18;
            case "Ulysses" -> bookIndex = 19;
        }
        return bookIndex;
    }

    private int getUserIndex(String username){
        int user = -1;
        if(username.equals("Alice")){
            user = 0;
        }
        if(username.equals("Bob")){
            user = 1;
        }
        if(username.equals("Charlie")){
            user = 2;
        }
        return user;
    }

    @Given("I have an initialized library")
    public void i_have_an_initialized_library(){
        library = new Library();
        System.out.println("Library, Catalog, and userlist initialized");
    }
    @When("{string} logs in")
    public void user_logs_in(String username) {
        String user1Login = "";
        if(username.equals("Alice")){
            user1Login += "alice" + "\n" +"pass123" + "\n";
        }
        if(username.equals("Bob")){
            user1Login += "bob" + "\n" +"pass456" + "\n";
        }
        if(username.equals("Charlie")){
            user1Login += "charlie" + "\n" +"pass789" + "\n";
        }

        Scanner user1Scanner = new Scanner(new ByteArrayInputStream(user1Login.getBytes()));
        UserIOHandler uiHandler = new UserIOHandler(user1Scanner, userList);
        library.initializeSessionHolder(user1Scanner,uiHandler,userList);
    }
    @When("{string} borrows {string}")
    public void user_borrows_book(String username, String book) {
        User crntUser = userList.getUser(getUserIndex(username));
        Scanner user1Scanner = new Scanner(new ByteArrayInputStream(("Y\n").getBytes()));
        crntUser.borrowBook(catalogue.getBook(getBookIndex(book)), user1Scanner);

    }
    @When("{string} logs out")
    public void user_logs_out(String username) {
        User crntUser = userList.getUser(getUserIndex(username));
        Scanner user1Scanner = new Scanner(new ByteArrayInputStream(("").getBytes()));
        UserIOHandler uiHandler = new UserIOHandler(user1Scanner, userList);
        library.logoutState(uiHandler,crntUser,catalogue,user1Scanner);
    }
    @Then("{string} is unavailable")
    public void book_is_unavailable(String book) {
        assertTrue(catalogue.getBook(getBookIndex(book)).getAvailablity() != 1);
    }
    @When("{string} returns {string}")
    public void user_returns_book(String username, String book) {

        User crntUser = userList.getUser(getUserIndex(username));
        Scanner user1Scanner = new Scanner(new ByteArrayInputStream(("Y\n").getBytes()));
        crntUser.returnBook(catalogue.getBook(getBookIndex(book)));

    }
    @Then("{string} is available")
    public void book_is_available(String book) {
        assertTrue(catalogue.getBook(getBookIndex(book)).getAvailablity() == 1);
    }


    @Then("{string} is first in hold queue of {string}")
    public void user_is_first_in_hold_queue_of_book(String username, String book) {
        Book crntBook = catalogue.getBook(getBookIndex(book));
        assertEquals(username.toLowerCase() + " ", crntBook.getHolder(0));
    }

    @Then("{string} is notified that a held book {string} is available")
    public void user_is_notified_that_a_held_book_book_is_available(String username, String book) {
        Book crntBook = catalogue.getBook(getBookIndex(book));
        assertTrue((username.toLowerCase() + " ").equals( crntBook.getHolder(0)) && crntBook.getAvailablity() == -1);
    }

    @Then("{string} has {int} books borrowed")
    public void user_has_number_books_borrowed(String username, int borrows) {
        int numBorrows = userList.getUser(getUserIndex(username)).getNumberOfBorrowedBooks();
        assertTrue(numBorrows == borrows);
    }

    @And("{string} has {int} held books")
    public void user_has_held_books(String username, int holds) {
        int numHolds = userList.getUser(getUserIndex(username)).getBooksOnHold().size();
        assertTrue(numHolds == holds);
    }

    @And("all books are available")
    public void all_books_are_available() {
        for(int i = 0; i <catalogue.getCatalogueSize(); i++){
            if(catalogue.getBook(i).getAvailablity() <= 0){
                assertTrue(1 == 2);
            }
        }
        assertTrue(1 == 1);
    }
}
