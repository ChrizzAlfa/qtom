package handler;

import entity.Entity;
import panel.GamePanel;

public class CollisionHandler {
    
    GamePanel gp;
    
    public CollisionHandler(GamePanel gp) {
        this.gp = gp;
    }

    public int detectTile(Entity entity) {
        int entityLeftX = entity.x + entity.solidArea.x;
        int entityRightX = entity.x + entity.solidArea.x + entity.solidArea.width;
        int entityTopY = entity.y + entity.solidArea.y;
        int entityBottomY = entity.y + entity.solidArea.y + entity.solidArea.height;
    
        int entityLeftCol = entityLeftX/gp.tileSize;
        int entityRightCol = entityRightX/gp.tileSize;
        int entityTopRow = entityTopY/gp.tileSize;
        int entityBottomRow = entityBottomY/gp.tileSize;

        int tileNum1 = 0;
        int tileNum2 = 0;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopY - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.worldNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.worldNum[entityRightCol][entityTopRow];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomY + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.worldNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.worldNum[entityRightCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftX - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.worldNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.worldNum[entityLeftCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightX + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.worldNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.worldNum[entityRightCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            }
        return tileNum1;
    }
}