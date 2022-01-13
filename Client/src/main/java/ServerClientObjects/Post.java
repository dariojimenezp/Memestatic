package ServerClientObjects;

import java.io.Serializable;
import java.util.ArrayList;

public class Post implements Serializable {

    /* post's unique imageID */
    private String itemID;

    /* post info */
    private String postName;
    private String postUser;

    /* comments */
    private ArrayList<String> comments;
    private ArrayList<String> commentUsers;


    /* rating info */
    private Double rating;
    private Double ratingSum;
    private Integer totalRatings;

    /* image */
    private String imgName;

    private byte[] imageArray;

    public Post(String postName, String postUser, byte[] imageArray) {
        this.postName = postName;
        this.postUser = postUser;
        this.imageArray = imageArray;
        ratingSum = 0.0;
        totalRatings = 0;
        rating = 0.0;
        comments = new ArrayList<String>();
        commentUsers =  new ArrayList<String>();
    }

    public void addRating(Double rating){
        ratingSum += rating;
        totalRatings++;
        this.rating = ratingSum/totalRatings;
    }

    public void addComment(String comment, String user){
        comments.add(comment);
        commentUsers.add(user);
    }

    /** getters **/
    public String getPostName() {
        return postName;
    }

    public String getPostUser() {
        return postUser;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public ArrayList<String> getCommentUsers() {
        return commentUsers;
    }

    public Double getRating() {
        return rating;
    }

    public String getImgName() {
        return imgName;
    }

    public byte[] getImageArray() {
        return imageArray;
    }

    public String getItemID() {
        return itemID;
    }

    public Double getRatingSum() {
        return ratingSum;
    }

    public Integer getTotalRatings() {
        return totalRatings;
    }



    /** setters **/
    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public void setImageArray(byte[] imageArray) {
        this.imageArray = imageArray;
    }

    public Boolean hasRatings(){
        return !(totalRatings == 0);
    }

}
