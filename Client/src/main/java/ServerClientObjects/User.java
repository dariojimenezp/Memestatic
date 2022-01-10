package ServerClientObjects;

import java.util.ArrayList;

public class User {

    private String username;
    private String passwordHash;
    private String ProfilePictureName;
    private ArrayList<String> PostHistory;
    private ArrayList<String> Activity;

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }
}
