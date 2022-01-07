package Server;

import javax.crypto.SealedObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

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

    private LinkedList<String> inMsgs;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        inMsgs = new LinkedList<String>();

        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

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



            } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }

            /* perform an action depending on received messages */
            respond(inMsgs);
        }
    }

    private void respond(LinkedList<String> msgs){

    }
}
