package org.example;

import java.util.ArrayList;

public class User {
    String username;
    String password;
    ArrayList<Book> booksOnHold     = new ArrayList<>();

    User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){return username;}

    public String getPassword(){return password;}

    public void addBookToOnHoldBooks(Book book){
        booksOnHold.add(book);
        book.addUserToHoldQueue(this.username);
    }
    public ArrayList<Book> getBooksOnHold() {
        return booksOnHold;
    }

}
