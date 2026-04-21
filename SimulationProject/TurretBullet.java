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
    
    public TurretBullet(Entity s, double angle) {
        shooter = s;
        
        this.angle = angle;
        
        bulletImage = new GreenfootImage("soldierBullet.png");
        bulletImage.scale(15,15);
        setImage(bulletImage);
        super.turn(angle);

        this.speed = 2.5;
        this.damage = 1;
    }
    
    public TurretBullet(Entity s, double angle, double speed, int damage) {
        shooter = s;
        
        this.angle = angle;
        
        bulletImage = new GreenfootImage("soldierBullet.png");
        bulletImage.scale(15,15);
        setImage(bulletImage);
        super.turn(angle);

        this.speed = speed; // not sure why but bullets are aims backwards, so we go backwards so that we are aimed at the right way
        this.damage = damage;
    }
    
    public void act()
    {
        super.act();
    }
}
