
package Objects;

import Entity.Entity;
import main.GamePanel;

public class OBJ_HEALINGPOTION extends Entity {
    public OBJ_HEALINGPOTION(GamePanel gp) {
        super(gp);
        name = "Healing potion";
        down1 = loadObjectImage("/Tiles/objects/healingpotion.png");
        collision = true;
    }
}