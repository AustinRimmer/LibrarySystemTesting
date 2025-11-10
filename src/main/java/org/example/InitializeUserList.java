package org.example;

public class InitializeUserList {

    UserList userList = new UserList();

    public UserList initializeUserList(){
        userList.addUser(new User("alice", "pass123"));
        userList.addUser(new User("bob", "pass456"));
        userList.addUser(new User("charlie ", "pass789"));
        return userList;
    }
}
