package Objects;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.io.File;

public class OBJ_LIFE extends SuperObject{

    GamePanel gp;
    public OBJ_LIFE(GamePanel gp){
        this.gp=gp;
        name="key";

        try{
            image= ImageIO.read(new File("C:\\Users\\User\\Desktop\\RPG-Game\\resources\\Tiles\\objects\\fullheart.png"));
            image2=ImageIO.read(new File("C:\\Users\\User\\Desktop\\RPG-Game\\resources\\Tiles\\objects\\halfheart.png"));
            image3=ImageIO.read(new File("C:\\Users\\User\\Desktop\\RPG-Game\\resources\\Tiles\\objects\\emplyheart.png"));
            image=UtilityTool.scaleImage(image,gp.tileSize,gp.tileSize);
            image2=UtilityTool.scaleImage(image2, gp.tileSize, gp.tileSize);
            image3=UtilityTool.scaleImage(image3, gp.tileSize, gp.tileSize);

        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
