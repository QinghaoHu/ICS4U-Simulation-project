import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Projectile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Projectile extends SuperSmoothMover
{
    /**
     * Act - do whatever the Projectile wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    protected Entity shooter;
    protected int damage;
    protected double angle;
    protected GreenfootImage img;
    protected double speed;
    public Projectile(Entity s, double angle, double speed, int damage){
        shooter = s;
        
        this.angle = angle;
        super.turn(angle);

        this.speed = speed; 
        this.damage = damage;
        sounds.put("shoot", new GreenfootSound(s.getClass().getName() + "Shoot.mp3"));
        
        sounds.get("shoot").play(); 
        
        img = new GreenfootImage(getClass().getName() + ".png");
        img.scale(10, 10);
        setImage(img);
    }
    
    public void act()
    {
        if(isAtEdge()){
            getWorld().removeObject(this);
            return;
        }
        move(speed);
        if (getWorld() == null) {
            return;
        }
        
        Entity targetHit = (Entity)getOneIntersectingObject(Entity.class);
        if (targetHit != null&& !targetHit.getTeam().equals(shooter.getTeam())) {
            ((Entity)targetHit).damage(damage); // damages entity if hit
            getWorld().removeObject(this);
            return;
        }
    }
}
