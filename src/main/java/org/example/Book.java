package org.example;

import java.util.LinkedList;

public class Book {
    String title;
    String author;
    int available = 1;
    // 1 = available
    // 0 = checked out
    //-1 = on hold
    LinkedList<String> holdQueue = new LinkedList<String>();
    Book(String title, String author){
        this.title = title;
        this.author = author;
    }
    public String getTitle(){return title;}
    public String getAuthor(){return author;}

    public void addUserToHoldQueue(String username){
        holdQueue.add("default");
    }

    public String getHolder(int i) {
        return holdQueue.get(i);
    }
}