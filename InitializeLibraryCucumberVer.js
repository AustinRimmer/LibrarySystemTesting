const Book = require('./Book');
const Catalogue = require('./Catalogue');

class InitializeLibraryCucumberVer {
    constructor() {
        this.catalogue = new Catalogue();
    }

    initializeLibraryCucumberVer() {
        this.catalogue.addBook(new Book("The Great Gatsby", "Dennis Bartholomew III"));
        this.catalogue.addBook(new Book("To Kill a Mockingbird", "Birdy McBorderman"));
        this.catalogue.addBook(new Book("1984", "Ann Alkelhoul Adick"));
        this.catalogue.addBook(new Book("Pride and Prejudice", "Birdy McBorderman"));
        this.catalogue.addBook(new Book("The Hobbit", "Alvin Evil"));
        this.catalogue.addBook(new Book("Harry Potter", "Bill Adams & Steven Lisberger"));
        this.catalogue.addBook(new Book("The Catcher in the Rye", "Sam Flynn"));
        this.catalogue.addBook(new Book("Animal Farm", "Sir Tophattington of Florence the III"));
        this.catalogue.addBook(new Book("Lord of the Flies", "Lisa Lizard"));
        this.catalogue.addBook(new Book("Jane Eyre", "Stephan Queen"));
        this.catalogue.addBook(new Book("Wuthering Heights", "Mr. Clean"));
        this.catalogue.addBook(new Book("Moby Dick", "Imogen Heap"));
        this.catalogue.addBook(new Book("The Odyssey", "Jason D"));
        this.catalogue.addBook(new Book("Hamlet", "alisha McHomewrecker"));
        this.catalogue.addBook(new Book("War and Peace", "Ieym Phatt"));
        this.catalogue.addBook(new Book("The Divine Comedy", "A.R Jr. Mcdavid"));
        this.catalogue.addBook(new Book("Crime and Punishment", "Thomas D.A. Tank"));
        this.catalogue.addBook(new Book("Don Quixote", "Eugine McGullible"));
        this.catalogue.addBook(new Book("The Iliad", "G. Grey"));
        this.catalogue.addBook(new Book("Ulysses", "Lord Sean Benjamin XII"));
        return this.catalogue;
    }
}

module.exports = InitializeLibraryCucumberVer;