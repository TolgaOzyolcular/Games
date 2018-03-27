package JavaFXGame;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.audio.*;
public class MusicPlayer implements Runnable{
    InputStream in=null;
    AudioStream audioStream=null;
    @Override
    public void run() {
        try {
            in = new FileInputStream("music.wav"); 
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

   
        try {
            audioStream = new AudioStream(in);
            AudioPlayer.player.start(audioStream);
        } catch (IOException ex) {
            Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
