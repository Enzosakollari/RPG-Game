package Entity;

import Objects.OBJ_DOOR;
import main.GamePanel;
import main.KeyHandler;
import main.Main;
import main.DatabaseConnection;

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
    public int attackCounter = 0;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        direction = "down";
        setup();
    }

    public void setup() {
        // Set collision area
        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();

        if (playerClass != null && !playerClass.isEmpty()) {
            loadSprites();
        }
    }

    public void setDefaultValues() {
        worldx = gp.tileSize * 70;
        worldy = gp.tileSize * 50;
        speed = 9;
        direction = "down";
        maxLife = 6;
        currentLife = maxLife;
        hasKey = 0;
    }

    // Load player data from database
    public void loadFromDatabase() {
        if (Main.CURRENT_PLAYER_ID == null) return;

        DatabaseConnection.PlayerData playerData = DatabaseConnection.getPlayerByUsername(Main.CURRENT_PLAYER_NAME);
        if (playerData != null) {
            System.out.println("DEBUG: Loading player data - Class: " + playerData.playerClass + ", Name: " + playerData.username);

            // Set player class FIRST
            this.playerClass = playerData.playerClass;
            this.playerName = playerData.username;

            // Load sprites for the class
            System.out.println("DEBUG: Loading sprites for class: " + this.playerClass);
            loadSprites();

            // Set other properties
            this.worldx = playerData.worldX;
            this.worldy = playerData.worldY;
            this.currentLife = playerData.currentLife;
            this.maxLife = playerData.maxLife;

            // Load items state
            loadItemsFromDatabase();

            System.out.println("Player data loaded from database - Sprites should be loaded");
        } else {
            System.out.println("ERROR: No player data found for: " + Main.CURRENT_PLAYER_NAME);
        }
    }

    // Load items state from database
    private void loadItemsFromDatabase() {
        if (Main.CURRENT_PLAYER_ID == null) return;

        // Load collected items and remove them from game world
        java.util.List<DatabaseConnection.ItemData> items = DatabaseConnection.getPlayerItems(Main.CURRENT_PLAYER_ID);
        for (DatabaseConnection.ItemData item : items) {
            if (item.collected) {
                // Remove collected items from the game world
                for (int i = 0; i < gp.obj.length; i++) {
                    if (gp.obj[i] != null &&
                            gp.obj[i].worldx == item.worldX &&
                            gp.obj[i].worldy == item.worldY &&
                            gp.obj[i].name.equals(item.itemName)) {
                        gp.obj[i] = null;
                        System.out.println("Removed collected item: " + item.itemName + " at " + item.worldX + "," + item.worldY);
                    }
                }

                // If it's a key, add to inventory
                if ("Key".equals(item.itemName)) {
                    hasKey++;
                }
            }
        }

        // Load door states
        java.util.List<DatabaseConnection.DoorData> doors = DatabaseConnection.getPlayerDoors(Main.CURRENT_PLAYER_ID);
        for (DatabaseConnection.DoorData door : doors) {
            if (door.isOpen) {
                // Remove opened doors from the game world
                for (int i = 0; i < gp.obj.length; i++) {
                    if (gp.obj[i] != null &&
                            gp.obj[i] instanceof OBJ_DOOR &&
                            gp.obj[i].worldx == door.worldX &&
                            gp.obj[i].worldy == door.worldY) {
                        gp.obj[i] = null;
                        System.out.println("Removed opened door at " + door.worldX + "," + door.worldY);
                    }
                }
            }
        }
    }

    // Save player state to database
    public void saveToDatabase() {
        if (Main.CURRENT_PLAYER_ID == null) return;

        // Save player position and health
        DatabaseConnection.updatePlayerPosition(Main.CURRENT_PLAYER_ID, worldx, worldy);
        DatabaseConnection.updatePlayerHealth(Main.CURRENT_PLAYER_ID, currentLife, maxLife);

        System.out.println("Player state saved to database");
    }

    public void loadSprites() {
        System.out.println("DEBUG loadSprites: Starting sprite load for class: " + playerClass);

        if (playerClass == null || playerClass.isEmpty()) {
            System.out.println("ERROR: playerClass is null or empty in loadSprites!");
            return;
        }

        String basePath = "C:\\Users\\User\\Desktop\\RPG-Game\\resources\\Sprites" + "\\" + playerClass + "\\" + playerClass.toLowerCase();
        System.out.println("Loading sprites from: " + basePath);

        // Regular movement sprites (16x16) - scale normally
        up1 = loadAndScaleImage(basePath + "-up-1.png");
        up2 = loadAndScaleImage(basePath + "-up-2.png");
        down1 = loadAndScaleImage(basePath + "-down-1.png");
        down2 = loadAndScaleImage(basePath + "-down-2.png");
        left1 = loadAndScaleImage(basePath + "-left-1.png");
        left2 = loadAndScaleImage(basePath + "-left-2.png");
        right1 = loadAndScaleImage(basePath + "-right-1.png");
        right2 = loadAndScaleImage(basePath + "-right-2.png");

        // Attacking sprites - handle different dimensions
        attackUp1 = loadAttackSprite(basePath + "-attack-up-1.png", "up");
        attackUp2 = loadAttackSprite(basePath + "-attack-up-2.png", "up");
        attackDown1 = loadAttackSprite(basePath + "-attack-down-1.png", "down");
        attackDown2 = loadAttackSprite(basePath + "-attack-down-2.png", "down");
        attackLeft1 = loadAttackSprite(basePath + "-attack-left-1.png", "left");
        attackLeft2 = loadAttackSprite(basePath + "-attack-left-2.png", "left");
        attackRight1 = loadAttackSprite(basePath + "-attack-right-1.png", "right");
        attackRight2 = loadAttackSprite(basePath + "-attack-right-2.png", "right");

        // If attack sprites don't exist, use regular sprites as fallback
        if (attackUp1 == null) attackUp1 = up1;
        if (attackDown1 == null) attackDown1 = down1;
        if (attackLeft1 == null) attackLeft1 = left1;
        if (attackRight1 == null) attackRight1 = right1;

        if (up1 == null || down1 == null || left1 == null || right1 == null) {
            throw new RuntimeException("Failed to load player sprites for class: " + playerClass);
        }
    }

    private BufferedImage loadAttackSprite(String path, String direction) {
        try {
            System.out.println("Trying to load attack sprite: " + path);
            File file = new File(path);

            if (!file.exists()) {
                System.err.println("Attack sprite does not exist: " + file.getAbsolutePath());
                return null;
            }

            BufferedImage originalImage = ImageIO.read(file);
            if (originalImage != null) {
                System.out.println("Successfully loaded attack sprite: " + path + " (" +
                        originalImage.getWidth() + "x" + originalImage.getHeight() + ")");

                // Scale attack sprites while maintaining proportions
                return scaleAttackImage(originalImage, direction);
            }
        } catch (Exception e) {
            System.err.println("Failed to load attack sprite: " + path + " - " + e.getMessage());
        }
        return null;
    }

    private BufferedImage scaleAttackImage(BufferedImage original, String direction) {
        int originalWidth = original.getWidth();
        int originalHeight = original.getHeight();

        int targetWidth, targetHeight;

        // Determine scaling based on direction and original dimensions
        switch (direction) {
            case "up":
            case "down":
                // For vertical attacks (16x32 or similar)
                if (originalWidth <= originalHeight) {
                    // Tall sprite (like thrust up/down)
                    targetHeight = gp.tileSize * 2; // Make it taller
                    targetWidth = (originalWidth * targetHeight) / originalHeight;
                } else {
                    // Wide sprite
                    targetWidth = gp.tileSize;
                    targetHeight = (originalHeight * targetWidth) / originalWidth;
                }
                break;
            case "left":
            case "right":
                // For horizontal attacks (32x16 or similar)
                if (originalWidth >= originalHeight) {
                    // Wide sprite (like swing left/right)
                    targetWidth = gp.tileSize * 2; // Make it wider
                    targetHeight = (originalHeight * targetWidth) / originalWidth;
                } else {
                    // Tall sprite
                    targetHeight = gp.tileSize;
                    targetWidth = (originalWidth * targetHeight) / originalHeight;
                }
                break;
            default:
                targetWidth = gp.tileSize;
                targetHeight = gp.tileSize;
        }

        // Ensure minimum size
        targetWidth = Math.max(targetWidth, gp.tileSize);
        targetHeight = Math.max(targetHeight, gp.tileSize);

        BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, targetWidth, targetHeight, null);
        g2.dispose();

        return scaledImage;
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

    public void attack() {
        if (!attacking) {
            attacking = true;
            attackCounter = 0;
            gp.playSE(5);
        }
    }

    public void setPlayerClass(String playerClass) {
        if (playerClass != null && !playerClass.isEmpty()) {
            this.playerClass = playerClass;
            loadSprites();
        }
    }

    public void update() {
        boolean moving = false;

        // -----------------------
        // Movement input
        // -----------------------
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

        if (direction == null) {
            direction = "down";
        }

        // -----------------------
        // Collision checks
        // -----------------------
        collisionOn = false;
        gp.cChecker.CheckTile(this);

        int objIndex = gp.cChecker.checkObject(this, true);
        // FIX: Added null check to prevent crash when objects are removed
        if (objIndex != 999 && gp.obj[objIndex] != null) {
            pickUpObject(objIndex);
        }

        int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monsters);
        contactMonster(monsterIndex);

        gp.eHandler.checkEvent();

        // -----------------------
        // Handle Interactions / Attack
        // -----------------------
        if (gp.keyH.interactPressed) {
            if (npcIndex != 999) {
                // Talk to NPC
                gp.gameState = gp.dialogueState;
                gp.npc[npcIndex].speak();
            } else {
                // Attack if no NPC
                attacking = true;
                attackCounter = 0;
                gp.playSE(5); // optional sound
            }
            gp.keyH.interactPressed = false; // reset so it doesn't repeat instantly
        }

        // -----------------------
        // Movement execution
        // -----------------------
        if (moving && !collisionOn) {
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

        // Clamp inside world
        worldx = Math.max(0, Math.min(worldx, gp.worldWidth - gp.tileSize));
        worldy = Math.max(0, Math.min(worldy, gp.worldHeight - gp.tileSize));

        // -----------------------
        // Animation update
        // -----------------------
        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }

        // -----------------------
        // Invisible timer
        // -----------------------
        if (invisible) {
            invisibleCounter++;
            if (invisibleCounter > 60) {
                invisible = false;
                invisibleCounter = 0;
            }
        }

        // -----------------------
        // Attack animation timing
        // -----------------------
        if (attacking) {
            attackCounter++;
            if (attackCounter > 20) {
                attacking = false;
                attackCounter = 0;
            }
        }

        // Auto-save every 10 seconds (optional)
        if (gp.gameState == gp.playState && Main.CURRENT_PLAYER_ID != null) {
            if (gp.saveCounter++ > 600) { // 60 FPS * 10 seconds
                saveToDatabase();
                gp.saveCounter = 0;
            }
        }
    }

    public void handleInteractionKey() {
        if (gp.keyH.interactPressed) {
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);

            if (npcIndex != 999) {
                // ✅ Interact with NPC
                gp.gameState = gp.dialogueState;
                gp.npc[npcIndex].speak();
            } else {
                // ✅ No NPC → Attack
                attacking = true;
                attackCounter = 0;
                gp.playSE(5); // optional sound effect
            }

            gp.keyH.interactPressed = false; // reset key
        }
    }

    public void pickUpObject(int i) {
        // EXTREME NULL CHECKING - Check everything before accessing any properties
        if (i == 999) {
            System.out.println("DEBUG: Invalid collision index 999");
            return; // Invalid index
        }
        if (gp.obj == null) {
            System.out.println("DEBUG: Object array is null");
            return; // Object array doesn't exist
        }
        if (i < 0 || i >= gp.obj.length) {
            System.out.println("DEBUG: Index " + i + " out of bounds (array length: " + gp.obj.length + ")");
            return; // Index out of bounds
        }
        if (gp.obj[i] == null) {
            System.out.println("DEBUG: Collision with null object at index " + i + " - ignoring");
            return; // Object is null - ignore this collision
        }
        if (gp.obj[i].name == null) {
            System.out.println("DEBUG: Object at index " + i + " has null name - ignoring");
            return; // Object has no name - ignore this collision
        }

        // Now it's safe to access the object
        String objectName = gp.obj[i].name;
        System.out.println("Collided with object: " + objectName + " at index " + i);

        // SAVE POSITION FIRST for all objects that need database saving
        int savedWorldX = gp.obj[i].worldx;
        int savedWorldY = gp.obj[i].worldy;

        if ("Key".equals(objectName)) {
            gp.playSE(1);
            hasKey++;
            gp.ui.showMessage("You got a key! Total keys: " + hasKey);

            // Save to database using saved position
            if (Main.CURRENT_PLAYER_ID != null) {
                DatabaseConnection.saveItem(Main.CURRENT_PLAYER_ID, objectName, "key",
                        savedWorldX, savedWorldY, true);
            }

            gp.obj[i] = null;
            System.out.println("Key collected! Total keys: " + hasKey);

        } else if ("Door".equals(objectName)) {
            if (hasKey > 0) {
                gp.playSE(3);
                hasKey--;
                gp.ui.showMessage("You opened a door! Keys remaining: " + hasKey);

                // Save to database using saved position
                if (Main.CURRENT_PLAYER_ID != null) {
                    DatabaseConnection.saveDoor(Main.CURRENT_PLAYER_ID, objectName,
                            savedWorldX, savedWorldY, true);
                }

                gp.obj[i] = null;
                System.out.println("Door opened! Keys remaining: " + hasKey);
            } else {
                gp.ui.showMessage("You need a key to open this door!");
                System.out.println("Need a key to open door!");
                collisionOn = true;
            }

        } else if ("Sword".equals(objectName) || "Sword2".equals(objectName)) {
            gp.obj[i] = null;
            gp.playSE(2);
            gp.ui.showMessage("You found a sword!");

            // Save to database using saved position
            if (Main.CURRENT_PLAYER_ID != null) {
                DatabaseConnection.saveItem(Main.CURRENT_PLAYER_ID, objectName, "weapon",
                        savedWorldX, savedWorldY, true);
            }

        } else if ("Chest".equals(objectName)) {
            gp.obj[i] = null;
            gp.ui.gameOver = true;
            gp.stopMusic();
            gp.playSE(4);
            gp.ui.showMessage("You found the treasure! Game Over!");

            // Save to database using saved position
            if (Main.CURRENT_PLAYER_ID != null) {
                DatabaseConnection.saveItem(Main.CURRENT_PLAYER_ID, objectName, "chest",
                        savedWorldX, savedWorldY, true);
            }

        } else if ("Potion".equals(objectName)) {
            gp.playSE(2);
            gp.obj[i] = null;
            speed += 2;
            gp.ui.showMessage("You got a speed potion! Speed: " + speed);

            // DON'T save potions to database - they're consumable
            System.out.println("Speed potion consumed! Speed: " + speed);

        } else if ("Low-speed potion".equals(objectName)) {
            gp.playSE(2);
            gp.obj[i] = null;
            speed = Math.max(1, speed - 1);
            gp.ui.showMessage("You got a decrease in speed! Speed: " + speed);

            // DON'T save potions to database - they're consumable
            System.out.println("Low-speed potion consumed! Speed: " + speed);

        } else if ("Healing potion".equals(objectName)) {
            gp.playSE(2);
            gp.obj[i] = null;
            currentLife = Math.min(currentLife + 2, maxLife);
            gp.ui.showMessage("You got a health potion! Health: " + currentLife + "/" + maxLife);

            // DON'T save potions to database - they're consumable
            System.out.println("Healing potion consumed! Health: " + currentLife + "/" + maxLife);

        } else {
            System.out.println("Unknown object: " + objectName);
        }
    }
    public void interactNPC(int i) {
        if (i != 999 && gp.npc[i] != null) {
            gp.gameState = gp.dialogueState;
            gp.npc[i].speak();
        }
    }

    public void contactMonster(int i) {
        if (i != 999) {
            if (invisible == false) {
                currentLife -= 1;
                invisible = true;

                // Save health update to database
                if (Main.CURRENT_PLAYER_ID != null) {
                    DatabaseConnection.updatePlayerHealth(Main.CURRENT_PLAYER_ID, currentLife, maxLife);
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = getCurrentImage();
        if (image != null) {
            int screenX = worldx - gp.getCameraX();
            int screenY = worldy - gp.getCameraY();

            int drawWidth = image.getWidth();
            int drawHeight = image.getHeight();

            // Adjust position for larger attack sprites to keep them centered
            if (attacking) {
                switch (direction) {
                    case "up":
                        screenY -= (drawHeight - gp.tileSize); // Move up for upward attack
                        break;
                    case "left":
                        screenX -= (drawWidth - gp.tileSize); // Move left for left attack
                        break;
                    // For down and right, we keep the same position since attack extends downward/rightward
                }
            }

            if (screenX + drawWidth > 0 && screenX < gp.screenWidth &&
                    screenY + drawHeight > 0 && screenY < gp.screenHeight) {

                if (invisible) {
                    AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
                    g2.setComposite(alphaComposite);

                    g2.drawImage(image, screenX, screenY, drawWidth, drawHeight, null);

                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                } else {
                    g2.drawImage(image, screenX, screenY, drawWidth, drawHeight, null);
                }
            }
        }
    }

    private BufferedImage getCurrentImage() {
        BufferedImage image = null;

        if (attacking) {
            switch (direction) {
                case "up":
                    image = (spriteNum == 1) ? attackUp1 : attackUp2;
                    break;
                case "down":
                    image = (spriteNum == 1) ? attackDown1 : attackDown2;
                    break;
                case "left":
                    image = (spriteNum == 1) ? attackLeft1 : attackLeft2;
                    break;
                case "right":
                    image = (spriteNum == 1) ? attackRight1 : attackRight2;
                    break;
            }
        } else {
            switch (direction) {
                case "up":
                    image = (spriteNum == 1) ? up1 : up2;
                    break;
                case "down":
                    image = (spriteNum == 1) ? down1 : down2;
                    break;
                case "left":
                    image = (spriteNum == 1) ? left1 : left2;
                    break;
                case "right":
                    image = (spriteNum == 1) ? right1 : right2;
                    break;
            }
        }

        return image;
    }
}