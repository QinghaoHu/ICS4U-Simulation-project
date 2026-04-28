import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class DefensiveTurret here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Turret extends Buildings
{
    private GreenfootImage img; // image when idle
    private GreenfootImage emptyImg;
    private GreenfootImage shootingImg; // image when shooting
    private int centerDist = 70;
    private int shootCounter; // delay the time it takes to shoot for each soldier
    private final int attackRange = 260;
    private final int cost = 175;
    private int damage = 19;
    private int decayTimer = 0;
    private int DECAY_RATE = 20;

    public Turret(Team team) {
        super(team, 1000);
        if (team != null) {
            team.addBuilding(this);
    
            Base base = team.getBase();
            if (base != null) {
                int level = base.getLevel();
    
                maxHealth += (level - 1) * 50; //adds 50 health per upgrade
                health += (level - 1) * 50;
    
                damage += (level - 1) * 2; //deals extra 2 damage per upgrade
                DECAY_RATE += (level - 1) * 2; //decay rate increases by 2 per leveling upgrade
            }
        }
        setupImage();
    }
    
    public int getCost(){
        return 175; 
    }
    
    public void act()
    {
        shootCounter++;

        decayTimer++;
        if (decayTimer >= 60) {
            health-=(DECAY_RATE);
            decayTimer = 0; // every 60 frames it will decrease its health
        }

        Entity target = findTarget(attackRange);
        if(target != null){
            if (shootCounter % 60 == 0){ // shoots by checking if delay shooting timer is correct and turns to target and shoots
                turnTowards(target.getX(), target.getY());
                double angle = getRotation();

                // spawns bullet at the tip
                int bulletX = getX() + (int)(centerDist * Math.cos(Math.toRadians(angle)));
                int bulletY = getY() + (int)(centerDist * Math.sin(Math.toRadians(angle)));

                getWorld().addObject(new SoldierBullet(this, angle, 3.5, damage, 15), bulletX, bulletY);
            }
            
            
        }
        super.act();
    }
    private void setupImage() {
        if (team == null) {
            return;
        }

        img = ResourceCache.getImage(team.getName() + getClass().getName() +  ".png");
        
        img.scale(150, 150);
        setImage(img);
        
        
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
        maxHealth += 50;
        health += 50;
        damage += 5;
        DECAY_RATE += 10;
        updateStatBar();
    }
}
