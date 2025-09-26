package Entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Entity {
    public GamePanel gp;
    public int worldx, worldy;
    public int speed;
    public BufferedImage up1, up2, right1, right2, left1, left2, down1, down2;
    public String direction = "down";
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    public boolean collisionOn = false;
    public int actionLockCounter = 0;
    String dialogues[] = new String[20];
    public int dialogueIndex = 0;
    // Character status
    public int maxLife;
    public int currentLife;
    public BufferedImage image, image2, image3;
    public String name;
    public boolean collision = false;
    public boolean invisible=false;
    public int invisibleCounter=0;
    public int type;
    // Add these properties to your Entity class in the Entity package
    public boolean isMonster = false;
    public int attackPower = 0;
    public int attackCooldown = 60;
    public int attackCounter = 0;
    public boolean canAttack = true;
    public int chaseDistance = 5 * 48; // 5 tiles (assuming tileSize is 48)
    public int attackRange = 48; // 1 tile distance
    public boolean isChasing = false;
    public Entity(GamePanel gp){
        this.gp = gp;
    }
    public void setAction(){}
    public void speak(){
        if(dialogueIndex < dialogues.length && dialogues[dialogueIndex] != null) {
            gp.ui.currentDialogue = dialogues[dialogueIndex];
            dialogueIndex++;

            switch (gp.player.direction) {
                case "up": direction = "down"; break;
                case "down": direction = "up"; break;
                case "left": direction = "right"; break;
                case "right": direction = "left"; break;
            }

            new Thread(() -> {
                try {
                    Thread.sleep(2000); // Fixed: 2 seconds instead of 200000
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
        collisionOn = false;
        gp.cChecker.CheckTile(this);
        gp.cChecker.checkObject(this,false);
        gp.cChecker.checkEntity(this,gp.monsters);
        gp.cChecker.checkEntity(this,gp.npc);

        // ONLY check player collision for monsters, not for NPCs or other entities
        if (isMonster) {
            boolean contactPlayer = gp.cChecker.checkPlayer(this);
            if (contactPlayer) {
                // Cast to Slime to call attackPlayer method
                if (this instanceof Monster.Slime) {
                    ((Monster.Slime)this).attackPlayer();
                }
            }
        }

        if (!collisionOn) {
            switch (direction) {
                case "up": worldy -= speed; break;
                case "down": worldy += speed; break;
                case "left": worldx -= speed; break;
                case "right": worldx += speed; break;
            }
        }

        // Clamp within world bounds
        worldx = Math.max(0, Math.min(worldx, gp.worldWidth - gp.tileSize));
        worldy = Math.max(0, Math.min(worldy, gp.worldHeight - gp.tileSize));

        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }

        // Handle attack cooldown for monsters only
        if (isMonster && !canAttack) {
            attackCounter++;
            if (attackCounter >= attackCooldown) {
                canAttack = true;
                attackCounter = 0;
            }
        }
    }

    public void setupSimple(String basePath, String spriteName) {
        UtilityTool uTool = new UtilityTool();

        // Just try to load whatever exists
        down1 = loadImage(uTool, basePath + spriteName + "-down-1.png");
        down2 = loadImage(uTool, basePath + spriteName + "-down-2.png");
        up1 = loadImage(uTool, basePath + spriteName + "-up-1.png");
        up2 = loadImage(uTool, basePath + spriteName + "-up-2.png");
        left1 = loadImage(uTool, basePath + spriteName + "-left-1.png");
        left2 = loadImage(uTool, basePath + spriteName + "-left-2.png");
        right1 = loadImage(uTool, basePath + spriteName + "-right-1.png");
        right2 = loadImage(uTool, basePath + spriteName + "-right-2.png");

        // Use down1 as default for any missing sprites
        if (down1 != null) {
            if (down2 == null) down2 = down1;
            if (up1 == null) up1 = down1;
            if (up2 == null) up2 = down1;
            if (left1 == null) left1 = down1;
            if (left2 == null) left2 = down1;
            if (right1 == null) right1 = down1;
            if (right2 == null) right2 = down1;
        }
    }

    private BufferedImage loadImage(UtilityTool uTool, String fullPath) {
        try {
            File imageFile = new File(fullPath);
            if (imageFile.exists()) {
                BufferedImage originalImage = ImageIO.read(imageFile);
                if (originalImage != null) {
                    System.out.println("Loaded: " + fullPath);
                    return uTool.scaleImage(originalImage, gp.tileSize, gp.tileSize);
                }
            }
        } catch (IOException e) {
            // File doesn't exist or can't be read - that's okay
        }
        return null;
    }

    public BufferedImage loadObjectImage(String imagePath) {
        UtilityTool uTool = new UtilityTool();
        try {
            var inputStream = getClass().getResourceAsStream(imagePath);
            if (inputStream == null) {
                System.err.println("Image file not found in resources: " + imagePath);
                return null;
            }

            BufferedImage originalImage = ImageIO.read(inputStream);
            if (originalImage != null) {
                return uTool.scaleImage(originalImage, gp.tileSize, gp.tileSize);
            }
        } catch (IOException e) {
            System.err.println("Error loading image: " + imagePath);
            e.printStackTrace();
        }
        return null;
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up": image = (spriteNum == 1) ? up1 : up2; break;
            case "down": image = (spriteNum == 1) ? down1 : down2; break;
            case "left": image = (spriteNum == 1) ? left1 : left2; break;
            case "right": image = (spriteNum == 1) ? right1 : right2; break;
        }

        if (image != null) {
            int screenX = worldx - gp.getCameraX();
            int screenY = worldy - gp.getCameraY();

            if (isOnScreen(screenX, screenY)) {
                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
        }
    }

    private boolean isOnScreen(int screenX, int screenY) {
        return screenX + gp.tileSize > 0 &&
                screenX - gp.tileSize < gp.screenWidth &&
                screenY + gp.tileSize > 0 &&
                screenY - gp.tileSize < gp.screenHeight;
    }
}
