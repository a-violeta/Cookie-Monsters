package consoleApp;

public class User {

    private static long idIncrementor = 0;
    // it s static so that all Users objects have the same idIncrementor
    // and the static method incrementId() is called in the constructors to give a 'bigger' id everytime
    // so ids are unique
    // so the ids returned will be 1 for the first User created, then 2, 3...

    private long userId;
    private String username;
    private String password;
    private String description;
    // missing: creationDate

    // the validations for username and password can be made in AccountManager class
    // and for now that s how I did it

    private static long incrementId(){
        idIncrementor++;
        return idIncrementor;
    }

    User(){
        this.userId = incrementId();
        this.username="";
        this.password="";
        this.description="";
    }

    User(String username, String password, String description){
        this.userId = incrementId();
        this.username=username;
        this.password=password;
        this.description=description;
    }

    public String getDescription() {
        return description;
    }
    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
    public long getUserId() {
        return userId;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
