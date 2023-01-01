import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
    public static void main(String[] args){
        String dir = "/Users/hudsonkoyanagi/ServerFolder";
        Node n = new Node(dir);
        for(Node i : n.getNodes()){
            System.out.println(i.getFilePath());
            for(Node x : i.getNodes()){
                System.out.println(x.getFilePath());
            }
        }
    }
}