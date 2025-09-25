package Objects;

import main.GamePanel;
import java.awt.Graphics2D;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

public class SuperObject {
    public BufferedImage image,image2,image3;
    public String name;
    public boolean collision=false;
    public int worldX,worldY;
    public Rectangle solidArea= new Rectangle(0,0,48,48);
    public int solidAreaDefaultX=0;
    public int solidAreaDefaultY=0;


    public void draw(Graphics2D g2, GamePanel gp){

        int cameraX = gp.getCameraX();
        int cameraY = gp.getCameraY();

        int screenX = worldX - cameraX;
        int screenY = worldY - cameraY;

        // Only draw tiles that are within the player's screen view
        if (screenX + gp.tileSize > 0 &&
                screenX - gp.tileSize < gp.screenWidth &&
                screenY + gp.tileSize > 0 &&
                screenY - gp.tileSize < gp.screenHeight) {

             {
                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
        }






    }


}
