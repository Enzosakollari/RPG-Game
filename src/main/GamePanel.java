package main;

import Entity.Entity;
import Entity.NPC_OldMan;
import Entity.Player;
import Objects.OBJ_DOOR;
import Tile.TileManage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {

    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int getMaxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * getMaxScreenRow;

    // World Map parameters
    public final int maxWorldCol = 100;
    public final int maxWorldRow = 100;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    // FPS
    int FPS = 60;
    public TileManage tileM = new TileManage(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound sound = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Thread gameThread;
    public Player player = new Player(this, keyH);
    public Entity npc[] = new Entity[10];
    public Entity obj[] = new Entity[30];
    public Entity monsters[] = new Entity[30];
    ArrayList<Entity> entityList = new ArrayList<>();
    public int gameState;
    public final int titleState = 0;
    public final int nameState = 1;
    public final int classState = 2;
    public final int playState = 3;
    public final int pauseState = 4;
    public final int dialogueState = 5;
    public final int gameOverState = 6;
    public final int loadState = 7;
    public int saveCounter = 0; // Add this for auto-save timing
// Add to your game state constants

    // Add this method to load game state


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setObject();
        aSetter.setNpc();
        aSetter.setMonster();
        playMusic(0);
        gameState = titleState;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void saveGameState() {
        if (Main.CURRENT_PLAYER_ID == null) return;

        // Save player state (position, health, etc.)
        player.saveToDatabase();

        // Save ONLY permanent items that are still in the world
        for (Entity entity : obj) {
            if (entity != null) {
                // Only save permanent items (not potions)
                if (isPermanentItem(entity.name)) {
                    DatabaseConnection.saveItem(Main.CURRENT_PLAYER_ID, entity.name, "object",
                            entity.worldx, entity.worldy, false);
                }
            }
        }

        // Save door states
        for (Entity entity : obj) {
            if (entity != null && entity instanceof OBJ_DOOR) {
                DatabaseConnection.saveDoor(Main.CURRENT_PLAYER_ID, entity.name,
                        entity.worldx, entity.worldy, false);
            }
        }

        System.out.println("Game state saved for player: " + Main.CURRENT_PLAYER_NAME);
    }

    // Helper method to determine if an item is permanent
    private boolean isPermanentItem(String itemName) {
        // Permanent items that should be saved
        String[] permanentItems = {"Key", "Sword", "Sword2", "Chest", "Door"};

        for (String permanentItem : permanentItems) {
            if (permanentItem.equals(itemName)) {
                return true;
            }
        }

        // Potions and other consumables are NOT permanent
        return false;
    }
    public void loadGameState() {
        if (Main.CURRENT_PLAYER_ID == null) return;

        // Load player data including position, health, and items
        player.loadFromDatabase();

        gameState = playState;
        System.out.println("Game state loaded for: " + Main.CURRENT_PLAYER_NAME);
    }
    public void update() {
        if (gameState == playState) {
            player.update();

            if (player.currentLife <= 0 && !ui.gameOver) {
                ui.triggerGameOver();
                gameState = gameOverState;
                return;
            }

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();
                }
            }
            for (int i = 0; i < monsters.length; i++) {
                if (monsters[i] != null) {
                    monsters[i].update();
                }
            }
        } else if (gameState == pauseState) {

        }
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Debug
        long drawStart = 0;
        if (keyH.checkDrawTime == true) {
            drawStart = System.nanoTime();
        }

        // DEBUG: Print game state
        System.out.println("DEBUG GamePanel paint: gameState = " + gameState);

        // Title, Name, Class, Load Game, and Game Over screens only draw UI
        if (gameState == titleState || gameState == nameState ||
                gameState == classState || gameState == gameOverState || gameState == loadState) {
            System.out.println("DEBUG: Drawing UI only for state: " + gameState);
            ui.draw(g2);
        } else if (gameState == playState || gameState == pauseState || gameState == dialogueState) {
            System.out.println("DEBUG: Drawing game world for state: " + gameState);
            tileM.draw(g2);

            // Build entity list for this frame
            entityList.clear();
            if (player.currentLife > 0) {
                entityList.add(player);
            }
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) entityList.add(npc[i]);
            }
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) entityList.add(obj[i]);
            }
            for (int i = 0; i < monsters.length; i++) {
                if (monsters[i] != null) entityList.add(monsters[i]);
            }

            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    return Integer.compare(e1.worldy, e2.worldy);
                }
            });

            for (Entity e : entityList) {
                e.draw(g2);
            }

            entityList.clear();

            ui.draw(g2);

            if (gameState == pauseState) {
                ui.drawPauseScreen();
            }
        } else {
            System.out.println("DEBUG: Unknown state in paintComponent: " + gameState);
        }

        if (keyH.checkDrawTime == true) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, 10, 400);
            System.out.println("Draw Time" + passed);
        }

        g2.dispose();
    }
    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void stopMusic() {
        sound.stop();
    }

    public void playSE(int i) {
        sound.setFile(i);
        sound.play();
    }

    private boolean isPlayerBehindTree() {
        int playerCol = player.worldx / tileSize;
        int playerRow = player.worldy / tileSize;


        int[] treeTiles = {4};

        if (playerCol >= 0 && playerCol < maxWorldCol && playerRow >= 0 && playerRow < maxWorldRow) {
            int tileNum = tileM.mapTileNum[playerCol][playerRow];
            for (int treeTile : treeTiles) {
                if (tileNum == treeTile) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getCameraX() {
        int cameraX = player.worldx - player.screenX;
        if (cameraX < 0) cameraX = 0;
        if (cameraX > worldWidth - screenWidth) cameraX = worldWidth - screenWidth;
        return cameraX;
    }

    public int getCameraY() {
        int cameraY = player.worldy - player.screenY;
        if (cameraY < 0) cameraY = 0;
        if (cameraY > worldHeight - screenHeight) cameraY = worldHeight - screenHeight;
        return cameraY;
    }

    // Add this method to restart the game
    public void restartGame() {
        // Reset player
        player.setDefaultValues();
        player.currentLife = player.maxLife;

        aSetter.setObject();
        aSetter.setNpc();

        // Reset game state
        gameState = titleState;
        ui.gameOver = false;
        ui.commandNum = 0;

        stopMusic();
        playMusic(0);
    }
}