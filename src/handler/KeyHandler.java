package handler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import panel.GamePanel;

public class KeyHandler implements KeyListener{

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed; 

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
        int code  = e.getKeyCode();

        // When in play state
        if (gp.gameState == gp.playState || gp.gameState == gp.menuState) {
            if (code == KeyEvent.VK_W) {
                upPressed = true;
            }
            if (code == KeyEvent.VK_S) {
                downPressed = true;
            }
            if (code == KeyEvent.VK_A) {
                leftPressed = true;
            }
            if (code == KeyEvent.VK_D) {
                rightPressed = true;
            }
        }

        // When in a dialogue state
        else if (gp.gameState == gp.dialogueState) {
            if (code == KeyEvent.VK_Y) {
                gp.gameState = gp.quizState;
            }
            if (code == KeyEvent.VK_N) {
                gp.gameState = gp.playState;
            }
        }

        // When in a quiz state
        else if (gp.gameState == gp.quizState) {
            if (code == KeyEvent.VK_A) {
                gp.quizHandler.nextQuestion(0);
            }
            if (code == KeyEvent.VK_B) {
                gp.quizHandler.nextQuestion(1);
            }
            if (code == KeyEvent.VK_C) {
                gp.quizHandler.nextQuestion(2);
            }
            if (code == KeyEvent.VK_D) {
                gp.quizHandler.nextQuestion(3);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }
    
}