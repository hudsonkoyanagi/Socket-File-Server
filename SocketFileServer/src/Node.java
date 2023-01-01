import java.io.File;
import java.io.Serializable;

public class Node implements Serializable {
    private Node[] nodes;
    private String[] files;
    private String filePath;

    public Node(String filePath){
        this.filePath = filePath;
        File dir = new File(filePath);

        int numDirs = 0;
        int numFiles = 0;

        File[] listDir = dir.listFiles();
        for(File f : listDir){
            if(f.isDirectory()){
                numDirs++;
            }
            else{
                numFiles++;
            }
        }

        nodes = new Node[numDirs];
        files = new String[numFiles];

        int dirInd = 0;
        int fileInd = 0;
        for(File f : listDir){
            if(f.isDirectory()){
                nodes[dirInd] = new Node(f.toString());
                dirInd++;
            }
            else{
                files[fileInd] = f.toString();
                fileInd++;
            }
        }

    }

    public Node[] getNodes() {
        return nodes;
    }

    public String getFilePath() {
        return filePath;
    }

    public String[] getFiles() {
        return files;
    }


    /*
    //old testing stuff
    public static void main(String[] args) {
        Node n = new Node("C:\\Users\\hudso\\Desktop\\Stuff");
        //traverse(n);
        for(Node i : n.nodes){
            System.out.println(i.filePath);
        }
        System.out.println(System.getProperty("user.home"));
    }


    public static void traverse(Node head){
        for(Node n: head.nodes){
            System.out.println(n.filePath);
            traverse(n);
        }
    }
     */
}
