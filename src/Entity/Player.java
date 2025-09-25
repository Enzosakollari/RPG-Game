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

        // Initialize player
        setup();
    }

    public void setup() {
        // Set collision area
        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // Set default values
        setDefaultValues();

        // Load sprites if class is already set
        if (playerClass != null && !playerClass.isEmpty()) {
            loadSprites();
        }
    }

    public void setDefaultValues() {
        worldx = gp.tileSize * 4;
        worldy = gp.tileSize * 6;
        speed = 2;
        direction = "down";
        maxLife = 6;
        currentLife = maxLife;
        hasKey = 0;
    }

    private void loadSprites() {
        String basePath = "C:\\Users\\User\\Desktop\\RPG-Game\\resources\\Sprites" + "\\" + playerClass + "\\" + playerClass.toLowerCase();

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
            loadSprites();
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

        // Reset collision
        collisionOn = false;

        // Check collisions
        gp.cChecker.CheckTile(this);

        // Check object collision and get index
        int objIndex = gp.cChecker.checkObject(this, true);

        // Handle object interaction
        if (objIndex != 999) {
            pickUpObject(objIndex);
        }

        // Check NPC collision
        gp.cChecker.checkEntity(this, gp.npc);

        // Handle interactions
        handleInteractionKey();

        // Check events
        gp.eHandler.checkEvent();

        // Move player if no collision and moving
        if (moving && !collisionOn) {
            switch (direction) {
                case "up": worldy -= speed; break;
                case "down": worldy += speed; break;
                case "left": worldx -= speed; break;
                case "right": worldx += speed; break;
            }
        }

        // Clamp player within world bounds
        worldx = Math.max(0, Math.min(worldx, gp.worldWidth - gp.tileSize));
        worldy = Math.max(0, Math.min(worldy, gp.worldHeight - gp.tileSize));

        // Update animation
        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }

        // Reset interaction key if not pressed this frame
        if (!keyH.interactPressed) {
            keyH.interactPressed = false;
        }
    }

    public void handleInteractionKey() {
        if (keyH.interactPressed) {
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            if (npcIndex != 999) {
                interactNPC(npcIndex);
            }
            keyH.interactPressed = false;
        }
    }

    public void pickUpObject(int i) {
        if (i != 999 && gp.obj[i] != null) {
            String objectName = gp.obj[i].name;

            // Debug output to see what object we're colliding with
            System.out.println("Collided with object: " + objectName);

            // Use equalsIgnoreCase for case-insensitive comparison or exact match
            if ("Key".equals(objectName)) {
                gp.playSE(1);
                hasKey++;
                gp.ui.showMessage("You got a key! Total keys: " + hasKey);
                gp.obj[i] = null; // Remove the key from the game world
                System.out.println("Key collected! Total keys: " + hasKey);

            } else if ("Door".equals(objectName)) {
                if (hasKey > 0) {
                    gp.playSE(3);
                    hasKey--;
                    gp.ui.showMessage("You opened a door! Keys remaining: " + hasKey);
                    gp.obj[i] = null; // Remove the door
                    System.out.println("Door opened! Keys remaining: " + hasKey);
                } else {
                    gp.ui.showMessage("You need a key to open this door!");
                    System.out.println("Need a key to open door!");
                    // Don't remove the door - keep it there until player has a key
                    collisionOn = true; // Block movement
                }

            } else if ("Sword".equals(objectName)) {
                gp.obj[i] = null;
                gp.playSE(2);
                gp.ui.showMessage("You found a sword!");

            } else if ("Chest".equals(objectName)) {
                gp.obj[i] = null;
                gp.ui.gameOver = true;
                gp.stopMusic();
                gp.playSE(4);
                gp.ui.showMessage("You found the treasure! Game Over!");

            } else if ("Potion".equals(objectName)) {
                gp.playSE(2);
                gp.obj[i] = null;
                speed += 2;
                gp.ui.showMessage("You got a speed potion! Speed: " + speed);

            } else if ("Low-speed potion".equals(objectName)) {
                gp.playSE(2);
                gp.obj[i] = null;
                speed = Math.max(1, speed - 1); // Don't go below 1 speed
                gp.ui.showMessage("You got a decrease in speed! Speed: " + speed);

            } else if ("Healing potion".equals(objectName)) {
                gp.playSE(2);
                gp.obj[i] = null;
                currentLife = Math.min(currentLife + 2, maxLife);
                gp.ui.showMessage("You got a health potion! Health: " + currentLife + "/" + maxLife);

            } else if ("Sword2".equals(objectName)) {
                gp.playSE(2);
                gp.obj[i] = null;
                gp.ui.showMessage("You got a legendary sword!");

            } else {
                // Default case for any unrecognized object
                System.out.println("Unknown object: " + objectName);
            }
        }
    }

    public void interactNPC(int i) {
        if (i != 999 && gp.npc[i] != null) {
            gp.gameState = gp.dialogueState;
            gp.npc[i].speak();
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = getCurrentImage();
        if (image != null) {
            int screenX = worldx - gp.getCameraX();
            int screenY = worldy - gp.getCameraY();

            // Only draw if on screen
            if (screenX + gp.tileSize > 0 && screenX < gp.screenWidth &&
                    screenY + gp.tileSize > 0 && screenY < gp.screenHeight) {
                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
        }
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

    // Helper method to reset player state
    public void reset() {
        setDefaultValues();
        if (playerClass != null && !playerClass.isEmpty()) {
            loadSprites();
        }
    }
}