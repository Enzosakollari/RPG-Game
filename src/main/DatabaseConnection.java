package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnection {

    // MySQL connection parameters - UPDATE THESE FOR YOUR SETUP
    private static final String URL = "jdbc:mysql://localhost:3306/Game";
    private static final String USER = "root"; // Your MySQL username
    private static final String PASSWORD = "1234"; // Your MySQL password

    // Load MySQL driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found! Add it to your classpath.");
            e.printStackTrace();
        }
    }

    // Initialize database and create table if it doesn't exist
    private static void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS players (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "class VARCHAR(50) NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Database initialized successfully - players table ready");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error getting database connection: " + e.getMessage());
            return null;
        }
    }

    // Test connection method
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }

    // Method to save player data
    public static int savePlayer(String name, String playerClass) {
        // Validate parameters
        if (name == null || name.trim().isEmpty() || playerClass == null || playerClass.trim().isEmpty()) {
            System.err.println("Invalid parameters for savePlayer");
            return -1;
        }

        String sql = "INSERT INTO players (name, class) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, name.trim());
            pstmt.setString(2, playerClass.trim());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // Get the generated ID
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving player to database: " + e.getMessage());
            e.printStackTrace();
        }
        return -1; // Return -1 if failed
    }

    // Optional: Method to retrieve player data
    public static PlayerData getPlayer(int playerId) {
        String sql = "SELECT * FROM players WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new PlayerData(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("class"),
                        rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving player: " + e.getMessage());
        }
        return null;
    }

    // Simple data class to hold player information
    public static class PlayerData {
        public final int id;
        public final String name;
        public final String playerClass;
        public final java.sql.Timestamp createdAt;

        public PlayerData(int id, String name, String playerClass, java.sql.Timestamp createdAt) {
            this.id = id;
            this.name = name;
            this.playerClass = playerClass;
            this.createdAt = createdAt;
        }
    }
}