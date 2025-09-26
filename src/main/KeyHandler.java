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

        if (gp.gameState == gp.titleState) {
            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) gp.ui.commandNum = 2;
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) gp.ui.commandNum = 0;
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    gp.gameState = gp.nameState;
                    gp.ui.playerName = ""; // Reset name
                }
                if (gp.ui.commandNum == 1) {
                }
                if (gp.ui.commandNum == 2) {
                    System.exit(0);
                }
            }
        }

        // NAME ENTRY STATE
        else if (gp.gameState == gp.nameState) {
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.playerName != null && !gp.ui.playerName.isEmpty()) {
                    gp.gameState = gp.classState;
                }
            } else if (Character.isLetterOrDigit(e.getKeyChar()) || e.getKeyChar() == ' ') {
                gp.ui.playerName += e.getKeyChar();
            } else if (code == KeyEvent.VK_BACK_SPACE && gp.ui.playerName.length() > 0) {
                gp.ui.playerName = gp.ui.playerName.substring(0, gp.ui.playerName.length() - 1);
            }
        }


        else if (gp.gameState == gp.classState) {
            if (code == KeyEvent.VK_W) {
                gp.ui.classCommandNum--;
                if (gp.ui.classCommandNum < 0) gp.ui.classCommandNum = gp.ui.classes.length - 1;
            }
            if (code == KeyEvent.VK_S) {
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
            }
        }

        else if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_W) upPressed = true;
            if (code == KeyEvent.VK_A) leftPressed = true;
            if (code == KeyEvent.VK_S) downPressed = true;
            if (code == KeyEvent.VK_D) rightPressed = true;
            if (code == KeyEvent.VK_P) gp.gameState = gp.pauseState;
            if (code == KeyEvent.VK_ENTER) interactPressed = true;
        }

        else if (gp.gameState == gp.pauseState) {
            if (code == KeyEvent.VK_P) gp.gameState = gp.playState;
        }

        else if (gp.gameState == gp.dialogueState) {
            if (code == KeyEvent.VK_ENTER) {
                gp.gameState = gp.playState;
                gp.ui.currentDialogue = "";
            }
        }
        else if (gp.gameState == gp.gameOverState) {
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
                } else if (gp.ui.commandNum == 1) {
                    System.exit(0);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_ENTER) interactPressed = false;
    }
}