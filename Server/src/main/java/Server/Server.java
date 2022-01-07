package Server;

import java.util.Observable;

public class Server extends Observable {

    /* Server settings */
    private String IP_ADDRESS;
    private Integer PORT;

    /**
     * Constructor
     *
     * @param IP_ADDRESS IP address where the server will be hosted
     * @param PORT
     */
    public Server(String IP_ADDRESS, Integer PORT) {
        this.IP_ADDRESS = IP_ADDRESS;
        this.PORT = PORT;
    }

    public void initialize(){

    }
}
