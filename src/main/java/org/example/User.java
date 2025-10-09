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
