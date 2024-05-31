package panel;

import entity.Player;
import handler.CollisionHandler;
import handler.KeyHandler;
import handler.QuizHandler;
import terrain.TileManager;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    
    // Screen Settings
    final int originalTileSize = 16;
    final int scale = 3;
    final int FPS = 60;
    
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    public String quizFile;
    public String seed;

    KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    Player player = new Player(this, keyH);

    public TileManager tileM = new TileManager(this);
    public CollisionHandler cDetection = new CollisionHandler(this);
    public QuizHandler quizHandler = new QuizHandler(this);
    
    public int gameState;
    public final int menuState = 0;
    public final int playState = 1;
    public final int dialogueState = 2;
    public final int quizState = 3;
    
    JFrame gameWindow;
    
    public GamePanel(JFrame gameWindow) {
        this.gameWindow = gameWindow;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    
    private void switchToMenu() {
        JFrame menuWindow = new JFrame();
        menuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuWindow.setResizable(true);
        menuWindow.setTitle("Qtom Menu");
    
        MenuStudentPanel menuPanel = new MenuStudentPanel(menuWindow);
        menuWindow.add(menuPanel);
        menuWindow.pack();
        menuWindow.setLocationRelativeTo(null);
        menuWindow.setVisible(true);
    }
    
    public String giveFile() {
        return quizFile;
    }
    
    public void setQuizFile(String quizFile) {
        this.quizFile = quizFile;
        quizHandler.loadQuiz(quizFile);
    }
    
    public void setSeed(String seed) {
        this.seed = seed;
        this.tileM.setSeed(seed);
    }
    
    public void setupGame() {
        gameState = playState;
    }
    
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (!Thread.currentThread().isInterrupted()) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                System.out.println(seed);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void end() {
        if (gameWindow != null) {
            gameThread.interrupt();
            gameWindow.dispose();
            switchToMenu();
        }
    }

    public void update() {
        if (gameState == playState) {
            player.update();
        } else {
            // Does nothing for now
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (gameState == menuState) {
            player.draw(g2);
        } else {
            // Drawing the tiles
            tileM.draw(g2);
            // Drawing the players
            player.draw(g2);
            // Drawing the quiz box
            quizHandler.draw(g2);    
        }
        g2.dispose();
    }
}