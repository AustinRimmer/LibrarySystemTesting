const Validator = require('./Validator');

class User {
    constructor(username, password) {
        this.username = username;
        this.password = password;
        this.booksOnHold = [];
        this.borrowedBooks = [];
    }

    getUsername() { return this.username; }
    getPassword() { return this.password; }
    getBooksOnHold() { return this.booksOnHold; }

    getNumberOfBorrowedBooks() {
        return this.borrowedBooks.length;
    }

    getBorrowedBook(i) {
        return this.borrowedBooks[i];
    }

    addBookToOnHoldBooks(book) {
        this.booksOnHold.push(book);
        book.addUserToHoldQueue(this.username);
    }

    borrowBook(book) {
        const validBorrow = Validator.validateBorrow(book, this);

        if (validBorrow === 1) {
            this.borrowedBooks.push(book);
            book.setAvailability(0);
            book.calculateDueDate();
            book.removeFromHoldQueue(this.username);
            return 1;
        }
        if (validBorrow === 0) {
            this.addBookToOnHoldBooks(book);
        }
        //def gonna need other stuff for disp later
        return validBorrow;
    }

    returnBook(book) {
        const index = this.borrowedBooks.indexOf(book);
        if (index > -1) {
            this.borrowedBooks.splice(index, 1);
        }

        if (book.getNumberOfHolders() === 0) {
            book.setAvailability(1);
            book.resetDueDate();
        } else if (book.getNumberOfHolders() !== 0) {
            book.setAvailability(-1);
            book.resetDueDate();
        }
    }

    getHeldBookAvailability() {
        const heldBookAvailabilities = [];
        if (this.booksOnHold.length === 0) {
            heldBookAvailabilities.push(-2);
            return heldBookAvailabilities;
        }

        for (let i = 0; i < this.booksOnHold.length; i++) {
            if (this.booksOnHold[i].getAvailability() === -1 &&
                this.booksOnHold[i].getHolder(0) === this.username) {
                heldBookAvailabilities.push(1);
            } else {
                heldBookAvailabilities.push(this.booksOnHold[i].getAvailability());
            }
        }
        return heldBookAvailabilities;
    }
}

module.exports = User;