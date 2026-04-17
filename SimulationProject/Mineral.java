import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Mineral here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Mineral extends Actor
{
    private boolean depleted;
    private double maxHits, hits;
    private GreenfootImage mineral;
    
    public Mineral(int side) {
        mineral = new GreenfootImage("Resources" + side + ".png");
        setImage(mineral);
        
        depleted = false;
        maxHits = 25;
        hits = 0;
    }
    
    public void mine() {
        if (hits == maxHits) {
            getWorld().removeObject(this);
            return;
        }
        hits++;
    }
}
