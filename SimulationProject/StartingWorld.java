import greenfoot.*;

public class StartingWorld extends World{
    private static final int WORLD_WIDTH = 1200;
    private static final int WORLD_HEIGHT = 800;
    private static final int CELL_SIZE = 1;

    private static final int BUTTON_Y_POSITION = 680;
    private static final int START_BUTTON_X = 600;
    private static final int TITLE_X = 180;
    private static final int TITLE_Y = 260;
    private static final int TITLE_FONT_SIZE = 90;
    private static final Color TITLE_COLOR = new Color(126, 255, 255);

    private Button startButton;
    private String fontName;
    private GreenfootSound bgm;
    private GreenfootImage background;

    public StartingWorld(){
        super(WORLD_WIDTH, WORLD_HEIGHT, CELL_SIZE);

        fontName = MyWorld.loadCustomFont("fonts/StarJediRounded-jW3R.ttf");
        setupBackground();
        setupButtons();
        SoundManager.buttonSoundSetup();
    }

    public void act(){
        checkClickButton();
    }

    private void setupBackground(){
        background = ResourceCache.getImage("StartBackground.png"); // placeHolder

        greenfoot.Font myFont = new greenfoot.Font(fontName, TITLE_FONT_SIZE);
        background.setFont(myFont);
        background.setColor(TITLE_COLOR);
        background.drawString("Siege Simulation", TITLE_X, TITLE_Y);

        setBackground(background);
    }

    private void setupButtons(){
        startButton = new Button("start", 150);

        addObject(startButton, START_BUTTON_X, BUTTON_Y_POSITION);
    }

    private void checkClickButton(){
        if (Greenfoot.mouseClicked(startButton)) {
            SoundManager.playButtonSound();
            Greenfoot.setWorld(new ConfigWorld());
            if (bgm != null) {
                bgm.stop();
            }
        }
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
