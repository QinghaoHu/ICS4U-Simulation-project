import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;
import java.util.*;
/**
 * Shared logic for all combat units.
 * Handles target choice, movement, and firing.
 */
public abstract class Soldier extends People
{
    private static final int RED_RALLY_X = 1000;
    private static final int RED_RALLY_Y = 165;
    private static final int BLUE_RALLY_X = 200;
    private static final int BLUE_RALLY_Y = 490;

    protected GreenfootImage img; // image when idle
    protected GreenfootImage emptyImg;
    protected GreenfootImage shootingImg; // image when shooting
   
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
        super(team, 20, 4);
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
        // Prefer a shot, then a chase, then the rally point.
        target = findTarget(attackRange); // first check is to see if you can fire your bullet at them
        repelSoldiers();
        if (target != null) {
            state = attack;
        }else{
            target = findTarget(detectionRange); // second check is to see if enemy is in detection range
            if (target != null) {
                state = chase;
            }else{
                if (!state.equals(idle) || !hasTargetPosition() || closeEnough()){ // if it is close enough or it wasn't idle before then it sets a location to walk towards
                    targetPosition = enemyBasePosition();
                }
                state = idle;
            }
        }
        
        if (state.equals(idle)){
            // Idle just means keep advancing.
            moveTowards(targetPosition[0], targetPosition[1]); // goes to a random position if no enemies are near enough
        }else if (state.equals(chase)){
            // Close the gap before firing.
            moveTowards(target.getX(), target.getY()); // walks towards enemies 
        }else{
            // Fire on a short rhythm so it does not spam.
            if (shootCounter % 15 == 0){ // shoots by checking if delay shooting timer is correct and turns to target and shoots
                turnTowards(target.getX(), target.getY());
                
                shoot(target);
                setImage(shootingImg);
            }
        }
        super.act();
    }
     /**
     * @author Mr Cohen
     * @since February 2023
     */
     public void repelSoldiers() {
        List<Soldier> pedsTouching = getIntersectingObjects(Soldier.class);

        ArrayList<Actor> actorsTouching = new ArrayList<Actor>();

        // Tiny shove so soldiers do not stack up.
        //actorsTouching.addAll(pedsTouching);
        for (Soldier p : pedsTouching){
            actorsTouching.add(p);
        }

        pushAwayFromObjects(actorsTouching, 4);
    }
     /**
     * @author Mr Cohen
     * @since February 2023
     */
    
     public void pushAwayFromObjects(ArrayList<Actor> nearbyObjects, double minDistance) {
        // Push along one axis only to keep it simple.
        int currentX = getX();
        int currentY = getY();

        // Iterate through the nearby objects.
        for (Actor object : nearbyObjects) {
            // Grab the nearby actor's footprint.
            int objectX = object.getX();
            int objectY = object.getY();
            int objectWidth = object.getImage().getWidth();
            int objectHeight = object.getImage().getHeight();

            // Quick distance check before doing the push.
            double distance = Math.sqrt(Math.pow(currentX - objectX, 2) + Math.pow(currentY - objectY, 2));

            // Approximate both actors as circles.
            double thisRadius = Math.max(getImage().getWidth() / 2.0, getImage().getHeight() / 2.0);
            double objectRadius = Math.max(objectWidth / 2.0, objectHeight / 2.0);

            // Only nudge when they are too close.
            if (distance < (thisRadius + objectRadius + minDistance)) {
                // Direction from this soldier to the other one.
                int deltaX = objectX - currentX;
                int deltaY = objectY - currentY;

                // Normalize before applying the shove.
                double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                if (length == 0){
                    continue;
                }
                double unitX = deltaX / length;
                double unitY = deltaY / length;

                // How far apart they should end up.
                double pushAmount = (thisRadius + objectRadius + minDistance) - distance;

                // Move the other actor out of the overlap.
                object.setLocation(objectX, objectY + (int)(pushAmount * unitY));

                // 2d version, allows pushing on x and y axis, commented out for now but it works, just not the
                // effect I'm after:
                //object.setLocation(objectX + (int)(pushAmount * unitX), objectY + (int)(pushAmount * unitY));
            }
        }
    }
    protected abstract void shoot(Entity target); 
    
    private boolean closeEnough(){
        return Math.hypot(getX() - targetPosition[0], getY() - targetPosition[1]) < this.speed * 2; // checks if the guy is basically on the location he wants to be
    }

    private boolean hasTargetPosition() {
        return targetPosition[0] >= 0 && targetPosition[1] >= 0;
    }

    private int[] enemyBasePosition() {
        World world = getWorld();

        if (world != null) {
            // Use the real enemy base when it exists.
            for (Base base : world.getObjects(Base.class)) {
                if (isOpponent(base)) {
                    return new int[]{base.getX(), base.getY()};
                }
            }
        }

        if (team != null && team.getTeamId() == Team.BLUE) {
            // Fallback rally point for the blue side.
            return new int[]{BLUE_RALLY_X, BLUE_RALLY_Y};
        }

        // Fallback rally point for the red side.
        return new int[]{RED_RALLY_X, RED_RALLY_Y};
    }
    
    protected void setupImage() {
        if (team == null) {
            return;
        }

        if (team.getTeamId() == Team.RED) {
            emptyImg = ResourceCache.getImage("Red" + getClass().getName() + ".png");
            shootingImg = ResourceCache.getImage("Red" + getClass().getName() + "Recoil.png");
        } else if (team.getTeamId() == Team.BLUE) {
            emptyImg = ResourceCache.getImage("Blue" + getClass().getName() + ".png");
            shootingImg = ResourceCache.getImage("Blue" + getClass().getName() + "Recoil.png");
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
        // Convert world position into an aim angle.
        double xDiff = getX() - e.getX(); // gets the difference in x between soldier and entity
        double yDiff = getY() - e.getY(); // gets the difference in y between soldier and entity
        
        double angleRad = Math.atan2(-yDiff, -xDiff); // gets the angle of soldier and entity in radians and correct for the grid system
        double angleDeg = Math.toDegrees(angleRad); // converts angle from rad to degrees
        
        return angleDeg; 
    }
}
