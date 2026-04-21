import greenfoot.*;
/**
 * Write a description of class Projectile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HealthBar extends Actor
{
    private Entity target;
    private int width = 50;
    private int height = 6;
    private int yOffset = 30;

    //yOffset is for making sure the healthabr doesnt clip into the people/buildings
    public HealthBar(Entity target, int yOffset) {
        this.target = target;
        this.yOffset = yOffset;
        updateImage();
    }

    public void act()
    {
        if (target == null || target.getWorld() == null) {
            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
            return;
        }

        setLocation(target.getX(), target.getY() - yOffset);

        updateImage();
    }
    //used gpt for this, so that it could be able to work on both buildings and people
    private void updateImage() {
        int health = target.getHealth();
        int max = target.getMaxHealth();
    
        int barWidth = (int)((health / (double)max) * width);
    
        GreenfootImage img = new GreenfootImage(width, height);
    
        img.setColor(Color.RED);
        img.fillRect(0, 0, width, height);
    
        img.setColor(Color.GREEN);
        img.fillRect(0, 0, barWidth, height);
    
        setImage(img);
    }
}