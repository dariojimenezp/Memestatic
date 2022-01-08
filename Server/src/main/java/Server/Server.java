package Server;

import Exceptions.CollectionNotFoundException;
import Exceptions.DatabaseNotFoundException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

public class Server {

    /* Server settings */
    private Integer PORT;
    private AtlasDB db;

    /**
     * Constructor
     *
     * @param PORT port where the server will be hosted
     * @param dbConnectionString connection string of MongoDB Atlas account
     * @param dbName name of desired database
     */
    public Server(Integer PORT, String dbConnectionString, String dbName) throws DatabaseNotFoundException, CollectionNotFoundException {
        this.PORT = PORT;

        db = new AtlasDB(dbConnectionString, dbName);

    }



    public void fetchClients(){
        try {

            /* create server socket */
            ServerSocket serverSocket = new ServerSocket(PORT);

            while (true){

                /* wait for a client socket */
                Socket clientSocket = serverSocket.accept();


                /* create and start a thread for client */
                Thread t = new Thread(new ClientHandler(clientSocket, this, db));
                t.start();

                /*indicate a connection with a client was established */
                System.out.println("got a connection");

            }

        }catch (IOException e){

        }
    }
}
