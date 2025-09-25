package main;

import Entity.NPC_GHOST;
import Entity.NPC_NECROMANCER;
import Entity.NPC_OldMan;
import Objects.*;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp){
        this.gp=gp;
    }
    public void setObject(){
        // Keys
        gp.obj[0] = new OBJ_KEY();
        gp.obj[0].worldX = 23 * gp.tileSize;
        gp.obj[0].worldY = 7 * gp.tileSize;

        gp.obj[1] = new OBJ_KEY();
        gp.obj[1].worldX = 23 * gp.tileSize;
        gp.obj[1].worldY = 40 * gp.tileSize;

        gp.obj[2] = new OBJ_KEY();
        gp.obj[2].worldX = 37 * gp.tileSize;
        gp.obj[2].worldY = 7 * gp.tileSize;

        gp.obj[3] = new OBJ_KEY();
        gp.obj[3].worldX = 50 * gp.tileSize;
        gp.obj[3].worldY = 50 * gp.tileSize;

        gp.obj[4] = new OBJ_KEY();
        gp.obj[4].worldX = 75 * gp.tileSize;
        gp.obj[4].worldY = 25 * gp.tileSize;

        gp.obj[22]=new OBJ_KEY();
        gp.obj[22].worldX=3* gp.tileSize;
        gp.obj[22].worldY=64*gp.tileSize;


        // Doors
        gp.obj[5] = new OBJ_DOOR();
        gp.obj[5].worldX = 17 * gp.tileSize;
        gp.obj[5].worldY = 78 * gp.tileSize;

        gp.obj[6] = new OBJ_DOOR();
        gp.obj[6].worldX = 10 * gp.tileSize;
        gp.obj[6].worldY = 16 * gp.tileSize;

        gp.obj[7] = new OBJ_DOOR();
        gp.obj[7].worldX = 85 * gp.tileSize;
        gp.obj[7].worldY = 78 * gp.tileSize;

        gp.obj[8] = new OBJ_DOOR();
        gp.obj[8].worldX = 70 * gp.tileSize;
        gp.obj[8].worldY = 15 * gp.tileSize;

        gp.obj[9] = new OBJ_DOOR();
        gp.obj[9].worldX = 70 * gp.tileSize;
        gp.obj[9].worldY = 14 * gp.tileSize;

        gp.obj[19]=new OBJ_DOOR();
        gp.obj[19].worldX=33*gp.tileSize;
        gp.obj[19].worldY=77*gp.tileSize;


        // Chests
        gp.obj[10] = new OBJ_CHEST();
        gp.obj[10].worldX = 95 * gp.tileSize;
        gp.obj[10].worldY = 81 * gp.tileSize;

        gp.obj[11] = new OBJ_CHEST();
        gp.obj[11].worldX = 41 * gp.tileSize;
        gp.obj[11].worldY = 10 * gp.tileSize;

        gp.obj[12] = new OBJ_CHEST();
        gp.obj[12].worldX = 78 * gp.tileSize;
        gp.obj[12].worldY = 17 * gp.tileSize;

        gp.obj[20]=new OBJ_CHEST();
        gp.obj[20].worldX=17*gp.tileSize;
        gp.obj[20].worldY=88*gp.tileSize;

        // Speed Potions
        gp.obj[13] = new OBJ_SPEEDPOTION();
        gp.obj[13].worldX = 19 * gp.tileSize;
        gp.obj[13].worldY = 38 * gp.tileSize;

        gp.obj[14] = new OBJ_SPEEDPOTION();
        gp.obj[14].worldX = 30 * gp.tileSize;
        gp.obj[14].worldY = 30 * gp.tileSize;

        gp.obj[15] = new OBJ_SPEEDPOTION();
        gp.obj[15].worldX = 11 * gp.tileSize;
        gp.obj[15].worldY = 13 * gp.tileSize;

        gp.obj[16]=new OBJ_SWORD();
        gp.obj[16].worldX=91*gp.tileSize;
        gp.obj[16].worldY=79*gp.tileSize;

        gp.obj[17]=new OBJ_LOWERSPEEDPOTION();
        gp.obj[17].worldX=37*gp.tileSize;
        gp.obj[17].worldY=13*gp.tileSize;

        gp.obj[18]=new OBJ_HEALINGPOTION();
        gp.obj[18].worldX=16* gp.tileSize;
        gp.obj[18].worldY=71*gp.tileSize;

        gp.obj[21]=new OBJ_SWORD2();
        gp.obj[21].worldX=60* gp.tileSize;
        gp.obj[21].worldY=80* gp.tileSize;


    }

    public void setNpc(){

        gp.npc[0]=new NPC_OldMan(gp);
        gp.npc[0].worldx=gp.tileSize*21;
        gp.npc[0].worldy=gp.tileSize*21;

        gp.npc[1]=new NPC_GHOST(gp);
        gp.npc[1].worldx=gp.tileSize*12;
        gp.npc[1].worldy=gp.tileSize*76;

        gp.npc[2]=new NPC_NECROMANCER(gp);
        gp.npc[2].worldx=33* gp.tileSize;
        gp.npc[2].worldy=79*gp.tileSize;
    }
}
