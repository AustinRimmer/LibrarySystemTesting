//file server
const express = require('express');
const path = require('path');
const app = express();
const PORT = 3000;

// Import your business logic classes
const InitializeLibraryCucumberVer = require('./InitializeLibraryCucumberVer');
const InitializeUserList = require('./InitializeUserList');
const User = require('./User');

let libraryInitializer = new InitializeLibraryCucumberVer();  // CHANGE TO 'let'
let userListInitializer = new InitializeUserList();           // CHANGE TO 'let'
let catalogue = libraryInitializer.initializeLibraryCucumberVer();  // 'let'
let userList = userListInitializer.initializeUserList();            // 'let'
let currentUser = null;

// Middleware
app.use(express.json());
app.use(express.static('public'));
app.use(express.urlencoded({ extended: true }));

// Routes
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'login.html'));
});

app.get('/dashboard', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'dashboard.html'));
});

// API Routes
app.post('/api/login', (req, res) => {
    const { username, password } = req.body;

    // Find user
    let user = null;
    for (let i = 0; i < userList.getNumberOfUsers(); i++) {
        const u = userList.getUser(i);
        if (u.getUsername() === username) {
            user = u;
            break;
        }
    }

    if (user && user.getPassword() === password) {
        currentUser = user;
        res.json({ success: true, user: { username: user.getUsername() } });
    } else {
        res.json({ success: false, message: "Invalid credentials" });
    }
});

app.post('/api/logout', (req, res) => {
    currentUser = null;
    res.json({ success: true });
});

app.get('/api/books', (req, res) => {
    const books = [];
    for (let i = 0; i < catalogue.getCatalogueSize(); i++) {
        const book = catalogue.getBook(i);

        // SIMPLE FIX: Access holdQueue directly
        let firstHolder = null;
        if (book.holdQueue && book.holdQueue.length > 0) {
            firstHolder = book.holdQueue[0];
        }

        const bookData = {
            id: i,
            title: book.getTitle(),
            author: book.getAuthor(),
            availability: book.getAvailability(),
            dueDate: book.getDueDate(),
            holders: book.getNumberOfHolders(),
            firstHolder: firstHolder
        };
        books.push(bookData);
    }
    res.json(books);
});
app.post('/api/books/:id/borrow', (req, res) => {
    if (!currentUser) {
        return res.json({ success: false, message: "Not logged in" });
    }

    const bookId = parseInt(req.params.id);
    const book = catalogue.getBook(bookId);

    const result = currentUser.borrowBook(book);

    if (result === 1) {
        res.json({ success: true, message: "Book borrowed successfully" });
    } else if (result === 0) {
        res.json({ success: true, message: "Added to hold queue" });
    } else {
        res.json({ success: false, message: "Cannot borrow or place hold" });
    }
});

app.post('/api/books/:id/return', (req, res) => {
    if (!currentUser) {
        return res.json({ success: false, message: "Not logged in" });
    }

    const bookId = parseInt(req.params.id);
    const book = currentUser.getBorrowedBook(bookId);

    if (book) {
        currentUser.returnBook(book);
        res.json({ success: true, message: "Book returned successfully" });
    } else {
        res.json({ success: false, message: "Book not found" });
    }
});

app.get('/api/user/books', (req, res) => {
    if (!currentUser) {
        return res.json([]); // Return empty array if not logged in
    }

    const borrowedBooks = [];
    for (let i = 0; i < currentUser.getNumberOfBorrowedBooks(); i++) {
        const book = currentUser.getBorrowedBook(i);
        borrowedBooks.push({
            id: i,
            title: book.getTitle(),
            author: book.getAuthor(),
            dueDate: book.getDueDate()
        });
    }

    // Return as array only - FIXED
    res.json(borrowedBooks);
});

app.get('/api/user/holds', (req, res) => {
    if (!currentUser) {
        return res.json([]); // Return empty array if not logged in
    }

    const holds = currentUser.getHeldBookAvailability();
    res.json(holds);
});

app.get('/api/user/current', (req, res) => {
    if (!currentUser) {
        return res.json({ username: null });
    }
    res.json({
        username: currentUser.getUsername(),
        borrowedCount: currentUser.getNumberOfBorrowedBooks()
    });
});


// Reset endpoint for Cypress tests
app.post('/api/reset', (req, res) => {
    // COMPLETELY reinitialize everything
    libraryInitializer = new InitializeLibraryCucumberVer();  // NEW instance
    userListInitializer = new InitializeUserList();           // NEW instance

    catalogue = libraryInitializer.initializeLibraryCucumberVer();
    userList = userListInitializer.initializeUserList();
    currentUser = null;
    res.json({ success: true, message: "System reset" });
});


app.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
});
