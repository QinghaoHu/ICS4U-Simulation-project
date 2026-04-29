import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Static defense for holding lanes and burning down attackers.
 */
public class Turret extends Buildings
{
    private GreenfootImage img; // image when idle
    private int centerDist = 70;
    private int shootCounter; // delay the time it takes to shoot for each soldier
    private final int attackRange = 260;
    private final int cost = 150;
    private int damage = 32;
    private int decayTimer = 0;
    private int DECAY_RATE = 20;

    public Turret(Team team) {
        super(team, 850);
        if (team != null) {
            team.addBuilding(this);
    
            Base base = team.getBase();
            if (base != null) {
                int level = base.getLevel();

                // Base level boosts placed turrets too.
                maxHealth += (level - 1) * 50; //adds 50 health per upgrade
                health += (level - 1) * 50;
    
                damage += (level - 1) * 2; //deals extra 2 damage per upgrade
                DECAY_RATE += (level - 1) * 2; //decay rate increases by 2 per leveling upgrade
            }
        }
        setupImage();
    }
    
    public int getCost(){
        return cost; 
    }
    
    public void act()
    {
        shootCounter++;

        // Turrets decay, so they need to keep fighting.
        decayTimer++;
        if (decayTimer >= 60) {
            health -= (DECAY_RATE);
            decayTimer = 0; // every 60 frames it will decrease its health
            updateStatBar();
        }

        Entity target = findTarget(attackRange);
        if(target != null){
            // Fire on a fixed rhythm once something is in range.
            if (shootCounter % 60 == 0){ // shoots by checking if delay shooting timer is correct and turns to target and shoots
                turnTowards(target.getX(), target.getY());
                double angle = getRotation();

                // spawns bullet at the tip
                int bulletX = getX() + (int)(centerDist * Math.cos(Math.toRadians(angle)));
                int bulletY = getY() + (int)(centerDist * Math.sin(Math.toRadians(angle)));

                if (getWorld() != null) {
                    getWorld().addObject(new SoldierBullet(this, angle, 3.5, damage, 15), bulletX, bulletY);
                }
            }
        }

        super.act();
    }

    private void setupImage() {
        if (team == null) {
            return;
        }

        // Turret art follows the owning team.
        img = ResourceCache.getImage(team.getName() + getClass().getName() + ".png");

        if (img != null) {
            img.scale(150, 150);
            setImage(img);
        }
    }
    
    public double shootAngle(Entity e){
        double xDiff = getX() - e.getX(); // gets the difference in x between soldier and entity
        double yDiff = getY() - e.getY(); // gets the difference in y between soldier and entity
        
        double angleRad = Math.atan2(-yDiff, -xDiff); // gets the angle of soldier and entity in radians and correct for the grid system
        double angleDeg = Math.toDegrees(angleRad); // converts angle from rad to degrees
        
        return angleDeg; 
    }
    
    private void updateDirection(int dx, int dy) {
        if (dx != 0 || dy != 0) {
            setRotation((int) Math.toDegrees(Math.atan2(dy, dx))); 
        }
    }
    
    public void upgradeTurret() {
        // Upgrade gives more HP and a bit more bite.
        maxHealth += 50;
        health += 50;
        damage += 5;
        DECAY_RATE += 10;
        updateStatBar();
    }
}
