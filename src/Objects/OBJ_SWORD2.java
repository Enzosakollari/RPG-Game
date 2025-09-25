package Objects;

import javax.imageio.ImageIO;
import java.io.File;

public class OBJ_SWORD2 extends SuperObject{
    public OBJ_SWORD2(){
        name="sword2";
        try{
            image= ImageIO.read(new File("C:\\Users\\User\\Desktop\\RPG-Game\\resources\\Tiles\\objects\\sword3.png"));

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
