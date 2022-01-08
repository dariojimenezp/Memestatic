package Exceptions;

public class NoServerFoundException extends Exception{

    public NoServerFoundException(String IPAddress, Integer port) {
        super(new String("No server found on " + IPAddress + ":" + port));
    }
}
