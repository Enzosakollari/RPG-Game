package main;

import main.GamePanel;
import Entity.Player;
import javax.swing.*;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Main {
    public static Integer CURRENT_PLAYER_ID = null;
    public static String CURRENT_PLAYER_NAME = null;

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("Runeblade - The Legend Begins");

        try {
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

    public static void savePlayerData(String playerName, String playerClass) {
        if (playerName == null || playerName.trim().isEmpty()) {
            System.out.println("Invalid player name");
            CURRENT_PLAYER_ID = null;
            CURRENT_PLAYER_NAME = null;
            return;
        }

        if (playerClass == null || playerClass.trim().isEmpty()) {
            System.out.println("Invalid player class");
            CURRENT_PLAYER_ID = null;
            CURRENT_PLAYER_NAME = null;
            return;
        }

        Integer playerId = DatabaseConnection.savePlayer(playerName.trim(), playerClass.trim());
        if (playerId != null) {
            CURRENT_PLAYER_ID = playerId;
            CURRENT_PLAYER_NAME = playerName.trim();
            System.out.println("Player saved with ID: " + CURRENT_PLAYER_ID);

            JOptionPane.showMessageDialog(null,
                    "Player '" + playerName + "' created successfully!",
                    "Character Created",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            System.out.println("Failed to save player data.");
            CURRENT_PLAYER_ID = null;
            CURRENT_PLAYER_NAME = null;

            JOptionPane.showMessageDialog(null,
                    "Failed to save character data. Progress may not be saved.",
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static boolean loadPlayerData(String username) {
        DatabaseConnection.PlayerData playerData = DatabaseConnection.getPlayerByUsername(username);
        if (playerData != null) {
            CURRENT_PLAYER_ID = playerData.id;
            CURRENT_PLAYER_NAME = playerData.username;
            System.out.println("Player loaded: " + CURRENT_PLAYER_NAME + " (ID: " + CURRENT_PLAYER_ID + ")");
            return true;
        } else {
            JOptionPane.showMessageDialog(null,
                    "Player '" + username + "' not found!",
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Method to show load game dialog
    public static void showLoadGameDialog(GamePanel gp) {
        String username = JOptionPane.showInputDialog(null,
                "Enter your username:",
                "Load Game",
                JOptionPane.QUESTION_MESSAGE);

        if (username != null && !username.trim().isEmpty()) {
            if (loadPlayerData(username.trim())) {
                gp.loadGameState();
            }
        }
    }
}