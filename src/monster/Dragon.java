package monster;

import Entity.Entity;
import main.GamePanel;

import java.awt.*;

public class Dragon extends Entity {

    public Dragon(GamePanel gp) {
        super(gp);
        this.isMonster = true;
        this.name = "Dragon";
        this.speed = 1;
        this.maxLife = 10; // Increased: Dragons should be tough!
        this.monsterDamage = 1;
        this.currentLife = maxLife;
        this.attackPower = 3; // Increased: Dragons hit hard!
        this.attackCooldown = 60; // Increased cooldown for balance
        this.chaseDistance = 8 * 48; // 8 tiles - dragons have better vision
        this.attackRange = 96; // 2 tiles attack range for Dragon (breathes fire!)
        this.collision = true;

        System.out.println("=== DRAGON CREATED ===");
        System.out.println("Dragon - MaxLife: " + maxLife + ", CurrentLife: " + currentLife);
        System.out.println("Dragon - AttackRange: " + attackRange + ", ChaseDistance: " + chaseDistance);
        System.out.println("Dragon - Damage: " + attackPower + ", Speed: " + speed);

        setupSimple("C:\\Users\\User\\Desktop\\RPG-Game\\resources\\monster\\", "dragon1");

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        System.out.println("Dragon setup complete - This is a BOSS monster!");
        System.out.println("=== END DRAGON CREATION ===");
    }

    @Override
    public void setAction() {
        System.out.println("Dragon setAction - Life: " + currentLife + "/" + maxLife);

        // Check if dead
        if (currentLife <= 0) {
            System.out.println("Dragon is dead (life <= 0) - skipping actions");
            return;
        }

        if (isPlayerInRange()) {
            chasePlayer();

            // Check if player is in attack range and attack if possible
            if (isPlayerInAttackRange() && canAttack) {
                attackPlayer();
            }
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
        System.out.println("Dragon player range check - Distance: " + (int)distance + ", ChaseDistance: " + chaseDistance + ", InRange: " + inRange);

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
        System.out.println("Dragon attack range check - Distance: " + (int)distance + ", AttackRange: " + attackRange + ", InAttackRange: " + inAttackRange);

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
        System.out.println("Dragon chasing player - Direction: " + direction + ", Position: " + worldx + ", " + worldy);
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
            System.out.println("Dragon moving randomly - New direction: " + direction);
        }

        isChasing = false;
    }

    public void attackPlayer() {
        if (canAttack && !gp.player.invisible) {
            System.out.println("=== DRAGON ATTACKING PLAYER ===");
            System.out.println("Before attack - Player health: " + gp.player.currentLife);

            gp.player.currentLife -= attackPower;
            gp.player.invisible = true;
            gp.player.invisibleCounter = 0;
            canAttack = false;

            // Play attack sound if available
//            if (gp.sound != null) {
//                gp.playSE(6);
//            }
            System.out.println("DRAGON FIRE BREATH! Player health: " + gp.player.currentLife);
            System.out.println("=== END DRAGON ATTACK ===");
        } else {
            System.out.println("Dragon cannot attack - CanAttack: " + canAttack + ", PlayerInvisible: " + gp.player.invisible);
        }
    }

    // ADD DAMAGE METHOD IF NOT IN ENTITY CLASS
    @Override
    public void takeDamage(int damage) {
        System.out.println("=== DRAGON TAKING DAMAGE ===");
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

    // ADD DIE METHOD IF NOT IN ENTITY CLASS
    public void die() {
        System.out.println("=== DRAGON DYING ===");
        System.out.println("DRAGON DEFEATED! at position: " + worldx + ", " + worldy);
        currentLife = 0;
        System.out.println("Dragon final life: " + currentLife);
        System.out.println("=== END DRAGON DEATH ===");
    }

    // ADD ISALIVE METHOD IF NOT IN ENTITY CLASS
    @Override
    public boolean isAlive() {
        boolean isAlive = currentLife > 0;
        System.out.println("Dragon isAlive check - CurrentLife: " + currentLife + ", Result: " + isAlive);
        return isAlive;
    }

    // ADD UPDATE METHOD FOR EXTRA DEBUGGING
    @Override
    public void update() {
        System.out.println("=== DRAGON UPDATE ===");
        System.out.println("Position: " + worldx + ", " + worldy);
        System.out.println("Life: " + currentLife + "/" + maxLife);
        System.out.println("CanAttack: " + canAttack);

        super.update();

        // Attack cooldown
        if (!canAttack) {
            attackCounter++;
            if (attackCounter >= attackCooldown) {
                canAttack = true;
                attackCounter = 0;
                System.out.println("Dragon can breathe fire again!");
            }
        }

        System.out.println("=== END DRAGON UPDATE ===");
    }
}