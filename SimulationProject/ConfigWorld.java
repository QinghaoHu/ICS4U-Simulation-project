import greenfoot.*;

public class ConfigWorld extends World {
    private static final int WORLD_WIDTH = 1200;
    private static final int WORLD_HEIGHT = 800;
    private static final int CELL_SIZE = 1;

    private static final String[] STRATEGIES = {"ECO", "ATK", "DEF", "REG"};
    private static final int[] RESOURCES = {100, 150, 250, 400, 600};
    private static final int[] EXTRA_WORKERS = {0, 1, 2, 4};

    private static final int RED_X = 280;
    private static final int BLUE_X = 910;
    private static final int TITLE_Y = 95;
    private static final int SUBTITLE_Y = 138;
    private static final int TEAM_Y = 210;
    private static final int STRATEGY_LABEL_Y = 275;
    private static final int STRATEGY_CONTROL_Y = 335;
    private static final int RESOURCES_LABEL_Y = 415;
    private static final int RESOURCES_CONTROL_Y = 475;
    private static final int WORKERS_LABEL_Y = 555;
    private static final int WORKERS_CONTROL_Y = 615;
    private static final int SUPPLY_DROP_Y = 725;
    private static final int START_X = 1020;
    private static final int START_Y = 735;
    private static final int BACK_X = 170;
    private static final int BACK_Y = 735;
    private static final int STEP_MINUS_OFFSET_X = -82;
    private static final int STEP_VALUE_OFFSET_X = 0;
    private static final int STEP_PLUS_OFFSET_X = 82;

    private static final Color TITLE_COLOR = new Color(126, 255, 255);
    private static final Color TEXT_COLOR = new Color(228, 235, 245);
    private static final Color RED_FILL = new Color(120, 36, 36);
    private static final Color BLUE_FILL = new Color(36, 62, 120);
    private static final Color GREEN_FILL = new Color(18, 96, 61);
    private static final Color GRAY_FILL = new Color(58, 66, 78);

    private final String fontName;
    private final GreenfootImage background;

    private int redStrategyIndex;
    private int blueStrategyIndex;
    private int redResourcesIndex = 1;
    private int blueResourcesIndex = 1;
    private int redWorkersIndex;
    private int blueWorkersIndex;
    private boolean supplyDropsEnabled = true;

    private Button redStrategyButton;
    private Button blueStrategyButton;
    private Button redResourcesMinusButton;
    private Button redResourcesPlusButton;
    private Button blueResourcesMinusButton;
    private Button blueResourcesPlusButton;
    private Button redWorkersMinusButton;
    private Button redWorkersPlusButton;
    private Button blueWorkersMinusButton;
    private Button blueWorkersPlusButton;
    private Button supplyDropsButton;
    private Button startButton;
    private Button backButton;

    private ConfigValueLabel redResourcesValue;
    private ConfigValueLabel blueResourcesValue;
    private ConfigValueLabel redWorkersValue;
    private ConfigValueLabel blueWorkersValue;

    public ConfigWorld() {
        super(WORLD_WIDTH, WORLD_HEIGHT, CELL_SIZE);
        fontName = MyWorld.loadCustomFont("fonts/StarJediRounded-jW3R.ttf");
        background = ResourceCache.getImage("Background.png");
        drawStaticBackground();
        setBackground(background);
        setupButtons();
        refreshButtons();
        SoundManager.buttonSoundSetup();
    }

    public void act() {
        handleClicks();
    }

    private void drawStaticBackground() {
        background.setColor(new Color(8, 18, 28, 215));
        background.fillRect(70, 160, 430, 520);
        background.fillRect(700, 160, 430, 520);

        background.setColor(new Color(180, 220, 230));
        background.drawRect(70, 160, 430, 520);
        background.drawRect(700, 160, 430, 520);

        background.setFont(new greenfoot.Font(fontName, 52));
        background.setColor(TITLE_COLOR);
        background.drawString("Battle Setup", 410, TITLE_Y);

        background.setFont(new greenfoot.Font(fontName, 18));
        background.setColor(TEXT_COLOR);
        background.drawString("Pick the starting values, then run the simulation", 345, SUBTITLE_Y);

        drawCenteredTextLabel("Strategy", RED_X, STRATEGY_LABEL_Y);
        drawCenteredIconLabel("Resources", RED_X, RESOURCES_LABEL_Y, "Resources1.png");
        drawCenteredIconLabel("Extra Workers", RED_X, WORKERS_LABEL_Y, "RedWorkerRegular.png");

        drawCenteredTextLabel("Strategy", BLUE_X, STRATEGY_LABEL_Y);
        drawCenteredIconLabel("Resources", BLUE_X, RESOURCES_LABEL_Y, "Resources2.png");
        drawCenteredIconLabel("Extra Workers", BLUE_X, WORKERS_LABEL_Y, "BlueWorkerRegular.png");
        
        background.setColor(TITLE_COLOR);
        background.drawString("Supply Drops", 485, SUPPLY_DROP_Y);

        background.setFont(new greenfoot.Font(fontName, 28));
        background.setColor(new Color(131, 26, 32));
        background.drawString("Red Team", RED_X - 70, TEAM_Y);
        background.setColor(new Color(44, 98, 191));
        background.drawString("Blue Team", BLUE_X - 80, TEAM_Y);
    }

    private void drawLabel(String text, int x, int y, String imageName) {
        GreenfootImage icon = ResourceCache.getImage(imageName);
        icon.scale(34, 34);
        background.drawImage(icon, x, y - 24);
        background.drawString(text, x + 46, y);
    }

    private void drawCenteredIconLabel(String text, int centerX, int y, String imageName) {
        GreenfootImage icon = ResourceCache.getImage(imageName);
        icon.scale(34, 34);
        background.drawImage(icon, centerX - 17, y - 36);
        drawCenteredTextLabel(text, centerX, y + 18);
    }

    private void drawCenteredTextLabel(String text, int centerX, int y) {
        int textX = centerX - (Button.getStringWidth(new greenfoot.Font(fontName, 18), text) / 2);
        background.drawString(text, textX, y);
    }

    private void setupButtons() {
        redStrategyButton = makeTeamButton(RED_FILL, 18, 60);
        blueStrategyButton = makeTeamButton(BLUE_FILL, 18, 60);
        redResourcesMinusButton = makeStepButton("-", RED_FILL);
        redResourcesPlusButton = makeStepButton("+", RED_FILL);
        blueResourcesMinusButton = makeStepButton("-", BLUE_FILL);
        blueResourcesPlusButton = makeStepButton("+", BLUE_FILL);
        redWorkersMinusButton = makeStepButton("-", RED_FILL);
        redWorkersPlusButton = makeStepButton("+", RED_FILL);
        blueWorkersMinusButton = makeStepButton("-", BLUE_FILL);
        blueWorkersPlusButton = makeStepButton("+", BLUE_FILL);
        supplyDropsButton = new Button("", 60, 40, Color.WHITE, GRAY_FILL, Color.WHITE, null, 18);
        startButton = new Button("Start Sim", 170, 40, Color.WHITE, GREEN_FILL, Color.WHITE, null, 18);
        backButton = new Button("Back", 140, 40, Color.WHITE, GRAY_FILL, Color.WHITE, null, 18);

        redResourcesValue = new ConfigValueLabel("0");
        blueResourcesValue = new ConfigValueLabel("0");
        redWorkersValue = new ConfigValueLabel("0");
        blueWorkersValue = new ConfigValueLabel("0");

        addObject(redStrategyButton, RED_X, STRATEGY_CONTROL_Y);
        addObject(blueStrategyButton, BLUE_X, STRATEGY_CONTROL_Y);
        addStepControl(RED_X, RESOURCES_CONTROL_Y, redResourcesMinusButton, redResourcesValue, redResourcesPlusButton);
        addStepControl(BLUE_X, RESOURCES_CONTROL_Y, blueResourcesMinusButton, blueResourcesValue, blueResourcesPlusButton);
        addStepControl(RED_X, WORKERS_CONTROL_Y, redWorkersMinusButton, redWorkersValue, redWorkersPlusButton);
        addStepControl(BLUE_X, WORKERS_CONTROL_Y, blueWorkersMinusButton, blueWorkersValue, blueWorkersPlusButton);
        addObject(supplyDropsButton, 680, SUPPLY_DROP_Y - 10);
        addObject(startButton, START_X, START_Y);
        addObject(backButton, BACK_X, BACK_Y);
    }

    private Button makeTeamButton(Color fill, int fontSize, int width) {
        return new Button("", width, 40, Color.WHITE, fill, Color.WHITE, null, fontSize);
    }

    private Button makeStepButton(String text, Color fill) {
        return new Button(text, 42, 42, Color.WHITE, fill, Color.WHITE, null, 22);
    }

    private void addStepControl(int centerX, int y, Button minusButton, ConfigValueLabel valueLabel, Button plusButton) {
        addObject(minusButton, centerX + STEP_MINUS_OFFSET_X, y);
        addObject(valueLabel, centerX + STEP_VALUE_OFFSET_X, y);
        addObject(plusButton, centerX + STEP_PLUS_OFFSET_X, y);
    }

    private void refreshButtons() {
        redStrategyButton.setText(STRATEGIES[redStrategyIndex]);
        blueStrategyButton.setText(STRATEGIES[blueStrategyIndex]);
        redResourcesValue.setText(String.valueOf(RESOURCES[redResourcesIndex]));
        blueResourcesValue.setText(String.valueOf(RESOURCES[blueResourcesIndex]));
        redWorkersValue.setText(String.valueOf(EXTRA_WORKERS[redWorkersIndex]));
        blueWorkersValue.setText(String.valueOf(EXTRA_WORKERS[blueWorkersIndex]));
        if (supplyDropsEnabled) {
            supplyDropsButton.setText("ON");
            supplyDropsButton.setButtonColor(GREEN_FILL);
        } else {
            supplyDropsButton.setText("OFF");
            supplyDropsButton.setButtonColor(GRAY_FILL);
        }

        redResourcesMinusButton.setDisabled(redResourcesIndex == 0);
        redResourcesPlusButton.setDisabled(redResourcesIndex == RESOURCES.length - 1);
        blueResourcesMinusButton.setDisabled(blueResourcesIndex == 0);
        blueResourcesPlusButton.setDisabled(blueResourcesIndex == RESOURCES.length - 1);
        redWorkersMinusButton.setDisabled(redWorkersIndex == 0);
        redWorkersPlusButton.setDisabled(redWorkersIndex == EXTRA_WORKERS.length - 1);
        blueWorkersMinusButton.setDisabled(blueWorkersIndex == 0);
        blueWorkersPlusButton.setDisabled(blueWorkersIndex == EXTRA_WORKERS.length - 1);
    }

    private void handleClicks() {
        if (Greenfoot.mouseClicked(startButton)) {
            SoundManager.playButtonSound();
            Greenfoot.setWorld(new MyWorld(buildConfig()));
            return;
        }
        if (Greenfoot.mouseClicked(backButton)) {
            SoundManager.playButtonSound();
            Greenfoot.setWorld(new StartingWorld());
            return;
        }

        if (Greenfoot.mouseClicked(redStrategyButton)) {
            SoundManager.playButtonSound();
            redStrategyIndex = nextIndex(redStrategyIndex, STRATEGIES.length);
        } else if (Greenfoot.mouseClicked(blueStrategyButton)) {
            SoundManager.playButtonSound();
            blueStrategyIndex = nextIndex(blueStrategyIndex, STRATEGIES.length);
        } else if (!redResourcesMinusButton.isDisabled() && Greenfoot.mouseClicked(redResourcesMinusButton)) {
            SoundManager.playButtonSound();
            redResourcesIndex = previousIndex(redResourcesIndex);
        } else if (!redResourcesPlusButton.isDisabled() && Greenfoot.mouseClicked(redResourcesPlusButton)) {
            SoundManager.playButtonSound();
            redResourcesIndex = nextIndex(redResourcesIndex, RESOURCES.length);
        } else if (!blueResourcesMinusButton.isDisabled() && Greenfoot.mouseClicked(blueResourcesMinusButton)) {
            SoundManager.playButtonSound();
            blueResourcesIndex = previousIndex(blueResourcesIndex);
        } else if (!blueResourcesPlusButton.isDisabled() && Greenfoot.mouseClicked(blueResourcesPlusButton)) {
            SoundManager.playButtonSound();
            blueResourcesIndex = nextIndex(blueResourcesIndex, RESOURCES.length);
        } else if (!redWorkersMinusButton.isDisabled() && Greenfoot.mouseClicked(redWorkersMinusButton)) {
            SoundManager.playButtonSound();
            redWorkersIndex = previousIndex(redWorkersIndex);
        } else if (!redWorkersPlusButton.isDisabled() && Greenfoot.mouseClicked(redWorkersPlusButton)) {
            SoundManager.playButtonSound();
            redWorkersIndex = nextIndex(redWorkersIndex, EXTRA_WORKERS.length);
        } else if (!blueWorkersMinusButton.isDisabled() && Greenfoot.mouseClicked(blueWorkersMinusButton)) {
            SoundManager.playButtonSound();
            blueWorkersIndex = previousIndex(blueWorkersIndex);
        } else if (!blueWorkersPlusButton.isDisabled() && Greenfoot.mouseClicked(blueWorkersPlusButton)) {
            SoundManager.playButtonSound();
            blueWorkersIndex = nextIndex(blueWorkersIndex, EXTRA_WORKERS.length);
        } else if (Greenfoot.mouseClicked(supplyDropsButton)) {
            SoundManager.playButtonSound();
            supplyDropsEnabled = !supplyDropsEnabled;
        } else {
            return;
        }

        refreshButtons();
    }

    private int nextIndex(int current, int length) {
        return Math.min(current + 1, length - 1);
    }

    private int previousIndex(int current) {
        return Math.max(current - 1, 0);
    }

    private SimulationConfig buildConfig() {
        TeamSetup redSetup = new TeamSetup(
            STRATEGIES[redStrategyIndex],
            RESOURCES[redResourcesIndex],
            EXTRA_WORKERS[redWorkersIndex],
            0,
            0,
            false
        );
        TeamSetup blueSetup = new TeamSetup(
            STRATEGIES[blueStrategyIndex],
            RESOURCES[blueResourcesIndex],
            EXTRA_WORKERS[blueWorkersIndex],
            0,
            0,
            false
        );
        return new SimulationConfig(redSetup, blueSetup, supplyDropsEnabled);
    }
}
