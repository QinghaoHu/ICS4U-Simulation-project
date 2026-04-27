import greenfoot.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

/**
 * Main simulation world.
 * <p>
 * This is the main world where all the game play occurs
 * What you are about to witness is a 2d top-down AI battle Simulator
 * The main game play of the game is having two teams: Red and Blue
 * The objective of these two teams is to destroy the others bases
 * The two teams will have an in-game currency called resources that they will use to buy people and buildings in order to achieve said objective
 * In the people class we have: Workers, Soldiers, and Supply Bots
 *     Workers: gather resources and build
 *     Soldiers: fire bullets and are the main way of destroying the other team's base
 *     Supply Bots:
 * In the Building class we have: Turrets, Bases, Barracks
 *     Turret: Defensive buildings with the objective of killing soldier that attempt to attack the base
 *     Base: Spawn in workers and must be defended in order to win
 *     Barrack: Spawn soldiers to destroy the other team's base
 * In the middle of this chaos, to account for a team not overwhelming the other team once an advantage is reached, supplies will drop from the sky to give certain buffs
 * 
 * 
 * 
 */
public class MyWorld extends World {
    private static final int WORLD_WIDTH = 1200;
    private static final int WORLD_HEIGHT = 800;
    private static final int CELL_SIZE = 1;

    private static final int MIN_SUPPLY_DROP_RANGE = 500;
    private static final int MAX_SUPPLY_DROP_RANGE = 700;
    private static final int SUPPLY_SPWAN_Y_OFFSET = -60;
    private static final int[][] RED_BARRACK_POSITIONS = {{120, 210}, {270, 165}};
    private static final int[][] BLUE_BARRACK_POSITIONS = {{1070, 600}, {930, 645}};
    private static final int[][] RED_TURRET_POSITIONS = {{350, 460}, {480, 530}};
    private static final int[][] BLUE_TURRET_POSITIONS = {{840, 250}, {720, 135}};
    private int supplySpawnTimer;

    private static final String GAME_STATE = "game";
    private static final String TITLE_STATE = "title";

    private final Map<String, Runnable> stateHandlers = new HashMap<>();
    private String currentState = GAME_STATE;
    private GreenfootImage background;

    private Team redTeam;
    private Team blueTeam;
    private final SimulationConfig config;
    private Counter fpsCounter = new Counter("FPS: ");
    private int frames = 0;
    private long lastTime = System.currentTimeMillis();

    public MyWorld() {
        this(SimulationConfig.defaultConfig());
        prepare();
    }

    public MyWorld(SimulationConfig config) {
        super(WORLD_WIDTH, WORLD_HEIGHT, CELL_SIZE);

        this.config = config == null ? SimulationConfig.defaultConfig() : config;
        TeamSetup redSetup = this.config.getRedSetup();
        TeamSetup blueSetup = this.config.getBlueSetup();
        redTeam = new Team(Team.RED, "Red", redSetup.getStartingResources(), redSetup.getStrategy(), this);
        blueTeam = new Team(Team.BLUE, "Blue", blueSetup.getStartingResources(), blueSetup.getStrategy(), this);

        stateHandlers.put(GAME_STATE, this::setGameState);
        stateHandlers.put(TITLE_STATE, this::setTitleState);
        
        supplySpawnTimer = 0;

        setUpWorld();
        setPaintOrder(EndScreen.class, Counter.class, UI.class);
        prepare();
    }

    //IF YOU PRESS YOUR SPACE KEY THEN THE SIMULATION WILL START, OTHERWISE IT WILL BE ON THE TITLE SCREEN
    public void act() {
        if (Greenfoot.isKeyDown("space") && !GAME_STATE.equals(currentState)) {
            changeState(GAME_STATE);
        }
        if (config.isSupplyDropsEnabled()) {
            spawnSupply();
        }
        countFPS();
    }

    private void countFPS() {
        frames++;
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime >= 1000) {
            fpsCounter.setValue(frames);
            frames = 0;
            lastTime = currentTime;
        }
    }
    
    private void stopMusic(){
        
    }

    private void setUpWorld() {
        ResourceCache.loadAllResources();
        removeObjects(getObjects(null));
        //background = new GreenfootImage(currentState + ".png");
        background = ResourceCache.getImage("Background.png");
        setBackground(background);

        Runnable stateHandler = stateHandlers.get(currentState);
        if (stateHandler == null) {
            currentState = TITLE_STATE;
            stateHandler = stateHandlers.get(currentState);
        }
        stateHandler.run();
        addObject(fpsCounter, WORLD_WIDTH / 2, 20);
    }

    private void setGameState() {
        removeObjects(getObjects(null));
        
        addObject(new GameTimer(), 0, 15);
        
        Base redBase = new Base(redTeam);
        addObject(redBase, 200, 490);
        redTeam.setBase(redBase);

        Base blueBase = new Base(blueTeam);
        addObject(blueBase, 990, 165);
        blueTeam.setBase(blueBase);
        
        addObject(redTeam, 0, 0);
        addObject(blueTeam, 0, 0);
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

        applyTeamSetup(redTeam, redBase, config.getRedSetup(), RED_BARRACK_POSITIONS, RED_TURRET_POSITIONS);
        applyTeamSetup(blueTeam, blueBase, config.getBlueSetup(), BLUE_BARRACK_POSITIONS, BLUE_TURRET_POSITIONS);

        redTeam.setUpWorld();
        blueTeam.setUpWorld();
        
        //addObject(new SupplyBot(redTeam), 0 , 0);
        
    
    

 
        
        //testing soldiers
        //addObject(new Marine(blueTeam), 600, 200);
        //addObject(new Officer(redTeam), 500, 300);

        //testing defensive turret
        //addObject(new DefensiveTurret(blueTeam), 600, 200);
        //addObject(new Barrack(redTeam), 200, 200);
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

    private void spawnSupply(){
        if (supplySpawnTimer <= 0 && getObjects(Supply.class).size() < 5) {
            // spawn supply
            addObject(new Supply(), Greenfoot.getRandomNumber(MAX_SUPPLY_DROP_RANGE - MIN_SUPPLY_DROP_RANGE + 1) + MIN_SUPPLY_DROP_RANGE, SUPPLY_SPWAN_Y_OFFSET);

            // Reset timer to random value between 300 (5s) and 900 (15s)
            supplySpawnTimer = 300 + Greenfoot.getRandomNumber(601);
        } else {
            supplySpawnTimer--;
        }
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

    public static double getDistance(Actor a, Actor b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double getDistance(Actor a, int targetX, int targetY) {
        double dx = a.getX() - targetX;
        double dy = a.getY() - targetY;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
    }

    private void applyTeamSetup(Team team, Base base, TeamSetup setup, int[][] barrackPositions, int[][] turretPositions) {
        if (team == null || base == null || setup == null) {
            return;
        }

        spawnStartingWorkers(team, base, setup.getExtraWorkers());
        spawnStartingBuildings(team, setup.getExtraBarracks(), barrackPositions, true);
        spawnStartingBuildings(team, setup.getExtraTurrets(), turretPositions, false);
        spawnStartingSupplyBot(team, base, setup.isSupplyBotsEnabled());
    }

    private void spawnStartingWorkers(Team team, Base base, int count) {
        for (int i = 0; i < count; i++) {
            Worker worker = new Worker(team, base);
            addObject(worker, base.getX() + (i * 14), base.getY() + (i * 10));
        }
    }

    private void spawnStartingBuildings(Team team, int count, int[][] positions, boolean barracks) {
        int limit = Math.min(count, positions.length);
        for (int i = 0; i < limit; i++) {
            Buildings building = barracks ? new Barrack(team) : new Turret(team);
            addObject(building, positions[i][0], positions[i][1]);
            team.correctBuildingList(building);
        }
    }

    private void spawnStartingSupplyBot(Team team, Base base, boolean enabled) {
        if (!enabled) {
            return;
        }

        SupplyBot bot = new SupplyBot(team);
        team.addSupplyBot(bot);
        addObject(bot, base.getX() + 40, base.getY() + 25);
    }

    public static String loadCustomFont(String file) {
        try {
            File fontFile = new File(file);
            java.awt.Font customFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, fontFile);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            return customFont.getFontName();
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return "Arial";
        }
    }
}
