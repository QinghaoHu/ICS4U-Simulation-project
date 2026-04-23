import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class SoldierBullet here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SoldierBullet extends Projectile
{
    /**
     * Act - do whatever the SoldierBullet wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    public SoldierBullet(Entity s, double angle) {
        super(s, angle, 2.5, 1);
    }
    
    public SoldierBullet(Entity s, double angle, double speed, int damage){
        super(s, angle, speed, damage);
    }
    
    public SoldierBullet(Entity s, double angle, double speed, int damage, int size){
        super(s, angle, speed, damage);
        
        this.img.scale(size,size);
        setImage(this.img);
    }
    
    public void act()
    {
        super.act();
    }
}
