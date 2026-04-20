import greenfoot.*;

import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

/**
 * Main simulation world.
 * <p>
 * This version intentionally changes most of the world setup code so it can be
 * used as a merge-conflict demonstration branch.
 */
public class MyWorld extends World {
    private static final int WORLD_WIDTH = 1200;
    private static final int WORLD_HEIGHT = 800;
    private static final int CELL_SIZE = 1;

    private static final String GAME_STATE = "game";
    private static final String TITLE_STATE = "title";

    private final Map<String, Runnable> stateHandlers = new HashMap<>();
    private String currentState = TITLE_STATE;
    private GreenfootImage background;

    private Team redTeam;
    private Team blueTeam;

    public MyWorld() {
        super(WORLD_WIDTH, WORLD_HEIGHT, CELL_SIZE);

        redTeam = new Team(Team.RED, "Red", 150);
        blueTeam = new Team(Team.BLUE, "Blue", 150);

        stateHandlers.put(GAME_STATE, this::setGameState);
        stateHandlers.put(TITLE_STATE, this::setTitleState);

        setUpWorld();
    }

    //IF YOU PRESS YOUR SPACE KEY THEN THE SIMULATION WILL START, OTHERWISE IT WILL BE ON THE TITLE SCREEN
    public void act() {
        if (Greenfoot.isKeyDown("space")) {
            changeState(GAME_STATE);
        }
    }

    private void setUpWorld() {
        removeObjects(getObjects(null));
        //background = new GreenfootImage(currentState + ".png");
        background = new GreenfootImage("Background.png");
        setBackground(background);

        Runnable stateHandler = stateHandlers.get(currentState);
        if (stateHandler == null) {
            currentState = TITLE_STATE;
            stateHandler = stateHandlers.get(currentState);
        }
        stateHandler.run();
    }

    private void setGameState() {
        removeObjects(getObjects(null));

        Base redBase = new Base(redTeam);
        addObject(redBase, 200, 490);
        Base blueBase = new Base(blueTeam);
        addObject(blueBase, 990, 165);
        //adds ui to the world
        addObject(new UI(), 600, 400);
        
        // resources near red base (left side)
        //top left resource
        addObject(new Resources(Team.RED), 50, 400);
        //mid left resource
        addObject(new Resources(Team.RED), 30, 475);
        //bottom left resource
        addObject(new Resources(Team.RED), 50, 550);

        // resources near blue base (right side)
        //top right resource
        addObject(new Resources(Team.BLUE), 1140, 70);
        //middle right resource
        addObject(new Resources(Team.BLUE), 1160, 145);
        //bottom right resource
        addObject(new Resources(Team.BLUE), 1140, 220);
        //adds Resource counters to the world
        addObject(new ResourceCounter(redTeam), 110, 670);
        addObject(new ResourceCounter(blueTeam), 1020, 670);
        
        //Adds workers to the left
        addObject(new Worker(redTeam, redBase), 170, 475);
        addObject(new Worker(redTeam, redBase), 170, 475);
        addObject(new Worker(redTeam, redBase), 170, 475); 
        //Adds workers to the right
        addObject(new Worker(blueTeam, blueBase), 1020, 145);
        addObject(new Worker(blueTeam, blueBase), 1020, 145);
        addObject(new Worker(blueTeam, blueBase), 1020, 145);
        
        //testing soldiers
        addObject(new Soldier(blueTeam), 600, 200);
        addObject(new Soldier(redTeam), 300, 200);
         
    }

    private void setTitleState() {
        /**GreenfootImage titleText = new GreenfootImage("Merge Conflict Demo", 54, Color.WHITE, new Color(0, 0, 0, 0));
         background.drawImage(titleText, 325, 315);

         GreenfootImage subtitleText = new GreenfootImage("Edit this world on two branches, then merge.", 28, Color.LIGHT_GRAY, new Color(0, 0, 0, 0));
         background.drawImage(subtitleText, 345, 385);
         **/
    }

    public void changeState(String nextState) {
        currentState = nextState;
        setUpWorld();
    }

    public String getWorld() {
        return currentState;
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
