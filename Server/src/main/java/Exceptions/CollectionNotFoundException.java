package Exceptions;

public class CollectionNotFoundException extends Exception{

    public CollectionNotFoundException(String collection, String database, String connectionString) {
        super(new String("Collection '" + collection + "' was not found on database '" + database + "' on '"
        + connectionString + "' connection string"));
    }
}
