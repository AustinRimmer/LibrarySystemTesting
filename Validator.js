class Validator {
    static validateUsername(username, userList) {
        for (let i = 0; i < userList.getNumberOfUsers(); i++) {
            if (username === userList.getUser(i).getUsername()) {
                return false;
            }
        }
        if (username.length > 12) {
            return false;
        }
        return /^[a-zA-Z]+$/.test(username);
    }

    static validatePassword(password) {
        let hasLetter = false;
        let hasNumber = false;

        if (password.length < 5) {
            return false;
        }

        for (let char of password) {
            if (/[a-zA-Z]/.test(char)) {
                hasLetter = true;
            } else if (/\d/.test(char)) {
                hasNumber = true;
            }
        }

        return hasLetter && hasNumber;
    }

    static validateUserOperationChoice(userInput) {
        return userInput === "1" || userInput === "2" || userInput === "3";
    }

    //1     = can borrow
    //0     = can place hold
    //-1    = cant place hold
    static validateBorrow(book, user) {
        let alreadyHolding = false;
        let alreadyBorrowed = false;

        for (let i = 0; i < book.getNumberOfHolders(); i++) {
            if (book.getHolder(i) === user.getUsername()) {
                alreadyHolding = true;
            }
        }
        for (let i = 0; i < user.getNumberOfBorrowedBooks(); i++) {
            if (user.getBorrowedBook(i) === book) {
                alreadyBorrowed = true;
            }
        }
        if (alreadyBorrowed) {
            return -1;
        }
        //can borrow avail books when avail and not 3 already borrowed
        if (book.getAvailability() === 1 && user.getNumberOfBorrowedBooks() < 3) {
            return 1;
        }
        //if book was returned and current user is first in queue they are able to check it out
        if (book.getAvailability() === -1 && book.getHolder(0) === user.getUsername()) {
            book.setAvailability(1);
            return 1;
        }
        else if (book.getAvailability() === -1 && book.getHolder(0) !== user.getUsername()) {
            //ask if they want to place a hold
            return 0;
        }
        //cases where no hold is possible
        if (alreadyHolding) {
            return -1;
        }

        if (book.getAvailability() === -1) {
            return 0;
        }

        if (book.getAvailability() === 0) {
            return 0;
        }

        //never able to borrow when held books = 3
        if (user.getNumberOfBorrowedBooks() === 3) {
            return 0;
        }
        return -10;
    }
}

module.exports = Validator;