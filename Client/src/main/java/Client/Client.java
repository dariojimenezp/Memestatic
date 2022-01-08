package Client;

import Exceptions.NoServerFoundException;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import Encryption.Encryption;
import com.sun.org.apache.xpath.internal.operations.Bool;

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

    public Client(String IP_ADDRESS, Integer PORT) throws NoServerFoundException {

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

    /** receives messages from the server and stores them in a queue **/
    private LinkedList<String> receiveMsgs(){

        LinkedList<String> msgs = new LinkedList<String>();

        try {
            /* wait to read integer */
            int numMsgs = in.readInt();

            for (int i = 0; i < numMsgs; i++) {
                msgs.add(encryption.Decrypt( (SealedObject) in.readObject()));
            }
        }
        catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }

        return msgs;
    }
}
