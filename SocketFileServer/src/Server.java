import javax.xml.crypto.Data;
import java.net.*;
import java.io.*;
import java.nio.file.Files;

public class Server extends Thread{

    static final String MAIN_DIR = "REPLACE_WITH_USER_DIR";
    private final ServerSocket serverSocket;
    int PORT;

    public Server(int port) throws IOException{
        PORT = port;
        serverSocket = new ServerSocket(PORT);
    }

    public void run(){
        //Server stays open to accept requests
        //noinspection InfiniteLoopStatement
        while(true){
            //Temporary socket to pass to client handler
            Socket tempSocket = null;
            try{
                System.out.println("(sever) Waiting for client...");
                tempSocket = serverSocket.accept();
                System.out.println("Client connected on: " + tempSocket);

                //open new client handler
                Thread t = new APIRequestHandler(tempSocket, MAIN_DIR);
                t.start();
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }

}

class APIRequestHandler extends Thread{

    final Socket socket;
    final String MAIN_DIR;


    public APIRequestHandler(Socket socket, String MAIN_DIR){
        this.socket = socket;
        this.MAIN_DIR = MAIN_DIR;
    }

    public void run() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            //DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            String request = dis.readUTF();
            String[] splitRequest = request.split(",");

            switch (splitRequest[0]) {
                case "GET":
                    if(splitRequest[1].equals("0")) {
                        System.out.println("Sending file " + splitRequest[2]);
                        //sendFile(dos, MAIN_DIR + splitRequest[2]);
                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                        sendFile(dos, splitRequest[2]);
                        System.out.println("Connecting closed");
                    } else if(splitRequest[1].equals("1")){
                        System.out.println("Getting directory " + splitRequest[2]);
                    } else {
                        System.out.println("Sending server contents");
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        sendTree(oos);
                    }
                    break;
                case "POST":

                    downloadFile(dis, MAIN_DIR+splitRequest[2]);
                    break;
                case "DELETE":
                    System.out.println("Deleting file " + splitRequest[2]);
                    deleteFile(MAIN_DIR + splitRequest[2]);
                    break;
                default:
                    System.out.println("Invalid request");
            }

            socket.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendTree(ObjectOutputStream oos) throws IOException{

        Node node = new Node(MAIN_DIR);
        oos.writeObject(node);
        oos.close();

    }

    public void sendFile(DataOutputStream dos, String filePath) throws IOException {

        File file = new File(filePath);
        long length = file.length();
        byte[] bytes = new byte[(int)length];
        FileInputStream in = new FileInputStream(file);

        //noinspection ResultOfMethodCallIgnored
        in.read(bytes);
        dos.writeInt(bytes.length);
        dos.write(bytes, 0, bytes.length);
        dos.flush();

        System.out.println("File("+file.getName()+") was sent successfully)");
        System.out.println("Connection closing...");

        in.close();
        dos.close();
    }

    public void downloadFile(DataInputStream dis, String filePath) throws IOException {
        int length = dis.readInt();
        if(length>0){
            System.out.println("downloading '" + filePath + "' from server...");
            byte[] data = new byte[length];
            dis.readFully(data, 0, data.length);
            Files.write(new File(filePath).toPath(), data);
        }
        System.out.println("File '" + filePath + "' finished downloading");
    }

    public void deleteFile(String filePath) throws IOException {
        Files.delete(new File(filePath).toPath());
    }
}
