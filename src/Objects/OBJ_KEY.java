package Objects;

import Entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.File;

public class OBJ_KEY extends Entity {
    public OBJ_KEY(GamePanel gp){
        super(gp);
        name="Key";
        down1=loadObjectImage("/Tiles/objects/key.png");
    }
}
