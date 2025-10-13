package org.example;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Book {
    String title;
    String author;
    // 1 = available
    // 0 = checked out
    //-1 = on hold
    int availablity                 = 1;
    String dueDate                  = "NOT CHECKED OUT";
    LinkedList<String> holdQueue    = new LinkedList<String>();

    Book(String title, String author){
        this.title  = title;
        this.author = author;
    }

    //getters and setters
    public String getTitle(){return title;}

    public String getAuthor(){return author;}

    public String getHolder(int i) {
        if (holdQueue.isEmpty()) {
            return "NO HOLDERS";
        } else {
            return holdQueue.get(i);
        }
    }

    public int getNumberOfHolders() {return holdQueue.size();}

    public int getAvailablity(){return availablity;}

    public String getDueDate() {return dueDate;}

    public void setAvailablity(int i){availablity = i;}

    public void addUserToHoldQueue(String username){
        holdQueue.add(username);
    }

    public void calculateDueDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime due = currentDateTime.plusDays(14);
        dueDate = due.format(formatter);
    }
    public void resetDueDate(){
        dueDate = "NOT CHECKED OUT";
    }
    public void removeFromHoldQueue(User user){
        holdQueue.remove(user.getUsername());
    }

}