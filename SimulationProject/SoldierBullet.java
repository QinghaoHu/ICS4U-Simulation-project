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
    
    private Entity target, shooter;
    private int damage;
    private double angle;
    private GreenfootImage bulletImage;
    private double speed;
    
    public SoldierBullet(Entity e, Entity s, double angle) {
        target = e;
        shooter = s;
        
        this.angle = angle;
        
        bulletImage = new GreenfootImage("soldierBullet.png");
        bulletImage.scale(10,10);
        setImage(bulletImage);
        super.turn(angle);

        speed = -2.5;
        damage = 1;
    }
    
    public void act()
    {
        super.act();
        move(speed);
        if (getWorld() == null) {
            return;
        }
        
        Entity targetHit = (Entity)getOneIntersectingObject(Entity.class);
        if (targetHit != null&& !targetHit.getTeam().equals(shooter.getTeam())) {
            if (targetHit instanceof Buildings) {
                ((Buildings) targetHit).takeDamage(damage);
            }
            else if (targetHit instanceof People) {
                ((People) targetHit).setHealth(((People) targetHit).getHealth() - damage);
            }
            getWorld().removeObject(this);
            return;
        }
    }
}
