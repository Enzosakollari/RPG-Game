package main;

import java.awt.*;

public class EventHandler {
    Rectangle eventRect;
    int eventRectDefaultX, eventRectDefaultY;
    GamePanel gp;

    public EventHandler(GamePanel gp) {
        this.gp = gp;
        eventRect = new Rectangle();
        eventRect.x = 23;
        eventRect.y = 23;
        eventRect.width = 2;
        eventRect.height = 2;
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
    }

    boolean canTriggerEvent = true;

    public void checkEvent() {
        int playerCol = gp.player.worldx / gp.tileSize;
        int playerRow = gp.player.worldy / gp.tileSize;

        // Damage pits
        if (hit(4, 10, "any") || hit(5, 11, "any") ||
                hit(74, 20, "any") || hit(87, 79, "any") ||
                hit(89, 83, "any") || hit(63, 80, "any") ||
                hit(11, 15, "any")) {
            if (canTriggerEvent) {
                damagePit(gp.dialogueState);
                canTriggerEvent = false;
            }
        } else {
            canTriggerEvent = true;
        }

        // Healing pit
        if (hit(24, 12, "any")) {
            if (canTriggerEvent) {
                healingPit(gp.dialogueState);
                canTriggerEvent = false;
            }
        }


        checkTeleport(98, 87, 92, 79);
        checkTeleport(91, 80, 97, 87);

        checkTeleport(10, 12, 40, 10);
        checkTeleport(41, 10, 11, 12);

        checkTeleport(70, 80, 80, 90);
        checkTeleport(80, 90, 70, 80);
    }

    public boolean hit(int eventCol, int eventRow, String reqDirection) {
        boolean hit = false;

        int playerAreaX = gp.player.solidArea.x;
        int playerAreaY = gp.player.solidArea.y;
        int eventAreaX = eventRect.x;
        int eventAreaY = eventRect.y;

        gp.player.solidArea.x = gp.player.worldx + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldy + gp.player.solidArea.y;

        eventRect.x = eventCol * gp.tileSize + eventRectDefaultX;
        eventRect.y = eventRow * gp.tileSize + eventRectDefaultY;

        if (gp.player.solidArea.intersects(eventRect)) {
            if (gp.player.direction.equals(reqDirection) || reqDirection.equals("any")) {
                hit = true;
                System.out.println("Event triggered!");
            }
        }

        gp.player.solidArea.x = playerAreaX;
        gp.player.solidArea.y = playerAreaY;
        eventRect.x = eventAreaX;
        eventRect.y = eventAreaY;

        return hit;
    }

    public void damagePit(int gameState) {
        System.out.println("Damage pit triggered!");
        gp.gameState = gameState;
        gp.ui.currentDialogue = "You fall into a pit";
        gp.player.currentLife -= 1;
    }

    public void healingPit(int gameState) {
        if (gp.keyH.interactPressed) {
            gp.gameState = gameState;
            gp.ui.currentDialogue = "You from the pool of holy water.\nYour life has been recovered.";
            gp.player.currentLife = gp.player.maxLife;
        }
        gp.keyH.interactPressed = false;
    }

    public void teleport(int gameState, int targetCol, int targetRow) {
        gp.gameState = gameState;
        gp.ui.currentDialogue = "You teleported!";
        gp.player.worldx = targetCol * gp.tileSize;
        gp.player.worldy = targetRow * gp.tileSize;
    }


    public void checkTeleport(int fromCol, int fromRow, int toCol, int toRow) {
        if (hit(fromCol, fromRow, "any")) {
            if (canTriggerEvent) {
                teleport(gp.dialogueState, toCol, toRow);
                canTriggerEvent = false;
            }
        }
    }
}
