package Server;

import Exceptions.CollectionNotFoundException;
import Exceptions.DatabaseNotFoundException;
import ServerClientObjects.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.*;
import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.*;
import com.mongodb.client.model.*;
import javafx.geometry.Pos;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.*;

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


            /* create indexes for searching posts */
            collections.get("Posts").createIndex(Indexes.text("postName"));
            //collections.get("Posts").createIndex(Indexes.text("postUser"));



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
    public synchronized List<String> getAllDocuments(String collectionName){

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
    public synchronized String findDocument(String collectionName, String fieldName, String value){

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

    public synchronized void addUser(String username, String passwordHash){
        Document user = new Document("username", username).append("passwordHash", passwordHash);
        collections.get("Users").insertOne(user);
    }

    public synchronized void addPost(Post post, String id){

        /* create post document and add it to the posts collection */
        Document postDocument = new Document("postName", post.getPostName()).append("postUser", post.getPostUser())
                .append("imgName", post.getImgName()).append("itemID", Integer.valueOf(id) ).append("ratingSum", 0).append("totalRatings", 0);
        collections.get("Posts").insertOne(postDocument);

        /* add post to user's post history */
        collections.get("Users").updateOne( eq("username", post.getPostUser()), new Document("$push", new Document("PostHistory", id)) );

    }


    public synchronized String getImageID(){
        /* get imageID */
        String doc = findDocument("ImageID", "_id", IMAGE_ID);
        Integer id = new GsonBuilder().create().fromJson(doc, ImageID.class).getImageID();

        /* increment and update imageID */
        collections.get("ImageID").updateOne( eq("_id", new ObjectId(IMAGE_ID)), new Document("$set", new Document("imageID", id+1)));

        return String.valueOf(id);
    }

    public synchronized void updateRating(Post post, String username){

        /* update post */
        collections.get("Posts").updateOne( eq("itemID", post.getItemID()), new Document("$set",
                new Document("rating", post.getRating()).append("totalRatings", post.getTotalRatings()).append("ratingSum", post.getRatingSum()) ) );

        /* add post to user's rated posts */
        collections.get("Users").updateOne( eq("username", username), new Document("$push", new Document("ratedPosts", post.getItemID())));

    }

    public synchronized ArrayList<Post> getPosts(Integer postIndex){

        AggregateIterable<Document> aggregate = null;

        if(postIndex == 0 || postIndex == 1) return new ArrayList<Post>();;

        if(postIndex > 0) {
            aggregate = db.getCollection("Posts").aggregate(Arrays.asList(
                    Aggregates.sort(Sorts.descending("itemID")),
                    Aggregates.match(Filters.lt("itemID", postIndex)),
                    Aggregates.limit(5)
            ));
        }

        else {
            aggregate = db.getCollection("Posts").aggregate(Arrays.asList(
                    Aggregates.sort(Sorts.descending("itemID")),
                    Aggregates.limit(5)
            ));
        }


        Iterator<Document> i = aggregate.iterator();

        /* put all posts in a list */
        ArrayList<Post> list = new ArrayList<Post>();
        Gson gson = new GsonBuilder().create();

        while (i.hasNext()){
            list.add(  gson.fromJson(i.next().toJson(), Post.class ));
        }

        return list;
    }

    public synchronized ArrayList<Post> fetchNewPosts(Integer postIndex){

        if(postIndex <= 0) return new ArrayList<Post>();

        AggregateIterable<Document> aggregate = db.getCollection("Posts").aggregate(Arrays.asList(
                Aggregates.match(Filters.gt("itemID", postIndex)),
                Aggregates.sort(Sorts.descending("itemID"))
        ));

        Iterator<Document> i = aggregate.iterator();

        /* put all posts in a list */
        ArrayList<Post> list = new ArrayList<Post>();
        Gson gson = new GsonBuilder().create();

        while (i.hasNext()){
            list.add(  gson.fromJson(i.next().toJson(), Post.class ));
        }

        return list;
    }

    public synchronized void addComment(String comment, String user, Post post){

        /* add comment to post */
        collections.get("Posts").updateOne( eq("itemID", Integer.valueOf(post.getItemID())),
                new Document("$push", new Document("comments", comment) ));

        collections.get("Posts").updateOne( eq("itemID", Integer.valueOf(post.getItemID())),
                new Document("$push", new Document("commentUsers", user) ));

    }

    public synchronized ArrayList<Post> search(String str){

        FindIterable<Document> list = collections.get("Posts").find( Filters.text(str)).projection(Projections.metaTextScore("score")).sort(Sorts.metaTextScore("score"));

        Iterator<Document> i = list.iterator();
        ArrayList<Post> posts = new ArrayList<Post>();
        Gson gson = new GsonBuilder().create();

        while (i.hasNext()){
            posts.add( gson.fromJson( i.next().toJson(), Post.class));
        }

        return posts;
    }


    public synchronized ArrayList<Post> findPosts(ArrayList<String> itemIDs){

        ArrayList<Post> posts = new ArrayList<Post>();
        Gson gson = new GsonBuilder().create();

        for(String itemID: itemIDs){
            FindIterable<Document> doc = collections.get("Posts").find( eq("itemID", Integer.valueOf(itemID)) );
            Iterator<Document> it = doc.iterator();

            if(it.hasNext()) posts.add( gson.fromJson(it.next().toJson(), Post.class));
        }

        return posts;
    }

}
