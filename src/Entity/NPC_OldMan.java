package Entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class NPC_OldMan extends Entity {
    public NPC_OldMan(GamePanel gp) {
        super(gp);
        direction="down";
        speed=1;
        // Set solidArea for collision
        solidArea = new java.awt.Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        setup();
        setDialogue();
    }

    public void setup() {
        direction = "down";
        speed = 1;
        
        // Load and scale NPC images
        setupImage("oldman_down_1", "down1");
        setupImage("oldman_down_2", "down2");
        setupImage("oldman_up_1", "up1");
        setupImage("oldman_up_2", "up2");
        setupImage("oldman_left_1", "left1");
        setupImage("oldman_left_2", "left2");
        setupImage("oldman_right_1", "right1");
        setupImage("oldman_right_2", "right2");
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
            
            // Set the appropriate field based on the fieldName
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

    public void setDialogue(){
        dialogues[0]="Hello there traveler!";
        dialogues[1]="Long ago, I wandered through storms of magic and shadow, seeking truth in forgotten scrolls.";
        dialogues[2]="Deep in the Labyrinth of Babylonia lies a hidden piece — older than time, waiting to awaken.";
        dialogues[3]="May your path be brighter than mine, young one, and your burden lighter.";



    }

    public void setAction(){

        actionLockCounter++;
        if(actionLockCounter==120){
        Random random=new Random();
        int i=random.nextInt(100)+1;//pick one number from one to hundred

        if (i<=25){
            direction="up";
        }else if(i>25&&i<=50){
            direction="down";
        }
        else if (i>50&&i<=75){
            direction="left";
        }
        if (i>75&&i<=100){
            direction="right";
        }
        actionLockCounter=0;
    }

} public void speak(){
      super.speak();
    }

}
