package Entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player extends Entity {
    private final KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    public int hasKey = 0;
    public String playerName = "";
    public String playerClass = "";

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        // Set default direction first to prevent null issues
        direction = "down";
    }

    public void setup() {
        // Set collision area
        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // Set default values
        setDefaultValues();
    }

    // ADD THIS METHOD
    public void setDefaultValues() {
        worldx = gp.tileSize * 4;
        worldy = gp.tileSize * 6;
        speed = 2;
        direction = "down"; // Ensure direction is never null
        maxLife = 6;
        currentLife = maxLife;
        hasKey = 0; // Reset keys
        // Reset any other player stats as needed
    }

    private void loadSprites() {
        String basePath = "C:\\Users\\User\\Desktop\\RPG-Game\\resources\\Sprites" +"\\"+ playerClass + "\\" + playerClass.toLowerCase();

        System.out.println("Loading sprites from: " + basePath);

        up1 = loadAndScaleImage(basePath + "-up-1.png");
        up2 = loadAndScaleImage(basePath + "-up-2.png");
        down1 = loadAndScaleImage(basePath + "-down-1.png");
        down2 = loadAndScaleImage(basePath + "-down-2.png");
        left1 = loadAndScaleImage(basePath + "-left-1.png");
        left2 = loadAndScaleImage(basePath + "-left-2.png");
        right1 = loadAndScaleImage(basePath + "-right-1.png");
        right2 = loadAndScaleImage(basePath + "-right-2.png");

        // If any sprites failed to load, throw an exception
        if (up1 == null || down1 == null || left1 == null || right1 == null) {
            throw new RuntimeException("Failed to load player sprites for class: " + playerClass +
                    ". Check if files exist in: " + basePath);
        }
    }

    private BufferedImage loadAndScaleImage(String path) {
        try {
            System.out.println("Trying to load: " + path);
            File file = new File(path);

            if (!file.exists()) {
                System.err.println("File does not exist: " + file.getAbsolutePath());
                return null;
            }

            BufferedImage image = ImageIO.read(file);
            if (image != null) {
                System.out.println("Successfully loaded: " + path);
                return scaleImage(image, gp.tileSize, gp.tileSize);
            } else {
                System.err.println("Image is null: " + path);
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Failed to load image: " + path + " - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error loading image: " + path + " - " + e.getMessage());
        }
        return null;
    }

    private BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        return scaledImage;
    }

    public void setPlayerClass(String playerClass) {
        if (playerClass != null && !playerClass.isEmpty()) {
            this.playerClass = playerClass;
            loadSprites(); // Reload sprites when class changes
        }
    }

    public void update() {
        boolean moving = false;

        if (keyH.upPressed) {
            direction = "up";
            moving = true;
        } else if (keyH.downPressed) {
            direction = "down";
            moving = true;
        } else if (keyH.leftPressed) {
            direction = "left";
            moving = true;
        } else if (keyH.rightPressed) {
            direction = "right";
            moving = true;
        }

        // Ensure direction is never null
        if (direction == null) {
            direction = "down";
        }

        collisionOn = false;
        gp.cChecker.CheckTile(this);
        int objIndex = gp.cChecker.checkObject(this, true);
        pickUpObject(objIndex);

        gp.cChecker.checkEntity(this, gp.npc);

        handleInteractionKey();
        gp.eHandler.checkEvent();

        if (moving && !collisionOn) {
            switch (direction) {
                case "up": worldy -= speed; break;
                case "down": worldy += speed; break;
                case "left": worldx -= speed; break;
                case "right": worldx += speed; break;
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

    public void handleInteractionKey() {
        if (keyH.interactPressed) {
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            if (npcIndex != 999) {
                interactNPC(npcIndex);
                keyH.interactPressed = false;
            }
        }
    }

    public void pickUpObject(int i) {
        if (i != 999) {
            String objectName = gp.obj[i].name;
            switch (objectName) {
                case "key":
                    gp.playSE(1);
                    hasKey++;
                    gp.obj[i] = null;
                    gp.ui.showMessage("You got a key!");
                    break;
                case "door":
                    if (hasKey > 0) {
                        gp.playSE(3);
                        gp.obj[i] = null;
                        hasKey--;
                        gp.ui.showMessage("You opened a door!");
                    } else {
                        gp.ui.showMessage("You need a key!");
                    }
                    break;
                case "sword":
                    gp.obj[i] = null;
                    gp.playSE(2);
                    break;
                case "chest":
                    gp.obj[i] = null;
                    gp.ui.gameOver = true;
                    gp.stopMusic();
                    gp.playSE(4);
                    break;
                case "potion":
                    gp.playSE(2);
                    gp.obj[i] = null;
                    speed += 2;
                    gp.ui.showMessage("You got a speed potion!");
                    break;
                case "low-speed potion":
                    gp.playSE(2);
                    gp.obj[i] = null;
                    speed -= 1;
                    gp.ui.showMessage("You got a decrease in speed!");
                    break;
                case "healing potion":
                    gp.playSE(2);
                    gp.obj[i] = null;
                    // Make sure we don't exceed max life
                    gp.player.currentLife = Math.min(gp.player.currentLife + 2, gp.player.maxLife);
                    gp.ui.showMessage("You got a health potion!");
                    break;
                case "sword2":
                    gp.playSE(2);
                    gp.obj[i] = null;
                    gp.ui.showMessage("You got a legendary sword!");
                    break;
            }
        }
    }

    public void interactNPC(int i) {
        if (i != 999) {
            gp.gameState = gp.dialogueState;
            gp.npc[i].speak();
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = getCurrentImage();
        int screenX = worldx - gp.getCameraX();
        int screenY = worldy - gp.getCameraY();
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

    private BufferedImage getCurrentImage() {
        if (direction == null) {
            return down1;
        }

        switch (direction) {
            case "up":
                return spriteNum == 1 ? up1 : up2;
            case "down":
                return spriteNum == 1 ? down1 : down2;
            case "left":
                return spriteNum == 1 ? left1 : left2;
            case "right":
                return spriteNum == 1 ? right1 : right2;
            default:
                return down1;
        }
    }
}