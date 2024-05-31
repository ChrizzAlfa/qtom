package handler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

public class UserHandler {

    public Map<String, String> loadUserCredentials(String filePath) {
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

    public String getStudentID(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/database/credentials/student_cred.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3 && parts[1].equals(username)) {
                    return parts[0]; // Return the student ID
                }
            }
        } catch (IOException e) {
            // JOptionPane.showMessageDialog(LoginPanel.this, "Error occurred while reading student credentials!");
            e.printStackTrace();
        }
        return null;
    }

    public String getTeacherID(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/database/credentials/teacher_cred.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3 && parts[1].equals(username)) {
                    return parts[0]; // Return the teacher ID
                }
            }
        } catch (IOException e) {
            // JOptionPane.showMessageDialog(LoginPanel.this, "Error occurred while reading teacher credentials!");
            e.printStackTrace();
        }
        return null;
    }    

    public void writeCurrentUser(String filePath, String userID) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(userID);
        } catch (IOException ex) {
            // JOptionPane.showMessageDialog(LoginPanel.this, "Error occurred while writing current user ID!");
            ex.printStackTrace();
        }
    }    
}