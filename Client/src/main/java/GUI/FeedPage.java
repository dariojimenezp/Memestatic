package GUI;

import Client.Main;
import ServerClientObjects.Post;
import ServerClientObjects.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

//TODO: add actual post information

public class FeedPage {

    private Integer MEME_LIMIT = 10;

    private Integer MEME_HEIGHT = 600;

    private Integer MEME_WIDTH = 600;

    private User user;

    private Stage stage;

    public FeedPage(User user){

        /* root */
        VBox root = new VBox();
        root.setId("rootBox");

        /* add top bar */
        root.getChildren().add(topBar());

        /* add padding */
        verticalPadding(root);
        verticalPadding(root);

        /* add posts */
        ArrayList<Post> posts = Main.client.getPosts();
        addPosts(root, posts);

        /* button to add more posts */
        Button morePostsButton = new Button("See more Posts");
        morePostsButton.setId("morePostsButton");

        /* more posts handler */
        morePostsButton.setOnAction(event -> {
            root.getChildren().remove(morePostsButton);

            ArrayList<Post> morePosts = Main.client.getPosts();

            /* if no more posts, add no more posts label */
            if(morePosts.isEmpty()){
                Label noMorePostsLabel = new Label("There are no more posts! :(");
                root.getChildren().addAll(noMorePostsLabel, new Text(""));
                return;
            }

            /* if there are more posts available, add them to feed page */
            addPosts(root, morePosts);
            root.getChildren().add(morePostsButton);

        });

        /* add button to root */
        root.getChildren().addAll(morePostsButton, new Text(""));

        /* add scroll pane */
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);

        /* sets up scene */
        Scene scene = new Scene(scrollPane, 1900, 900);
        scene.getStylesheets().add(GUI.class.getResource("/Styles/Feed.css").toExternalForm());

        /* sets up stage */
        stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    private HBox topBar(){

        /* logo */
        ImageView logo = new ImageView(new Image(String.valueOf(getClass().getResource("/Images/LogoOnly.png"))));

        /* 'memestatic' label */
        Label memestaticLabel = new Label("Memestatic");

        /* feed label */
        Label feedLabel = new Label("Meme Feed");

        /* root */
        HBox root = new HBox();
        root.setId("topBarBox");
        root.getChildren().addAll(logo, memestaticLabel, topBarPadding(), feedLabel);



        /* if user is not logged in, put log in and create account buttons */
        if(user == null){
            /* log in button */
            Button logInButton = new Button("Log In");
            logInButton.setId("logInButton");
            logInButton.setOnAction(event -> {
                stage.close();
                GUI.logInPage();
            });

            /* create account button */
            Button createAccountButton = new Button("Create Account");
            createAccountButton.setId("createAccountButton");
            createAccountButton.setOnAction(event -> {
                stage.close();
                GUI.createAccountPage();
            });

            root.getChildren().addAll( topBarPadding(), logInButton, new Text("      "), createAccountButton);
        }


        /* profile logo */
        ImageView profileLogo = new ImageView(new Image(String.valueOf(getClass().getResource("/Images/profile.png"))));
        profileLogo.setFitWidth(35);
        profileLogo.setFitHeight(35);
        roundImage(profileLogo, 35, 35);

        //handler todo: profile logo
        profileLogo.setOnMouseClicked(event -> {

        });
        root.getChildren().addAll(new Text("  "), profileLogo);


        return root;
    }

    private Text topBarPadding(){
        return new Text("                                                                                                           " +
                "                                   ");
    }

    private HBox post(Post post){

        /* root */
        VBox postBox = new VBox();
        postBox.setId("postBox");

        /* post name */
        Label postName = new Label("   " + post.getPostName());

        /* username of who posted it */
        Label username = new Label("       " + post.getPostUser());
        username.setId("username");

        /* rating */
        Label rating = new Label(post.hasRatings() ? "   " + post.getRating() + "/10" : "   no ratings");

        postBox.getChildren().addAll(postBoxPadding(), postName, username, rating, new Text(""), memeImage(post.getImgName()),
                new Text(""), postBottom(post, rating), new Text(""));

        HBox root = new HBox();
        root.getChildren().addAll(horizontalPadding(),postBox, horizontalPadding());

        return root;
    }

    private HBox memeImage(String imageName){

        /* image */
        ImageView imageView = new ImageView();
        Image img = new Image(String.valueOf(getClass().getResource("/Images/" + imageName)));
        imageView.setImage(img);
        imageView.setFitHeight(MEME_HEIGHT);
        imageView.setFitWidth(MEME_WIDTH);

        roundImage(imageView, 10, 10);

        /* root */
        HBox root = new HBox();
        root.getChildren().addAll(imageView);
        root.setAlignment(Pos.CENTER);
        return root;
    }

    private HBox postBottom(Post post, Label ratingLabel){

        /* image */
        ImageView imageView = new ImageView();
        Image img = new Image(String.valueOf(getClass().getResource("/Images/comment.png")));
        imageView.setImage(img);
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);

        /* comment label */
        Label commentLabel = new Label(" 300 comments");
        commentLabel.setId("commentLabel");

        /* comment box */
        HBox commentBox = new HBox();
        commentBox.getChildren().addAll(new Text("  "), imageView, commentLabel, new Text("  "));
        commentBox.setId("postBox");

        /* Rating section */

        /* rate button */
        Button rateButton = new Button("Rate");
        rateButton.setId("rateButton");



        /* change color when mouse is hovering button */
        rateButton.setOnMouseEntered(event -> rateButton.setStyle("-fx-background-color: #3b3b3b"));

        rateButton.setOnMouseExited(event -> rateButton.setStyle("-fx-background-color: #454545"));

        /* text field */
        VBox rateFieldBox = new VBox();
        TextField rateField = new TextField();
        rateFieldBox.getChildren().add(rateField);

        /* button handler */
        rateButton.setOnAction(event -> {
            try{
                double rating = Integer.parseInt(rateField.getText());

                /* if input is invalid, show warning */
                if(rating < 0 || rating > 10) throw new NumberFormatException();

                post.addRating(rating);
                ratingLabel.setText("   " + post.getRating() + "/10");
                Main.client.addRating(post);
            }
            catch (NumberFormatException e){ rateFieldBox.getChildren().add(wrongRatingInput());}
        });

        /* rate box */
        HBox ratingBox = new HBox();
        ratingBox.getChildren().addAll(rateFieldBox, new Text("  "), rateButton);

        HBox box = new HBox();
        box.getChildren().addAll(new Text("             "), commentBox,
                new Text("                                                              "), ratingBox);

        /* handler if clicked */
        commentBox.setOnMouseClicked(event -> {
            //TODO
        });

        commentBox.setOnMouseEntered(event -> commentBox.setStyle("-fx-background-color: #454545"));

        commentBox.setOnMouseExited(event -> commentBox.setStyle("-fx-background-color: #383838"));
        return box;
    }

    private Label wrongRatingInput(){
        Label label = new Label("Enter valid value!");
        label.setId("validValueLabel");
        return label;
    }

    private void addPosts(VBox root, ArrayList<Post> posts){

        for(int i=0; i< posts.size(); i++){
            root.getChildren().addAll(post(posts.get(i)), new Text(""));
        }

    }

    private void verticalPadding(VBox root){

        root.getChildren().addAll(new Text("") );
    }

    private Text horizontalPadding(){
        return new Text("                                                                                                   " +
                "                           ");
    }

    private Text postBoxPadding(){
        return new Text("                                                                                                   " +
                        "                                                                                                   ");
    }


    /** makes an image round **/
    private void roundImage(ImageView imageView, int arcHeight, int arcWidth){

        /* rounded frame */
        Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
        clip.setArcHeight(arcHeight);
        clip.setArcWidth(arcWidth);
        imageView.setClip(clip);

        /* snapshot */
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = imageView.snapshot(parameters, null);

        imageView.setClip(null);
        imageView.setImage(image);
    }
}
