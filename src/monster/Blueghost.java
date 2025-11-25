package monster;

import Entity.Entity;
import main.GamePanel;

import java.awt.Rectangle;

public class Blueghost extends Entity {

    public Blueghost(GamePanel gp) {
        super(gp);
        this.isMonster = true;
        this.name = "BlueGhost";
        this.speed = 1;
        this.maxLife = 4;
        this.currentLife = maxLife;
        this.attackPower = 1;
        this.attackCooldown = 90;
        this.chaseDistance = 6 * 48; // 6 tiles
        this.collision = true;

        System.out.println("=== BLUEGHOST CREATED ===");
        System.out.println("BlueGhost - MaxLife: " + maxLife + ", CurrentLife: " + currentLife);
        System.out.println("BlueGhost - Position: " + worldx + ", " + worldy);

        setupSimple("C:\\Users\\User\\Desktop\\RPG-Game\\resources\\monster\\", "blueghost");

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        System.out.println("BlueGhost setup complete");
        System.out.println("=== END BLUEGHOST CREATION ===");
    }

    @Override
    public void setAction() {
        System.out.println("BlueGhost setAction - Life: " + currentLife + "/" + maxLife);

        // Check if dead using currentLife instead of alive
        if (currentLife <= 0) {
            System.out.println("BlueGhost is dead (life <= 0) - skipping actions");
            return;
        }

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

        boolean inRange = distance <= chaseDistance;
        System.out.println("BlueGhost player range check - Distance: " + distance + ", ChaseDistance: " + chaseDistance + ", InRange: " + inRange);

        return inRange;
    }

    public boolean isPlayerInAttackRange() {
        int playerX = gp.player.worldx;
        int playerY = gp.player.worldy;

        double distance = Math.sqrt(
                Math.pow(playerX - worldx, 2) +
                        Math.pow(playerY - worldy, 2)
        );

        boolean inAttackRange = distance <= attackRange;
        System.out.println("BlueGhost attack range check - Distance: " + distance + ", AttackRange: " + attackRange + ", InAttackRange: " + inAttackRange);

        return inAttackRange;
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
        System.out.println("BlueGhost chasing player - Direction: " + direction + ", Position: " + worldx + ", " + worldy);
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
            System.out.println("BlueGhost moving randomly - New direction: " + direction);
        }

        isChasing = false;
    }

    public void attackPlayer() {
        if (canAttack && gp.player.invisible == false) {
            System.out.println("BlueGhost attacking player!");
            gp.player.currentLife -= attackPower;
            gp.player.invisible = true;
            gp.player.invisibleCounter = 0;
            canAttack = false;

            System.out.println(name + " attacked! Player health: " + gp.player.currentLife);
        } else {
            System.out.println("BlueGhost cannot attack - CanAttack: " + canAttack + ", PlayerInvisible: " + gp.player.invisible);
        }
    }

    // ADD THIS METHOD TO DEBUG DAMAGE - WITHOUT 'alive' CHECKS
    @Override
    public void takeDamage(int damage) {
        System.out.println("=== BLUEGHOST TAKING DAMAGE ===");
        System.out.println("Before damage - Life: " + currentLife + "/" + maxLife);
        System.out.println("Damage amount: " + damage);

        if (this.isMonster) {
            this.currentLife -= damage;
            System.out.println("After damage - Life: " + currentLife + "/" + maxLife);

            if (currentLife <= 0) {
                die();
            }
        } else {
            System.out.println("Damage ignored - Not a monster");
        }
        System.out.println("=== END DAMAGE ===");
    }

    // ADD THIS METHOD - SIMPLIFIED WITHOUT 'alive'

    public void die() {
        System.out.println("=== BLUEGHOST DYING ===");
        System.out.println("BlueGhost died at position: " + worldx + ", " + worldy);
        // You might want to set currentLife to 0 to be safe
        currentLife = 0;
        System.out.println("BlueGhost final life: " + currentLife);
        System.out.println("=== END DEATH ===");
    }

    // ADD THIS METHOD - CHECK LIFE INSTEAD OF ALIVE
    @Override
    public boolean isAlive() {
        boolean isAlive = currentLife > 0;
        System.out.println("BlueGhost isAlive check - CurrentLife: " + currentLife + ", Result: " + isAlive);
        return isAlive;
    }

    // ADD THIS METHOD FOR EXTRA DEBUGGING
    @Override
    public void update() {
        System.out.println("=== BLUEGHOST UPDATE ===");
        System.out.println("Position: " + worldx + ", " + worldy);
        System.out.println("Life: " + currentLife + "/" + maxLife);

        super.update();

        System.out.println("=== END BLUEGHOST UPDATE ===");
    }
}