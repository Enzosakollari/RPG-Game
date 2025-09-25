package main;
import main.DatabaseConnection;
import main.Main;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KeyDataDAO {

    public static void saveKeyCount(int keyCount) {
        String playerId = Main.CURRENT_PLAYER_ID;
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if record exists
            String select = "SELECT id FROM key_data WHERE id = ?";
            PreparedStatement selectStmt = conn.prepareStatement(select);
            selectStmt.setString(1, playerId);
            ResultSet rs = selectStmt.executeQuery();

            if(rs.next()) {
                // Update
                String update = "UPDATE key_data SET key_count = ? WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(update);
                updateStmt.setInt(1, keyCount);
                updateStmt.setString(2, playerId);
                updateStmt.executeUpdate();
            } else {
                // Insert
                String insert = "INSERT INTO key_data (id, key_count) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insert);
                insertStmt.setString(1, playerId);
                insertStmt.setInt(2, keyCount);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int loadKeyCount() {
        String playerId = Main.CURRENT_PLAYER_ID;
        try (Connection conn = DatabaseConnection.getConnection()) {
            String select = "SELECT key_count FROM key_data WHERE id = ?";
            PreparedStatement selectStmt = conn.prepareStatement(select);
            selectStmt.setString(1, playerId);
            ResultSet rs = selectStmt.executeQuery();
            if(rs.next()) {
                return rs.getInt("key_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Default if not found
    }
}