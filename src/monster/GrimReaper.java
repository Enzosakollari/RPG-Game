package monster;

import Entity.Entity;
import main.GamePanel;

import java.awt.Rectangle;

public class GrimReaper extends Entity {

    public GrimReaper(GamePanel gp) {
        super(gp);
        this.isMonster = true;
        this.name = "GrimReaper";
        this.speed = 3;
        this.monsterDamage = 2;
        this.maxLife = 5;
        this.currentLife = maxLife;
        this.attackPower = 1;
        this.attackCooldown = 90;
        this.chaseDistance = 6 * 48; // 6 tiles
        this.attackRange = 72; // 1.5 tiles attack range for GrimReaper
        this.collision = true;

        System.out.println("=== GRIM REAPER CREATED ===");
        System.out.println("GrimReaper - MaxLife: " + maxLife + ", CurrentLife: " + currentLife);
        System.out.println("GrimReaper - AttackRange: " + attackRange + ", ChaseDistance: " + chaseDistance);
        System.out.println("GrimReaper - Damage: " + attackPower + ", Speed: " + speed);

        setupSimple("C:\\Users\\User\\Desktop\\RPG-Game\\resources\\monster\\", "grimreaper");

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        System.out.println("GrimReaper setup complete");
        System.out.println("=== END GRIM REAPER CREATION ===");
    }

    @Override
    public void setAction() {
        System.out.println("GrimReaper setAction - Life: " + currentLife + "/" + maxLife);

        // Check if dead
        if (currentLife <= 0) {
            System.out.println("GrimReaper is dead (life <= 0) - skipping actions");
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
        System.out.println("GrimReaper player range check - Distance: " + (int)distance + ", ChaseDistance: " + chaseDistance + ", InRange: " + inRange);

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
        System.out.println("GrimReaper attack range check - Distance: " + (int)distance + ", AttackRange: " + attackRange + ", InAttackRange: " + inAttackRange);

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
        System.out.println("GrimReaper chasing player - Direction: " + direction + ", Position: " + worldx + ", " + worldy);
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
            System.out.println("GrimReaper moving randomly - New direction: " + direction);
        }

        isChasing = false;
    }

    public void attackPlayer() {
        if (canAttack && !gp.player.invisible) {
            System.out.println("=== GRIM REAPER ATTACKING PLAYER ===");
            System.out.println("Before attack - Player health: " + gp.player.currentLife);

            gp.player.currentLife -= attackPower;
            gp.player.invisible = true;
            gp.player.invisibleCounter = 0;
            canAttack = false;

            // Play attack sound if available
//            if (gp.sound != null) {
//                gp.playSE(6);
//            }
            System.out.println(name + " attacked! Player health: " + gp.player.currentLife);
            System.out.println("=== END GRIM REAPER ATTACK ===");
        } else {
            System.out.println("GrimReaper cannot attack - CanAttack: " + canAttack + ", PlayerInvisible: " + gp.player.invisible);
        }
    }

    // ADD DAMAGE METHOD IF NOT IN ENTITY CLASS
    @Override
    public void takeDamage(int damage) {
        System.out.println("=== GRIM REAPER TAKING DAMAGE ===");
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

    public void die() {
        System.out.println("=== GRIM REAPER DYING ===");
        System.out.println("GrimReaper died at position: " + worldx + ", " + worldy);
        currentLife = 0;
        System.out.println("GrimReaper final life: " + currentLife);
        System.out.println("=== END DEATH ===");
    }

    @Override
    public boolean isAlive() {
        boolean isAlive = currentLife > 0;
        System.out.println("GrimReaper isAlive check - CurrentLife: " + currentLife + ", Result: " + isAlive);
        return isAlive;
    }

    // ADD UPDATE METHOD FOR EXTRA DEBUGGING
    @Override
    public void update() {
        System.out.println("=== GRIM REAPER UPDATE ===");
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
                System.out.println("GrimReaper can attack again!");
            }
        }

        System.out.println("=== END GRIM REAPER UPDATE ===");
    }
}