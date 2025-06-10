package com.mycompany.connectfourgame;


       
 
//The big list of imports 
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.geometry.Point2D;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Optional;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
 
// ConnectFourLoginPage.java
 
// Author: Galib Yasar Kabir and Swopnil Dahal (assisted by ChatGPT in figuring out the logic)
public class ConnectFourGame extends Application {
    private final Map<String, String> accounts = new HashMap<>(); // Stores username-password pairs

    /**
     *
     * @param primaryStage
     */
    @Override
        public void start(Stage primaryStage) {
            loadAccountsFromFile();
            // Title text
            Text title = new Text("Connect Four");
            title.setFont(Font.font("times new roman", FontWeight.BOLD, 26));
            title.setStyle("-fx-fill: #000000");
 
            // Username input field
            Label usernameLabel = new Label("Username:");
            usernameLabel.setFont(Font.font("Aharoni", FontWeight.NORMAL, 13));
            TextField usernameField = new TextField();
 
            // Password input field
            Label passwordLabel = new Label("Password:");
            passwordLabel.setFont(Font.font("Aharoni", FontWeight.NORMAL, 13));
            PasswordField passwordField = new PasswordField();
 
            // Buttons for login and account creation
            Button loginButton = new Button("Login");
            Button createAccountButton = new Button("Create Account");
            Label statusLabel = new Label(); // Displays status messages
 
            // Grid layout for the form
            GridPane gridPane = new GridPane();
            gridPane.setAlignment(Pos.CENTER);
            gridPane.setHgap(15);
            gridPane.setVgap(15);
 
            // Add components to the grid
            gridPane.add(usernameLabel, 0, 0);
            gridPane.add(usernameField, 1, 0);
            gridPane.add(passwordLabel, 0, 1);
            gridPane.add(passwordField, 1, 1);
            gridPane.add(loginButton, 1, 2);
            gridPane.add(createAccountButton, 0, 2);
            gridPane.add(statusLabel, 1, 4);
 
            // Vertical layout for the entire scene
            VBox vbox = new VBox(20);
            vbox.setStyle("-fx-background-color: aqua;");
            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(title, gridPane);
 
            // Login button 
            loginButton.setOnAction(e -> {
                String username = usernameField.getText();
                String password = passwordField.getText();
                String hashedPassword = PasswordHasher.hashPassword(password);
 
                if (authenticate(username, hashedPassword)) {
                    statusLabel.setText("Login successful! Starting Connect Four...");
                    
                    //Begin the Connect4 game.
                    try {
                        Menu(primaryStage);
                    } catch (Exception ex) {
            }
                } else {
                    statusLabel.setText("Invalid username or password.");
                }
            });
 
            // Create account button 
            createAccountButton.setOnAction(e -> {
                String username = usernameField.getText();
                String password = passwordField.getText();
 
                if (username.isEmpty() || password.isEmpty()) {
                    statusLabel.setText("Username and password cannot be empty.");
                } else if (accounts.containsKey(username)) {
                    statusLabel.setText("Username already exists. Choose another.");
                } else {
                    String hashedPassword = PasswordHasher.hashPassword(password);
                    accounts.put(username, hashedPassword);
                    saveAccountsToFile();
                    statusLabel.setText("Account created successfully!");
                }
            });
 
            // Set up and display the stage
            Scene scene = new Scene(vbox, 400, 250);
            primaryStage.setTitle("Connect Four Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
 
        // Authenticate user by checking if the username and hashed password match
        private boolean authenticate(String username, String hashedPassword) {
            return accounts.containsKey(username) && accounts.get(username).equals(hashedPassword);
        }
        // Debugging done here by Galib to store passwords.
        private void saveAccountsToFile() {
             try (BufferedWriter writer = new BufferedWriter(new FileWriter("accounts.txt"))) {
                 for (Map.Entry<String, String> entry : accounts.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
              }
           } catch (IOException e) {
           }
        }
        private void loadAccountsFromFile() {
             try (BufferedReader reader = new BufferedReader(new FileReader("accounts.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        accounts.put(parts[0], parts[1]);
                    }
                }
             } catch (IOException e) {
             }
        }

        public static void main(String[] args) {
            launch(args);
        }
    
 
// Password hashing class, hashes the password using SHA-256 algorithm
static class PasswordHasher {
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return byteToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found", e);
        }
    }
 
    // Converts byte array to hexadecimal string
    private static String byteToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
 
//Connect 4 main game coding done by Sarwat, most of the code is all in a bunch of methods,
//so the main code, in order to start will be done by calling Begin method. 
//The begin method will also start the main menu, which is coded by Nidah.
 
// Initializes the main variables and constants needed for the Connect 4 game,
// including board dimensions, tracking player turns, and setting up a pane for
// graphical elements
    private static final int TILE_SIZE = 80;
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;
 
    private boolean redMove = true;
    private Disc[][] grid = new Disc[COLUMNS][ROWS];
 
    private Pane discRoot = new Pane();
 
    // Organizes and returns the complete graphical layout for the Connect 4 game board
    private Parent createContent()
    {
        
        Pane root = new Pane();
        root.getChildren().add(discRoot);
 
        Shape grideShape = makeGrid();
        root.getChildren().add(grideShape);
        root.getChildren().addAll(makeColumns());
 
        return root;
    }
 
    // Constructs and styles the Connect 4 game board's grid
    private  Shape makeGrid()
    {
        Shape shape = new Rectangle((COLUMNS + 1) * TILE_SIZE, (ROWS + 1) * TILE_SIZE);
 
        for(int y = 0; y < ROWS; y++)
        {
            for (int x = 0; x < COLUMNS; x++)
            {
                Circle circle = new Circle(TILE_SIZE / 2);
                circle.setCenterX(TILE_SIZE / 2);
                circle.setCenterY(TILE_SIZE / 2);
 
                circle.setTranslateX(x * (TILE_SIZE + 5) + TILE_SIZE / 4);
                circle.setTranslateY(y * (TILE_SIZE + 5) + TILE_SIZE / 4);
 
                shape = Shape.subtract(shape, circle);
            }
        }
 
        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(30.0);
 
        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);
 
        shape.setFill(Color.BLUE);
        shape.setEffect(lighting);
 
        return shape;
    }
 
    // Creates a list of interactive rectangular areas that correspond
    // to the columns on the Connect 4 game board.
    // These areas allow players to interact with the game
    // by clicking to drop discs in the desired column
    private List<Rectangle> makeColumns()
    {
        List<Rectangle> list = new ArrayList<>();
 
        for(int x = 0; x < COLUMNS; x++)
        {
            Rectangle rect = new Rectangle(TILE_SIZE, (ROWS + 1) * TILE_SIZE);
            rect.setTranslateX(x * (TILE_SIZE + 5) + TILE_SIZE / 4);
            rect.setFill(Color.TRANSPARENT);
 
            rect.setOnMouseEntered(e -> rect.setFill(Color.rgb(200, 200, 50, 0.3)));
            rect.setOnMouseExited(e -> rect.setFill(Color.TRANSPARENT));
 
            final int column = x;
            rect.setOnMouseClicked(e -> placeDisc(new Disc(redMove), column));
 
            list.add(rect);
        }
        return list;
    }
 
    // Handles placing a game piece (disc) into the specified column.
    // It manages the disc's logical position in the grid, its graphical position,
    // and checks if the game has ended after the move
    // This was a fix to our old issue of being able to place disks too fast, and was fixed in 
    //  debugging with John.
     private boolean isAnimating = false; // Add a lock to prevent clicks during animations
 
    private void placeDisc(Disc disc, int column) {
        if (isAnimating) return; // Prevent placing another disc if animation is ongoing
 
        int row = ROWS - 1;
        do {
            if (!getDisc(column, row).isPresent())
                break;
            row--;
        } while (row >= 0);
        if (row < 0)
            return;
 
        grid[column][row] = disc;
        discRoot.getChildren().add(disc);
        disc.setTranslateX((column * (TILE_SIZE + 5) + TILE_SIZE / 4));
 
        final int currentRow = row;
 
        isAnimating = true; // Lock further clicks during the animation
        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5), disc);
        animation.setToY((row * (TILE_SIZE + 5) + TILE_SIZE / 4));
        animation.setOnFinished(e -> {
            isAnimating = false; // Unlock clicks after the animation is done
 
            if (gameEnded(column, currentRow)) {
                gameOver();
            }
            redMove = !redMove; // Toggle after animation
        });
        animation.play();
    }
 
    // Checks if the game has been won after a disc is placed in the specified column and row.
    // It looks for four connected discs in vertical, horizontal, or diagonal directions
    private  boolean gameEnded(int column, int row)
    {
        List<Point2D> vertical = IntStream.rangeClosed(row - 3 , row + 3)
                .mapToObj(r -> new Point2D(column, r))
                .collect(Collectors.toList());
 
        List<Point2D> horizontal = IntStream.rangeClosed(column - 3 , column + 3)
                .mapToObj(c -> new Point2D(c, row))
                .collect(Collectors.toList());
 
        Point2D topLeft = new Point2D(column - 3, row - 3);
        List<Point2D> diagonal1 = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> topLeft.add(i, i))
                .collect(Collectors.toList());
 
        Point2D botLeft = new Point2D(column - 3, row + 3);
        List<Point2D> diagonal2 = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> botLeft.add(i, -i))
                .collect(Collectors.toList());
 
        return checkRange(vertical) || checkRange(horizontal)
                || checkRange(diagonal1) || checkRange(diagonal2);
 
    }
 
    // Checks whether there are four consecutive discs
    // of the same color in the given list of positions
    private boolean checkRange(List<Point2D> points)
    {
        int chain = 0;
        for (Point2D p : points)
        {
            int column = (int) p.getX();
            int row = (int) p.getY();
            Disc disc = getDisc(column, row).orElse(new Disc(!redMove));
            if(disc.red == redMove) {
                chain++;
                if (chain == 4) {
                    return true;
                }
            } else {
                chain = 0;
            }
        }
        return false;
    }
 
    // Handles the game's end by announcing the winner
    private void gameOver() {
        // Determine the winner
        String winner = redMove ? "Red" : "Yellow";
 
        // Create a new stage for the winner announcement
        Stage winnerStage = new Stage();
        winnerStage.setTitle("Game Over");
 
        // Create a layout
        VBox layout = new VBox(10);
        layout.setStyle("-fx-background-color: aqua;");
        layout.setAlignment(Pos.CENTER);
 
        // Add winner message
        Label winnerLabel = new Label("Winner: " + winner + "!");
        winnerLabel.setFont(new Font("Arial", 20));
 
        // Create buttons
        Button restartButton = new Button("Restart");
        Button exitButton = new Button("Exit");
 
        // Add button actions
        restartButton.setOnAction(e -> {
            // Close the winner stage
            winnerStage.close();
 
            // Restart the entire application
            openNewGame();
        });
 
        exitButton.setOnAction(e -> {
            Platform.exit();
        });
 
        // Add components to the layout
        layout.getChildren().addAll(winnerLabel, restartButton, exitButton);
 
        // Create a scene and set it on the stage
        Scene winnerScene = new Scene(layout, 300, 200);
        winnerStage.setScene(winnerScene);
 
        // Show the winner stage
        winnerStage.show();
    }
 
    // Used to safely retrieve discs from the grid, particularly in other methods like checkRange,
    // where it avoids NullPointerException by handling empty slots
    private Optional<Disc> getDisc(int column, int row)
    {
        if(column < 0 || column >= COLUMNS
                || row < 0 || row >= ROWS)
            return Optional.empty();
 
        return Optional.ofNullable(grid[column][row]);
    }
 
    // The Disc class represents a game piece in the Connect 4 game.
    // It is a static nested class that extends Circle from the JavaFX library,
    // which means it inherits all the graphical properties and behavior of a circle
    private static class Disc extends Circle
    {
        private final boolean red;
 
        public Disc(boolean red) {
            super(TILE_SIZE / 2, red ? Color.RED : Color.YELLOW);
            this.red = red;
            setCenterX(TILE_SIZE / 2);
            setCenterY(TILE_SIZE / 2);
        }
    }
    
    //This method is written by Chrissy for some extra fixes in the restart to make it flow better
    //This is to start the game back again instead of bringing you back to a login screen
    //The logic for figuring out a little bit here was assisted by ChatGPT to figure some of
    // the complexities out or a plan to follow, this also assisted in learning to commands.
    private void openNewGame() {
        
        //Close the old game stage
        Stage currentStage = (Stage) discRoot.getScene().getWindow();
        currentStage.close();
        
        Stage newBoard = new Stage();
        
        try {
            begin(newBoard);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        //Show the stage
        newBoard.show();
        
    }
    // Sets a method to launche the game window
    public void begin (Stage stage) throws Exception
    {
        //Following section was coded by Chrissy to help with some logical issues
        redMove = true; //Set the first turn as the red players.
        grid = new Disc[COLUMNS][ROWS]; //Clears the board.
        discRoot.getChildren().clear(); //Clear any discs left on the board.
        //End of section added by Chrissy.
        
        //Create the actual board
        stage.setScene (new Scene(createContent()));
        stage.show();
        
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.setFullScreenExitHint("");
    }
    
    //Coded by Nidah. There were some issues at first with it not being coded exactly in JavaFX, however with a bit
    //And some assistance from ChatGPT (Not direct copying, but help with translation) we managed to get it fully
    //Converted into JavaFX
    private Stage primaryStage;
    
    public void Menu(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("Connect 4");
 
        // Create menu scene
        VBox menuLayout = new VBox(20);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setStyle("-fx-alignment: center; -fx-padding: 20;");
 
        Label titleLabel = new Label("Connect 4");
        titleLabel.setFont(Font.font("DialogInput", FontWeight.BOLD, 50));
        titleLabel.setTextFill(Color.DARKBLUE);
    
        Button startButton = new Button("Start");
        Button backButton = new Button("Exit");
 
        String buttonStyle = "-fx-background-color: #F0EAD6; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 2;";
        startButton.setStyle(buttonStyle);
        backButton.setStyle(buttonStyle);
    
        startButton.setPrefSize(100, 80);
        backButton.setPrefSize(100, 80);
 
        startButton.setOnAction(e -> showGamePanel());
        backButton.setOnAction(e -> primaryStage.close());
        
        Label extraLabel = new Label("A game for friends about connecting 4 discs in a row.");
        titleLabel.setFont(Font.font("DialogInput", FontWeight.BOLD, 25));
        titleLabel.setTextFill(Color.DARKBLUE);
        
        menuLayout.getChildren().addAll(titleLabel, startButton, backButton, extraLabel);
 
        Scene menuScene = new Scene(menuLayout, 500, 600);
        menuLayout.setStyle("-fx-background-color: aqua;");
        
        // Set initial scene
        primaryStage.setScene(menuScene);
        primaryStage.show();
    } 
    private void showGamePanel() {
        try {
            begin(primaryStage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
