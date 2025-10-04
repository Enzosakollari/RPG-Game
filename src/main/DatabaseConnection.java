package main;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:game_save.db";
    private static Connection connection;

    static {
        initializeDatabase();
    }

    private static void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);

            // Create players table
            String createPlayersTable = "CREATE TABLE IF NOT EXISTS players (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE NOT NULL, " +
                    "class TEXT NOT NULL, " +
                    "current_life INTEGER DEFAULT 6, " +
                    "max_life INTEGER DEFAULT 6, " +
                    "world_x INTEGER DEFAULT 0, " +
                    "world_y INTEGER DEFAULT 0, " +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)";

            // Create items table
            String createItemsTable = "CREATE TABLE IF NOT EXISTS player_items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "player_id INTEGER NOT NULL, " +
                    "item_name TEXT NOT NULL, " +
                    "item_type TEXT NOT NULL, " +
                    "collected BOOLEAN DEFAULT FALSE, " +
                    "world_x INTEGER, " +
                    "world_y INTEGER, " +
                    "FOREIGN KEY (player_id) REFERENCES players (id), " +
                    "UNIQUE(player_id, item_name, world_x, world_y))";

            // Create doors table
            String createDoorsTable = "CREATE TABLE IF NOT EXISTS player_doors (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "player_id INTEGER NOT NULL, " +
                    "door_name TEXT NOT NULL, " +
                    "world_x INTEGER NOT NULL, " +
                    "world_y INTEGER NOT NULL, " +
                    "is_open BOOLEAN DEFAULT FALSE, " +
                    "FOREIGN KEY (player_id) REFERENCES players (id), " +
                    "UNIQUE(player_id, door_name, world_x, world_y))";

            Statement stmt = connection.createStatement();
            stmt.execute(createPlayersTable);
            stmt.execute(createItemsTable);
            stmt.execute(createDoorsTable);
            stmt.close();

            System.out.println("SQLite database initialized successfully");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("Error getting database connection: " + e.getMessage());
            return null;
        }
    }

    // Player methods
    public static Integer savePlayer(String username, String playerClass) {
        String sql = "INSERT INTO players (username, class) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, username);
            pstmt.setString(2, playerClass);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving player: " + e.getMessage());
        }
        return null;
    }

    public static PlayerData getPlayerByUsername(String username) {
        String sql = "SELECT * FROM players WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new PlayerData(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("class"),
                        rs.getInt("current_life"),
                        rs.getInt("max_life"),
                        rs.getInt("world_x"),
                        rs.getInt("world_y"),
                        rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving player: " + e.getMessage());
        }
        return null;
    }

    public static boolean updatePlayerPosition(int playerId, int worldX, int worldY) {
        String sql = "UPDATE players SET world_x = ?, world_y = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, worldX);
            pstmt.setInt(2, worldY);
            pstmt.setInt(3, playerId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating player position: " + e.getMessage());
            return false;
        }
    }

    public static boolean updatePlayerHealth(int playerId, int currentLife, int maxLife) {
        String sql = "UPDATE players SET current_life = ?, max_life = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, currentLife);
            pstmt.setInt(2, maxLife);
            pstmt.setInt(3, playerId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating player health: " + e.getMessage());
            return false;
        }
    }

    // Item methods
    public static void saveItem(int playerId, String itemName, String itemType, int worldX, int worldY, boolean collected) {
        String sql = "INSERT OR REPLACE INTO player_items (player_id, item_name, item_type, world_x, world_y, collected) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playerId);
            pstmt.setString(2, itemName);
            pstmt.setString(3, itemType);
            pstmt.setInt(4, worldX);
            pstmt.setInt(5, worldY);
            pstmt.setBoolean(6, collected);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving item: " + e.getMessage());
        }
    }

    public static List<ItemData> getPlayerItems(int playerId) {
        List<ItemData> items = new ArrayList<>();
        String sql = "SELECT * FROM player_items WHERE player_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                items.add(new ItemData(
                        rs.getInt("id"),
                        rs.getInt("player_id"),
                        rs.getString("item_name"),
                        rs.getString("item_type"),
                        rs.getInt("world_x"),
                        rs.getInt("world_y"),
                        rs.getBoolean("collected")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving player items: " + e.getMessage());
        }
        return items;
    }

    public static boolean isItemCollected(int playerId, String itemName, int worldX, int worldY) {
        String sql = "SELECT collected FROM player_items WHERE player_id = ? AND item_name = ? AND world_x = ? AND world_y = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playerId);
            pstmt.setString(2, itemName);
            pstmt.setInt(3, worldX);
            pstmt.setInt(4, worldY);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("collected");
            }
        } catch (SQLException e) {
            System.err.println("Error checking item collection: " + e.getMessage());
        }
        return false;
    }

    // Door methods
    public static void saveDoor(int playerId, String doorName, int worldX, int worldY, boolean isOpen) {
        String sql = "INSERT OR REPLACE INTO player_doors (player_id, door_name, world_x, world_y, is_open) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playerId);
            pstmt.setString(2, doorName);
            pstmt.setInt(3, worldX);
            pstmt.setInt(4, worldY);
            pstmt.setBoolean(5, isOpen);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving door: " + e.getMessage());
        }
    }

    public static List<DoorData> getPlayerDoors(int playerId) {
        List<DoorData> doors = new ArrayList<>();
        String sql = "SELECT * FROM player_doors WHERE player_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                doors.add(new DoorData(
                        rs.getInt("id"),
                        rs.getInt("player_id"),
                        rs.getString("door_name"),
                        rs.getInt("world_x"),
                        rs.getInt("world_y"),
                        rs.getBoolean("is_open")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving player doors: " + e.getMessage());
        }
        return doors;
    }

    public static boolean isDoorOpen(int playerId, String doorName, int worldX, int worldY) {
        String sql = "SELECT is_open FROM player_doors WHERE player_id = ? AND door_name = ? AND world_x = ? AND world_y = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playerId);
            pstmt.setString(2, doorName);
            pstmt.setInt(3, worldX);
            pstmt.setInt(4, worldY);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("is_open");
            }
        } catch (SQLException e) {
            System.err.println("Error checking door state: " + e.getMessage());
        }
        return false;
    }

    // Data classes
    public static class PlayerData {
        public final int id;
        public final String username;
        public final String playerClass;
        public final int currentLife;
        public final int maxLife;
        public final int worldX;
        public final int worldY;
        public final Timestamp createdAt;

        public PlayerData(int id, String username, String playerClass, int currentLife, int maxLife, int worldX, int worldY, Timestamp createdAt) {
            this.id = id;
            this.username = username;
            this.playerClass = playerClass;
            this.currentLife = currentLife;
            this.maxLife = maxLife;
            this.worldX = worldX;
            this.worldY = worldY;
            this.createdAt = createdAt;
        }
    }

    public static class ItemData {
        public final int id;
        public final int playerId;
        public final String itemName;
        public final String itemType;
        public final int worldX;
        public final int worldY;
        public final boolean collected;

        public ItemData(int id, int playerId, String itemName, String itemType, int worldX, int worldY, boolean collected) {
            this.id = id;
            this.playerId = playerId;
            this.itemName = itemName;
            this.itemType = itemType;
            this.worldX = worldX;
            this.worldY = worldY;
            this.collected = collected;
        }
    }

    public static class DoorData {
        public final int id;
        public final int playerId;
        public final String doorName;
        public final int worldX;
        public final int worldY;
        public final boolean isOpen;

        public DoorData(int id, int playerId, String doorName, int worldX, int worldY, boolean isOpen) {
            this.id = id;
            this.playerId = playerId;
            this.doorName = doorName;
            this.worldX = worldX;
            this.worldY = worldY;
            this.isOpen = isOpen;
        }
    }
}