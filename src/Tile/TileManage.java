package Tile;

import main.GamePanel;
import Entity.Entity;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class TileManage {

    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManage(GamePanel gp) {
        this.gp = gp;
        this.tile = new Tile[100];
        loadTileImages();
        this.mapTileNum = loadMap("C:\\Users\\User\\Desktop\\RPG-Game\\resources\\map\\newmap.txt");
    }

    // Load all tile images and return them in a Tile[] array
    public void loadTileImages() {
        setup(0, "general/grass", false);
        setup(1, "general/wall2", true);
        setup(2, "general/water", true);
        setup(3, "general/ground", false);
        setup(4, "general/tree2", false);
        setup(5, "general/road", false);


        setup(6, "house/house1", true);
        setup(7, "house/house2", true);
        setup(8, "house/house3", true);
        setup(9, "house/house4", true);
        setup(10, "house/house5", true);
        setup(11, "house/house6", true);
        setup(12, "house/house7", true);
        setup(13, "house/house8", true);
        setup(14, "house/house9", true);
        setup(15, "house/house10", true);
        setup(16, "house/house11", true);
        setup(17, "house/house12", true);
        setup(18, "house/house13", true);
        setup(19, "house/house14", true);
        setup(20, "house/house15", true);
        setup(21, "house/house16", true);


        setup(22, "castle/22", true);
        setup(23, "castle/23", false);
        setup(24, "castle/24", true);
        setup(25, "castle/25", true);
        setup(26, "castle/26", true);
        setup(27, "castle/27", true);
        setup(28, "castle/28", true);
        setup(29, "castle/29", true);
        setup(30, "castle/30", true);
        setup(31, "castle/31", true);
        setup(32, "castle/32", true);
        setup(33, "castle/33", true);
        setup(34, "castle/34", true);
        setup(35, "castle/35", true);
        setup(36, "castle/36", true);

        ///water
        setup(37,"water/water00",true);
        setup(38,"water/water01",true);
        setup(39,"water/water02",true);
        setup(40,"water/water03",true);
        setup(41,"water/water04",true);
        setup(42,"water/water05",true);
        setup(43,"water/water06",true);
        setup(44,"water/water07",true);
        setup(45,"water/water08",true);
        setup(46,"water/water09",true);
        setup(47,"water/water10",true);
        setup(48,"water/water11",true);
        setup(49,"water/water12",true);
        setup(50,"water/water13",true);

        //dungeon
        setup(51, "dungeon/51", true);
        setup(52, "dungeon/52", true);
        setup(53, "dungeon/53", true);
        setup(54, "dungeon/54", true);
        setup(55, "dungeon/55", true);
        setup(56, "dungeon/56", true);
        setup(57, "dungeon/57", true);
        setup(58, "dungeon/58", true);
        setup(59, "dungeon/59", true);
        setup(60, "dungeon/60", true);
        setup(61, "dungeon/61", true);
        setup(62, "dungeon/62", true);
        setup(63, "dungeon/63", true);
        setup(64, "dungeon/64", true);
        setup(65,"dungeon/65", true);
        setup(66,"dungeon/66", true);
        setup(67,"dungeon/67", false);
        setup(68,"dungeon/68", false);
        setup(69,"dungeon/69", false);
        setup(70,"dungeon/70", false);
        setup(71,"dungeon/71", false);
        setup(72,"dungeon/72", false);
        setup(73,"dungeon/73", false);


    }

    public void setup(int index, String imagePath, boolean collision) {
        UtilityTool uTool = new UtilityTool();
        try {
            tile[index] = new Tile();
            String fullPath = "C:\\Users\\User\\Desktop\\RPG-Game\\resources\\Tiles\\" + imagePath + ".png";
            System.out.println("Attempting to load tile: " + fullPath); // Debug output
            File imageFile = new File(fullPath);
            if (!imageFile.exists()) {
                System.err.println("File does not exist: " + fullPath);
                return;
            }
            BufferedImage originalImage = ImageIO.read(imageFile);
            if (originalImage == null) {
                System.err.println("Failed to load image: " + fullPath);
                return;
            }
            System.out.println("Successfully read image for: " + imagePath + " (Type: " + originalImage.getType() + ")"); // Debug output
            tile[index].image = uTool.scaleImage(originalImage, gp.tileSize, gp.tileSize);
            if (tile[index].image == null) {
                System.err.println("Failed to scale image for: " + imagePath);
                return;
            }
            System.out.println("Successfully scaled image for: " + imagePath + " (Type: " + tile[index].image.getType() + ")"); // Debug output
            tile[index].collision = collision;
            System.out.println("Successfully loaded tile: " + imagePath); // Debug output
        } catch (IOException e) {
            System.err.println("Error loading tile " + imagePath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Helper method to create a tile from a file path
    private Tile createTile(String filePath) throws IOException {
        System.out.println("Loading tile: " + filePath); // Debug output
        Tile t = new Tile();
        BufferedImage image = ImageIO.read(new File(filePath));
        if (image == null) {
            throw new IOException("Failed to load image from: " + filePath);
        }
        t.image = image;
        return t;

    }
    // Load the map into a 2D int array
    public int[][] loadMap(String mapPath) {

        int[][] map = new int[gp.maxWorldCol][gp.maxWorldRow];

        try (BufferedReader br = new BufferedReader(new FileReader(mapPath))) {

            int row = 0;
            String line;


            while ((line = br.readLine()) != null && row < gp.maxWorldRow) {
                // Skip empty lines

                if (line.trim().isEmpty()) continue;
                
                String[] numbers = line.trim().split("\\s+");

                for (int col = 0; col < gp.maxWorldCol && col < numbers.length; col++) {

                    try {
                        map[col][row] = Integer.parseInt(numbers[col].trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing number at row " + row + ", col " + col + ": " + numbers[col]);
                        map[col][row] = 0; // Default to grass if parsing fails
                    }
                }
                row++;
            }
        } catch (IOException e) {
            System.err.println("Error reading map file: " + mapPath);
            e.printStackTrace();
            // Initialize with grass tiles if file can't be read
            for (int col = 0; col < gp.maxWorldCol; col++) {
                for (int row = 0; row < gp.maxWorldRow; row++) {
                    map[col][row] = 0;
                }

            }

        }

        return map;

    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        int cameraX = gp.getCameraX();
        int cameraY = gp.getCameraY();

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - cameraX;
            int screenY = worldY - cameraY;

            // Only draw tiles that are within the player's screen view
            if (screenX + gp.tileSize > 0 &&
                    screenX - gp.tileSize < gp.screenWidth &&
                    screenY + gp.tileSize > 0 &&
                    screenY - gp.tileSize < gp.screenHeight) {

                if (tileNum >= 0 && tileNum < tile.length && tile[tileNum] != null && tile[tileNum].image != null) {
                    g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                }
            }

            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}