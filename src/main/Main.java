package main;

import main.GamePanel;
import Entity.Player;
import javax.swing.*;
import java.sql.Connection;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Image;

public class Main {
    public static String CURRENT_PLAYER_ID = null; // Initialize as null

    public static void main(String[] args) {

        // Test database connection
        if (DatabaseConnection.testConnection()) {
            System.out.println("Connected to MySQL database!");
        } else {
            System.out.println("Failed to connect to database.");
            // You might want to show an error dialog or exit
            JOptionPane.showMessageDialog(null,
                    "Cannot connect to database. The game will continue without saving.",
                    "Database Error",
                    JOptionPane.WARNING_MESSAGE);
        }

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);

        // Change the window title
        window.setTitle("Runeblade - The Legend Begins");

        // Add window icon
        try {
            // Load the icon image from your resources
            Image icon = ImageIO.read(Main.class.getResourceAsStream("/images/icon.png"));
            window.setIconImage(icon);
        } catch (IOException e) {
            System.out.println("Could not load window icon: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error loading icon: " + e.getMessage());
        }

        GamePanel gamePanel1 = new GamePanel();
        window.add(gamePanel1);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel1.setupGame();
        gamePanel1.startGameThread();
    }

    // Method to save player data when they confirm their selection
    public static void savePlayerData(String playerName, String playerClass) {
        // Validate input
        if (playerName == null || playerName.trim().isEmpty()) {
            System.out.println("Invalid player name");
            CURRENT_PLAYER_ID = null;
            return;
        }

        if (playerClass == null || playerClass.trim().isEmpty()) {
            System.out.println("Invalid player class");
            CURRENT_PLAYER_ID = null;
            return;
        }

        // Save to database and get the player ID
        int playerId = DatabaseConnection.savePlayer(playerName.trim(), playerClass.trim());
        if (playerId != -1) {
            CURRENT_PLAYER_ID = String.valueOf(playerId);
            System.out.println("Player saved with ID: " + CURRENT_PLAYER_ID);

            // Optional: Show success message
            JOptionPane.showMessageDialog(null,
                    "Player '" + playerName + "' created successfully!",
                    "Character Created",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            System.out.println("Failed to save player data.");
            CURRENT_PLAYER_ID = null;

            // Optional: Show error message
            JOptionPane.showMessageDialog(null,
                    "Failed to save character data. Progress may not be saved.",
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}