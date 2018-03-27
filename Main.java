package JavaFXGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.canvas.*;
import javafx.scene.shape.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.event.*;
import javafx.util.Duration;
import java.util.concurrent.TimeUnit;
import java.net.URL;
import java.util.Random;
import javafx.scene.media.*;
import javafx.scene.text.Font;

public class Main extends Application {

	/** array of buttons **/
	private Button buttons[];
	TextField creditField;
	private Button playGame, viewScore, refillCredit, playMusic;
	// Random number generator
	private Random random;

	// Number going to be guessed
	private int numberToGuess;

	// Graphics Context to draw images
	private GraphicsContext gc;

	/** Timer Label **/
	private Label timerLabel;

	AudioClip clip; // audio clip
	private int chances; // chances max 10
	private boolean play; // run the timer
	private String username;
	private String songName = "music.wav"; // Song File
	DbConnection connection = new DbConnection();
	thre musc = new thre();

	/**
	 * Function to prepare the Primary Stage to display in Root pane. The function
	 * calls other house keeping methods to generate the GUI for the Game and
	 * display components in the scene.
	 * 
	 * Left side Grid will display the Button panel and right side grid will display
	 * a panel for drawing
	 * 
	 */

	public Main(String username) {
		this.username = username;
	}

	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 800, 700);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// Set the Scene of the Primary Stage
			primaryStage.setScene(scene);
			primaryStage.setTitle("Guessing Game");

			// Number to guess
			random = new Random();
			numberToGuess = random.nextInt(100) + 1;

			// Add the Button grid into the Scene
			this.createButtons(root);

			// Create Canvas for drawing
			this.createCanvas(root);
			disable(true);

			// Display the Scene
			primaryStage.setOnCloseRequest(e ->  System.exit(0));

			root.setBottom(addNavHBox(primaryStage));
			creditField.setText("");
			creditField.setText("Credit: " + connection.getCredit(username));
			primaryStage.show();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HBox addNavHBox(Stage primaryStage) {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: lightgrey;");
		
		Image imageDollor = new Image(getClass().getResourceAsStream("Dollar.png"));
		Image imageScore = new Image(getClass().getResourceAsStream("Score.png"));
		Image imageMusic = new Image(getClass().getResourceAsStream("Music.png"));
		

		playGame = new Button("Play Game");
		playGame.setPrefSize(150, 65);
		playGame.setStyle("-fx-font: 18 verdana; -fx-base: darkgreen;");

		playMusic = new Button("", new ImageView(imageMusic));
		playMusic.setPrefSize(150, 65);
		playMusic.setStyle("-fx-base: darkgreen;");

		refillCredit = new Button("", new ImageView(imageDollor));
		refillCredit.setPrefSize(150, 65);
		refillCredit.setStyle("-fx-base: darkgreen;");

		viewScore = new Button("", new ImageView(imageScore));
		viewScore.setPrefSize(150, 65);
		viewScore.setStyle("-fx-base: darkgreen;");

		creditField = new TextField();
		creditField.setPrefSize(150, 65);
		creditField.setEditable(false);

		hbox.getChildren().addAll(playGame, viewScore, playMusic, refillCredit, creditField);

		playGame.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (connection.getCredit(username) > 0) {
					disable(false);
					playGame.setDisable(true);
					play = true;
					startTask();

				} else {
					doMessage("You don't have enough credit to play the game, Please refill credits: "
							+ connection.getCredit(username));
				}
				
			}
		});

		playMusic.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				
				try {
					musc.start(primaryStage);
					final URL resource = getClass().getResource(songName);
					clip = new AudioClip(resource.toString());
					clip.setCycleCount(AudioClip.INDEFINITE);
					clip.play();
				} catch (Exception e) {
					System.out.println("Unable to play music: " + e);
				}

			}
		});

		refillCredit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (connection.refillCredit(username, connection.getCredit(username) + 20)) {
					doMessage("Credit Refilled with 20");
					creditField.setText("");
					creditField.setText("Credit: " + connection.getCredit(username));
				} else {
					doMessage("Couldnot Refill Credit");
				}

			}
		});

		viewScore.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				doMessage("Your score: " + connection.getOverAllScore(username));

			}
		});

		return hbox;
	}

	/**
	 * Start the background task to update the timer
	 */
	public void startTask() {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				runTask();
			}
		};

		// start the thread for the timer
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}

	/**
	 * Function to disable the game either win or lost
	 */
	private void disable(boolean enable) {

		// stop timer
		play = false;

		// disable buttons
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setDisable(enable);

		}

	}

	/**
	 * Run the background task to update the timer counter
	 */

	public void runTask() {
		int i = 0;
		User user = new User();
		user.setUsername(username);
		while (play) {
			i++;
			try {
				String update = String.format("Timer: 00:%02d", i);

				// Update the Label on the JavaFx Application Thread
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						user.setCredit(connection.getCredit(username) - 1);
						connection.updateRealCredit(user);
						timerLabel.setText(update);
						creditField.setText("");
						creditField.setText("Credit: " + connection.getCredit(username));
					}
				});

				Thread.sleep(1000);

				// Cycle back
				if (i == 39) {
					i = 0;
					
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Function to create a Canvas Panel to draw the Human body based on the Missing
	 * Guesses count.
	 * 
	 * @param root
	 */
	public void createCanvas(BorderPane root) {

		Canvas canvas = new Canvas(300, 550);
		this.gc = canvas.getGraphicsContext2D();

		// set the back color of canvas
		this.gc.setFill(Color.rgb(204, 230, 255));
		this.gc.fillRect(0, 0, 300, 550);

		root.setRight(canvas);
	}

	/**
	 * Draw Human Body based on Changes.
	 */
	private void drawBody() {

		// set the back color of canvas
		this.gc.setFill(Color.rgb(204, 230, 255));
		this.gc.fillRect(0, 0, 300, 550);

		if (chances == 1) {
			drawHead();
		} else if (chances == 2) {
			drawHead();
			drawEyes();
		} else if (chances == 3) {
			drawHead();
			drawEyes();
			drawMouth();
		} else if (chances == 4) {
			drawHead();
			drawEyes();
			drawMouth();
			drawNose();
		} else if (chances == 5) {
			drawHead();
			drawEyes();
			drawMouth();
			drawNose();
			drawEar();
		} else if (chances == 6) {
			drawHead();
			drawEyes();
			drawMouth();
			drawNose();
			drawEar();
			drawNeck();
		} else if (chances == 7) {
			drawHead();
			drawEyes();
			drawMouth();
			drawNose();
			drawEar();
			drawNeck();
			drawChest();
		} else if (chances == 8) {
			drawHead();
			drawEyes();
			drawMouth();
			drawNose();
			drawEar();
			drawNeck();
			drawChest();
			drawArms();
		} else if (chances == 9) {
			drawHead();
			drawEyes();
			drawMouth();
			drawNose();
			drawEar();
			drawNeck();
			drawChest();
			drawArms();
			drawLegs();
		} else if (chances == 10) {
			drawHead();
			drawEyes();
			drawMouth();
			drawNose();
			drawEar();
			drawNeck();
			drawChest();
			drawArms();
			drawLegs();
			drawLost();
		}
	}

	/**
	 * Helper function to draw Head of the Human Body
	 */
	private void drawHead() {

		this.gc.setFill(Color.rgb(0, 153, 204));
		this.gc.fillOval(120, 50, 60, 60);
	}

	/**
	 * Draw 2 Eyes on the face of the body
	 */
	private void drawEyes() {
		this.gc.setFill(Color.rgb(255, 255, 255));
		this.gc.fillOval(135, 70, 10, 8);
		this.gc.fillOval(155, 70, 10, 8);
	}

	/**
	 * Function to draw mouth
	 */
	private void drawMouth() {
		this.gc.setFill(Color.rgb(255, 255, 255));
		this.gc.fillOval(142, 90, 15, 8);
	}

	/**
	 * Function to draw nose.
	 */
	private void drawNose() {
		this.gc.setFill(Color.rgb(0, 0, 0));
		this.gc.fillOval(147, 70, 7, 15);
	}

	/**
	 * Function to draw years
	 */
	public void drawEar() {
		this.gc.setFill(Color.rgb(0, 153, 204));
		this.gc.fillOval(110, 70, 10, 15);
		this.gc.fillOval(180, 70, 10, 15);
	}

	/**
	 * Function to draw neck
	 */
	public void drawNeck() {
		this.gc.setFill(Color.rgb(0, 153, 204));
		this.gc.fillRect(147, 110, 6, 25);
	}

	/**
	 * Function to draw Chest
	 */
	public void drawChest() {
		this.gc.setFill(Color.rgb(0, 153, 204));
		this.gc.fillRect(130, 135, 40, 100);
	}

	/**
	 * Function to draw arms
	 */
	public void drawArms() {
		this.gc.setStroke(Color.rgb(0, 153, 204));
		this.gc.setLineWidth(4);
		this.gc.strokeLine(90, 185, 130, 140);
		this.gc.strokeLine(210, 185, 170, 140);

	}

	/**
	 * Function to draw Legs
	 */
	public void drawLegs() {
		this.gc.setStroke(Color.rgb(0, 153, 204));
		this.gc.setLineWidth(4);
		this.gc.strokeLine(110, 335, 135, 235);
		this.gc.strokeLine(190, 335, 165, 235);
		this.gc.fillRect(45, 335, 200, 100);
	}

	/**
	 * Draw Lost
	 */
	public void drawLost() {
		this.gc.setStroke(Color.rgb(255, 0, 0));
		this.gc.setLineWidth(2);
		this.gc.strokeArc(90, 0, 120, 120, 180, 180, ArcType.OPEN);

	}

	/**
	 * Helper function to create 100 Buttons in a Grid of 500x500 size Each button
	 * will be of Size 50x50
	 * 
	 * @param scene
	 */
	public void createButtons(BorderPane root) {

		// Allocate buttons
		this.buttons = new Button[100];

		// Create a Grid
		VBox box = new VBox(2);
		box.setPadding(new Insets(1));

		timerLabel = new Label("Let's Play!");
		timerLabel.setPrefSize(200, 40);
		timerLabel.setPadding(new Insets(0, 0, 0, 20));
		timerLabel.setFont(Font.font("Verdana", 34));
		timerLabel.setTextFill(Color.web("darkgreen"));

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(15, 12, 15, 12));
		grid.setHgap(1);
		grid.setVgap(1);
		int index = 0;

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {

				buttons[index] = new Button(String.valueOf(index + 1));
				buttons[index].setPrefSize(50, 50);
				buttons[index].setStyle("-fx-font-family: Verdana; -fx-text-fill: darkgreen; -fx-background-radius: 8,7,6;");
				buttons[index].setOnAction(new EventHandler<ActionEvent>() {

					@SuppressWarnings("deprecation")
					@Override
					public void handle(ActionEvent event) {

						Button button = (Button) event.getSource();

						// Parse the value
						int guess = Integer.parseInt(button.getText());

						// if the number is low, make the button yellow
						if (guess < numberToGuess) {
							button.setStyle("-fx-background-color: yellow");
							chances++;
						} else if (guess > numberToGuess) {
							// Red if the guess is higher
							button.setStyle("-fx-background-color: red");
							chances++;
						} else {

							// Green if the Guess is correct
							try {
								button.setStyle("-fx-background-color: darkgreen");
								doMessage("Bravo! You won the Game");
								disable(true);

								playMusic.setDisable(true);
								User user = new User();
								user.setUsername(username);
								user.setOverall_score(connection.getOverAllScore(username) + 20);
								connection.updateScore(user);
								musc.stop();
								
								
							} catch (Exception e) {
								System.out.println("Unable to stop music: " + e);
							}

							return;
						}

						drawBody();

						if (chances == 10) {
							try {
								doMessage("Uh oh! You've lost the game.");
								disable(true);
								playMusic.setDisable(true);
								musc.stop();

								
							} catch (Exception e) {
								System.out.println("Unable to stop music: " + e);
							}
						}
					}
				});

				grid.add(buttons[index], j, i);
				index++;
			}
		}

		// add to scene
		box.getChildren().addAll(this.timerLabel, grid);
		root.setCenter(box);
	}

	// Display message of win or lost
	public void doMessage(String message) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Guessing Game");
		alert.setContentText(message);
		alert.show();
	}

}
