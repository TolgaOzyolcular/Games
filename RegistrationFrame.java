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
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;

public class RegistrationFrame extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Guessing Game");
		GridPane gridPane = createRgistrationFormPane();
		addRegistrationLayout(gridPane, primaryStage);

		Scene scene = new Scene(gridPane, 600, 500);
		// Set the scene in primary stage
		primaryStage.setScene(scene);
		scene.getStylesheets().add(Password.class.getResource("Password.css").toExternalForm());
		primaryStage.show();

	}

	private GridPane createRgistrationFormPane() {

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

	private void addRegistrationLayout(GridPane gridPane, Stage primaryStage) {
		Label headerLabel = new Label("Registration Form");
		headerLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
		headerLabel.setTextFill(Color.web("darkgreen"));
		gridPane.add(headerLabel, 0, 0, 2, 1);
		GridPane.setHalignment(headerLabel, HPos.CENTER);
		GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

		// Add User Name Label
		Label nameLabel = new Label("User Name: ");
		gridPane.add(nameLabel, 0, 1);

		// Add User Name Text Field
		TextField userNameField = new TextField();
		userNameField.setPrefHeight(40);
		gridPane.add(userNameField, 1, 1);
		
		userNameField.setStyle("-fx-text-inner-color: darkgreen;");

		// Add Password Label
		Label passwordLabel = new Label("Password : ");
		gridPane.add(passwordLabel, 0, 2);

		// Add Password Field
		PasswordField passwordField = new PasswordField();
		passwordField.setPrefHeight(40);
		gridPane.add(passwordField, 1, 2);
		passwordField.setEditable(false);

		Password passwordModel = new Password();
		gridPane.add(passwordModel.addPasswordLayout(passwordField, gridPane), 1, 3);
		
		passwordField.setStyle("-fx-text-inner-color: darkgreen;");

		// Add Submit Button
		Button submitButton = new Button("Register");
		submitButton.setPrefHeight(40);
		submitButton.setDefaultButton(true);
		submitButton.setPrefWidth(130);
		gridPane.add(submitButton, 0, 4, 2, 1);

		Button reset = new Button("Reset");
		reset.setPrefHeight(40);
		reset.setDefaultButton(true);
		reset.setPrefWidth(130);
		gridPane.add(reset, 0, 4, 2, 1);

		GridPane.setHalignment(submitButton, HPos.LEFT);
		GridPane.setMargin(submitButton, new Insets(20, 0, 20, 0));
		
		GridPane.setHalignment(reset, HPos.RIGHT);
		GridPane.setMargin(reset, new Insets(20, 0, 20, 0));
		
		submitButton.setStyle("-fx-font: 18 verdana; -fx-base: darkgreen;");
		reset.setStyle("-fx-font: 18 verdana; -fx-base: darkgreen;");

		reset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				passwordField.setText("");
				userNameField.setText("");
			}
		});

		submitButton.setOnAction(new EventHandler<ActionEvent>() {
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
				registerUser(user);

			}

			private void registerUser(User user) {
				DbConnection conn = new DbConnection();
				if (!conn.checkUserName(user)) {
					if (conn.register(user)) {
						showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(),
								"Registration Successful!",
								"Welcome " + userNameField.getText() + "\nClose this window and login to play");
						userNameField.setText("");
						passwordField.setText("");

					} else {
						showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Oops!",
								"Could not register this user");
					}
				} else {
					showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Oops!",
							"User already exists! Please try another username");
				}

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
