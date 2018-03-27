package JavaFXGame;
public class DisplaysNumbers implements Runnable{

    
    public DisplaysNumbers(){
        
    }
    @Override
    public void run() {
        int i=1;
        while(i<=100){
            System.out.println(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
               ex.printStackTrace();
            }
            i++;
        }
    }

}
