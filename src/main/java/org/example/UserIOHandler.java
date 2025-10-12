package org.example;
import java.util.ArrayList;
import java.util.Scanner;

public class UserIOHandler {
    Scanner inputScanner;
    UserList userList;
    public UserIOHandler(Scanner userInput, UserList userList){
        this.inputScanner = userInput;
        this.userList = userList;
    }
    public UserIOHandler(Scanner userInput){
        this.inputScanner = userInput;
    }
    public User getUserLogin(Scanner usrIn){
        String username = reqUsername(usrIn);
        String password = reqPassword(usrIn);
        return new User(username, password);
    }
    public String reqUsername(Scanner usrIn){
        System.out.println("Enter username:");
        String username = usrIn.nextLine();
        return username;
    }
    public String reqHoldChoice(Scanner usrIn){
        String choice = usrIn.nextLine();
        return choice;
    }
    public String reqPassword(Scanner usrIn){
        System.out.println("Enter password");
        String password = usrIn.nextLine();
        return password;
    }
    public void dispUserHolds(User user){
        ArrayList<Integer> userBookAvails = user.getHeldBookAvailability();
        if(userBookAvails.get(0) == -3){
            System.out.println("No User Holds");
        }
        for(int i = 0; i < userBookAvails.size(); i++){
            if(userBookAvails.get(i) == 1 || (userBookAvails.get(i) == -1 && user.getUsername().equals(user.getBooksOnHold().get(i).getHolder(0))) ){
                System.out.println("<=====------<NOTIFICATION>------=====>");
                System.out.println("The book: " + user.getBooksOnHold().get(i).getTitle() +"\n" + "By: " + user.getBooksOnHold().get(i).getAuthor() + "\nIs now available...");
            }
        }
    }
    //-1 for no books borrowed
    //1 for books borrowed
    public int dispUserBorrows(User user) {
        if(user.getNumberOfBorrowedBooks() == 0){
            System.out.println("You Have No Books Borrowed");
            return -1;
        }
        System.out.println("_------:=|{[BORROWED BOOKS]}|=:------_");
        for(int i = 0; i < user.getNumberOfBorrowedBooks(); i ++){
            Book book = user.getBorrowedBook(i);
            System.out.println("(" + (i + 1) + ")");
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Due Date: " + book.getDueDate());
            System.out.println("<=====------------<>------------=====>");
        }
        return 1;
    }
    public String reqSystemOperation(Scanner usrIn){
        System.out.println("-----:=|{[SYSTEM OPERATIONS]}|=:------");
        System.out.println("(1) borrow a book");
        System.out.println("(2) return a book");
        System.out.println("(3) logout");
        System.out.println("<=====------------<>------------=====>");
        return usrIn.nextLine();
    }
    public void dispNumBorrowedBooks(User user){
        System.out.println("[NUMBER OF BORROWED BOOKS " + user.getNumberOfBorrowedBooks() +"]");
    }

    public void dispAllBooks(Catalogue catalogue, User user) {
        String availabilityString = "";
        System.out.println("");
        System.out.println("_------:=|{[BOOK SELECTION]}|=:------_");
        for (int i = 0; i < catalogue.getCatalogueSize(); i++) {
            System.out.println("(" + (i + 1) + ")");
            System.out.println("Title: " + catalogue.getBook(i).getTitle());
            System.out.println("Author: " + catalogue.getBook(i).getAuthor());

            if (catalogue.getBook(i).getAvailablity() == 1) {
                availabilityString = "Available";
            }
            if (catalogue.getBook(i).getAvailablity() == 0) {
                availabilityString = "Checked Out";
            }
            if (catalogue.getBook(i).getAvailablity() == -1) {
                availabilityString = "On Hold";
            }

            System.out.println("Status: {" + availabilityString + "}");
            System.out.println("<=====------------<>------------=====>");
        }
    }
    public void dispBook(Book book){
        String availabilityString = "";
        System.out.println("_-------:=|{[BOOK DETAILS]}|=:-------_");
        System.out.println("Title: " + book.getTitle());
        System.out.println("Author: " + book.getAuthor());

        if (book.getAvailablity() == 1) {
            availabilityString = "Available";
        }
        if (book.getAvailablity() == 0) {
            availabilityString = "Checked Out";
        }
        if (book.getAvailablity() == -1) {
            availabilityString = "On Hold";
        }
        System.out.println("Status: {" + availabilityString + "}");
        System.out.println("<=====------------<>------------=====>");
    }
    public int reqBorrowConfirm(Scanner usrIn){
        System.out.println("Confirm Borrow (Y/N)");
        String resp = usrIn.nextLine();
        if(resp.equals("Y")){
            return 1;
        }
        return -1;
    }
    public int getBorrowSelection(Scanner usrIn){
        System.out.println("");
        System.out.println("What book would you like to borrow?");
        if(!usrIn.hasNextLine()) {
            return -1;
        }
        String userBorrowSelection = usrIn.nextLine();
        if(userBorrowSelection.matches("^([1-9]|1[0-9]|20)$")){
            return Integer.parseInt(userBorrowSelection);
        }
        else{
            //for errors
            return -1;
        }
    }
    public int getReturnSelection(Scanner usrIn, User user){
        System.out.println("");
        System.out.println("What book would you like to return?");
        if(!usrIn.hasNextLine()) {
            return -1;
        }
        String userHoldSelection = usrIn.nextLine();
        if(Integer.parseInt(userHoldSelection) >= 1 && Integer.parseInt(userHoldSelection) <= user.getNumberOfBorrowedBooks()){
            return Integer.parseInt(userHoldSelection);
        }
        else{
            //for errors
            return -1;
        }
    }
    public int reqReturnConfirm(Scanner usrIn){
        System.out.println("Confirm Return (Y/N)");
        String resp = usrIn.nextLine();
        if(resp.equals("Y")){
            return 1;
        }
        return -1;
    }
    public void dispLogOutConfirm(){
        System.out.println("");
    }




}