import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class UI extends Actor {
    public static final int PLAY_AREA_BOTTOM_Y = 640;

    private static final int RED_TEXT_X = 20;
    private static final int BLUE_TEXT_X = 940;
    private static final int TEXT_Y = 680;
    private static final int FONT_SIZE = 20;

    private Team redTeam;
    private Team blueTeam;
    private GreenfootImage baseImage;
    private String fontName;

    public UI() {
        baseImage = new GreenfootImage("Ui.png");
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

    private void syncTeams(World world) {
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
        GreenfootImage image = new GreenfootImage(baseImage);
        image.setColor(Color.WHITE);
        greenfoot.Font myFont = new greenfoot.Font(fontName, FONT_SIZE);
        image.setFont(myFont);

        // Resources display
        image.drawString("Resources: " + String.valueOf(getResourcesFor(redTeam)), RED_TEXT_X, TEXT_Y);
        image.drawString("Resources: " + String.valueOf(getResourcesFor(blueTeam)), BLUE_TEXT_X, TEXT_Y);

        // Possible UI additions:
        // image.drawString(String.valueOf(redPopulation), ...);

        setImage(image);
    }

    private int getResourcesFor(Team team) {
        return team == null ? 0 : team.getResources();
    }
}
