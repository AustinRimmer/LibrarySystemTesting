let currentUser = null;

// Login functionality
if (window.location.pathname === '/' || window.location.pathname === '/index.html') {
    document.getElementById('loginForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        try {
            const response = await fetch('/api/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });

            const result = await response.json();

            if (result.success) {
                window.location.href = '/dashboard';
            } else {
                document.getElementById('error').textContent = result.message || 'Login failed';
            }
        } catch (error) {
            document.getElementById('error').textContent = 'Login error: ' + error.message;
        }
    });
}

// Dashboard functionality
if (window.location.pathname === '/dashboard') {
    document.addEventListener('DOMContentLoaded', async () => {
        await loadDashboard();
        setInterval(loadDashboard, 5000); // Refresh every 5 seconds
    });
}

async function loadDashboard() {
    try {
        // Load user info and books
        const [userResponse, booksResponse] = await Promise.all([
            fetch('/api/user/books'),
            fetch('/api/books')
        ]);

        if (!userResponse.ok) {
            window.location.href = '/';
            return;
        }

        let borrowedBooks = await userResponse.json();

        // Ensure it's always an array - FIXED
        if (!Array.isArray(borrowedBooks)) {
            console.warn('borrowedBooks is not an array, converting:', borrowedBooks);
            borrowedBooks = [];
        }

        const allBooks = await booksResponse.json();

        // Display borrowed books
        const borrowedTbody = document.getElementById('borrowedBooks').querySelector('tbody');
        borrowedTbody.innerHTML = '';

        if (borrowedBooks.length === 0) {
            const row = borrowedTbody.insertRow();
            const cell = row.insertCell(0);
            cell.colSpan = 4;
            cell.textContent = 'No books borrowed';
            cell.style.textAlign = 'center';
            cell.style.color = '#6c757d';
        } else {
            borrowedBooks.forEach((book, index) => {
                const row = borrowedTbody.insertRow();
                row.insertCell(0).textContent = book.title;
                row.insertCell(1).textContent = book.author;
                row.insertCell(2).textContent = book.dueDate;

                const actionCell = row.insertCell(3);
                const returnBtn = document.createElement('button');
                returnBtn.textContent = 'Return Book';
                returnBtn.className = 'btn-warning';
                returnBtn.onclick = () => returnBook(index);
                actionCell.appendChild(returnBtn);
            });
        }

        document.getElementById('borrowedCount').textContent = borrowedBooks.length;

        // Display all books
        const allBooksTbody = document.getElementById('allBooks').querySelector('tbody');
        allBooksTbody.innerHTML = '';

        allBooks.forEach((book, index) => {
            const row = allBooksTbody.insertRow();
            row.insertCell(0).textContent = book.title;
            row.insertCell(1).textContent = book.author;

            const statusCell = row.insertCell(2);
            let statusText = '';
            let statusClass = '';

            if (book.availability === 1) {
                statusText = 'Available';
                statusClass = 'available';
            } else if (book.availability === 0) {
                statusText = 'Checked Out';
                statusClass = 'checked-out';
            } else {
                statusText = 'On Hold';
                statusClass = 'on-hold';
            }

            statusCell.textContent = statusText;
            statusCell.className = statusClass;

            const holdersCell = row.insertCell(3);
            holdersCell.textContent = book.holders > 0 ? `${book.holders} holder(s)` : 'No holders';

            const actionCell = row.insertCell(4);
            const borrowBtn = document.createElement('button');

            // FIX: Check if user has 3 books borrowed - show "Place Hold" instead of "Borrow"
            const hasMaxBooks = borrowedBooks.length >= 3;

            if (book.availability === 1 && !hasMaxBooks) {
                borrowBtn.textContent = 'Borrow';
                borrowBtn.className = 'btn-success';
            } else {
                borrowBtn.textContent = 'Place Hold';
                borrowBtn.className = 'btn-primary';
            }

            borrowBtn.onclick = () => borrowBook(index);
            actionCell.appendChild(borrowBtn);
        });

        // Load notifications
        await loadNotifications();

    } catch (error) {
        console.error('Dashboard load error:', error);
        showMessage('Error loading dashboard: ' + error.message, 'error');
    }
}

async function borrowBook(bookId) {
    try {
        const response = await fetch(`/api/books/${bookId}/borrow`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });

        const result = await response.json();
        showMessage(result.message, result.success ? 'success' : 'error');
        await loadDashboard();
    } catch (error) {
        showMessage('Error borrowing book: ' + error.message, 'error');
    }
}

async function returnBook(bookIndex) {
    try {
        const response = await fetch(`/api/books/${bookIndex}/return`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });

        const result = await response.json();
        showMessage(result.message, result.success ? 'success' : 'error');
        await loadDashboard();
    } catch (error) {
        showMessage('Error returning book: ' + error.message, 'error');
    }
}

function showMessage(message, type) {
    const messageDiv = document.getElementById('message');
    messageDiv.textContent = message;
    messageDiv.className = type;
    setTimeout(() => {
        messageDiv.textContent = '';
        messageDiv.className = '';
    }, 5000);
}

async function logout() {
    try {
        await fetch('/api/logout', { method: 'POST' });
        window.location.href = '/';
    } catch (error) {
        console.error('Logout error:', error);
    }
}