package org.example;
import java.util.ArrayList;
public class UserList {
    ArrayList<User> userList;

    public UserList(){
        userList = new ArrayList<User>();
    }

    public void addUser(User user){

        userList.add(user);
    }

    User getUser(int index){
        return userList.get(index);
    }

    public int getNumberOfUsers(){return 0;}


}
