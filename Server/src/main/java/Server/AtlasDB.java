package Server;

import Exceptions.CollectionNotFoundException;
import Exceptions.DatabaseNotFoundException;
import ServerClientObjects.Post;
import com.google.gson.GsonBuilder;
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
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class AtlasDB {

    private MongoDatabase db;
    private HashMap<String, MongoCollection<Document>> collections;
    private String IMAGE_ID = "61dbda1c7014788fb8ab3fce";

    /**
     * Consructor
     * @param connectionString connection string as indicated in AtlasDB
     * @param database database name
     */
    public AtlasDB(String connectionString, String database) throws DatabaseNotFoundException, CollectionNotFoundException {

        try {

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

            /* fetch the necessary collection and store them in hash map */
            collections = new HashMap<String, MongoCollection<Document>>();
            collection = "Users";
            collections.put("Users", db.getCollection("Users"));
            collection = "Posts";
            collections.put(collection, db.getCollection("Posts"));
            collection = "ImageID";
            collections.put(collection, db.getCollection("ImageID"));


        }catch (Exception e){
            throw new CollectionNotFoundException(collection, database, connectionString);
        }

    }

    /**
     * gets all the documents from a collection
     *
     * @param collectionName the name of the collection
     * @return a list of the all the documents in the colleciton
     */
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

    /**
     * finds a specific document from a collection
     *
     * @param collectionName name of the desired collection
     * @param fieldName name of the document specific identifier
     * @param value value of the identifier
     * @return
     */
    public String findDocument(String collectionName, String fieldName, String value){

        /* get collection from database */
        MongoCollection<Document> collection = collections.get(collectionName);


        /* find document */
        FindIterable<Document> document = null;
        if(fieldName.equals("_id")) document = collection.find(eq(fieldName, new ObjectId(value)));


        else document = collection.find(eq(fieldName, value));
        Iterator<Document> iterator = document.iterator();

        if(iterator.hasNext()){
            return iterator.next().toJson();
        }

        else return null;
    }

    public void addUser(String username, String passwordHash){
        Document user = new Document("username", username).append("passwordHash", passwordHash);
        collections.get("Users").insertOne(user);
    }

    public void addPost(Post post, String id){
        Document postDocument = new Document("postName", post.getPostName()).append("postUser", post.getPostUser())
                .append("imgName", post.getImgName()).append("itemID", id).append("ratingSum", 0).append("totalRatings", 0);

        collections.get("Posts").insertOne(postDocument);

    }

    public void addComment(String post, String comment){

        //collections.get("Post").updateOne( eq("postName"))
    }

    public String getImageID(){
        /* get imageID */
        String doc = findDocument("ImageID", "_id", IMAGE_ID);
        Integer id = new GsonBuilder().create().fromJson(doc, ImageID.class).getImageID();

        /* increment and update imageID */
        collections.get("ImageID").updateOne( eq("_id", new ObjectId(IMAGE_ID)), new Document("$set", new Document("imageID", id+1)));

        return String.valueOf(id);
    }

}
