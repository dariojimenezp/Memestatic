package Server;

import javax.crypto.SealedObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import ServerClientObjects.User;
import com.google.gson.*;
import Hashing.Hashing;


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


    private LinkedList<String> inMsgs;

    public ClientHandler(Socket socket, Server server, AtlasDB db) {
        this.socket = socket;
        this.server = server;
        this.db = db;
        gson = new GsonBuilder().create();
        inMsgs = new LinkedList<String>();

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

                /* read and decrypt the specified number of objects from the client */
                for (int i = 0; i < numObjects; i++) { inMsgs.add(encryption.Decrypt( (SealedObject) in.readObject()) ); }



            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Socket closed");
                return;
            }


            /* perform an action depending on received messages */
            respond(inMsgs);
        }
    }

    private void respond(LinkedList<String> msgs){
        switch (msgs.pop()){

            case "create account":
                createAccount(msgs);
                break;

            case "log in":
                logIn(msgs);
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
}
