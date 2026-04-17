import greenfoot.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
/**
 * Write a description of class MyWorld here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class MyWorld extends World {

    /**
     * Constructor for objects of class MyWorld.
     *
     */
    
    private String state = "game";
    private static GreenfootImage background;
    private Map<String, Runnable> states = new HashMap<>();
    
    public MyWorld() {
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1200, 800, 1);
        states.put("game", () -> setGameState());
        states.put("title", () -> setTitleState());
    }
    
    private void setUpWorld () {
        background = new GreenfootImage(state + ".png");
        setBackground(background);
        states.get(state).run();
    }
    
    private void setGameState() {
        
    }
    
    private void setTitleState() {
        
    }
    
    public void changeState(String s){
        state = s;
        setUpWorld();
    }
    
    public String getState(){
        return state; 
    }

    public Entity findClosestOpponent(Entity source) {

        ArrayList<Entity> allEntities = (ArrayList) getObjects(Entity.class);

        Entity closest = null;
        double closestDist = Double.MAX_VALUE;

        for (Entity e : allEntities) {

            // skip self
            if (e == source) continue;

            // must be alive
            if (!e.isAlive()) continue;

            // must be enemy
            if (!source.isOpponent(e)) continue;

            double dx = source.getX() - e.getX();
            double dy = source.getY() - e.getY();
            double distSquared = dx * dx + dy * dy;

            if (distSquared < closestDist) {
                closestDist = distSquared;
                closest = e;
            }
        }

        return closest;
    }
}
