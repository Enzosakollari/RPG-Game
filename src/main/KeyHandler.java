package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    boolean checkDrawTime = false;
    private GamePanel gp;
    public boolean interactPressed = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // TITLE STATE
        if (gp.gameState == gp.titleState) {
            titleStateInput(code);
        }
        // LOAD GAME STATE
        else if (gp.gameState == gp.loadState) {
            loadStateInput(code, e);
        }
        // NAME ENTRY STATE
        else if (gp.gameState == gp.nameState) {
            nameStateInput(code, e);
        }
        // CLASS SELECTION STATE
        else if (gp.gameState == gp.classState) {
            classStateInput(code);
        }
        // PLAY STATE
        else if (gp.gameState == gp.playState) {
            playStateInput(code);
        }
        // PAUSE STATE
        else if (gp.gameState == gp.pauseState) {
            pauseStateInput(code);
        }
        // DIALOGUE STATE
        else if (gp.gameState == gp.dialogueState) {
            dialogueStateInput(code);
        }
        // GAME OVER STATE
        else if (gp.gameState == gp.gameOverState) {
            gameOverStateInput(code);
        }
    }

    private void titleStateInput(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0) gp.ui.commandNum = 2;
            System.out.println("DEBUG: Title state - commandNum: " + gp.ui.commandNum);
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            if (gp.ui.commandNum > 2) gp.ui.commandNum = 0;
            System.out.println("DEBUG: Title state - commandNum: " + gp.ui.commandNum);
        }
        if (code == KeyEvent.VK_ENTER) {
            if (gp.ui.commandNum == 0) {
                // NEW GAME
                gp.gameState = gp.nameState;
                gp.ui.playerName = ""; // Reset name
                System.out.println("DEBUG: Switching to nameState (New Game)");
            }
            if (gp.ui.commandNum == 1) {
                // LOAD GAME
                gp.gameState = gp.loadState;
                gp.ui.playerName = ""; // Reset for username input
                System.out.println("DEBUG: Switching to loadState (Load Game)");
            }
            if (gp.ui.commandNum == 2) {
                // QUIT
                System.out.println("DEBUG: Quitting game");
                System.exit(0);
            }
        }
    }

    private void loadStateInput(int code, KeyEvent e) {
        System.out.println("DEBUG: Load state - processing key: " + code + ", current name: '" + gp.ui.playerName + "'");

        if (code == KeyEvent.VK_ENTER) {
            if (gp.ui.playerName != null && !gp.ui.playerName.trim().isEmpty()) {
                System.out.println("DEBUG: Attempting to load player: '" + gp.ui.playerName.trim() + "'");
                if (Main.loadPlayerData(gp.ui.playerName.trim())) {
                    System.out.println("DEBUG: Player found, loading game state");
                    gp.loadGameState();
                } else {
                    System.out.println("DEBUG: Player not found");
                    // Player not found - you might want to show an error message
                    gp.ui.showMessage("Player '" + gp.ui.playerName.trim() + "' not found!");
                }
            } else {
                System.out.println("DEBUG: Empty username entered");
                gp.ui.showMessage("Please enter a username!");
            }
        }
        else if (code == KeyEvent.VK_ESCAPE) {
            System.out.println("DEBUG: Returning to title screen from load state");
            gp.gameState = gp.titleState;
            gp.ui.playerName = "";
        }
        else if (code == KeyEvent.VK_BACK_SPACE && gp.ui.playerName.length() > 0) {
            gp.ui.playerName = gp.ui.playerName.substring(0, gp.ui.playerName.length() - 1);
            System.out.println("DEBUG: Backspace - new name: '" + gp.ui.playerName + "'");
        }
        else if (Character.isLetterOrDigit(e.getKeyChar()) || e.getKeyChar() == ' ') {
            gp.ui.playerName += e.getKeyChar();
            System.out.println("DEBUG: Character added - new name: '" + gp.ui.playerName + "'");
        }
    }

    private void nameStateInput(int code, KeyEvent e) {
        if (code == KeyEvent.VK_ENTER) {
            if (gp.ui.playerName != null && !gp.ui.playerName.isEmpty()) {
                gp.gameState = gp.classState;
                System.out.println("DEBUG: Switching to classState");
            }
        } else if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.titleState;
            gp.ui.playerName = "";
            System.out.println("DEBUG: Returning to title from name entry");
        } else if (Character.isLetterOrDigit(e.getKeyChar()) || e.getKeyChar() == ' ') {
            gp.ui.playerName += e.getKeyChar();
        } else if (code == KeyEvent.VK_BACK_SPACE && gp.ui.playerName.length() > 0) {
            gp.ui.playerName = gp.ui.playerName.substring(0, gp.ui.playerName.length() - 1);
        }
    }

    private void classStateInput(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.ui.classCommandNum--;
            if (gp.ui.classCommandNum < 0) gp.ui.classCommandNum = gp.ui.classes.length - 1;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.ui.classCommandNum++;
            if (gp.ui.classCommandNum >= gp.ui.classes.length) gp.ui.classCommandNum = 0;
        }
        if (code == KeyEvent.VK_ENTER) {
            gp.player.playerName = gp.ui.playerName;
            String chosenClass = gp.ui.classes[gp.ui.classCommandNum];

            gp.player.setPlayerClass(chosenClass);
            gp.player.setup();

            Main.savePlayerData(gp.player.playerName, chosenClass);

            gp.gameState = gp.playState;
            System.out.println("DEBUG: New game started - Player: " + gp.player.playerName + ", Class: " + chosenClass);
        } else if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.nameState;
        }
    }

    private void playStateInput(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) upPressed = true;
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed = true;
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed = true;
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = true;
        if (code == KeyEvent.VK_P) {
            gp.gameState = gp.pauseState;
            System.out.println("DEBUG: Game paused");
        }
        if (code == KeyEvent.VK_ENTER) interactPressed = true;
        if (code == KeyEvent.VK_L) { // Quick save
            gp.saveGameState();
            gp.ui.showMessage("Game saved!");
            System.out.println("DEBUG: Quick save performed");
        }
        if (code == KeyEvent.VK_T) { // Debug: print player position
            System.out.println("DEBUG: Player position - X: " + gp.player.worldx + ", Y: " + gp.player.worldy);
        }
    }

    private void pauseStateInput(int code) {
        if (code == KeyEvent.VK_P) {
            gp.gameState = gp.playState;
            System.out.println("DEBUG: Game resumed");
        }
        if (code == KeyEvent.VK_S) { // Save from pause menu
            gp.saveGameState();
            gp.ui.showMessage("Game saved!");
            System.out.println("DEBUG: Game saved from pause menu");
        }
    }

    private void dialogueStateInput(int code) {
        if (code == KeyEvent.VK_ENTER) {
            gp.gameState = gp.playState;
            gp.ui.currentDialogue = "";
            System.out.println("DEBUG: Dialogue closed");
        }
    }

    private void gameOverStateInput(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
            }
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            if (gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
        }
        if (code == KeyEvent.VK_ENTER) {
            if (gp.ui.commandNum == 0) {
                gp.restartGame();
                System.out.println("DEBUG: Restarting game");
            } else if (gp.ui.commandNum == 1) {
                System.out.println("DEBUG: Quitting from game over");
                System.exit(0);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) upPressed = false;
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed = false;
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed = false;
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = false;
        if (code == KeyEvent.VK_ENTER) interactPressed = false;
    }
}