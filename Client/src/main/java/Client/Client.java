package Client;

import GUI.ImageExplorer;
import ServerClientObjects.Post;
import Exceptions.NoServerFoundException;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import Encryption.Encryption;
import ServerClientObjects.User;

public class Client {

    /** fields **/

    /* encryption public key */
    private String PUBLIC_KEY = "dy1k82p08wm";

    /* socket */
    private Socket socket;

    /* socket streams */
    private ObjectInputStream in;
    private ObjectOutputStream out;

    /* encryption for messages */
    private Encryption encryption;

    private HashSet<String> downloadedImagesSet;

    public Client(String IP_ADDRESS, Integer PORT) throws NoServerFoundException {

        downloadedImagesSet = new HashSet<String>();
        try {
            /* connect to server socket a establish output and input streams */
            socket = new Socket(IP_ADDRESS, PORT);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            /* send public key */
            out.writeObject(new String(PUBLIC_KEY));

            /* receive encryption key ane create encryption object */
            SecretKey key = (SecretKey) in.readObject();
            encryption = new Encryption(key);

        } catch (IOException | ClassNotFoundException e){throw new NoServerFoundException(IP_ADDRESS, PORT); }
    }

    /**
     * checks with the server if an account can be created
     * @param username account username
     * @param passwordHash password hash of the account
     * @return true if account was created. false if not created
     */
    public Boolean wasAccountCreated(String username, String passwordHash){

        try {
            out.writeInt(3);
            out.writeObject(encryption.Encrypt("create account"));
            out.writeObject(encryption.Encrypt(username));
            out.writeObject(encryption.Encrypt(passwordHash));
        }
        catch (IOException e) { e.printStackTrace(); }

        LinkedList msgs = receiveMsgs();

        if(msgs.pop().equals("created account")) return true;

        else return false;
    }

    public User logIn(String username, String password){

        User user = null;
        try {
            out.writeInt(3);
            out.writeObject(encryption.Encrypt("log in"));
            out.writeObject(encryption.Encrypt(username));
            out.writeObject(encryption.Encrypt(password));

            user = encryption.Decrypt( (SealedObject) in.readObject(), User.class);

        }
        catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }

        return user;

    }

    /** receives messages from the server and stores them in a queue **/
    private LinkedList<String> receiveMsgs(){

        LinkedList<String> msgs = new LinkedList<String>();

        try {
            /* wait to read integer */
            int numMsgs = in.readInt();

            for (int i = 0; i < numMsgs; i++) {
                msgs.add(encryption.Decrypt( (SealedObject) in.readObject(), String.class));
            }
        }
        catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }

        return msgs;
    }

    public String publishPost(Post post, String path){

        String imageID = null;

        try {

            /* send the data */
            out.writeInt(4);
            out.writeObject( encryption.Encrypt("post"));
            out.writeObject(encryption.Encrypt(post));
            out.writeObject(encryption.Encrypt(ImageExplorer.getImageType(path)));

            /*receive the image id and download image to src folder */
            imageID = encryption.Decrypt( (SealedObject) in.readObject(), String.class);
            ImageExplorer.downloadImage(imageID, ImageExplorer.getImageType(path), post.getImageArray(), ImageExplorer.Project.CLIENT);
        }
        catch (IOException | ClassNotFoundException e) { e.printStackTrace();}

        return imageID;
    }

    public ArrayList<Post> getPosts(){

        ArrayList<Post> posts = new ArrayList<Post>();
        try {
            /* ask for posts */
            out.writeInt(1);
            out.writeObject(encryption.Encrypt("get posts"));

            /* get posts */
            posts = encryption.Decrypt( (SealedObject) in.readObject(), ArrayList.class);
        }

        catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }

        downloadImages(posts);
        return posts;
    }

    public ArrayList<Post> getNewPosts(){

        ArrayList<Post> posts = new ArrayList<Post>();
        try {
            /* ask for posts */
            out.writeInt(1);
            out.writeObject(encryption.Encrypt("new posts"));

            /* get posts */
            posts = encryption.Decrypt( (SealedObject) in.readObject(), ArrayList.class);
        }

        catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }

        downloadImages(posts);
        return posts;
    }

    private void downloadImages(ArrayList<Post> posts){

        File directory = new File("target\\classes\\Images");
        String[] downloadedImages = directory.list();

        for (int i = 0; i < downloadedImages.length; i++) {
            if(!downloadedImagesSet.contains(downloadedImages[i])) downloadedImagesSet.add(downloadedImages[i]);
        }

        for(Post post: posts){
            if(!downloadedImagesSet.contains(post.getImgName())) ImageExplorer.downloadImage(post.getItemID(), ImageExplorer.getImageType(post.getImgName()), post.getImageArray(), ImageExplorer.Project.CLIENT);
        }
    }

    public void addRating(Post post, String username){

        try {
            out.writeInt(3);
            out.writeObject(encryption.Encrypt("update rating"));
            out.writeObject(encryption.Encrypt(post));
            out.writeObject(encryption.Encrypt(username));
        }
        catch (IOException e) {  e.printStackTrace(); }
    }


    public void addComment(String comment, String user, Post post){

        try {
            out.writeInt(4);
            out.writeObject( encryption.Encrypt("add comment"));
            out.writeObject( encryption.Encrypt(comment));
            out.writeObject( encryption.Encrypt(user));
            out.writeObject( encryption.Encrypt(post));
        }

        catch (IOException e) { e.printStackTrace(); }

    }

    public void refreshPosts(){

        ArrayList<Post> posts = new ArrayList<Post>();

        try {
            out.writeInt(1);
            out.writeObject( encryption.Encrypt("reset posts"));
        }

        catch (IOException e) { e.printStackTrace(); }

    }

    public ArrayList<Post> search(String str){

        ArrayList<Post> posts = new ArrayList<Post>();
        try {
            out.writeInt(2);
            out.writeObject( encryption.Encrypt("search"));
            out.writeObject( encryption.Encrypt(str));

            posts = encryption.Decrypt( (SealedObject) in.readObject(), ArrayList.class);
        }

        catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }

        return posts;
    }

    public ArrayList<Post> findPosts(ArrayList<String> itemIDs){

        ArrayList<Post> posts = new ArrayList<Post>();
        try {
            out.writeInt(2);
            out.writeObject( encryption.Encrypt("find posts"));
            out.writeObject( encryption.Encrypt(itemIDs));

            posts = encryption.Decrypt( (SealedObject) in.readObject(), ArrayList.class);
        }

        catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }

        return posts;
    }

    public User getUser(String username){

        User user = null;
        try {
            out.writeInt(2);
            out.writeObject( encryption.Encrypt("get user"));
            out.writeObject( encryption.Encrypt(username));

            user = encryption.Decrypt( (SealedObject) in.readObject(), User.class);
        }

        catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }

        return user;
    }
}
