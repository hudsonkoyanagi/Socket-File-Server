import javax.crypto.CipherInputStream;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;


public class FileFrame extends JFrame{
    private JPanel mainPanel;
    private JButton downloadButton;
    private JTree fileTree;
    private JButton uploadButton;
    private Object currentSelection;

    public FileFrame(String title) {
        super(title);

        downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(currentSelection);
                APIClient client1 = new APIClient(50000, "GET", 0, (String) currentSelection);
                client1.start();
            }
        } );

        JFileChooser fileChooser = new JFileChooser();
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               fileChooser.showOpenDialog(mainPanel);
               //To be implemented: upload file
               System.out.println(fileChooser.getSelectedFile());
            }
        } );

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
    }

    public static void main(String[] args) {
        FileFrame x = new FileFrame("Koyanagi Cloud");
        x.setVisible(true);
    }

    private void createUIComponents() {
        DefaultMutableTreeNode t = new DefaultMutableTreeNode("Root");
        createTree(t, getNode());
        fileTree = new JTree(t);

        fileTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                        fileTree.getLastSelectedPathComponent();

                /* if nothing is selected */
                if (node == null) return;

                currentSelection= node.getUserObject();
            }
        });
    }

    public void createTree(DefaultMutableTreeNode t, Node n){
        for(String filePath:n.getFiles()){
            DefaultMutableTreeNode file = new DefaultMutableTreeNode(filePath);
            t.add(file);
        }
        for(Node subDir: n.getNodes()){
            DefaultMutableTreeNode dir = new DefaultMutableTreeNode(subDir.getFilePath());
            createTree(dir, subDir);
            t.add(dir);
        }
    }

    public Node getNode(){
        String req = "GET,2";
        Socket socket = null;
        Node n = null;
        try {
            socket = new Socket("localhost", 50000);

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(req);

            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());

            n = (Node) is.readObject();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return n;
    }
}
