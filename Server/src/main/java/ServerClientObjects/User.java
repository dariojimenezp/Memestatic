package ServerClientObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class User implements Serializable {

    private String username;
    private String passwordHash;
    private String ProfilePictureName;
    private ArrayList<String> PostHistory;
    private HashSet<String> ratedPosts;

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.PostHistory = new ArrayList<String>();
        this.ratedPosts = new HashSet<String>();
    }

    public void addPublishedPost(String itemID){
        this.PostHistory.add(itemID);
    }

    public void addRatePost(String itemID){
        this.ratedPosts.add(itemID);
    }

    /** Getters **/
    public String getUsername() {
        return username;
    }

    public ArrayList<String> getPostHistory() {
        return PostHistory;
    }

    public HashSet<String> getRatedPosts() {
        return ratedPosts;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getProfilePictureName() {
        return ProfilePictureName;
    }

    /** Setters **/
    public void setUsername(String username) {
        this.username = username;
    }


    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }


    public void setProfilePictureName(String profilePictureName) {
        ProfilePictureName = profilePictureName;
    }

    public Boolean hasRatedPost(String itemID){
        return ratedPosts.contains(itemID);
    }
}
