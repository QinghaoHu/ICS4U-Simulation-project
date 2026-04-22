import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;
/**
 * Write a description of class Soldier here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Soldier extends People
{
    private GreenfootImage img; // image when idle
    private GreenfootImage emptyImg;
    private GreenfootImage shootingImg; // image when shooting
   
    private int shootCounter; // delay the time it takes to shoot for each soldier
    private int moveCounter; // delay the time to move to a different location for each soldier
    private Entity target; // the target that the soldier is trying to shoot
    private String state = idle; // handles what state it is, which can help with handling firing / behaviour
    
    private static final String idle = "idle";
    private static final String chase = "chase";
    private static final String attack = "attack";
    
    private int[] targetPosition = new int[] {-1, -1};
    private static final int attackRange = 200;
    private static final int detectionRange = 600;
    
    public Soldier(Team team) {
        super(team, 30, 2);
        if (team != null) {
            team.addUnit(this);
        }
        setupImage();
    }
    /**
     * Act - do whatever the Soldier wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act(){
        shootCounter++;
        target = findTarget(attackRange); // first check is to see if you can fire your bullet at them
    
        if (target != null) {
            state = attack;
        }else{
            target = findTarget(detectionRange); // second check is to see if enemy is in detection range
            if (target != null) {
                state = chase;
            }else{
                if (state != idle || closeEnough()){ // if it is close enough or it wasn't idle before then it sets a location to walk towards
                    int xLocation = 1000;
                    int yLocation = 100; // place holder values i just want them to walk towards the enemy's base 
                    targetPosition = new int[]{xLocation, yLocation};
                }
                state = idle;
            }
        }
        
        if (state.equals(idle)){
            moveTowards(targetPosition[0], targetPosition[1]); // goes to a random position if no enemies are near enough
        }else if (state.equals(chase)){
            moveTowards(target.getX(), target.getY()); // walks towards enemies 
        }else{
            if (shootCounter % 30 == 0){ // shoots by checking if delay shooting timer is correct and turns to target and shoots
                turnTowards(target.getX(), target.getY());
                shoot(target);
                setImage(shootingImg);
            }
        }
        super.act();
    }
    
    protected abstract void shoot(Entity target); 
    
    private boolean closeEnough(){
        return (getX()-targetPosition[0]) + (getY()-targetPosition[1]) < this.speed*2; // checks if the guy is basically on the location he wants to be
    }
    
    private void setupImage() {
        if (team == null) {
            return;
        }

        if (team.getTeamId() == Team.RED) {
            emptyImg = new GreenfootImage("Red" + getClass().getName() + ".png");
            shootingImg = new GreenfootImage("Red" + getClass().getName() + "Recoil.png");
        } else if (team.getTeamId() == Team.BLUE) {
            emptyImg = new GreenfootImage("Red" + getClass().getName() + ".png");
            shootingImg = new GreenfootImage("Red" + getClass().getName() + "Recoil.png");
        }

        if (emptyImg != null) {
            emptyImg.scale(50, 50);
            setImage(img);
        }
        
        if (shootingImg != null) {
            shootingImg.scale(50, 50);
            setImage(img);
        }
        
        setImage(emptyImg);
    }
    
    public double shootAngle(Entity e){
        double xDiff = getX() - e.getX(); // gets the difference in x between soldier and entity
        double yDiff = getY() - e.getY(); // gets the difference in y between soldier and entity
        
        double angleRad = Math.atan2(-yDiff, -xDiff); // gets the angle of soldier and entity in radians and correct for the grid system
        double angleDeg = Math.toDegrees(angleRad); // converts angle from rad to degrees
        
        return angleDeg; 
    }
}
