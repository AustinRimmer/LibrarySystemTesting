package org.example;

public class InitializeLibrary {

    Catalogue catalogue = new Catalogue();

    public Catalogue initializeLibrary(){
        catalogue.addBook(new Book("The Miscellaneous Mis-adventures of Captain Borqueefious", "Dennis Bartholomew III"));
        catalogue.addBook(new Book("How to train Pigeons: A guide to flight", "Birdy McBorderman"));
        catalogue.addBook(new Book("Day Drinking: How to do it & How not to stop", "Ann Alkelhoul Adick"));
        catalogue.addBook(new Book("Sacrificial Birds and how to raise them", "Birdy McBorderman"));
        catalogue.addBook(new Book("Summoning Rituals: Beginner Edition", "Alvin Evil"));
        catalogue.addBook(new Book("How to program Tron & Make it a movie", "Bill Adams & Steven Lisberger"));
        catalogue.addBook(new Book("How to enter old arcade machines", "Sam Flynn"));
        catalogue.addBook(new Book("The cunning and witty emaculations of sophisticated Gentlemen", "Sir Tophattington of Florence the III"));
        catalogue.addBook(new Book("How to train dragons: a guide to owning house geckos", "Lisa Lizard"));
        catalogue.addBook(new Book("The Murderizing Mutilator Strikes back", "Stephan Queen"));
        catalogue.addBook(new Book("A sudsy and cleaning story", "Mr. Clean"));
        catalogue.addBook(new Book("Trains and sewing machines", "Imogen Heap"));
        catalogue.addBook(new Book("How to sample someone else's song", "Jason D"));
        catalogue.addBook(new Book("Burning Bridges: a guide to messy relationships", "alisha McHomewrecker"));
        catalogue.addBook(new Book("10 dos and donts of eating burritos", "Ieym Phatt"));
        catalogue.addBook(new Book("A Guide on Exterminating Gnomes", "A.R Jr. Mcdavid"));
        catalogue.addBook(new Book("Trains and their inner workings", "Thomas D.A. Tank"));
        catalogue.addBook(new Book("How to download RAM", "Eugine McGullible"));
        catalogue.addBook(new Book("Wizard Spells for beginners: pyrokinesis edition", "G. Grey"));
        catalogue.addBook(new Book("How to code in scheme", "Lord Sean Benjamin XII"));
        return catalogue;
    }
}
