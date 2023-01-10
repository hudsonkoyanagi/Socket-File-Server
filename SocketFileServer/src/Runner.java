import java.io.IOException;

public class Runner {
    public static void main(String[] args) {
        try {
            Thread server = new Server(50000);
            server.run();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
