import greenfoot.*;

public class SupplyText extends Actor
{
    private int timer = 240;
    private int transparency = 255;
    private int moveCounter;

    public SupplyText(String text, Color color) {
        GreenfootImage img = new GreenfootImage(text, 22, color, new Color(0, 0, 0, 0));
        setImage(img);
    }

    public void act() {
        if (getWorld() == null) {
            return;
        }
    
        moveCounter++;
    
        if (moveCounter % 3 == 0) {
            setLocation(getX(), getY() - 1);
        }
    
        timer--;
    
        if (timer < 60) {
            transparency -= 4;
            getImage().setTransparency(Math.max(0, transparency));
        }
    
        if (timer <= 0) {
            getWorld().removeObject(this);
        }
    }
}