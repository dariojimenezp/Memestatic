package Server;

import Exceptions.CollectionNotFoundException;
import Exceptions.DatabaseNotFoundException;
import com.mongodb.*;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class AtlasDB {

    private MongoDatabase db;
    private HashMap<String, MongoCollection<Document>> collections;

    /**
     * Consructor
     * @param connectionString connection string as indicated in AtlasDB
     * @param database database name
     */
    public AtlasDB(String connectionString, String database) throws DatabaseNotFoundException, CollectionNotFoundException {

        try {
            /* set a 15-second timeout */
            Thread.sleep(5000);

            /* create Connection string for database */
            ConnectionString cs = new ConnectionString(connectionString);

            /* build the settings for the database client */
            MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();

            /* Create mongo client using the built setting */
            MongoClient mongoClient = MongoClients.create(settings);

            /* retrieve desired database from mongo client */
            this.db = mongoClient.getDatabase(database);

        } catch (Exception e){
            throw new DatabaseNotFoundException(connectionString, database);
        }

        String collection = null;
        try {
            /* set a 15-second timeout */
            Thread.sleep(15000);

            /* fetch the necessary collection and store them in hash map */
            collections = new HashMap<String, MongoCollection<Document>>();
            collection = "Users";
            collections.put("Users", db.getCollection("Users"));
            collection = "Posts";
            collections.put("Items", db.getCollection("Posts"));

        }catch (Exception e){
            throw new CollectionNotFoundException(collection, database, connectionString);
        }

    }

    public List<String> getAllDocuments(String collectionName){

        /* get collection from database */
        MongoCollection<Document> collection = collections.get(collectionName);


        /* get iterator for collection */
        FindIterable<Document> documents = collection.find();
        Iterator<Document> iterator = documents.iterator();

        List<String> list = new ArrayList<String>();

        while (iterator.hasNext()){
            list.add(iterator.next().toJson());
        }

        return list;
    }

    public String findDocument(String collectionName, String fieldName, String value){

        /* get collection from database */
        MongoCollection<Document> collection = collections.get(collectionName);


        /* find document */
        FindIterable<Document> document = null;
        if(fieldName.equals("itemID")) document = collection.find(eq(fieldName, Integer.valueOf(value)));

        else document = collection.find(eq(fieldName, value));
        Iterator<Document> iterator = document.iterator();

        if(iterator.hasNext()){
            return iterator.next().toJson();
        }

        else return null;
    }

}
