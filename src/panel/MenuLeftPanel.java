package panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MenuLeftPanel extends JPanel {

    JFrame window;
    MenuRightPanel rightPanel;

    public MenuLeftPanel(JFrame window, MenuRightPanel rightPanel) {
        this.window = window;
        this.rightPanel = rightPanel;

        // JPanel settings
        setPreferredSize(new Dimension(100, getPreferredSize().height));
        setMinimumSize(new Dimension(100, getPreferredSize().height));
        setMaximumSize(new Dimension(100, Integer.MAX_VALUE));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(220, 95, 0));
        
        // Add dynamic buttons based on quiz files
        addDynamicButtons();
    }

    public void addDynamicButtons() {
        String currentUser = readCurrentUser("src/database/current_user.txt");
        if (currentUser != null) {
            String[] quizFiles = readQuizDirectory();
            for (String quizFile : quizFiles) {
                if (quizFile.startsWith(currentUser)) {
                    String subjectName = quizFile.split("_")[1].replace(".txt", "");
                    JButton button = createButton(subjectName, quizFile);
                    add(button);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error reading current user.");
        }
    }

    private JButton createButton(String text, String quizFile) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));  // Ensure all buttons have the same height
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rightPanel.handleAddToQuiz(quizFile);
            }
        });
        return button;
    }

    private String readCurrentUser(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.readLine().trim();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error occurred while reading current user!");
            ex.printStackTrace();
            return null;
        }
    }

    private String[] readQuizDirectory() {
        String directoryPath = "src/database/quiz";
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            String[] fileNames = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                fileNames[i] = files[i].getName();
            }
            return fileNames;
        }
        return new String[0];
    }
}