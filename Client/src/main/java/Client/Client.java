package Client;

import Exceptions.NoServerFoundException;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Encryption.Encryption;

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
}
