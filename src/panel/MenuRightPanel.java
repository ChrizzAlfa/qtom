package panel;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;

import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Random;

public class MenuRightPanel extends JPanel {

    PanelSetting panelSettings;
    JFrame window;
    MenuLeftPanel leftPanel;

    private JTextArea question;
    private JTextArea optionsAreaA;
    private JTextArea optionsAreaB;
    private JTextArea optionsAreaC;
    private JTextArea optionsAreaD;
    private JComboBox<String> answerComboBox;

    public MenuRightPanel(JFrame window) {
        panelSettings = new PanelSetting(this);
        this.window = window;

        question = new JTextArea(3, 20);
        optionsAreaA = new JTextArea(3, 20);
        optionsAreaB = new JTextArea(3, 20);
        optionsAreaC = new JTextArea(3, 20);
        optionsAreaD = new JTextArea(3, 20);

        answerComboBox = new JComboBox<>(new String[]{"A", "B", "C", "D"});

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Add components
        add(currentUserPanel());
        add(textAreasPanel(question, optionsAreaA, optionsAreaB, optionsAreaC, optionsAreaD));
        add(createButtonAndAnswerPanel(answerComboBox));
        add(Box.createVerticalStrut(10));
    }

    private JPanel currentUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Current User", SwingConstants.CENTER);

        label.setForeground(Color.WHITE);
        panel.setBackground(new Color(55, 58, 64));
        panel.setPreferredSize(new Dimension(100, 100));
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    private JPanel textAreasPanel(JTextArea question, JTextArea a, JTextArea b, JTextArea c, JTextArea d) {
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        Insets defaultInsets = new Insets(5, 5, 5, 5);

        // Manually setting GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = defaultInsets;
        gbc.fill = GridBagConstraints.BOTH;

        // Add question JTextArea with weightx and weighty
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        panel.add(new JScrollPane(question), gbc);

        // Add labels and options JTextAreas
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Option A:"), gbc);

        gbc.gridx = 1;
        panel.add(new JScrollPane(a), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Option B:"), gbc);

        gbc.gridx = 1;
        panel.add(new JScrollPane(b), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Option C:"), gbc);

        gbc.gridx = 1;
        panel.add(new JScrollPane(c), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Option D:"), gbc);

        gbc.gridx = 1;
        panel.add(new JScrollPane(d), gbc);

        return panel;
    }

    private JPanel createButtonAndAnswerPanel(JComboBox<String> answerComboBox) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JLabel answerLabel = new JLabel("Select the correct answer:");
        panel.add(answerLabel);
        panel.add(answerComboBox);

        JButton newQuizButton = new JButton("New Quiz");
        JButton deleteQuizButton = new JButton("Delete Quiz");

        newQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String subjectName = JOptionPane.showInputDialog("Enter the subject name:");
                if (subjectName != null && !subjectName.trim().isEmpty()) {
                    String currentUser = readCurrentUser("src/database/current_user.txt");
                    if (currentUser != null) {
                        createNewQuiz(question.getText(), optionsAreaA.getText(), optionsAreaB.getText(), optionsAreaC.getText(), optionsAreaD.getText(), answerComboBox.getSelectedItem().toString(), currentUser, subjectName);
                        question.setText("");
                        optionsAreaA.setText("");
                        optionsAreaB.setText("");
                        optionsAreaC.setText("");
                        optionsAreaD.setText("");
                        answerComboBox.setSelectedIndex(0);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error reading current user.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Subject name cannot be empty!");
                }
            }
        });

        deleteQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> quizBox = new JComboBox<>(readQuizDirectory());
                quizBox.setEditable(true);

                int result = JOptionPane.showConfirmDialog(null, quizBox, "Select Quiz to Delete", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String selectedQuiz = (String) quizBox.getSelectedItem();
                    deleteQuiz(selectedQuiz);
                }
            }
        });

        panel.add(newQuizButton);
        panel.add(deleteQuizButton);

        return panel;
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

    private void createNewQuiz(String question, String optionA, String optionB, String optionC, String optionD, String answer, String currentUser, String subjectName) {
        try (FileWriter writer = new FileWriter("src/database/quiz/" + currentUser + "_" + subjectName + ".txt", true)) {
            // String formattedQuestion = formatQuestion(question);
            String id = generateRandomID();
            writer.write(id + "\n" + question + ";" + optionA + ";" + optionB + ";" + optionC + ";" + optionD + ";" + answer + "\n");
            JOptionPane.showMessageDialog(this, "Quiz saved successfully!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error occurred while saving quiz!");
            ex.printStackTrace();
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

    private void addToQuiz(String question, String optionA, String optionB, String optionC, String optionD, String answer, String selectedQuiz) {
        try (FileWriter writer = new FileWriter("src/database/quiz/" + selectedQuiz, true)) {
            // String formattedQuestion = formatQuestion(question);
            writer.write(question + ";" + optionA + ";" + optionB + ";" + optionC + ";" + optionD + ";" + answer + "\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error occurred while saving question!");
            ex.printStackTrace();
        }
    }

    private void deleteQuiz(String selectedQuiz) {
        File file = new File("src/database/quiz/" + selectedQuiz);
        if (file.delete()) {
            JOptionPane.showMessageDialog(this, "Quiz deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Error occurred while deleting quiz!");
        }
    }

    // Uncomment if you want to handle long questions
    /*private String formatQuestion(String question) {
        StringBuilder formattedQuestion = new StringBuilder();
        int length = question.length();
        for (int i = 0; i < length; i += 100) {
            int end = Math.min(length, i + 10);
            formattedQuestion.append(question, i, end).append("\\n");
        }
        return formattedQuestion.toString().trim();
    }*/

    private String generateRandomID() {
        Random rand = new Random();
        int id = rand.nextInt(900000) + 100000; // Generate a random 6-digit number
        return String.valueOf(id);
    }

    public void handleAddToQuiz(String selectedQuiz) {
        addToQuiz(question.getText(), optionsAreaA.getText(), optionsAreaB.getText(), optionsAreaC.getText(), optionsAreaD.getText(), answerComboBox.getSelectedItem().toString(), selectedQuiz);
        question.setText("");
        optionsAreaA.setText("");
        optionsAreaB.setText("");
        optionsAreaC.setText("");
        optionsAreaD.setText("");
        answerComboBox.setSelectedIndex(0);
    }
}