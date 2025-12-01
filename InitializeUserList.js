const User = require('./User');
const UserList = require('./UserList');

class InitializeUserList {
    constructor() {
        this.userList = new UserList();
    }

    initializeUserList() {
        this.userList.addUser(new User("alice", "pass123"));
        this.userList.addUser(new User("bob", "pass456"));
        this.userList.addUser(new User("charlie", "pass789"));
        return this.userList;
    }
}

module.exports = InitializeUserList;