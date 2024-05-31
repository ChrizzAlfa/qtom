package panel;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.JTextField;

import handler.UserHandler;

import javax.swing.JPasswordField;

import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import java.util.Map;

public class LoginPanel extends JPanel {

    PanelSetting panelSettings;
    JFrame window;
    JLabel loginTitle;
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;
    String studentCredPath, teacherCredPath;
    Map<String, String> studentCred, teacherCred;

    UserHandler userHandler = new UserHandler();

    public LoginPanel(JFrame window) {

        panelSettings = new PanelSetting(this);
        this.window = window;

        setPreferredSize(panelSettings.screenDimension);
        setBackground(panelSettings.screenColor);
        setLayout(new GridBagLayout());
        setFocusable(true);

        // Initialize the user credentials map
        studentCredPath = "src/database/credentials/student_cred.txt";
        teacherCredPath = "src/database/credentials/teacher_cred.txt";

        studentCred = userHandler.loadUserCredentials(studentCredPath);
        teacherCred = userHandler.loadUserCredentials(teacherCredPath);

        loginTitle = new JLabel("LOGIN");
        usernameField = new JTextField(15);
        loginButton = new JButton("Login");
        passwordField = new JPasswordField(15);

        // 'LOGIN' label
        panelSettings.addComponent(0, 0, 0, loginTitle, new Font("Arial", Font.BOLD, 30), Color.BLACK, new Insets(0, 0, 120, 0));

        // Username label and username input
        panelSettings.addComponent(0, 1, 0, new JLabel("Username"), null, null, null);
        panelSettings.addComponent(0, 2, 0, usernameField, null, null, null);

        // Password label and password input
        panelSettings.addComponent(0, 3, 0, new JLabel("Password"), null, null, null);
        panelSettings.addComponent(0, 4, 0, passwordField, null, null, null);

        // Login button
        loginButton.addActionListener(new LoginListener());
        loginButton.setBackground(new Color(55, 58, 64));
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(55, 58, 64), 5));
        panelSettings.addComponent(0, 5, 0, loginButton, new Font("Arial", Font.BOLD, 14), Color.WHITE, null);
    }

    private class LoginListener implements java.awt.event.ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (studentCred.containsKey(username) && studentCred.get(username).equals(password)) {
                String studentID = userHandler.getStudentID(username);
                if (studentID != null) {
                    userHandler.writeCurrentUser("src/database/current_user.txt", studentID);
                    switchToStudentPanel();
                    System.out.println(studentCred);
                }
            } else if (teacherCred.containsKey(username) && teacherCred.get(username).equals(password)) {
                String teacherID = userHandler.getTeacherID(username);
                if (teacherID != null) {
                    userHandler.writeCurrentUser("src/database/current_user.txt", teacherID);
                    switchToTeacherPanel();
                    System.out.println(teacherCred);
                }
            } else {
                JOptionPane.showMessageDialog(LoginPanel.this, "Incorrect username or password");
            }
        }

        private void switchToStudentPanel() {
            window.getContentPane().removeAll();
            window.add(new MenuStudentPanel(window));
            window.revalidate();
            window.repaint();
        }

        private void switchToTeacherPanel() {
            window.getContentPane().removeAll();
            MenuRightPanel rightPanel = new MenuRightPanel(window);
            window.add(new MenuLeftPanel(window, rightPanel), BorderLayout.WEST);
            window.add(rightPanel, BorderLayout.CENTER);
            window.revalidate();
            window.repaint();
        }
    }
}