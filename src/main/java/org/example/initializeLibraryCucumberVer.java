package org.example;

public class initializeLibraryCucumberVer {

    Catalogue catalogue = new Catalogue();

    public Catalogue initializeLibraryCucumberVer(){
        catalogue.addBook(new Book("The Great Gatsby", "Dennis Bartholomew III"));
        catalogue.addBook(new Book("To Kill a Mockingbird", "Birdy McBorderman"));
        catalogue.addBook(new Book("1984", "Ann Alkelhoul Adick"));
        catalogue.addBook(new Book("Pride and Prejudice", "Birdy McBorderman"));
        catalogue.addBook(new Book("The Hobbit", "Alvin Evil"));
        catalogue.addBook(new Book("Harry Potter", "Bill Adams & Steven Lisberger"));
        catalogue.addBook(new Book("The Catcher in the Rye", "Sam Flynn"));
        catalogue.addBook(new Book("Animal Farm", "Sir Tophattington of Florence the III"));
        catalogue.addBook(new Book("Lord of the Flies", "Lisa Lizard"));
        catalogue.addBook(new Book("Jane Eyre", "Stephan Queen"));
        catalogue.addBook(new Book("Wuthering Heights", "Mr. Clean"));
        catalogue.addBook(new Book("Moby Dick", "Imogen Heap"));
        catalogue.addBook(new Book("The Odyssey", "Jason D"));
        catalogue.addBook(new Book("Hamlet", "alisha McHomewrecker"));
        catalogue.addBook(new Book("War and Peace", "Ieym Phatt"));
        catalogue.addBook(new Book("The Divine Comedy", "A.R Jr. Mcdavid"));
        catalogue.addBook(new Book("Crime and Punishment", "Thomas D.A. Tank"));
        catalogue.addBook(new Book("Don Quixote", "Eugine McGullible"));
        catalogue.addBook(new Book("The Iliad", "G. Grey"));
        catalogue.addBook(new Book("Ulysses", "Lord Sean Benjamin XII"));
        return catalogue;
    }
}
