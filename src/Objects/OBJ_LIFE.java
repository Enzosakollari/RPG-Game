package Objects;

import Entity.Entity;
import main.GamePanel;

public class OBJ_LIFE extends Entity {
    public OBJ_LIFE(GamePanel gp) {
        super(gp);
        name = "Life";

        image = loadObjectImage("/Tiles/objects/fullheart.png");
        image2 = loadObjectImage("/Tiles/objects/halfheart.png");
        image3 = loadObjectImage("/Tiles/objects/emplyheart.png");
    }
}