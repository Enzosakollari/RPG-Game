package monster;

import Entity.Entity;
import main.GamePanel;

import java.awt.Rectangle;

public class Snake extends Entity {

    public Snake(GamePanel gp) {
        super(gp);
        this.isMonster = true;
        this.name = "Slime";
        this.speed = 1;
        this.maxLife = 1;
        this.monsterDamage=1;
        this.currentLife = maxLife;
        this.attackPower = 1;
        this.attackCooldown = 90;
        this.chaseDistance = 6 * 48; // 6 tiles
        this.collision = true;

        setupSimple("C:\\Users\\User\\Desktop\\RPG-Game\\resources\\monster\\", "red-snake");

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    @Override
    public void setAction() {
        if (isPlayerInRange()) {
            chasePlayer();
        } else {
            moveRandomly();
        }
    }

    public boolean isPlayerInRange() {
        int playerX = gp.player.worldx;
        int playerY = gp.player.worldy;

        double distance = Math.sqrt(
                Math.pow(playerX - worldx, 2) +
                        Math.pow(playerY - worldy, 2)
        );

        return distance <= chaseDistance;
    }

    public boolean isPlayerInAttackRange() {
        int playerX = gp.player.worldx;
        int playerY = gp.player.worldy;

        double distance = Math.sqrt(
                Math.pow(playerX - worldx, 2) +
                        Math.pow(playerY - worldy, 2)
        );

        return distance <= attackRange;
    }

    public void chasePlayer() {
        int playerX = gp.player.worldx;
        int playerY = gp.player.worldy;

        int xDiff = playerX - worldx;
        int yDiff = playerY - worldy;

        if (Math.abs(xDiff) > Math.abs(yDiff)) {
            if (xDiff > 0) {
                direction = "right";
            } else {
                direction = "left";
            }
        } else {
            if (yDiff > 0) {
                direction = "down";
            } else {
                direction = "up";
            }
        }

        isChasing = true;
    }

    public void moveRandomly() {
        actionLockCounter++;

        if (actionLockCounter == 120) {
            int directionIndex = (int) (Math.random() * 4);
            switch (directionIndex) {
                case 0:
                    direction = "up";
                    break;
                case 1:
                    direction = "down";
                    break;
                case 2:
                    direction = "left";
                    break;
                case 3:
                    direction = "right";
                    break;
            }
            actionLockCounter = 0;
        }

        isChasing = false;
    }

    public void attackPlayer() {
        if (canAttack && gp.player.invisible == false) {
            gp.player.currentLife -= attackPower;
            gp.player.invisible = true;
            gp.player.invisibleCounter = 0;
            canAttack = false;

            // Play attack sound if available
//            if (gp.sound != null) {
//                gp.playSE(6);
//            }
            System.out.println(name + " attacked! Player health: " + gp.player.currentLife);
        }
    }
}