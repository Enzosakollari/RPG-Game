package main;

import Entity.NPC_GHOST;
import Entity.NPC_NECROMANCER;
import Entity.NPC_OldMan;

import monster.*;
import Objects.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
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

        gp.obj[22] = new OBJ_KEY(gp);
        gp.obj[22].worldx = 3 * gp.tileSize;
        gp.obj[22].worldy = 64 * gp.tileSize;


        // Doors
        gp.obj[5] = new OBJ_DOOR(gp);
        gp.obj[5].worldx = 17 * gp.tileSize;
        gp.obj[5].worldy = 78 * gp.tileSize;

        gp.obj[6] = new OBJ_DOOR(gp);
        gp.obj[6].worldx = 10 * gp.tileSize;
        gp.obj[6].worldy = 16 * gp.tileSize;

        gp.obj[7] = new OBJ_DOOR(gp);
        gp.obj[7].worldx = 80 * gp.tileSize;
        gp.obj[7].worldy = 78 * gp.tileSize;

        gp.obj[8] = new OBJ_DOOR(gp);
        gp.obj[8].worldx = 70 * gp.tileSize;
        gp.obj[8].worldy = 15 * gp.tileSize;

        gp.obj[9] = new OBJ_DOOR(gp);
        gp.obj[9].worldx = 70 * gp.tileSize;
        gp.obj[9].worldy = 14 * gp.tileSize;

        gp.obj[19] = new OBJ_DOOR(gp);
        gp.obj[19].worldx = 33 * gp.tileSize;
        gp.obj[19].worldy = 77 * gp.tileSize;


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

        gp.obj[20] = new OBJ_CHEST(gp);
        gp.obj[20].worldx = 17 * gp.tileSize;
        gp.obj[20].worldy = 88 * gp.tileSize;

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

        gp.obj[16] = new OBJ_SWORD(gp);
        gp.obj[16].worldx = 91 * gp.tileSize;
        gp.obj[16].worldy = 79 * gp.tileSize;

        gp.obj[17] = new OBJ_LOWERSPEEDPOTION(gp);
        gp.obj[17].worldx = 37 * gp.tileSize;
        gp.obj[17].worldy = 13 * gp.tileSize;

        gp.obj[18] = new OBJ_HEALINGPOTION(gp);
        gp.obj[18].worldx = 16 * gp.tileSize;
        gp.obj[18].worldy = 71 * gp.tileSize;

        gp.obj[21] = new OBJ_SWORD2(gp);
        gp.obj[21].worldx = 60 * gp.tileSize;
        gp.obj[21].worldy = 80 * gp.tileSize;


    }

    public void setNpc() {

        gp.npc[0] = new NPC_OldMan(gp);
        gp.npc[0].worldx = gp.tileSize * 21;
        gp.npc[0].worldy = gp.tileSize * 21;

        gp.npc[1] = new NPC_GHOST(gp);
        gp.npc[1].worldx = gp.tileSize * 12;
        gp.npc[1].worldy = gp.tileSize * 76;

        gp.npc[2] = new NPC_NECROMANCER(gp);
        gp.npc[2].worldx = 33 * gp.tileSize;
        gp.npc[2].worldy = 79 * gp.tileSize;
    }

    public void setMonster() {
        int monsterIndex = 0;
        Random random = new Random();

        // Define monster types
        Class<?>[] monsterTypes = {Slime.class, Snake.class, Bat.class, Spider.class,
                Ghost.class, Blueghost.class, GrimReaper.class, Dragon.class};

        // First, scan the entire map for valid spawn tiles
        List<int[]> validSpawnPositions = new ArrayList<>();
        for (int row = 0; row < 100; row++) {
            for (int col = 0; col < 100; col++) {
                if (isValidSpawnTile(row, col)) {
                    validSpawnPositions.add(new int[]{col, row}); // Store as [x, y]
                }
            }
        }

        System.out.println("Valid spawn tiles found: " + validSpawnPositions.size());

        // If we found valid positions, spawn monsters randomly across them
        if (!validSpawnPositions.isEmpty()) {
            // Shuffle the positions to randomize spawning
            Collections.shuffle(validSpawnPositions, random);

            // Spawn monsters on random valid tiles (use 40% of valid tiles or until array is full)
            int monstersToSpawn = Math.min(gp.monsters.length, (int)(validSpawnPositions.size() * 0.4));

            for (int i = 0; i < monstersToSpawn && monsterIndex < gp.monsters.length; i++) {
                int[] position = validSpawnPositions.get(i);
                int col = position[0];
                int row = position[1];

                // Randomly select monster type with weighted probabilities
                Class<?> monsterType = getRandomMonsterType(random);

                try {
                    // Create monster instance based on type
                    if (monsterType == Slime.class) {
                        gp.monsters[monsterIndex] = new Slime(gp);
                    } else if (monsterType == Snake.class) {
                        gp.monsters[monsterIndex] = new Snake(gp);
                    } else if (monsterType == Bat.class) {
                        gp.monsters[monsterIndex] = new Bat(gp);
                    } else if (monsterType == Spider.class) {
                        gp.monsters[monsterIndex] = new Spider(gp);
                    } else if (monsterType == Ghost.class) {
                        gp.monsters[monsterIndex] = new Ghost(gp);
                    } else if (monsterType == Blueghost.class) {
                        gp.monsters[monsterIndex] = new Blueghost(gp);
                    } else if (monsterType == GrimReaper.class) {
                        gp.monsters[monsterIndex] = new GrimReaper(gp);
                    } else if (monsterType == Dragon.class) {
                        gp.monsters[monsterIndex] = new Dragon(gp);
                    }

                    // Set position
                    gp.monsters[monsterIndex].worldx = col * gp.tileSize;
                    gp.monsters[monsterIndex].worldy = row * gp.tileSize;

                    monsterIndex++;

                } catch (Exception e) {
                    System.out.println("Error creating monster at [" + col + "," + row + "]: " + e.getMessage());
                }
            }
        }

        System.out.println("Random monsters placed: " + monsterIndex);

        // Ensure specific boss monsters are placed in valid locations
        placeBossMonsters(monsterIndex);
    }

    // Helper method to check if a tile is valid for spawning (0, 3, 4, or 67)
    private boolean isValidSpawnTile(int row, int col) {
        int tileValue = getTileValue(row, col);
        return tileValue == 0 || tileValue == 3 || tileValue == 4 || tileValue == 67;
    }

    // Helper method to get tile value
    private int getTileValue(int row, int col) {
        try {
            // Adjust this based on your actual map storage
            // Try both possible configurations since map storage varies
            if (gp.tileM.mapTileNum != null) {
                if (col < gp.tileM.mapTileNum.length && row < gp.tileM.mapTileNum[col].length) {
                    return gp.tileM.mapTileNum[col][row];
                } else if (row < gp.tileM.mapTileNum.length && col < gp.tileM.mapTileNum[row].length) {
                    return gp.tileM.mapTileNum[row][col];
                }
            }
            return -1; // Invalid tile
        } catch (Exception e) {
            return -1;
        }
    }

    // Weighted random monster type selection
    private Class<?> getRandomMonsterType(Random random) {
        double rand = random.nextDouble();

        if (rand < 0.30) {      // 30% Slimes (most common)
            return Slime.class;
        } else if (rand < 0.50) { // 20% Snakes (common)
            return Snake.class;
        } else if (rand < 0.65) { // 15% Bats (common)
            return Bat.class;
        } else if (rand < 0.75) { // 10% Spiders (uncommon)
            return Spider.class;
        } else if (rand < 0.82) { // 7% Ghosts (uncommon)
            return Ghost.class;
        } else if (rand < 0.88) { // 6% Blue Ghosts (uncommon)
            return Blueghost.class;
        } else if (rand < 0.96) { // 8% Grim Reapers (rare)
            return GrimReaper.class;
        } else {                 // 4% Dragons (very rare)
            return Dragon.class;
        }
    }

    // Place specific boss monsters in valid locations
    private void placeBossMonsters(int startIndex) {
        int monsterIndex = startIndex;

        // Try these boss positions in order until we find valid ones
        int[][] bossPositions = {
                {17, 89}, {60, 30}, {90, 70}, {35, 85}, {75, 15}, {25, 95},
                {5, 5}, {95, 95}, {5, 95}, {95, 5}, {50, 50} // Additional boss spots
        };

        // Place Grim Reapers
        for (int i = 0; i < bossPositions.length && monsterIndex < gp.monsters.length; i += 2) {
            int x = bossPositions[i][0];
            int y = bossPositions[i][1];

            if (isValidSpawnTile(y, x)) {
                gp.monsters[monsterIndex] = new GrimReaper(gp);
                gp.monsters[monsterIndex].worldx = x * gp.tileSize;
                gp.monsters[monsterIndex].worldy = y * gp.tileSize;
                monsterIndex++;
                System.out.println("Placed Grim Reaper at [" + x + "," + y + "]");
                break; // Place one Grim Reaper
            }
        }

        // Place Dragons
        for (int i = 1; i < bossPositions.length && monsterIndex < gp.monsters.length; i += 2) {
            int x = bossPositions[i][0];
            int y = bossPositions[i][1];

            if (isValidSpawnTile(y, x)) {
                gp.monsters[monsterIndex] = new Dragon(gp);
                gp.monsters[monsterIndex].worldx = x * gp.tileSize;
                gp.monsters[monsterIndex].worldy = y * gp.tileSize;
                monsterIndex++;
                System.out.println("Placed Dragon at [" + x + "," + y + "]");
                break; // Place one Dragon
            }
        }
    }
}