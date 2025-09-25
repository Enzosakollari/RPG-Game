package Objects;

import javax.imageio.ImageIO;
import java.io.File;

public class OBJ_DOOR extends SuperObject{

    public OBJ_DOOR(){
        name="door";
        try{
            image= ImageIO.read(new File("C:\\Users\\User\\Desktop\\RPG-Game\\resources\\Tiles\\objects\\door1.png"));

        }catch(Exception e){

            e.printStackTrace();
        }
        collision=true;
    }



}
