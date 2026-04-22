import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class BaseBullet here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TurretBullet extends Projectile
{
    /**
     * Act - do whatever the BaseBullet wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private Entity shooter;
    private int damage;
    private double angle;
    private GreenfootImage bulletImage;
    private double speed;
    
    
    public TurretBullet(Entity s, double angle, double speed, int damage) {
        super(s, angle, speed, damage);
        
        bulletImage = new GreenfootImage(getClass().getName() + ".png");
        bulletImage.scale(10, 10);
        setImage(bulletImage);
    }
    
    public void act()
    {
        super.act();
    }
}
