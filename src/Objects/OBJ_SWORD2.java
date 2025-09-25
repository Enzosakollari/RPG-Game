package Objects;

import Entity.Entity;
import main.GamePanel;

public class OBJ_SWORD2 extends Entity {
    public OBJ_SWORD2(GamePanel gp) {
        super(gp);
        name = "Sword2";
       down1 = loadObjectImage("/Tiles/objects/sword3.png");
    }
}