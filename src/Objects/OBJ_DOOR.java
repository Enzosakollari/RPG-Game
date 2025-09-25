package Objects;

import Entity.Entity;
import main.GamePanel;

public class OBJ_DOOR extends Entity {
    public OBJ_DOOR(GamePanel gp) {
        super(gp);
        name = "Door";
        down1 = loadObjectImage("/Tiles/objects/door1.png");
        collision = true;
    }
}
