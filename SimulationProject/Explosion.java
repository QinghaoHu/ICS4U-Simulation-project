import greenfoot.*;
import java.util.*;
/**
 * Write a description of class Explosion here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Explosion extends Actor {
    private int size;
    private int maxSize;
    private int transperency = 255;
    private Color explosionColor;
    private int transperencyDecrease;
    private int sizeIncrease;
    private static boolean isInUse = false;

    public Explosion(int size, int time) {
        this.size = size;
        transperencyDecrease = (int)(60 * 255/time);
        this.explosionColor = Color.RED;

        updateImage();
    }

    public Explosion(int size, int sizeIncrease, int maxSize, int frame, Color explosionColor) {
        this.size = size;
        isInUse = true;
        this.explosionColor = explosionColor;

        transperencyDecrease = 255/frame;
        this.maxSize = maxSize;

        this.sizeIncrease = sizeIncrease;

        if (transperencyDecrease == 0) {
            transperencyDecrease = 1;
        }

        updateImage();
    }

    /**
     * Act - do whatever the Explosion wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {
        // Add your action code here.
        size += sizeIncrease;
        transperency -= transperencyDecrease;

//        if (size >= maxSize) {
//            sizeIncrease = 0;
//        }

        if (transperency <= 0) {
            isInUse = false;
            getWorld().removeObject(this);
        } else {
            updateImage();
        }
    }

    private void updateImage() {
        GreenfootImage image = new GreenfootImage(size, size);
        image.setColor(new Color(explosionColor.getRed(), explosionColor.getGreen(), explosionColor.getBlue(), transperency));
        image.fillOval(0, 0, size, size);
        setImage(image);
    }

    public static boolean getIsInUse() {
        return isInUse;
    }
}
