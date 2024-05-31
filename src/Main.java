import javax.swing.JFrame;
import panel.LoginPanel;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("Qtom");

        LoginPanel loginPanel = new LoginPanel(window);
        window.add(loginPanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}