package Server;

import javax.crypto.SealedObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ServerClientObjects.ImageExplorer;
import ServerClientObjects.Post;
import ServerClientObjects.User;
import com.google.gson.*;
import Hashing.Hashing;
import Encryption.Encryption;
import org.bson.Document;


public class ClientHandler implements Runnable{

    /** fields **/

    /* encryption public key */
    private String PUBLIC_KEY = "dy1k82p08wm";


    /* socket streams */
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /* socket */
    private Socket socket;

    /* encryption for messages */
    private Encryption encryption;

    /* server */
    private Server server;

    /* database */
    private AtlasDB db;

    private Gson gson;

    /* post index */
    private int postIndex;

    /* posts */
    private List<String> posts;


    private LinkedList<String> inMsgs;

    public ClientHandler(Socket socket, Server server, AtlasDB db) {
        this.socket = socket;
        this.server = server;
        this.db = db;
        gson = new GsonBuilder().create();
        inMsgs = new LinkedList<String>();
        postIndex = 0;

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            /* check if public key matches */
            String publicKey = (String)in.readObject();
            if(!publicKey.equals(PUBLIC_KEY)) {
                socket.close();
                return;
            }

            encryption = new Encryption("AES");
            out.writeObject(encryption.getKey());

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        /* wait for client to send a message */
        while(true){
            try {

                /* wait until an integer is sent from the client indicating how many objects will be sent */
                Integer numObjects = in.readInt();

                /* perform an action depending on received messages */
                respond(numObjects);

            } catch (IOException e) {
                System.out.println("Socket closed");
                return;
            }

        }
    }

    private void respond(Integer numObjects){

        String action = null;
        try {
            action = encryption.Decrypt( (SealedObject) in.readObject(), String.class);
        }

        catch (IOException | ClassNotFoundException e ) {  e.printStackTrace(); }
        switch (action){

            case "create account":

                createAccount(getStrings(numObjects));
                break;

            case "log in":
                logIn(getStrings(numObjects));
                break;

            case "post":
                post();
                break;

            case "get posts":
                sendPosts();
                break;

            case "update rating":
                updateRating();
                break;

            default:
                System.out.println("Invalid message");
                return;
        }
    }

    private void createAccount(LinkedList<String> msgs){

        String username = msgs.pop();
        String passwordHash = msgs.pop();

        if(db.findDocument("Users", "username", username) != null){
            try {
                out.writeInt(1);
                out.writeObject(encryption.Encrypt("account not created"));
                return;
            }
            catch (IOException e) { e.printStackTrace(); }
        }

        db.addUser(username, passwordHash);

        try {
            out.writeInt(1);
            out.writeObject(encryption.Encrypt("created account"));
        }
        catch (IOException e) { e.printStackTrace(); }

    }

    private LinkedList<String> getStrings(Integer numObjects){

        LinkedList<String> strings = new LinkedList<String>();

        for (int i = 0; i < numObjects; i++) {

            try {strings.add(encryption.Decrypt( (SealedObject)in.readObject(), String.class));}

            catch (IOException | ClassNotFoundException e) { e.printStackTrace();}
        }

        return strings;
    }

    private void logIn(LinkedList<String> msgs){

        String username = msgs.pop();
        String password = msgs.pop();

        if(db.findDocument("Users", "username", username) == null || !validatePassword(username, password)) {
            try {
                out.writeInt(1);
                out.writeObject(encryption.Encrypt("not logged in"));
                return;
            }
            catch (IOException e) { e.printStackTrace(); }
        }

        try {
            out.writeInt(1);
            out.writeObject(encryption.Encrypt("logged in"));
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    private Boolean validatePassword(String username, String password){

        String userJSON = db.findDocument("Users", "username", username);
        User user = gson.fromJson(userJSON, User.class);

        return Hashing.PBKDF2HashMatch(password, user.getPasswordHash());

    }

    private void post(){

        /* get image ID */
        String id = db.getImageID();

        /* add post */
        Post post = null;

        /* get image array to download it */
        byte[] imgArray = null;

        /* image type */
        String imgType = null;

        try {
            post = encryption.Decrypt( (SealedObject) in.readObject(), Post.class);
            imgType = encryption.Decrypt( (SealedObject) in.readObject(), String.class);
            post.setImgName("img_" + id + "." + imgType);

            db.addPost(post, id);
        }

        catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }

        /* download image */
        ImageExplorer.downloadImage(id, imgType, post.getImageArray(), ImageExplorer.Project.SERVER);

        /* send imageID */
        try { out.writeObject(encryption.Encrypt(id)); }

        catch (IOException e) { e.printStackTrace(); }

    }

    private void sendPosts(){

        ArrayList<Post> postsArray = new ArrayList<Post>();

        /* get all posts */
        posts = db.getAllDocuments("Posts");

        if(posts.size() > postIndex) {
            /* put all posts into postsArray and put the imageArray into it */
            for (int i = postIndex; i < postIndex + 5; i++) {

                /* get and update post */
                Post post = gson.fromJson(posts.get(i), Post.class);
                byte[] imageArray = ImageExplorer.convertImageToByteArray("src\\main\\resources\\Images\\" + post.getImgName(), ImageExplorer.getImageType(post.getImgName()));
                post.setImageArray(imageArray);

                /* add post to array */
                postsArray.add(post);

                /* check if this is the last post */
                if (posts.size() - 1 == i) break;
            }
        }

        /* increment index for the next time more posts need to be fetched */
        postIndex += 5;

        /* send the list of posts to client */
        try { out.writeObject(encryption.Encrypt(postsArray)); }

        catch (IOException e) { e.printStackTrace(); }
    }

    private void updateRating(){

        Post post = null;
        String username = null;
        try {
            post = encryption.Decrypt( (SealedObject) in.readObject(), Post.class);
            username = encryption.Decrypt( (SealedObject) in.readObject(), String.class);
        }

        catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }

        db.updateRating(post, username);

    }


}
