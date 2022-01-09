package ServerClientObjects;

import java.util.ArrayList;
import java.util.Arrays;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getProfilePictureName() {
        return ProfilePictureName;
    }

    public void setProfilePictureName(String profilePictureName) {
        ProfilePictureName = profilePictureName;
    }
}
