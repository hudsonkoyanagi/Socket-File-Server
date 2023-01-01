import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI extends JFrame{
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordPasswordField;
    private JButton submitButton;

    public ClientGUI(String title){
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();


        submitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String username = usernameField.getText();
                char[] passwordArr = passwordPasswordField.getPassword();
                String password = new String(passwordArr);
                if(username.equals("h") && password.equals("k")) {
                    JOptionPane.showMessageDialog(null, "Login Success");
                    switchFrame();
                } else{
                    JOptionPane.showMessageDialog(null, "Login Failure");
                }
            }
        });
    }

    public void switchFrame(){
        this.dispose();
        FileFrame ff = new FileFrame("Koyanagi Cloud");
        ff.setVisible(true);
    }

    public static void main(String[] args) {
        ClientGUI x = new ClientGUI("Koyanagi Cloud");
        x.setVisible(true);
    }
}
