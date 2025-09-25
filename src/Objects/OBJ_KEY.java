package Objects;

import javax.imageio.ImageIO;
import java.io.File;

public class OBJ_KEY extends SuperObject{



    public OBJ_KEY(){
        name="key";
        try{
            image= ImageIO.read(new File("C:\\Users\\User\\Desktop\\RPG-Game\\resources\\Tiles\\objects\\key.png"));

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
