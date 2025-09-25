package Objects;

import javax.imageio.ImageIO;
import java.io.File;

public class OBJ_HEALINGPOTION extends SuperObject {
    public OBJ_HEALINGPOTION(){
        name="healing potion";
        try{
            image= ImageIO.read(new File("C:\\Users\\User\\Desktop\\RPG-Game\\resources\\Tiles\\objects\\healingpotion.png"));

        }catch(Exception e){

            e.printStackTrace();
        }
        collision=true;
    }




}
