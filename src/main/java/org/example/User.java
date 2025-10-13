package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class User {
    String username;
    String password;
    ArrayList<Book> booksOnHold     = new ArrayList<>();
    ArrayList<Book> borrowedBooks   = new ArrayList<>();

    User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){return username;}

    public String getPassword(){return password;}

    public ArrayList<Book> getBooksOnHold() {return booksOnHold;}

    public int getNumberOfBorrowedBooks() {
        if (borrowedBooks.isEmpty()) {
            return 0;
        } else {
            return borrowedBooks.size();
        }
    }

    public Book getBorrowedBook(int i){return borrowedBooks.get(i);}

    public void addBookToOnHoldBooks(Book book){
        booksOnHold.add(book);
        book.addUserToHoldQueue(this.username);
    }
    public int borrowBook(Book book, Scanner userInput){
        int validBorrow = Validator.validateBorrow(book, this, userInput);
        if(validBorrow == 1){
            borrowedBooks.add(book);
            book.setAvailablity(0);
            book.calculateDueDate();
            book.removeFromHoldQueue(this);
        }
        if(validBorrow == 0){
            addBookToOnHoldBooks(book);
        }
        //def gonna need other stuff for disp later
        return validBorrow;
    }
    public void returnBook(Book book){
        this.borrowedBooks.remove(book);
        if(book.getNumberOfHolders() == 0){
            book.setAvailablity(1);
            book.resetDueDate();
        }
        else if(book.getNumberOfHolders() != 0){
            book.setAvailablity(-1);
            book.resetDueDate();
            //ignoring UC here since it contradicts itself constantly, it will be set to on Hold, and the user first in hold queue can borrow it
        }
    }
    //if returns -2 it means user has no holds
    public ArrayList<Integer> getHeldBookAvailability(){
        ArrayList<Integer> heldBookAvailabilities = new ArrayList<>();
        if(booksOnHold.isEmpty()){
            heldBookAvailabilities.add(-2);
            //-2 meaning no holds
            return heldBookAvailabilities;
        }
        for(int i = 0; i < booksOnHold.size(); i ++){
            if(booksOnHold.get(i).getAvailablity() == -1 && booksOnHold.get(i).getHolder(0).equals(this.username)){
                heldBookAvailabilities.add(1);
            }
            else{
                heldBookAvailabilities.add(booksOnHold.get(i).getAvailablity());
            }
        }
        return heldBookAvailabilities;
    }

}
