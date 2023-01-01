import java.io.*;
import java.net.*;
import java.nio.file.Files;

public class APIClient extends Thread{
    static final String DOWNLOAD_PATH = "/Users/hudsonkoyanagi/ClientFolder";
    static int PORT;
    String req;
    int type;
    String fileName;

    public APIClient(int port, String req, int type, String fileName){
        PORT = port;
        this.req = req;
        this.type = type;
        this.fileName = fileName;
    }

    @Override
    public void run(){
        try {
            //initialize socket connection
            Socket client = new Socket("localhost", PORT);
            System.out.println("Connected to server on " + client);

            //open data in/output streams for request and file transfer
            DataInputStream dis = new DataInputStream(client.getInputStream());
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());


            //sendAPIRequest(dos, "DELETE, 0, "testFile.rtf");
            //sendAPIRequest(dos, "GET", 0, "testFile.rtf");

            sendAPIRequest(dos, req, type, fileName);
            String[] arr = fileName.split("/");
            //downloadFile(dis, DOWNLOAD_PATH + arr[arr.length-1]);
            downloadFile(dis, arr[arr.length-1]);

            //sendAPIRequest(dos, "POST", 0, "testFile.rtf");
            //uploadFile(dos, DOWNLOAD_PATH + "testFile.rtf");

            System.out.println("Connection closing");

            dis.close();
            dos.close();
            client.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void sendAPIRequest(DataOutputStream dos, String request, int type, String fileName) throws IOException {
        String message = request + "," + type + "," + fileName;
        dos.writeUTF(message);
        System.out.println("Request: '" + message + "' was sent");
    }

    public static void downloadFile(DataInputStream dis, String filePath) throws IOException {
        //reads incoming bytes from server + writes byte array to new file
        //retrieves byte array length
        int length = dis.readInt();
        if(length>0){
            System.out.println("downloading '" + filePath + "' from server...");
            byte[] data = new byte[length];
            dis.readFully(data, 0, data.length);
            Files.write(new File(DOWNLOAD_PATH+"/"+filePath).toPath(), data);
        }
        System.out.println("File '" + filePath + "' finished downloading");
    }

    public static void uploadFile(DataOutputStream dos, String filePath) throws IOException {
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
    }
}