package Objects;

import javax.imageio.ImageIO;
import java.io.File;

public class OBJ_LOWERSPEEDPOTION extends SuperObject{
    public OBJ_LOWERSPEEDPOTION(){
        name="low-speed potion";
        try{
            image= ImageIO.read(new File("C:\\Users\\User\\Desktop\\RPG-Game\\resources\\Tiles\\objects\\slow1.png"));

        }catch(Exception e){

            e.printStackTrace();
        }
        collision=true;
    }

}
