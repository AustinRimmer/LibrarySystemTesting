package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;


public class Book {
    String title;
    String author;
    int availablity = 1;
    String dueDate = "";
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
        holdQueue.add(username);
    }

    public String getHolder(int i) {
        return holdQueue.get(i);
    }


    public void setAvailablity(int i){availablity = i;}

    public int getAvailablity(){return availablity;}

    public void calculateDueDate(){
        dueDate = "NOT IMPLEMENTED";
    }

    public String getDueDate() {
        return dueDate;
    }
}