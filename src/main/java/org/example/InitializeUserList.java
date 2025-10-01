package org.example;

public class InitializeUserList {

    UserList userList = new UserList();

    public UserList initializeUserList(){
        userList.addUser(new User("MrDavidman", "1234!password"));
        userList.addUser(new User("xxx_edgelord", "password@11"));
        userList.addUser(new User("ReaderMan", "blah12345*"));
        return userList;
    }
}
