package panel;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

public class LoginPanel extends JPanel {

    PanelSetting panelSettings;
    JFrame window;
    JLabel loginTitle;
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;
    Map<String, String> studentCred, teacherCred;

    public LoginPanel(JFrame window) {

        panelSettings = new PanelSetting(this);
        this.window = window;

        setPreferredSize(panelSettings.screenDimension);
        setBackground(panelSettings.screenColor);
        setLayout(new GridBagLayout());
        setFocusable(true);

        // Initialize the user credentials map
        studentCred = loadUserCredentials("src/database/credentials/student_cred.txt");
        teacherCred = loadUserCredentials("src/database/credentials/teacher_cred.txt");
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

    private Map<String, String> loadUserCredentials(String filePath) {
        Map<String, String> credentials = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String username = parts[1];
                    String password = parts[2];
                    credentials.put(username, password);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return credentials;
    }

    private class LoginListener implements java.awt.event.ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (studentCred.containsKey(username) && studentCred.get(username).equals(password)) {
                String studentID = getStudentID(username);
                if (studentID != null) {
                    writeCurrentUser("src/database/current_user.txt", studentID);
                    switchToStudentPanel();
                    System.out.println(studentCred);
                }
            } else if (teacherCred.containsKey(username) && teacherCred.get(username).equals(password)) {
                String teacherID = getTeacherID(username);
                if (teacherID != null) {
                    writeCurrentUser("src/database/current_user.txt", teacherID);
                    switchToTeacherPanel();
                    System.out.println(teacherCred);
                }
            } else {
                JOptionPane.showMessageDialog(LoginPanel.this, "Incorrect username or password");
            }
        }

        private String getStudentID(String username) {
            try (BufferedReader reader = new BufferedReader(new FileReader("src/database/credentials/student_cred.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 3 && parts[1].equals(username)) {
                        return parts[0]; // Return the student ID
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(LoginPanel.this, "Error occurred while reading student credentials!");
                ex.printStackTrace();
            }
            return null;
        }

        private String getTeacherID(String username) {
            try (BufferedReader reader = new BufferedReader(new FileReader("src/database/credentials/teacher_cred.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 3 && parts[1].equals(username)) {
                        return parts[0]; // Return the teacher ID
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(LoginPanel.this, "Error occurred while reading teacher credentials!");
                ex.printStackTrace();
            }
            return null;
        }

        private void writeCurrentUser(String filePath, String userID) {
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(userID);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(LoginPanel.this, "Error occurred while writing current user ID!");
                ex.printStackTrace();
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