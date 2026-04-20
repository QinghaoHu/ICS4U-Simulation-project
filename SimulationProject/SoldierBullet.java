import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;
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
    
    private Entity target;
    private Soldier shooter;
    private double angle;
    private double speed;
    private GreenfootImage bulletImage;
    private int damage;
    
    public SoldierBullet(Soldier s, Entity e, double angle) {
        target = e;
        shooter = s;
        this.angle = angle;
        //bulletImage = new GreenfootImage("soldierBullet.png");
        //setImage(bulletImage);
        super.turn(angle);
        damage = s.getDamage();
        speed = 2.5;
        move(-300);
    }
    
    public void act()
    {
        super.act();
        move(speed * -1);
        if (target instanceof Buildings) {
            ((Buildings) target).takeDamage(damage);
        }
        else if (target instanceof People) {
            ((People) target).setHealth(((People) target).getHealth() - damage);
        }
    }
}