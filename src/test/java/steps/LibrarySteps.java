package steps;

import org.example.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class LibrarySteps {
    Library library;
    InitializeUserList initializeUserList = new InitializeUserList();
    InitializeLibrary lib = new InitializeLibrary();
    Catalogue catalogue = lib.initializeLibrary();
    UserList userList = initializeUserList.initializeUserList();


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

        int bookIndex = -1;

        switch (book) {
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

        User crntUser = userList.getUser(user);
        Scanner user1Scanner = new Scanner(new ByteArrayInputStream(("Y\n").getBytes()));
        crntUser.borrowBook(catalogue.getBook(bookIndex), user1Scanner);

    }
    @When("{string} logs out")
    public void user_logs_out(String username) {
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
        User crntUser = userList.getUser(user);
        Scanner user1Scanner = new Scanner(new ByteArrayInputStream(("").getBytes()));
        UserIOHandler uiHandler = new UserIOHandler(user1Scanner, userList);
        library.logoutState(uiHandler,crntUser,catalogue,user1Scanner);
    }
    @Then("{string} is unavailable")
    public void book_is_unavailable(String book) {
        int bookIndex = -1;
        switch (book) {
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
        assertEquals(0, catalogue.getBook(bookIndex).getAvailablity());
    }
    @When("{string} returns {string}")
    public void user_returns_book(String username, String book) {
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

        int bookIndex = -1;

        switch (book) {
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

        User crntUser = userList.getUser(user);
        Scanner user1Scanner = new Scanner(new ByteArrayInputStream(("Y\n").getBytes()));
        crntUser.returnBook(catalogue.getBook(bookIndex));

    }
    @Then("{string} is available")
    public void book_is_available(String book) {
        int bookIndex = -1;
        switch (book) {
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
        assertEquals(1, catalogue.getBook(bookIndex).getAvailablity());
    }


}
