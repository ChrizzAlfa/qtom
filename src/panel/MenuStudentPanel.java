package panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class MenuStudentPanel extends JPanel {

    PanelSetting panelSettings;
    JFrame window;
    MenuRightPanel rightPanel;
    Map<String, Integer> userScores;

    public MenuStudentPanel(JFrame window) {

        panelSettings = new PanelSetting(this);
        this.window = window;

        // JPanel settings
        setPreferredSize(panelSettings.screenDimension);
        setBackground(panelSettings.screenColor);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(220, 95, 0));

        // Load user scores
        userScores = loadUserScores();

        // Add dynamic buttons based on quiz files
        addDynamicButtons();
    }

    private void addDynamicButtons() {
        String[] quizFiles = readQuizDirectory();
        for (String quizFile : quizFiles) {
            String subjectName = quizFile.split("_")[1].replace(".txt", "");
            Integer score = userScores.getOrDefault(subjectName, 0);
            JButton button = createButton(subjectName, score, quizFile);
            add(button);
        }
    }

    private JButton createButton(String subjectName, Integer score, String quizFile) {
        JButton button = new JButton(subjectName + " - Score: " + score);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));  // Ensure all buttons have the same height
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seed = readSeedFromFile(quizFile);
                window.dispose();
                startGame(seed, quizFile);
            }
        });
        return button;
    }

    private String readSeedFromFile(String quizFile) {
        String seed = "";
        try {
            InputStream is = getClass().getResourceAsStream("/database/quiz/" + quizFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            seed = br.readLine();  // Read the first line which is the seed
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seed;
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

    private Map<String, Integer> loadUserScores() {
        Map<String, Integer> scores = new HashMap<>();
        String currentUser = getCurrentUser();  // Assuming this function fetches the current user ID
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/database/score.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[0].equals(currentUser)) {
                    for (int i = 1; i < parts.length; i++) {
                        String[] scoreParts = parts[i].split(" ");
                        if (scoreParts.length == 2) {
                            String subjectName = scoreParts[0];
                            int score = Integer.parseInt(scoreParts[1]);
                            scores.put(subjectName, score);
                        }
                    }
                    break;  // Exit loop once the current user's scores are found
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scores;
    }

    private String getCurrentUser() {
        // This method should fetch the current user's ID. Implement this based on your actual logic.
        // For example, it could read from a file that stores the current user ID.
        try (BufferedReader br = new BufferedReader(new FileReader("src/database/current_user.txt"))) {
            return br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startGame(String seed, String quizFile) {
        JFrame gameWindow = new JFrame();
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setResizable(true);
        gameWindow.setTitle("Qtom");
    
        GamePanel gamePanel = new GamePanel(gameWindow); // Pass the JFrame reference
        gameWindow.add(gamePanel);
        gameWindow.pack();
        gameWindow.setLocationRelativeTo(null);
        gameWindow.setVisible(true);
    
        gamePanel.setQuizFile(quizFile);
        gamePanel.setSeed(seed);
        gamePanel.setupGame();
        gamePanel.startGameThread();
    }    
}
