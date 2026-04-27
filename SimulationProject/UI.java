import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

public class UI extends Actor {
    public static final int PLAY_AREA_BOTTOM_Y = 640;

    private static final int RED_TEXT_X = 25;
    private static final int BLUE_TEXT_X = 1015;
    private static final int RESOURCE_TEXT_Y = 675;
    private static final int WORKER_TEXT_Y = 775;
    private static final int BASE_HP_TEXT_Y = 725;
    private static final int STRATEGY_TEXT_Y = 775;
    private static final int FONT_SIZE = 18;
    private static final int LEVEL_TEXT_Y = 750;
    private static final int RED_SIDE_TEXT_X = 220;
    private static final int BLUE_SIDE_TEXT_X = 830;
    private static final int MARINE_TEXT_Y = 725;

    private Team redTeam;
    private Team blueTeam;
    private GreenfootImage baseImage;
    private String fontName;
    // the ui that is on the game screen at the bottom that tracks the stats of both teams
    public UI() {
        baseImage = ResourceCache.getImage("Ui.png");
        setImage(baseImage);
        fontName = MyWorld.loadCustomFont("fonts/StarJediRounded-jW3R.ttf");
    }

    protected void addedToWorld(World world) {
        syncTeams(world);

        updateImage();
    }

    public void act() {
        if (getWorld() == null) {
            return;
        }

        if (redTeam == null || blueTeam == null) {
            syncTeams(getWorld());
        }

        updateImage();
    }

    private void syncTeams(World world) { // make sure the stats are correct
        List<Team> teams = world.getObjects(Team.class);
        for (Team team : teams) {
            if (team.getTeamId() == Team.RED) {
                redTeam = team;
            } else if (team.getTeamId() == Team.BLUE) {
                blueTeam = team;
            }
        }
    }

    private void updateImage() {
        // updates the displayed stats on screen
        GreenfootImage image = new GreenfootImage(baseImage);
        image.setColor(Color.WHITE);
        greenfoot.Font myFont = new greenfoot.Font(fontName, FONT_SIZE);
        image.setFont(myFont);

        // Resources display
        image.drawString("Resources: " + String.valueOf(getResourcesFor(redTeam)), RED_TEXT_X, RESOURCE_TEXT_Y);
        image.drawString("Resources: " + String.valueOf(getResourcesFor(blueTeam)), BLUE_TEXT_X, RESOURCE_TEXT_Y);

        // Worker display
        image.drawString("Workers: " + String.valueOf(getWorkersFor(redTeam)), RED_SIDE_TEXT_X, WORKER_TEXT_Y);
        image.drawString("Workers: " + String.valueOf(getWorkersFor(blueTeam)), BLUE_SIDE_TEXT_X, WORKER_TEXT_Y);

        // Base HP display
        image.drawString(getBaseHealthFor(redTeam), RED_TEXT_X, BASE_HP_TEXT_Y);
        image.drawString(getBaseHealthFor(blueTeam), BLUE_TEXT_X, BASE_HP_TEXT_Y);
        
        //level display
        image.drawString("Level: " + getLevelFor(redTeam), RED_TEXT_X, LEVEL_TEXT_Y);
        image.drawString("Level: " + getLevelFor(blueTeam), BLUE_TEXT_X, LEVEL_TEXT_Y);
        
        // Draws the marine damage
        image.drawString(getStrongestMarineStats(redTeam), RED_SIDE_TEXT_X, MARINE_TEXT_Y);
        image.drawString(getStrongestMarineStats(blueTeam), BLUE_SIDE_TEXT_X , MARINE_TEXT_Y);
        
        // Strategy display
        greenfoot.Font font = new greenfoot.Font(fontName, FONT_SIZE - 3);
        image.setFont(font);
        image.drawString("Strategy: " + getStrategyFor(redTeam), RED_TEXT_X, STRATEGY_TEXT_Y);
        image.drawString("Strategy: " + getStrategyFor(blueTeam), BLUE_TEXT_X, STRATEGY_TEXT_Y);

        setImage(image);
    }

    private int getLevelFor(Team team) {
        return team == null || team.getBase() == null ? 0 : team.getBase().getLevel();
    }
    
    private int getResourcesFor(Team team) {
        return team == null ? 0 : team.getResources();
    }

    private int getWorkersFor(Team team) {
        return team == null ? 0 : team.getWorkerCount();
    }

    private String getBaseHealthFor(Team team) {
        if (team == null || team.getBase() == null) {
            return "0/0";
        }

        Base base = team.getBase();
        return base.getHealth() + "/" + base.getMaxHealth();
    }
    
    
    private String getStrongestMarineStats(Team team) { 
        if (team == null) return "0 HP | 0 DMG";
    
        Marine strongest = null;
    
        for (People p : team.getUnits()) {
            if (p instanceof Marine) {
                Marine m = (Marine) p;
    
                if (strongest == null) {
                    strongest = m;
                    continue;
                }
    
                // Shows hp+ damage
                int mScore = m.getHealth() + m.getTotalDamage() * 10;
                int sScore = strongest.getHealth() + strongest.getTotalDamage() * 10;
    
                if (mScore > sScore) {
                    strongest = m;
                }
            }
        }
    
        if (strongest == null) return "20 HP | 3 DMG";
    
        return strongest.getMaxHealth()
                + " HP | " + strongest.getTotalDamage() + " DMG";
    }
    
    private String getStrategyFor(Team team) {
        return team == null ? "" : team.getStrategy();
    }
}
