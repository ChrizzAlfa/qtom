package handler;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import panel.GamePanel;


public class QuizHandler {

    GamePanel gp;
    Graphics2D g2;
    Timer countdownTimer;
    int point = 0;
    int questionNum = 0;
    int optionNum = 6;

    Font arial_40, arial_80B;
    int countdown = 5; // 10 seconds countdown

    public String[][] quizNum;
    public String[] answer = {"A", "B", "C", "D"};
    private boolean quizEnded = false;

    public QuizHandler(GamePanel gp) {
        this.gp = gp;

        quizNum = new String[optionNum][20];
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
    }

    public void loadQuiz(String quizFile) {
        try {
            InputStream is = getClass().getResourceAsStream("/database/quiz/" + quizFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            br.readLine(); // Ignoring the first line since it's the seed

            int question = 0; // Row
            while (question < optionNum) {
                String line = br.readLine();
                if (line == null) break; // Exit if there are no more lines to read

                String[] numbers = line.split(";");
                for (int option = 0; option < optionNum; option++) {
                    quizNum[option][question] = numbers[option];
                }
                question++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nextQuestion(int ans) {

        if (quizEnded) {
            return;
        }

        // To check the answer
        if (answer[ans].equals(quizNum[5][questionNum])) {
            point++;
        }
        questionNum++;

        // Check if there are no more questions
        if (questionNum >= quizNum[0].length || quizNum[0][questionNum] == null) {
            quizEnded = true;
            updateStudentScore();
            startCountdownToMenu();
        }

    }

    private String getCurrentUserId() {
        String userId = "";
        try (BufferedReader br = new BufferedReader(new FileReader("src/database/current_user.txt"))) {
            userId = br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }

    private void updateStudentScore() {
        String file = gp.giveFile();
        String userId = getCurrentUserId();
        String subject = file.split("_")[1].replace(".txt", "");
        int score = (point * 100) / questionNum;
        Map<String, String> userScores = new LinkedHashMap<>();
        // Read the file and store the scores in a map
        try (BufferedReader reader = new BufferedReader(new FileReader("src/database/score.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", 2);
                userScores.put(parts[0], parts[1]);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Update the user's scores
        if (userScores.containsKey(userId)) {
            String[] subjects = userScores.get(userId).split(";");
            Map<String, String> subjectScores = new LinkedHashMap<>();
            for (String sub : subjects) {
                String[] subParts = sub.split(" ");
                subjectScores.put(subParts[0], subParts[1]);
            }
            subjectScores.put(subject, String.valueOf(score));
            userScores.put(userId, String.join(";", subjectScores.entrySet().stream().map(e -> e.getKey() + " " + e.getValue()).toArray(String[]::new)));
        } else {
            userScores.put(userId, subject + " " + score);
        }

        // Write the updated scores back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/database/score.txt"))) {
            for (Map.Entry<String, String> entry : userScores.entrySet()) {
                writer.write(entry.getKey() + ";" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void startCountdownToMenu() {
        countdown = 5; // Reset countdown to 10 seconds
        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countdown--;
                if (countdown <= 0) {
                    countdownTimer.stop();
                    gp.end();
                }
                gp.repaint();
            }
        });
        countdownTimer.start();
    }
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.WHITE);

        if (gp.gameState == gp.dialogueState) {
            drawDialogueScreen();
        } else if (gp.gameState == gp.quizState) {
            drawQuizScreen();
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 230);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public void drawDialogueScreen() {
        // This is the dialogue window
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - gp.tileSize;
        int height = gp.tileSize * 3;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        x += gp.tileSize * 1.9;
        y += gp.tileSize * 1.7;
        
        String text = "Apakah anda ingin memulai quiz (y/n)?";
        g2.drawString(text, x, y);
    }

    public void drawQuizScreen() {
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - gp.tileSize;
        int height = gp.tileSize * 11;
    
        drawSubWindow(x, y, width, height);
    
        if (questionNum < quizNum[0].length && quizNum[0][questionNum] != null) {

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
            x += gp.tileSize;
            y += gp.tileSize;
            String text = quizNum[0][questionNum];
            g2.drawString(text, x, y);
    
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
            
            text = answer[0] + ". " + quizNum[1][questionNum];
            y += gp.tileSize;
            g2.drawString(text, x, y);
    
            text = answer[1] + ". " + quizNum[2][questionNum];
            y += gp.tileSize;
            g2.drawString(text, x, y);
    
            text = answer[2] + ". " + quizNum[3][questionNum];
            y += gp.tileSize;
            g2.drawString(text, x, y);
    
            text = answer[3] + ". " + quizNum[4][questionNum];
            y += gp.tileSize;
            g2.drawString(text, x, y);

        } else {
            String text = "Anda benar " + point + "/" + questionNum;
            x += gp.tileSize;
            y += gp.tileSize * 1.5;
            g2.drawString(text, x, y);

            // Draw the countdown
            if (quizEnded) {
                text = "Returning to menu in " + countdown + " seconds...";
                y += gp.tileSize * 2;
                g2.drawString(text, x, y);
            }
        }
    }
}
