import greenfoot.*;
import java.util.HashMap;
import java.util.Map;

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
}
