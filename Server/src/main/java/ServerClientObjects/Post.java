package ServerClientObjects;

import java.io.Serializable;
import java.util.ArrayList;

public class Post implements Serializable {

    /* post info */
    private String postName;
    private String postUser;

    /* comments */
    private ArrayList<String> comments;

    /* rating info */
    private Double rating;
    private Double ratingSum;
    private Integer totalRatings;

    /* image */
    private String imgName;

    public Post(String postName, String postUser) {
        this.postName = postName;
        this.postUser = postUser;
        ratingSum = 0.0;
        totalRatings = 0;
        rating = 0.0;
        comments = new ArrayList<String>();
    }

    public void addRating(Double rating){
        ratingSum += rating;
        totalRatings++;
        rating = ratingSum/totalRatings;
    }

    public void addComment(String comment){
        comments.add(comment);
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

    public Double getRating() {
        return rating;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}
