package entity;

import java.io.IOException;
import javax.imageio.ImageIO;

import handler.KeyHandler;
import panel.GamePanel;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        // This is essentially the hitbox
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
    }
    
    public void setDefaultValues() {
        x = 0;
        y = 0;
        speed = 4;
        direction = "down";
    }
    
    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/resource/player/bill_back_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/resource/player/bill_back_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/resource/player/bill_front_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/resource/player/bill_front_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/resource/player/bill_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/resource/player/bill_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/resource/player/bill_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/resource/player/bill_right_2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {

            if(keyH.upPressed == true) {
                direction = "up";
            }
            else if (keyH.downPressed == true) {
                direction = "down";
            }
            else if (keyH.leftPressed == true) {
                direction = "left";
            }
            else if (keyH.rightPressed == true) {
                direction = "right";
            }
            
            // Checks tile collision
            collisionOn = false;
            int tileType = gp.cDetection.detectTile(this);

            // Interacting with tile
            interactTile(tileType);

            // Player can move when collisionOn is false
            if (collisionOn == false || gp.gameState == gp.menuState) {
                switch (direction) {
                    case "up": y -= speed; break;
                    case "down": y += speed; break;
                    case "left": x -= speed; break;
                    case "right": x += speed; break;
                }
            }

            spriteCounter++;
            if (spriteCounter > 15) {
                if (spriteNum == 1) {spriteNum = 2;}
                else if (spriteNum == 2) {spriteNum = 1;}
                spriteCounter = 0;
            }
        }
    }

    public void interactTile(int tileNum1) {
        if (tileNum1 == 0 && gp.gameState == gp.playState) {
            gp.gameState = gp.dialogueState;
        } else if (tileNum1 == 0 && gp.gameState == gp.menuState) {
            // Continue as normal
        } else {
            gp.gameState = gp.playState;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1) {image = up1;}
                if (spriteNum == 2) {image = up2;}
                break;

            case "down":
                if (spriteNum == 1) {image = down1;}
                if (spriteNum == 2) {image = down2;}
                break;

            case "left":
                if (spriteNum == 1) {image = left1;}
                if (spriteNum == 2) {image = left2;}
                break;

            case "right":
                if (spriteNum == 1) {image = right1;}
                if (spriteNum == 2) {image = right2;}
                break;
        }
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}