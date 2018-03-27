package JavaFXGame;
import java.io.IOException;

public class TestThreads {

    
    public static void main(String[] args) throws IOException, InterruptedException{
        Thread thread1 = new Thread(new DisplaysNumbers());
        Thread thread2 = new Thread(new MusicPlayer());
        thread2.start();
        thread2.join();
        thread1.start();
    }
}
