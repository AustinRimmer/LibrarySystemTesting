class UserList {
    constructor() {
        this.userList = [];
    }

    addUser(user) {
        this.userList.push(user);
    }

    getUser(index) {
        return this.userList[index];
    }

    getNumberOfUsers() {
        return this.userList.length;
    }
}

module.exports = UserList;