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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

//TODO: enable user to rate post again, but have it just replace the old rating with the new rating

public class FeedPage {

    private Integer MEME_LIMIT = 10;

    private Integer MEME_HEIGHT = 600;

    private Integer MEME_WIDTH = 600;

    private Integer MAX_TITLE_LENGTH = 30;

    private User user = new User("spooderman", "");

    private Stage stage;

    private Boolean uploadedImage;

    private String uploadedImagePath;

    public FeedPage(User user){

        createFeedPage();
    }

    private void createFeedPage(){
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
        Scene scene = new Scene(scrollPane, 1920, 980);
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

        else root.getChildren().addAll(topBarPadding(), new Text("                                                    "));


        /* create post */
        StackPane createPostPane  = new StackPane();
        ImageView createPostButton = new ImageView(new Image(String.valueOf(getClass().getResource("/Images/plus3.png"))));
        createPostButton .setFitWidth(35);
        createPostButton .setFitHeight(35);
        roundImage(createPostButton , 35, 35);

        /* add image to pane */
        createPostPane.getChildren().add(createPostButton);
        createPostPane.setId("createPostPane");

        /* create post handlers */
        createPostPane.setOnMouseEntered(event -> createPostPane.setStyle("-fx-background-color: #e6e6e6"));

        createPostPane.setOnMouseExited(event -> createPostPane.setStyle("-fx-background-color: white"));

        createPostPane.setOnMouseClicked(event -> {
            stage.close();
            createPost();
        });

        /* profile logo */
        ImageView profileLogo = new ImageView(new Image(String.valueOf(getClass().getResource("/Images/profile.png"))));
        profileLogo.setFitWidth(35);
        profileLogo.setFitHeight(35);
        roundImage(profileLogo, 35, 35);

        //handler todo: profile logo
        profileLogo.setOnMouseClicked(event -> {

        });
        root.getChildren().addAll(createPostPane, new Text("      "), profileLogo);


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

        postBox.getChildren().addAll(boxPadding(), postName, username, rating, new Text(""), memeImage(post.getImgName()),
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

                /* if user is not logged in, tell user to log in to rate */
                if(user == null) throw new NumberFormatException("Must be logged in to rate");

                if(user.hasRatedPost(post.getItemID())) throw new NumberFormatException("Already rated this post!");

                if(rateField.getText().isEmpty()) throw new NumberFormatException("Enter valid value!");

                double rating = Integer.parseInt(rateField.getText());

                /* if input is invalid, show warning */
                if(rating < 0 || rating > 10) throw new NumberFormatException("Enter valid value!");

                post.addRating(rating);
                ratingLabel.setText("   " + post.getRating() + "/10");
                Main.client.addRating(post, user.getUsername());
                user.addRatePost(post.getItemID());
            }
            catch (NumberFormatException e){ wrongRatingInput(rateFieldBox, e.getMessage()); }
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


    private void wrongRatingInput(VBox box, String msg){

        /* if it already has warning, don't add another warning */
        if(box.getChildren().size() == 2) return;

        Label label = new Label(msg);
        label.setId("rateWarningLabel");
        box.getChildren().add(label);
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

    private Text boxPadding(){
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

    private void createPost(){

        VBox root = new VBox();
        root.setId("rootBox");

        root.getChildren().addAll(topBar(), new Text(""), new Text(""), createPostBox());



        /* sets up scene */
        Scene scene = new Scene(root, 1920, 980);
        scene.getStylesheets().add(GUI.class.getResource("/Styles/Feed.css").toExternalForm());

        /* sets up stage */
        stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }


    private HBox createPostBox(){

        uploadedImage = false;
        uploadedImagePath = null;

        /* root */
        VBox postBox = new VBox();
        postBox.setId("postBox");
        postBox.setAlignment(Pos.CENTER);

        /* post label */
        Label postLabel = new Label("Post");


        /* title text field */
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        titleField.setId("titleField");

        HBox pictureBox = createPostPicturePadding();

        /* button to upload picture/meme */
        VBox uploadPicBox = new VBox();
        Button uploadPicButton = new Button("Upload Picture");
        uploadPicButton.setId("createPostButtons");
        uploadPicBox.setAlignment(Pos.CENTER);
        uploadPicBox.getChildren().add(uploadPicButton);


        uploadPicButton.setOnAction(event -> {
            /* get image path from user */
            String path = ImageExplorer.getPath(stage);


            /* if chosen file is not supported, tell user to pick correct file */
            String imageType = ImageExplorer.getImageType(path);
            if(!imageType.equals("png") && !imageType.equals("jpeg") && !imageType.equals("jpg")){
                if(uploadPicBox.getChildren().size() == 2) uploadPicBox.getChildren().remove(1);
                Label picWarning = new Label("Invalid file format! select a png, jpg, or jpeg file!");
                picWarning.setId("unsuccessfulLabel");
                uploadPicBox.getChildren().add(picWarning);
                return;
            }

            /* upload image to box */
            ImageView imageView = new ImageView(new Image("file:" + path));
            imageView.setFitWidth(MEME_WIDTH);
            imageView.setFitHeight(MEME_HEIGHT);
            pictureBox.getChildren().clear();
            pictureBox.getChildren().addAll(imageView );
            pictureBox.setAlignment(Pos.CENTER);

            /* add successfully uploaded label */
            Label successfulUpload = new Label("Image successfully uploaded");
            successfulUpload.setId("successfulLabel");
            if(uploadPicBox.getChildren().size() == 2) uploadPicBox.getChildren().remove(1);
            uploadPicBox.getChildren().add(successfulUpload);
            uploadedImage = true;
            uploadedImagePath = path;

        });

        /* button to create the post */
        Button postButton = new Button("Post");
        postButton.setId("createPostButtons");


        /* button to cancel the post */
        Button cancelButton = new Button("Cancel");
        cancelButton.setId("createPostButtons");
        cancelButton.setOnAction(event -> {
            stage.close();
            createFeedPage();
        });

        /* hbox for post and cancel buttons */
        HBox bottomBox = new HBox();
        bottomBox.setId("bottomCreatePostBox");
        bottomBox.getChildren().addAll(cancelButton, new Text("  "), postButton);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);

        postButton.setOnAction(event -> {

            String title = titleField.getText();

            /* if user did not put a title or upload an image, indicate user to do so */
            if(title.isEmpty() || !uploadedImage || title.equals("Title")){
                if(uploadPicBox.getChildren().size() == 2) uploadPicBox.getChildren().remove(1);
                Label warning = new Label("Set a title and upload an image!");
                warning.setId("unsuccessfulLabel");
                uploadPicBox.getChildren().add(warning);
                return;
            }

            if(title.length() > MAX_TITLE_LENGTH){
                if(uploadPicBox.getChildren().size() == 2) uploadPicBox.getChildren().remove(1);
                Label warning = new Label("Max title length is" + MAX_TITLE_LENGTH + "!");
                warning.setId("unsuccessfulLabel");
                uploadPicBox.getChildren().add(warning);
                return;
            }

            /* upload post */
            Post post = new Post(title, user.getUsername(), ImageExplorer.convertImageToByteArray(uploadedImagePath, ImageExplorer.getImageType(uploadedImagePath)));
            Main.client.publishPost(post, uploadedImagePath);
            postBox.getChildren().remove(postBox.getChildren().size()-1);
            postBox.getChildren().removeAll(uploadPicBox, bottomBox, titleField);

            Label warning = new Label("successfully published!");
            warning.setId("successfulLabel");

            Button homeButton = new Button("Home");
            homeButton.setId("createPostButtons");
            homeButton.setOnAction(e -> {
                stage.close();
                createFeedPage();
            });

            postBox.getChildren().addAll(warning, homeButton, new Text(""));
        });

        /* add everything to post box */
        postBox.getChildren().addAll(boxPadding(), postLabel, new Text(""), titleField, new Text(""),pictureBox,
                new Text(""), uploadPicBox, new Text(""), bottomBox, new Text(""));
        HBox root = new HBox();

        root.getChildren().addAll(horizontalPadding(),postBox, horizontalPadding());

        return root;
    }

    private HBox createPostPicturePadding(){

        HBox root = new HBox();

        VBox imageBox = new VBox();
        imageBox.setId("createPostImageBox");

        for (int i = 0; i < 25; i++) {
            imageBox.getChildren().add(new Text("                                                                                                    " +
                                                "                                                                          "));
        }

        root.getChildren().addAll(new Text("             "), imageBox, new Text("             "));

        return root;
    }
}
