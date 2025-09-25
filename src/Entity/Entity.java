package Entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Entity {
    GamePanel gp;
    public int worldx,worldy;
    public int speed;
    public BufferedImage up1,up2,right1,right2,left1,left2,down1,down2;
    public String direction;

    public int spriteCounter=0;
    public int spriteNum=1;

    public Rectangle solidArea=new Rectangle(0,0,48,48);
    public int solidAreaDefaultX;
    public int solidAreaDefaultY;
    public boolean collisionOn;
    public int actionLockCounter=0;
    String dialogues[]=new String[20];
    public int dialogueIndex=0;
    //character status
    public int maxLife;
    public int currentLife;



    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    public Entity(GamePanel gp){
        this.gp=gp;
    }


    public void setAction(){

    }
    public void speak(){

        if(dialogueIndex < dialogues.length && dialogues[dialogueIndex] != null) {
            gp.ui.currentDialogue = dialogues[dialogueIndex];
            dialogueIndex++;

            switch (gp.player.direction) {
                case "up":
                    direction = "down";
                    break;
                case "down":
                    direction = "up";
                    break;
                case "left":
                    direction = "right";
                    break;
                case "right":
                    direction = "left";
                    break;
            }

            new Thread(() -> {
                try {
                    Thread.sleep(200000); // Wait 2 seconds
                    if(gp.gameState == gp.dialogueState) {
                        gp.gameState = gp.playState;
                        gp.ui.currentDialogue = "";
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            dialogueIndex = 0;
            gp.ui.currentDialogue = "";
            gp.gameState = gp.playState;
        }
    }
    public void update(){
        setAction();
        collisionOn=false;
        gp.cChecker.CheckTile(this);
        
        // Check collision with player
        gp.cChecker.checkPlayer(this);

        if (collisionOn==false) {
            switch (direction) {
                case "up":
                    worldy -= speed;
                    break;
                case "down":
                    worldy += speed;
                    break;
                case "left":
                    worldx -= speed;
                    break;
                case "right":
                    worldx += speed;
                    break;
            }
        }

        // Clamp player within world bounds
        if (worldx < 0) worldx = 0;
        if (worldy < 0) worldy = 0;
        if (worldx > gp.worldWidth - gp.tileSize) worldx = gp.worldWidth - gp.tileSize;
        if (worldy > gp.worldHeight - gp.tileSize) worldy = gp.worldHeight - gp.tileSize;

        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    public void setup(String entityType) {
        UtilityTool uTool = new UtilityTool();
        try {
            String basePath = "RPG-Game/resources/Player/";
            String[] directions = {"down", "up", "left", "right"};
            String[] numbers = {"1", "2"};

            for (String dir : directions) {
                for (String num : numbers) {
                    String imageName = "boy_" + dir + "_" + num;
                    String fullPath = basePath + imageName + ".png";
                    
                    File imageFile = new File(fullPath);
                    if (!imageFile.exists()) {
                        System.err.println("Image file not found: " + fullPath);
                        continue;
                    }
                    
                    BufferedImage originalImage = ImageIO.read(imageFile);
                    if (originalImage != null) {
                        BufferedImage scaledImage = uTool.scaleImage(originalImage, gp.tileSize, gp.tileSize);
                        switch (dir + num) {
                            case "down1" -> down1 = scaledImage;
                            case "down2" -> down2 = scaledImage;
                            case "up1" -> up1 = scaledImage;
                            case "up2" -> up2 = scaledImage;
                            case "left1" -> left1 = scaledImage;
                            case "left2" -> left2 = scaledImage;
                            case "right1" -> right1 = scaledImage;
                            case "right2" -> right2 = scaledImage;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading " + entityType + " images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        
        switch (direction) {
            case "up" -> image = spriteNum == 1 ? up1 : up2;
            case "down" -> image = spriteNum == 1 ? down1 : down2;
            case "left" -> image = spriteNum == 1 ? left1 : left2;
            case "right" -> image = spriteNum == 1 ? right1 : right2;
        }

        if (image != null) {
            int screenX = worldx - gp.getCameraX();
            int screenY = worldy - gp.getCameraY();


            if (screenX + gp.tileSize > 0 &&
                screenX - gp.tileSize < gp.screenWidth &&
                screenY + gp.tileSize > 0 &&
                screenY - gp.tileSize < gp.screenHeight) {
                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
        }
    }
}
