import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Marine here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Marine extends Soldier
{
    /**
     * Act - do whatever the Marine wants to do. This method is called whenever
     * the 'Act' or 'Run' button 
     * gets pressed in the environment.
     */
    
    private static final int cost = 100;
    private static int maxMarineCoolDown = 400;
    
    public Marine(Team team) {
        super(team);
        if (team != null) {
            team.addUnit(this);
        }
    }
    
    public int getCost(){
        return 75; 
    }
    
    public void act()
    {
        super.act();
        // Add your action code here.
    }
    
    protected void shoot(Entity target){
        turnTowards(target.getX(), target.getY());
        double angle = shootAngle(target);
        int X = getX() + (int)(centerDist * Math.cos(Math.toRadians(angle)));
        int Y = getY() + (int)(centerDist * Math.sin(Math.toRadians(angle)));
        getWorld().addObject(new SoldierBullet(this, angle, 2.5, 3), X, Y); // adds bullet
    }

    public static int getMaxMarineCoolDown() {
        return maxMarineCoolDown;
    }

    public static void modifyMaxMarineCoolDown(int marineCoolDown) {
        maxMarineCoolDown = marineCoolDown;
    }
}
