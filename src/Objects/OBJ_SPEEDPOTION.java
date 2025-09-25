package Objects;

import javax.imageio.ImageIO;
import java.io.File;

public class OBJ_SPEEDPOTION extends SuperObject{
    public OBJ_SPEEDPOTION(){
        name="potion";
        try{
            image= ImageIO.read(new File("C:\\Users\\User\\Desktop\\RPG-Game\\resources\\Tiles\\objects\\potion1.png"));

        }catch(Exception e){

            e.printStackTrace();
        }
        collision=true;
    }



}
