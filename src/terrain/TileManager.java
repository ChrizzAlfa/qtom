package terrain;

import java.io.IOException;
import javax.imageio.ImageIO;

import panel.GamePanel;

import java.awt.Graphics2D;

public class TileManager {
    
    GamePanel gp;
    PerlinNoise pn;
    int terrain_img = 0;
    int world;
    
    public Tile[] tile;
    public int[][] worldNum;
    public String seed;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        worldNum = new int[gp.maxScreenCol][gp.maxScreenRow];
        getTileImage();
    }

    public void setSeed(String seed) {
        this.seed = seed;
        this.pn = new PerlinNoise(Long.parseLong(seed));  // Initialize PerlinNoise with the seed
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/resource/tiles/water.png"));
            tile[0].collision = true;
            
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/resource/tiles/grass.png"));
            
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/resource/tiles/tree.png"));
            tile[2].collision = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g2) {
        double frequency = 0.1;
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while(col < gp.maxScreenCol && row < gp.maxScreenRow) {
            world = pn.getTile(pn.noise(col * frequency, row * frequency));
            g2.drawImage(tile[world].image, x, y, gp.tileSize, gp.tileSize, null);
            worldNum [col][row] = world;
            col++;
            x += gp.tileSize;
            
            if(col == gp.maxScreenCol) {
                col = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
        }
    }
}