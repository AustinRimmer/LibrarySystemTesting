class Book {
    constructor(title, author) {
        this.title = title;
        this.author = author;
        this.availability = 1; // 1=available, 0=checked out, -1=on hold
        this.dueDate = "NOT CHECKED OUT";
        this.holdQueue = [];
    }

    getTitle() { return this.title; }
    getAuthor() { return this.author; }
    getAvailability() { return this.availability; }
    getDueDate() { return this.dueDate; }
    getNumberOfHolders() { return this.holdQueue.length; }

    getHolder(i) {
        if (this.holdQueue.length === 0) {
            return "NO HOLDERS";
        } else {
            return this.holdQueue[i];
        }
    }

    setAvailability(i) { this.availability = i; }

    addUserToHoldQueue(username) {
        this.holdQueue.push(username);
    }

    calculateDueDate() {
        const due = new Date();
        due.setDate(due.getDate() + 14);
        this.dueDate = due.toISOString().split('T')[0];
    }

    resetDueDate() {
        this.dueDate = "NOT CHECKED OUT";
    }

    removeFromHoldQueue(username) {
        const index = this.holdQueue.indexOf(username);
        if (index > -1) {
            this.holdQueue.splice(index, 1);
        }
    }
}

module.exports = Book;