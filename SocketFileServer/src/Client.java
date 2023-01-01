import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;


public class Client implements Runnable{
    private Node n;
    private String req;


    public Client(String req){
        this.req = req;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {

            socket = new Socket("localhost", 50000);

            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            dos.writeUTF(req);
            n = (Node) is.readObject();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Node getNode() {
        return n;
    }
}