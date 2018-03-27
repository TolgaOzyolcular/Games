package JavaFXGame;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MainFrame extends Application {

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Guessing Game");
		GridPane gridPane = createLoginFormPane();
		addUIControls(gridPane, primaryStage);

		Scene scene = new Scene(gridPane, 600, 500);
		// Set the scene in primary stage
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e ->  System.exit(0));
		scene.getStylesheets().add(Password.class.getResource("Password.css").toExternalForm());
		primaryStage.show();

	}

	private GridPane createLoginFormPane() {

		GridPane gridPane = new GridPane();

		gridPane.setAlignment(Pos.CENTER);
		gridPane.setPadding(new Insets(40, 40, 40, 40));
		gridPane.setHgap(10);

		gridPane.setVgap(10);

		ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
		columnOneConstraints.setHalignment(HPos.RIGHT);
		ColumnConstraints columnTwoConstrains = new ColumnConstraints(200, 200, Double.MAX_VALUE);
		columnTwoConstrains.setHgrow(Priority.ALWAYS);

		gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

		return gridPane;
	}

	private void addUIControls(GridPane gridPane, Stage primaryStage) {
		
		Label projectUserName = new Label("");
		projectUserName.setFont(Font.font("TVerdana", FontWeight.BOLD, 18));
		Label headerLabel = new Label("Welcome to the Guessing Game");
		headerLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
		projectUserName.setTextFill(Color.web("darkgreen"));
		headerLabel.setTextFill(Color.web("darkgreen"));
		gridPane.add(headerLabel, 0, 0, 2, 1);
		gridPane.add(projectUserName,0, 5, 2, 1);
		GridPane.setHalignment(headerLabel, HPos.CENTER);
		GridPane.setHalignment(projectUserName, HPos.CENTER);
		GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

		// Add User Name Label
		Label nameLabel = new Label("User Name: ");
		gridPane.add(nameLabel, 0, 1);

		// Add User Name Text Field
		TextField userNameField = new TextField();
		userNameField.setPrefHeight(40);
		gridPane.add(userNameField, 1, 1);
		
		userNameField.setStyle("-fx-text-inner-color: darkgreen;");

		// Add Email Label
		Label passwordLabel = new Label("Password : ");
		gridPane.add(passwordLabel, 0, 2);

		// Add Email Text Field
		PasswordField passwordField = new PasswordField();
		passwordField.setPrefHeight(40);
		gridPane.add(passwordField, 1, 2);
		passwordField.setEditable(false);

		Password passwordModel = new Password();
		gridPane.add(passwordModel.addPasswordLayout(passwordField, gridPane), 1, 3);

		// Add Submit Button
		Button login = new Button("Login");
		login.setPrefHeight(40);
		login.setDefaultButton(true);
		login.setPrefWidth(130);
		gridPane.add(login, 0, 4, 2, 1);
		
		GridPane.setHalignment(login, HPos.LEFT);
		GridPane.setMargin(login, new Insets(20, 0, 20, 0));
		
		Button reset = new Button("Reset");
		reset.setPrefHeight(40);
		reset.setDefaultButton(true);
		reset.setPrefWidth(130);
		gridPane.add(reset, 0, 4, 2, 1);
		
		GridPane.setHalignment(reset, HPos.RIGHT);
		GridPane.setMargin(reset, new Insets(20, 0, 20, 0));
		

		Button registerButton = new Button("New User");
		registerButton.setPrefHeight(40);
		registerButton.setDefaultButton(true);
		registerButton.setPrefWidth(150);
		gridPane.add(registerButton, 0, 4, 2, 1);
		
		GridPane.setHalignment(registerButton, HPos.CENTER);
		GridPane.setMargin(registerButton, new Insets(20, 0, 20, 0));
		
		login.setStyle("-fx-font: 18 verdana; -fx-base: darkgreen;");
		reset.setStyle("-fx-font: 18 verdana; -fx-base: darkgreen;");
		registerButton.setStyle("-fx-font: 18 verdana; -fx-base: darkgreen;");
		

		reset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				passwordField.setText("");
				userNameField.setText("");
			}
		});
		login.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (userNameField.getText().isEmpty()) {
					showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Oops!",
							"Please enter username");
					return;
				}

				if (passwordField.getText().isEmpty()) {
					showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Oops!",
							"Please enter a password");
					return;
				}

				User user = new User(userNameField.getText(), passwordField.getText());
				loginUser(user);

			}

			private void loginUser(User user) {
				
				DbConnection conn = new DbConnection();
				if (conn.login(user)) {
					Main game = new Main(user.getUsername());
					Stage stage = new Stage();
					try {
						game.start(stage);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					stage.show();
					primaryStage.close();

				} else {
					showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Oops!",
							"Wrong username and/or password!");
				}

			}
		});

		registerButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				RegistrationFrame register = new RegistrationFrame();
				Stage stage = new Stage();
				try {
					register.start(stage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stage.show();

			}
		});

	}

	private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.initOwner(owner);
		alert.show();
	}

}
