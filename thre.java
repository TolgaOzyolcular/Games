package JavaFXGame;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class thre extends Application {

	private int MAX;
	static Timer timer;
	Label label;
	MediaPlayer player;

	@Override
	public void start(Stage primaryStage) {
		// Creating file chooser
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose .mp3 file");
		File f = fc.showOpenDialog(primaryStage);

		// Creating label to display countdown
		label = new Label("");
		label.setFont(Font.font("Microsoft YaHei UI", 20));

		// Creating Media links
		Media media = new Media(f.toURI().toString());
		MediaView mediaView = new MediaView(player);
		player = new MediaPlayer(media);
		player.setOnReady(new Runnable() {
			@Override
			public void run() {
				// Getting the duration of the .mp3 file
				MAX = (int) (media.getDuration().toSeconds());
				// Creating thread for countdown
				timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask() {
					int i = MAX;

					@Override
					public void run() {
						Platform.runLater(() -> {
							// Displaying value
							if (i == -1) {
								i = MAX;
								player.play();
							}
								
							label.setText("Left : " + i-- + " seconds");
						});
					}
				}, 0, 1000);
			}
		});
		
		

		// Creating thread
		Thread playMusic = new Thread() {
			@Override
			public void run() {
				player.play();

			}
		};
		// Playing music
		playMusic.start();

		HBox box = new HBox();
		box.setPadding(new Insets(15, 12, 15, 12));
		box.setSpacing(10);
		box.getChildren().addAll(mediaView, label);

		BorderPane bp2 = (BorderPane) primaryStage.getScene().getRoot();
		bp2.setTop(box);

	}

	public void stop() throws Exception {
		super.stop();
		player.stop();
		timer.cancel();

	}

	

	/*
	 * public static void main(String[] args) { launch(args); }
	 */
}
