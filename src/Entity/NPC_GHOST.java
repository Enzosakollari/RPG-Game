package Entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class NPC_GHOST extends Entity {
    public NPC_GHOST(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 1;
        solidArea = new java.awt.Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        setup();
        setDialogue();
    }

    public void setup() {
        direction = "down";
        speed = 1;

        setupImage("ghost1", "down1");
        setupImage("ghost1", "down2");
        setupImage("ghost1", "up1");
        setupImage("ghost1", "up2");
        setupImage("ghost1", "left1");
        setupImage("ghost1", "left2");
        setupImage("ghost1", "right1");
        setupImage("ghost1", "right2");
    }

    private void setupImage(String imageName, String fieldName) {
        UtilityTool uTool = new UtilityTool();
        try {
            String fullPath = "C:\\Users\\User\\Desktop\\RPG-Game\\resources\\NPC\\" + imageName + ".png";
            System.out.println("Attempting to load NPC image: " + fullPath);
            File imageFile = new File(fullPath);
            if (!imageFile.exists()) {
                System.err.println("File does not exist: " + fullPath);
                return;
            }
            BufferedImage originalImage = ImageIO.read(imageFile);
            if (originalImage == null) {
                System.err.println("Failed to load image: " + fullPath);
                return;
            }
            BufferedImage scaledImage = uTool.scaleImage(originalImage, gp.tileSize, gp.tileSize);
            if (scaledImage == null) {
                System.err.println("Failed to scale image for: " + imageName);
                return;
            }

            switch (fieldName) {
                case "down1" -> down1 = scaledImage;
                case "down2" -> down2 = scaledImage;
                case "up1" -> up1 = scaledImage;
                case "up2" -> up2 = scaledImage;
                case "left1" -> left1 = scaledImage;
                case "left2" -> left2 = scaledImage;
                case "right1" -> right1 = scaledImage;
                case "right2" -> right2 = scaledImage;
            }
            System.out.println("Successfully loaded NPC image: " + imageName);
        } catch (IOException e) {
            System.err.println("Error loading NPC image " + imageName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setDialogue() {
        dialogues[0] = "Few dare to tread the path that lies ahead,traveler.The legends speak of a power that lies within.";
        dialogues[1] = "A red fire awaits in the heart of the labyrinth. Its power..some say it transcends mortal limits.";
        dialogues[2] = "The labyrinth tests all who seek it. Its red glow can sear the unworthy. Tread carefully.";
        dialogues[3] = "The secrets of the labyrinth shift.The sword's might is a double-edged gift.";


    }

    public void setAction() {

        actionLockCounter++;
        if (actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if (i <= 25) {
                direction = "up";
            } else if (i > 25 && i <= 50) {
                direction = "down";
            } else if (i > 50 && i <= 75) {
                direction = "left";
            }
            if (i > 75 && i <= 100) {
                direction = "right";
            }
            actionLockCounter = 0;
        }

    }

    public void speak() {
        super.speak();
    }

}


