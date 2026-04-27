import greenfoot.*;

public class ConfigValueLabel extends Actor {
    private static final int WIDTH = 84;
    private static final int HEIGHT = 42;
    private static final Color BACKGROUND_COLOR = new Color(16, 24, 32, 225);
    private static final Color BORDER_COLOR = new Color(200, 215, 225);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);

    private final String fontName;
    private String text;

    public ConfigValueLabel(String text) {
        this.fontName = MyWorld.loadCustomFont("fonts/StarJediRounded-jW3R.ttf");
        this.text = text;
        redraw();
    }

    public void setText(String text) {
        this.text = text;
        redraw();
    }

    private void redraw() {
        GreenfootImage image = new GreenfootImage(WIDTH, HEIGHT);
        image.setColor(BACKGROUND_COLOR);
        image.fillRect(0, 0, WIDTH, HEIGHT);
        image.setColor(BORDER_COLOR);
        image.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
        image.setColor(TEXT_COLOR);
        image.setFont(new greenfoot.Font(fontName, 20));
        Button.drawCenteredText(image, text, 27);
        setImage(image);
    }
}
