
package Objects;

import Entity.Entity;
import main.GamePanel;

public class OBJ_CHEST extends Entity {
    public OBJ_CHEST(GamePanel gp) {
        super(gp);
        name = "Chest";
        image = loadObjectImage("/Tiles/objects/treasureChest1.png");
    }
}