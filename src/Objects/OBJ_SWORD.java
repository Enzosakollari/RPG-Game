package Objects;

import javax.imageio.ImageIO;
import java.io.File;

public class OBJ_SWORD extends SuperObject{



    public OBJ_SWORD(){
        name="sword";
        try{
            image= ImageIO.read(new File("C:\\Users\\User\\Desktop\\RPG-Game\\resources\\Tiles\\objects\\sword2.png"));

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
