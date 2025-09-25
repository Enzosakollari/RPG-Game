package Objects;

import javax.imageio.ImageIO;
import java.io.File;

public class OBJ_CHEST extends SuperObject{
    public OBJ_CHEST(){
        name="chest" ;
        try{
            image= ImageIO.read(new File("C:\\Users\\User\\Desktop\\RPG-Game\\resources\\Tiles\\objects\\treasureChest1.png"));

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
