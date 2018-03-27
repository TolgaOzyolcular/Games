package JavaFXGame;

import java.util.ArrayList;
import java.util.Collections;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Password extends Application {

    private final int[] pwArray;

    //Default constructor to initialise values
    public Password() {
        //Setting five 4 lettered hard-coded passwords
        this.pwArray = new int[]{1111, 2222, 3333, 1234, 5555};
    }

    @Override
    public void start(Stage primaryStage) {
        //Creating the text field to enter data and label to print the result
        final TextField txt = new TextField();
        txt.setFont(Font.font("Microsoft YaHei UI", 16));
        final Label print = new Label();
        print.setFont(Font.font("Microsoft YaHei UI", 16));
        VBox vb1 = new VBox();
        vb1.setPadding(new Insets(15, 12, 15, 12));
        vb1.setSpacing(10);
        vb1.getChildren().addAll(txt, print);

        //Creating the input panel using labels
        final Label[] label = new Label[10];
        HBox hb1 = new HBox();
        hb1.setSpacing(10);
        HBox hb2 = new HBox();
        hb2.setSpacing(10);
        
        //Creating a list of randomly generated integers
        ArrayList<Integer> num = shuffle();
        for (int i = 1; i <= 10; i++) {
            //Creating each label
            label[i - 1] = new Label(num.get(i - 1) + "");
            String m = label[i - 1].getText();
            //Creating mouse click event for each label
            label[i - 1].setOnMouseClicked((MouseEvent e) -> {
                String s = txt.getText();
                //Checking if the entered value is four lettered or not
                if (s.length() < 4) {
                    txt.setText(s + m);
                    //Checking for the correctness of the entered password
                    if (txt.getText().length() == 4) {
                        String p = "Password Incorrect!!!";
                        for (int n : pwArray) {
                            //Checking if the entered password is correct or not
                            if (n == Integer.parseInt(txt.getText().trim())) {
                                p = "Password Correct!!!";
                            }
                        }
                        //Setting the label to print the final result
                        print.setText(p);
                    } else print.setText("");
                }
            });
            label[i - 1].setId("bevel-grey");
            //Adding the input panel to the UI
            if (i <= 5) {
                hb1.getChildren().add(label[i - 1]);
            } else {
                hb2.getChildren().add(label[i - 1]);
            }
        }

        VBox vb = new VBox();
        vb.setPadding(new Insets(15, 12, 15, 12));
        vb.getChildren().addAll(hb1, hb2);
        BorderPane bp = new BorderPane();
        bp.setCenter(vb);

        BorderPane root = new BorderPane();
        root.setCenter(vb1);
        root.setBottom(bp);

        Scene scene = new Scene(root, 300, 250);
        scene.getStylesheets().add(Password.class.getResource("Password.css").toExternalForm());
        primaryStage.setTitle("Password");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Method to create an array list containing shuffled numbers
    public ArrayList<Integer> shuffle() {
        ArrayList<Integer> num = new ArrayList<>();
        //Filling the array list
        for (int i = 0 ; i < 10 ; i++) num.add(i);
        //Shuffling the list
        Collections.shuffle(num);
        //Returning the list
        return num;
    }
    
    public VBox addPasswordLayout(PasswordField txt,GridPane gridPane) {
    	 final Label[] label = new Label[10];
         HBox hb1 = new HBox();
         hb1.setSpacing(10);
         HBox hb2 = new HBox();
         hb2.setSpacing(10);
         
         ArrayList<Integer> num = shuffle();
         for (int i = 1; i <= 10; i++) {
             //Creating each label
             label[i - 1] = new Label(num.get(i - 1) + "");
             String m = label[i - 1].getText();
             //Creating mouse click event for each label
             label[i - 1].setOnMouseClicked((MouseEvent e) -> {
                 String s = txt.getText();
                 //Checking if the entered value is four lettered or not
                 if (s.length() < 4) {
                     txt.setText(s + m);
                     //Checking for the correctness of the entered password
                     if (txt.getText().length() == 4) {
                         String p = "";
                         for (int n : pwArray) {
                             //Checking if the entered password is correct or not
                             if (n != Integer.parseInt(txt.getText().trim())) {
                                 p = "Password Incorrect!!!";
                                 return;
                             }
                         }
                         //Setting the label to print the final result
//                         print.setText(p);
                         doMessage(p);
                     } else {
//                    	 print.setText("");
                    	 }
                 }
             });
             label[i - 1].setId("bevel-grey");
             //Adding the input panel to the UI
             if (i <= 5) {
                 hb1.getChildren().add(label[i - 1]);
             } else {
                 hb2.getChildren().add(label[i - 1]);
             }
         }
         
         VBox vb = new VBox();
         vb.setSpacing(10);
         vb.setPadding(new Insets(15, 12, 15, 12));
         vb.getChildren().addAll(hb1, hb2);
		return vb;
    }
    
    public void doMessage(String message) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Guessing Game");
		alert.setContentText(message);
		alert.show();
	}
    
 /*public static void main(String[] args) {
        launch(args);
    }*/

}
