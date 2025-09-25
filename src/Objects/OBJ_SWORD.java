package Objects;

import Entity.Entity;
import main.GamePanel;

public class OBJ_SWORD extends Entity {
    public OBJ_SWORD(GamePanel gp) {
        super(gp);
        name = "Sword";
        down1 = loadObjectImage("/Tiles/objects/sword2.png");
    }
}