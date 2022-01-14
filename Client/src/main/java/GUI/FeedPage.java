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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashSet;

//TODO: enable user to rate post again, but have it just replace the old rating with the new rating

public class FeedPage {

    private Integer MEME_HEIGHT = 600;

    private Integer MEME_WIDTH = 600;

    private Integer MAX_TITLE_LENGTH = 40;

    private User user = new User("spooderman", "");

    private Stage stage;

    private Boolean uploadedImage;

    private String uploadedImagePath;

    private ArrayList<Post> postsList;

    private HashSet<String> postsPosted;

    private Boolean hasFetched;

    private Boolean isCommentOpened;

    private Boolean isFeedPageOn;

    private Boolean isRefresh;

    public FeedPage(User user){

        postsList = new ArrayList<Post>();
        postsPosted = new HashSet<String>();
        hasFetched =  false;
        isCommentOpened = false;
        isFeedPageOn = false;
        isRefresh = false;
        this.user = user;
        createFeedPage();
    }

    private void createFeedPage(){
        /* root */
        VBox root = new VBox();
        root.setId("rootBox");
        root.setAlignment(Pos.TOP_CENTER);

        /* add top bar */
        root.getChildren().add(topBar());

        /* add padding */
        verticalPadding(root);
        verticalPadding(root);

        /* add posts */
        ArrayList<Post> newPostsList = new ArrayList<Post>();

        /* refresh */
        if(isRefresh){
            postsList.clear();
            Main.client.refreshPosts();
            isRefresh = false;
        }

        if (!hasFetched) {
            postsList.addAll(Main.client.getPosts());
            newPostsList = Main.client.getNewPosts();
            hasFetched = true;

            if (newPostsList != null || newPostsList.isEmpty()) {
                newPostsList.addAll(postsList);
                postsList = newPostsList;
            }
        }

        addPosts(root, postsList);

        /* button to add more posts */
        Button morePostsButton = new Button("See more Posts");
        morePostsButton.setId("morePostsButton");

        /* more posts handler */
        morePostsButton.setOnAction(event -> {

            int numPosts = postsList.size();
            postsList.addAll(Main.client.getPosts());
            ArrayList<Post> newPostsList2 = Main.client.getNewPosts();
            hasFetched = true;

            if(newPostsList2 != null && !newPostsList2.isEmpty()){
                newPostsList2.addAll(postsList);
                postsList = newPostsList2;
            }

            /* if no more posts, add no more posts label */
            if(numPosts == postsList.size()){
                Label noMorePostsLabel = new Label("There are no more posts! :(");
                root.getChildren().addAll(noMorePostsLabel, new Text(""));
                morePostsButton.setId("transparentObject");
                return;
            }

            root.getChildren().remove(morePostsButton);

            /* if there are more posts available, add them to feed page */
            addPosts(root, postsList);
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

        isFeedPageOn = true;
    }

    private HBox topBar(){

        /* logo */
        ImageView logo = new ImageView(new Image(String.valueOf(getClass().getResource("/Images/LogoOnly.png"))));

        /* 'memestatic' label */
        Label memestaticLabel = new Label("Memestatic");

        /* feed label */
        Label feedLabel = new Label("Meme Feed");

        /* search field */
        StackPane searchPane = new StackPane();
        ImageView searchImage = new ImageView(new Image(String.valueOf(getClass().getResource("/Images/search.png"))));
        searchImage.setFitHeight(35);
        searchImage.setFitWidth(35);
        searchPane.getChildren().add(searchImage);
        searchPane.setId("searchPane");

        /* create post handlers */
        searchPane.setOnMouseEntered(event -> searchPane.setStyle("-fx-background-color: #5040db"));

        searchPane.setOnMouseExited(event -> searchPane.setStyle("-fx-background-color: #5B49F5"));

        TextField searchField = new TextField();
        searchField.setPromptText("Search");
        searchField.setId("searchField");
        searchPane.setOnMouseClicked(event -> {
            String string = searchField.getText();

            if(string.isEmpty()) return;

            ArrayList<Post> posts = Main.client.search(string);
            closeFeedPage();
            showSearchedPosts(posts);
        });

        /* root */
        HBox root = new HBox();
        root.setId("topBarBox");
        root.getChildren().addAll(logo, memestaticLabel, topBarPadding(),
                new Text("                                                      "), searchPane, new Text(" "), searchField);


        /* if user is not logged in, put log in and create account buttons */
        if(user == null){
            /* log in button */
            Button logInButton = new Button("Log In");
            logInButton.setId("logInButton");
            logInButton.setOnAction(event -> {
                closeFeedPage();
                GUI.logInPage();
            });

            /* create account button */
            Button createAccountButton = new Button("Create Account");
            createAccountButton.setId("createAccountButton");

            createAccountButton.setOnAction(event -> {
                closeFeedPage();
                GUI.createAccountPage();
            });

            root.getChildren().addAll( topBarPadding(), new Text("                                                               "), logInButton, new Text("      "), createAccountButton);
            return root;
        }

        else root.getChildren().addAll(topBarPadding(), new Text("                                     "));

        /* home button */
        StackPane homePane  = new StackPane();
        ImageView homeButton = new ImageView(new Image(String.valueOf(getClass().getResource("/Images/home.png"))));
        homeButton.setFitWidth(35);
        homeButton.setFitHeight(35);
        roundImage(homeButton , 35, 35);

        /* add image to pane */
        homePane.getChildren().add(homeButton);
        homePane.setId("createPostPane");

        /* create post handlers */
        homePane.setOnMouseEntered(event -> homePane.setStyle("-fx-background-color: #e6e6e6"));

        homePane.setOnMouseExited(event -> homePane.setStyle("-fx-background-color: white"));

        homePane.setOnMouseClicked(event -> {
            isRefresh = true;
            hasFetched = false;
            postsPosted.clear();
            if(isFeedPageOn) closeFeedPage();
            else stage.close();
            createFeedPage();
        });

        /* create post */
        StackPane createPostPane  = new StackPane();
        ImageView createPostButton = new ImageView(new Image(String.valueOf(getClass().getResource("/Images/plus.png"))));
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
            closeFeedPage();
            createPost();
        });


        /* menu items */
        MenuItem postHistoryMenu = new MenuItem("Post History");
        postHistoryMenu.setStyle("-fx-text-fill: #5B49F5; -fx-font-size: 18px;");
        postHistoryMenu.setOnAction(event -> {
            closeFeedPage();
            createPostHistory();
        });

        MenuItem logOutMenu = new MenuItem("Log Out");
        logOutMenu.setStyle("-fx-text-fill: #5B49F5; -fx-font-size: 18px;");
        logOutMenu.setOnAction(event -> {
            closeFeedPage();
            //todo: logout
        });

        /* menu */
        Menu account = new Menu("Account");
        account.getItems().addAll(postHistoryMenu, logOutMenu);

        /* bar */
        MenuBar accountBar = new MenuBar(account);

        root.getChildren().addAll(accountBar);


        return root;
    }


    private Text topBarPadding(){
        return new Text("                                                                      ");
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
        Label commentLabel = new Label(post.getComments() == null ? "0 Comments" : post.getComments().size() + " Comments");
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
        if(!isCommentOpened) {
            commentBox.setOnMouseClicked(event -> {
                closeFeedPage();
                commentPage(post);
            });
        }

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


        for(Post post: posts){
            if(!postsPosted.contains(post.getItemID())){
                root.getChildren().addAll(post(post), new Text(""));
                postsPosted.add(post.getItemID());
            }

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
            isRefresh = true;
            hasFetched = false;
            postsPosted.clear();
            stage.close();
            createFeedPage();
        });

        /* hbox for post and cancel buttons */
        HBox bottomBox = new HBox();
        bottomBox.setId("bottomCreatePostBox");
        bottomBox.getChildren().addAll(cancelButton, new Text("     "), postButton, new Text("   "));
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

            /* title too long */
            if(title.length() > MAX_TITLE_LENGTH){
                if(uploadPicBox.getChildren().size() == 2) uploadPicBox.getChildren().remove(1);
                Label warning = new Label("Max title length is" + MAX_TITLE_LENGTH + "!");
                warning.setId("unsuccessfulLabel");
                uploadPicBox.getChildren().add(warning);
                return;
            }

            /* upload post */
            Post post = new Post(title, user.getUsername(), ImageExplorer.convertImageToByteArray(uploadedImagePath, ImageExplorer.getImageType(uploadedImagePath)));
            String imageID = Main.client.publishPost(post, uploadedImagePath);
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
            hasFetched = false;
            user.addPublishedPost(imageID);
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

    private void closeFeedPage(){
        stage.close();
        postsList.clear();

        isFeedPageOn = false;
    }

    private void commentPage(Post post){

        /* root */
        VBox root = new VBox();
        root.setId("rootBox");
        isCommentOpened = true;

        /* add top bar */
        root.getChildren().addAll(topBar(), new Text(""), new Text(""), post(post), new Text(""));


        /* add comment box */
        TextArea commentField = new TextArea();
        commentField.setPromptText("Add a comment");
        commentField.setId("commentField");
        HBox addCommentBox = new HBox();
        addCommentBox.getChildren().addAll(new Text("    "), commentField, new Text("    "));

        Button addCommentButton = new Button("Add Comment");
        addCommentButton.setId("addCommentButton");


        /* all comments box */
        VBox allCommentBox = new VBox();
        allCommentBox.setAlignment(Pos.CENTER);
        allCommentBox.setId("postBox");
        allCommentBox.getChildren().addAll( new Text(""), addCommentBox, new Text(""), addCommentButton, new Text(""));

        /* add comment button handler */
        addCommentButton.setOnAction(event -> {

            if(commentField.getText().isEmpty()){
                allCommentBox.getChildren().remove(2);
                Label noTextWarning = new Label("You have to enter a valid comment!");
                noTextWarning.setId("unsuccessfulLabel");
                allCommentBox.getChildren().add(2, noTextWarning);
                return;
            }

            Main.client.addComment(commentField.getText(), user.getUsername(), post);
            allCommentBox.getChildren().remove(2);
            Label noTextWarning = new Label("successfully uploaded comment!");
            noTextWarning.setId("successfulLabel");
            allCommentBox.getChildren().add(2, noTextWarning);

        });

        /* add comments */
        if(post.getComments() != null && !post.getComments().isEmpty()){
            for (int i = 0; i < post.getComments().size(); i++) {

                VBox commentBox = new VBox();

                /* get comment and user string */
                String comment = post.getComments().get(i);
                String user = post.getCommentUsers().get(i);

                /* divide comment into 95 character long lines */
                int numLines = comment.length()/85 + 1;
                String[] lines = new String[numLines];
                for (int j = 0; j < numLines; j++) {

                    if(j == numLines-1) lines[j] = comment.substring(j*86);
                    else lines[j] = comment.substring(j*86, (j+1)*86);

                    Label commentLabel = new Label(lines[j]);
                    commentLabel.setId("commentText");
                    commentBox.getChildren().add(commentLabel);
                }


                /* user label */
                Label userLabel = new Label( "  " + user);
                userLabel.setId("commentUserLabel");
                HBox userBox = new HBox();
                userBox.setAlignment(Pos.CENTER_LEFT);
                userBox.getChildren().addAll(userLabel);

                /* box where comment will be */
                commentBox.setId("createPostImageBox");
                commentBox.setAlignment(Pos.CENTER_LEFT);
                commentBox.getChildren().addAll(new Text("                                                                                                                            " +
                                "                                                                  "));

                /* box for padding */
                HBox paddingBox = new HBox();
                paddingBox.getChildren().addAll(new Text("     "), commentBox, new Text("     "));


                allCommentBox.getChildren().addAll( userBox, paddingBox, new Text(""));
            }
        }

        HBox paddingBox = new HBox();
        paddingBox.getChildren().addAll(horizontalPadding(), new Text("    "), allCommentBox, new Text("    "), horizontalPadding());
        root.getChildren().addAll(paddingBox);

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

    private void showSearchedPosts(ArrayList<Post> posts){

        /* root */
        VBox root = new VBox();
        root.setId("rootBox");
        root.setAlignment(Pos.TOP_CENTER);

        /* add top bar */
        root.getChildren().addAll(topBar(), new Text(""));

        /* add posts */
        for(Post post: posts){
            root.getChildren().addAll(post(post), new Text(""));
        }

        /* add padding to complete page if there is only one post */
        if(posts.size() == 1){
            root.getChildren().addAll(new Text(), new Text(), new Text(), new Text(), new Text());
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);

        Scene scene = new Scene(scrollPane, 1920, 980);
        scene.getStylesheets().add(GUI.class.getResource("/Styles/Feed.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    private void createPostHistory(){
        /* root */
        VBox root = new VBox();
        root.setId("rootBox");
        root.setAlignment(Pos.TOP_CENTER);

        /* add top bar */
        root.getChildren().addAll(topBar(), new Text(""));

        if(user.getPostHistory() == null || user.getPostHistory().isEmpty()) {
            Label noPostsLabel = new Label("No Posts");
            root.getChildren().addAll(noPostsLabel, new Text());
            for (int i = 0; i < 45; i++) {
                root.getChildren().add(new Text(""));
            }
        }

        else {
            ArrayList<Post> posts = Main.client.findPosts(user.getPostHistory());

            /* add posts */
            for (Post post : posts) {
                root.getChildren().addAll(post(post), new Text());
            }

            /* add padding to complete page if there is only one post */
            if (posts.size() == 1) {
                root.getChildren().addAll(new Text(), new Text(), new Text(), new Text(), new Text());
            }
        }

        /* scroll pane */
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);

        /* set up scene and stage */
        Scene scene = new Scene(scrollPane, 1920, 980);
        scene.getStylesheets().add(GUI.class.getResource("/Styles/Feed.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
