package consoleApp;

import java.util.ArrayList;
import java.util.List;

// i made this a singleton class, there is no need for more than 1 entity that manages accounts
// and the list of application users belongs here, it is most important here
// maybe the names consoleApp.AccountManager and applicationUsers sound bad, almost like they re not related
// maybe this is UserManager and we can have CommentManager and PostManager

public class AccountManager {
    private List<User> applicationUsers;

    //private constructor for singleton design pattern
    private AccountManager() {
        this.applicationUsers = new ArrayList<>();
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

    //public User accountCreation(String username, String password, String description){
        //User user = User(username, password, description);
        //validate username and password
            //applicationUsers.add(user);
        //}
    //}
}
//account_creation: receives (username, password, description) calls user constructor, if user constructor successful, add user to list of application users, returns created user or null