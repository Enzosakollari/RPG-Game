package Objects;

import Entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.File;

public class OBJ_SPEEDPOTION extends Entity {
    public OBJ_SPEEDPOTION(GamePanel gp){
        super(gp);
        name="Potion";
        down1=loadObjectImage("/Tiles/objects/potion1.png");

    }



}
