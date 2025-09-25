package Objects;

import Entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.File;

public class OBJ_LOWERSPEEDPOTION extends Entity {
    public OBJ_LOWERSPEEDPOTION(GamePanel gp){
        super(gp);
        name="Low-speed potion";
        down1=loadObjectImage("/Tiles/objects/slow1.png");

    }


}
