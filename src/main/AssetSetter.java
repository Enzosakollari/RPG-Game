package main;

import Entity.NPC_GHOST;
import Entity.NPC_NECROMANCER;
import Entity.NPC_OldMan;

import Monster.Slime;
import Objects.*;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp){
        this.gp=gp;
    }
    public void setObject(){
        // Keys
        gp.obj[0] = new OBJ_KEY(gp);
        gp.obj[0].worldx = 23 * gp.tileSize;
        gp.obj[0].worldy = 7 * gp.tileSize;

        gp.obj[1] = new OBJ_KEY(gp);
        gp.obj[1].worldx = 23 * gp.tileSize;
        gp.obj[1].worldy = 40 * gp.tileSize;

        gp.obj[2] = new OBJ_KEY(gp);
        gp.obj[2].worldx = 37 * gp.tileSize;
        gp.obj[2].worldy = 7 * gp.tileSize;

        gp.obj[3] = new OBJ_KEY(gp);
        gp.obj[3].worldx = 50 * gp.tileSize;
        gp.obj[3].worldy = 50 * gp.tileSize;

        gp.obj[4] = new OBJ_KEY(gp);
        gp.obj[4].worldx = 75 * gp.tileSize;
        gp.obj[4].worldy = 25 * gp.tileSize;

        gp.obj[22]=new OBJ_KEY(gp);
        gp.obj[22].worldx=3* gp.tileSize;
        gp.obj[22].worldy=64*gp.tileSize;


        // Doors
        gp.obj[5] = new OBJ_DOOR(gp);
        gp.obj[5].worldx = 17 * gp.tileSize;
        gp.obj[5].worldy = 78 * gp.tileSize;

        gp.obj[6] = new OBJ_DOOR(gp);
        gp.obj[6].worldx = 10 * gp.tileSize;
        gp.obj[6].worldy = 16 * gp.tileSize;

        gp.obj[7] = new OBJ_DOOR(gp);
        gp.obj[7].worldx = 85 * gp.tileSize;
        gp.obj[7].worldy = 78 * gp.tileSize;

        gp.obj[8] = new OBJ_DOOR(gp);
        gp.obj[8].worldx = 70 * gp.tileSize;
        gp.obj[8].worldy = 15 * gp.tileSize;

        gp.obj[9] = new OBJ_DOOR(gp);
        gp.obj[9].worldx = 70 * gp.tileSize;
        gp.obj[9].worldy = 14 * gp.tileSize;

        gp.obj[19]=new OBJ_DOOR(gp);
        gp.obj[19].worldx=33*gp.tileSize;
        gp.obj[19].worldy=77*gp.tileSize;


        // Chests
        gp.obj[10] = new OBJ_CHEST(gp);
        gp.obj[10].worldx = 95 * gp.tileSize;
        gp.obj[10].worldy = 81 * gp.tileSize;

        gp.obj[11] = new OBJ_CHEST(gp);
        gp.obj[11].worldx = 41 * gp.tileSize;
        gp.obj[11].worldy = 10 * gp.tileSize;

        gp.obj[12] = new OBJ_CHEST(gp);
        gp.obj[12].worldx = 78 * gp.tileSize;
        gp.obj[12].worldy = 17 * gp.tileSize;

        gp.obj[20]=new OBJ_CHEST(gp);
        gp.obj[20].worldx=17*gp.tileSize;
        gp.obj[20].worldy=88*gp.tileSize;

        // Speed Potions
        gp.obj[13] = new OBJ_SPEEDPOTION(gp);
        gp.obj[13].worldx = 19 * gp.tileSize;
        gp.obj[13].worldy = 38 * gp.tileSize;

        gp.obj[14] = new OBJ_SPEEDPOTION(gp);
        gp.obj[14].worldx = 30 * gp.tileSize;
        gp.obj[14].worldy = 30 * gp.tileSize;

        gp.obj[15] = new OBJ_SPEEDPOTION(gp);
        gp.obj[15].worldx = 11 * gp.tileSize;
        gp.obj[15].worldy = 13 * gp.tileSize;

        gp.obj[16]=new OBJ_SWORD(gp);
        gp.obj[16].worldx=91*gp.tileSize;
        gp.obj[16].worldy=79*gp.tileSize;

        gp.obj[17]=new OBJ_LOWERSPEEDPOTION(gp);
        gp.obj[17].worldx=37*gp.tileSize;
        gp.obj[17].worldy=13*gp.tileSize;

        gp.obj[18]=new OBJ_HEALINGPOTION(gp);
        gp.obj[18].worldx=16* gp.tileSize;
        gp.obj[18].worldy=71*gp.tileSize;

        gp.obj[21]=new OBJ_SWORD2(gp);
        gp.obj[21].worldx=60* gp.tileSize;
        gp.obj[21].worldy=80* gp.tileSize;


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
    public void setMonster(){
        gp.monsters[0]=new Slime(gp);
        gp.monsters[0].worldx=13* gp.tileSize;
        gp.monsters[0].worldy=77*gp.tileSize;
    }
}
