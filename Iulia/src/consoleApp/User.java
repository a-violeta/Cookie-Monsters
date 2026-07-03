package consoleApp;

public class User {
    private String username;
    private String password;
    private String description;
    // missing: userId, creationDate

    // also missing the constructor with parameters that assigns userId and creationDate
    // the validations for username and password can be made in AccountManager class
    // and for now that s how I did it

    public String getDescription() {
        return description;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
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
        return super.toString();
    }
}
