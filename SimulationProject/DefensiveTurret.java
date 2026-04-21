import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class DefensiveTurret here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DefensiveTurret extends Buildings
{
    private GreenfootImage img; // image when idle
    private GreenfootImage emptyImg;
    private GreenfootImage shootingImg; // image when shooting
    
    private int shootCounter; // delay the time it takes to shoot for each soldier
    private static final int attackRange = 225;


    public DefensiveTurret(Team team) {
        super(team, 700);
        if (team != null) {
            team.addBuilding(this);
        }
        setupImage();
    }
    public void act()
    {
        shootCounter++;
        Entity target = findTarget(attackRange);
        if(target != null){
            if (shootCounter % 30 == 0){ // shoots by checking if delay shooting timer is correct and turns to target and shoots
                turnTowards(target.getX(), target.getY());
                getWorld().addObject(new TurretBullet(this, shootAngle(target), 2.5, 3), getX(), getY()); // adds bullet
            }
        }
        super.act();
    }
    private void setupImage() {
        if (team == null) {
            return;
        }

        if (team.getTeamId() == Team.RED) {
            img = new GreenfootImage("RedTurret.png");            
        } else if (team.getTeamId() == Team.BLUE) {
            img = new GreenfootImage("BlueTurret.png");
        }
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
    
}
