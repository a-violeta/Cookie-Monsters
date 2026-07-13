package consoleApp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

// i made this a singleton class, there is no need for more than 1 entity that manages accounts
// and the list of application users belongs here, it is most important here
// maybe the names consoleApp.AccountManager and applicationUsers sound bad, almost like they re not related
// maybe this is UserManager and we can have CommentManager and PostManager

// AccountManager also does logging actions
// the applicationUsers list and loggedUsers list need to be in Logger class
// but applicationUsers list also needs to be in AccountManager class
// so i ve combined the 2 classes for now

public class AccountManager {
    private List<User> applicationUsers;
    private List<User> loggedUsers;

    //private constructor for singleton design pattern
    private AccountManager() {
        this.applicationUsers = new ArrayList<>();
        this.loggedUsers = new ArrayList<>();
    }

    // holder implementation for singleton
    // so we call method getInstance() once and it spawns 1 consoleApp.AccountManager
    // if we call the method again it returns the same consoleApp.AccountManager made earlier
    private static class Holder {
        private static final AccountManager INSTANCE = new AccountManager();
    }

    public static AccountManager getInstance() {
        return Holder.INSTANCE;
    }

    public List<User> getApplicationUsers() {
        return applicationUsers;
    }
    public List<User> getLoggedUsers() {
        return loggedUsers;
    }

    // made this method void, maybe there s no need to return the user created
    public void createAccount(String username, String password, String description) {
        User user = new User(username, password, description);

        if (!user.getUsername().isEmpty()
                && !user.getPassword().isEmpty()) {

            boolean isUsernameUnique = true;

            for (User u : applicationUsers) {
                if (Objects.equals(u.getUsername(), user.getUsername())) {
                    isUsernameUnique = false;
                    System.out.println("Username taken!");
                    break;
                }
            }

            if (isUsernameUnique) {
                //validate unique username

                applicationUsers.add(user);
                System.out.println("User created successfully!");
            }
        }
        else{
            System.out.println("Username or password empty!");
        }
    }

    public void logIn(String username, String password){
        // check that the user exists
        // and take the whole user (with id and all to add to the loggedUsers list)
        boolean isExistingUser = false;
        User loggingUser = null;

        for(User u: applicationUsers){
            if(u.getUsername().equals(username)){
                isExistingUser = true;
                loggingUser = u;
                break;
            }
        }

        if(!isExistingUser){
            System.out.println("User does not exist!");
            return;
        }

        // check that the user is not already logged in
        boolean isLoggedAlready = false;
        for(User u: loggedUsers){
            if(u.getUsername().equals(username)){
                isLoggedAlready = true;
            }
        }

        if(isLoggedAlready){
            System.out.println("User is already logged in!");
            return;
        }

        // check password
        if(password.equals(loggingUser.getPassword())) {
            // log in the user
            loggedUsers.add(loggingUser);
            System.out.println("User has been logged in!");
        }
        else{
            System.out.println("Incorrect password!");
        }
    }

    public void logOut(String username){
        // check that the user exists
        // and take the whole user (with id and all to add to the loggedUsers list)
        boolean isExistingUser = false;
        User loggingUser = null;

        for(User u: applicationUsers){
            if(u.getUsername().equals(username)){
                isExistingUser = true;
                //loggingUser = u;
                break;
            }
        }

        if(!isExistingUser){
            System.out.println("User does not exist!");
            return;
        }

        // check that the user is already logged in
        boolean isLoggedAlready = false;
        for(User u: loggedUsers){
            if(u.getUsername().equals(username)){
                isLoggedAlready = true;
            }
        }

        if(!isLoggedAlready){
            System.out.println("User is not logged in!");
            return;
        }

        // log out the user

        Iterator<User> it = loggedUsers.iterator();
        // removing from list by using iterator
        while(it.hasNext()){
            User u = it.next();
            if(Objects.equals(u.getUsername(), username)){
                it.remove();
                break;
            }
        }

        System.out.println("User has been logged out!");
    }
}
