package ClientServerObjects;

import java.util.ArrayList;
import java.util.Arrays;

import Hashing.Hashing;

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
