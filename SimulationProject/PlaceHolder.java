import greenfoot.Actor;
import greenfoot.GreenfootImage;

import java.util.ArrayList;

/**
 * Write a description of class PlaceHolder here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class PlaceHolder extends Actor {
    /**
     * Act - do whatever the PlaceHolder wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private int height;
    private int width;
    private GreenfootImage img;

    public PlaceHolder(int height, int width) {
        this.height = height;
        this.width = width;
        // Invisible footprint for build checks.
        img = new GreenfootImage(height, width);
        setImage(img);
    }

    public void act() {
        // Intentionally blank.
    }

    public boolean ifTouchingOthers() {
        ArrayList<Buildings> intersectingBuilding = (ArrayList<Buildings>) getIntersectingObjects(Buildings.class);
        ArrayList<Resources> intersectingResources = (ArrayList<Resources>) getIntersectingObjects(Resources.class);
        ArrayList<People> intersectingPeoples = (ArrayList<People>) getIntersectingObjects(People.class);
        // Same collision test as the real building.
        if (intersectingBuilding.isEmpty() && intersectingResources.isEmpty() && intersectingPeoples.isEmpty()) {
            return false;
        }
        return true;
    }
}
