import Exceptions.CollectionNotFoundException;
import Exceptions.DatabaseNotFoundException;
import Server.AtlasDB;
import Server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {

        try {
            Server server = new Server(3000, "mongodb+srv://dario_jimenezp:999@cluster0.v8i4y.mongodb.net/Memestatic?retryWrites=true&w=majority", "Memestatic");
            server.fetchClients();

        } catch (DatabaseNotFoundException e) {
            e.printStackTrace();
        } catch (CollectionNotFoundException e) {
            e.printStackTrace();
        }

    }
}
