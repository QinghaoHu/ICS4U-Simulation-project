import greenfoot.*;

public class StartingWorld extends World{
    private static final int WORLD_WIDTH = 1200;
    private static final int WORLD_HEIGHT = 800;
    private static final int CELL_SIZE = 1;

    private static final int BUTTON_Y_POSITION = 700;
    private static final int START_BUTTON_X = 425;
    private static final int HELP_BUTTON_X = 590;
    private static final int TELE_BUTTON_X = 512;
    private static final int TITLE_X = 255;
    private static final int TITLE_Y = 150;
    private static final int TITLE_FONT_SIZE = 52;

    private String fontName;
    private GreenfootSound bgm;
    private GreenfootImage background;

    public StartingWorld(){
        super(WORLD_WIDTH, WORLD_HEIGHT, CELL_SIZE);

        fontName = MyWorld.loadCustomFont("fonts/StarJediRounded-jW3R.ttf");
        setupBackground();
        setupButtons();
        bgm = new GreenfootSound("welcomeWorld.mp3");
    }

    private void setupBackground(){
        background = new GreenfootImage("Background.png"); // placeHolder

        greenfoot.Font myFont = new greenfoot.Font(fontName, TITLE_FONT_SIZE);
        background.setFont(myFont);
        background.setColor(new Color(243, 200, 115, 255));
        background.drawString("Siege Simulation", TITLE_X, TITLE_Y);

        setBackground(background);
    }

    private void setupButtons(){

    }

    /**
     * Called when the world starts (e.g., when returning from pause).
     */
    public void started() {
        if (bgm != null) {
            bgm.playLoop();
        }
    }

    /**
     * Called when the world is stopped (e.g., when paused or leaving world).
     */
    public void stopped() {
        if (bgm != null) {
            bgm.pause();
        }
    }
}
