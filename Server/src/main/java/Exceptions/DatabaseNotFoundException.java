package Exceptions;

public class DatabaseNotFoundException extends Exception{

    /**
     * Constructor
     *
     * @param connectionString connection string for database
     * @param database database name
     */
    public DatabaseNotFoundException(String connectionString, String database) {
        super(new String("Database '" + database + "' on '" + connectionString + "' connection string not found!" ));
    }



}
