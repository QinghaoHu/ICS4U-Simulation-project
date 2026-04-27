import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.File;
import java.util.List;

public class UI extends Actor {
    public static final int PLAY_AREA_BOTTOM_Y = 640;

    private static final int RED_TEXT_X = 25;
    private static final int BLUE_TEXT_X = 940;
    private static final int RESOURCE_TEXT_Y = 680;
    private static final int STRATEGY_TEXT_Y = 770;
    private static final int FONT_SIZE = 20;
    private static final int UPGRADE_ICON_SIZE = 55;
    private static final int RED_UPGRADE_X = 430;
    private static final int BLUE_UPGRADE_X = 640;
    private static final int UPGRADE_ICON_Y = 720;
    private static final int UPGRADE_TEXT_Y = 755;
    private static final int UPGRADE_TEXT_OFFSET_X = 100;
    private static final int UPGRADE_DISPLAY_TIME = 180;
    private static UI instance;

    private Team redTeam;
    private Team blueTeam;
    private GreenfootImage baseImage;
    private String fontName;
    private GreenfootImage redUpgradeIcon;
    private GreenfootImage blueUpgradeIcon;
    private int redUpgradeAmount;
    private int blueUpgradeAmount;
    private int redUpgradeTimer;
    private int blueUpgradeTimer;

    public UI() {
        baseImage = new GreenfootImage("Ui.png");
        setImage(baseImage);
        fontName = MyWorld.loadCustomFont("fonts/SupremeSpike-KVO8D.ttf");
    }

    protected void addedToWorld(World world) {
        instance = this;
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
        tickUpgradeTimers();
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

        // upgrade display
        drawUpgradeDisplay(image, redUpgradeIcon, redUpgradeAmount, redUpgradeTimer, RED_UPGRADE_X);
        drawUpgradeDisplay(image, blueUpgradeIcon, blueUpgradeAmount, blueUpgradeTimer, BLUE_UPGRADE_X);

        setImage(image);
    }

    private int getLevelFor(Team team) {
        return team == null || team.getBase() == null ? 0 : team.getBase().getLevel();
    }
    
    public void updateUpgrade(){
        updateImage();
    }

    public static void reportUpgrade(Team team, String typeName, int amount) {
        if (instance == null || team == null || typeName == null || amount <= 0) {
            return;
        }
        instance.recordUpgrade(team, typeName, amount);
    }

    private void recordUpgrade(Team team, String typeName, int amount) {
        GreenfootImage icon = loadUpgradeImage(team, typeName);
        if (team.getTeamId() == Team.RED) {
            redUpgradeIcon = icon;
            redUpgradeAmount = amount;
            redUpgradeTimer = UPGRADE_DISPLAY_TIME;
        } else if (team.getTeamId() == Team.BLUE) {
            blueUpgradeIcon = icon;
            blueUpgradeAmount = amount;
            blueUpgradeTimer = UPGRADE_DISPLAY_TIME;
        }
    }

    private void tickUpgradeTimers() {
        if (redUpgradeTimer > 0) {
            redUpgradeTimer--;
            if (redUpgradeTimer == 0) {
                redUpgradeIcon = null;
                redUpgradeAmount = 0;
            }
        }
        if (blueUpgradeTimer > 0) {
            blueUpgradeTimer--;
            if (blueUpgradeTimer == 0) {
                blueUpgradeIcon = null;
                blueUpgradeAmount = 0;
            }
        }
    }

    private GreenfootImage loadUpgradeImage(Team team, String typeName) {
        String imageName = getUpgradeImageName(team, typeName);
        GreenfootImage icon = new GreenfootImage(imageName);
        icon.scale(UPGRADE_ICON_SIZE, UPGRADE_ICON_SIZE);
        return icon;
    }

    private String getUpgradeImageName(Team team, String typeName) {
        String teamName = team == null ? "" : team.getName();

        if ("Marine".equals(typeName)) {
            return findExistingImage(teamName + "Marine.png", "placeholder.png");
        }
        if ("Worker".equals(typeName)) {
            return findExistingImage(teamName + "WorkerRegular.png", "placeholder.png");
        }
        if ("Officer".equals(typeName)) {
            return findExistingImage(teamName + "Officer.png", "placeholder.png");
        }
        if ("Barrack".equals(typeName)) {
            return findExistingImage(teamName + "Barrack.png", "placeholder.png");
        }
        if ("Turret".equals(typeName)) {
            return findExistingImage(teamName + "Turret.png", "placeholder.png");
        }
        if ("SupplyBot".equals(typeName)) {
            return findExistingImage("SupplyBot.png", "placeholder.png");
        }

        return "placeholder.png";
    }

    private String findExistingImage(String preferred, String fallback) {
        File preferredFile = new File("images/" + preferred);
        if (preferredFile.exists()) {
            return preferred;
        }
        return fallback;
    }

    private void drawUpgradeDisplay(GreenfootImage image, GreenfootImage icon, int amount, int timer, int x) {
        if (icon == null || amount <= 0 || timer <= 0) {
            return;
        }

        int transparency = (int)(255.0 * timer / UPGRADE_DISPLAY_TIME);

        GreenfootImage fadedIcon = new GreenfootImage(icon);
        fadedIcon.setTransparency(transparency);
        image.drawImage(fadedIcon, x, UPGRADE_ICON_Y);
        image.setColor(new Color(255, 255, 255, transparency));
        image.setFont(new greenfoot.Font(fontName, FONT_SIZE));
        image.drawString("+" + amount, x + UPGRADE_TEXT_OFFSET_X, UPGRADE_TEXT_Y);
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