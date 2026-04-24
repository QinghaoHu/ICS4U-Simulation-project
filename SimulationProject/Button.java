import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Button extends Actor
{
    private static final int DEFAULT_HEIGHT = 48;
    private static final int DEFAULT_FONT_SIZE = 24;
    private static final String DEFAULT_FONT_FILE = "fonts/StarJediRounded-jW3R.ttf";
    private static final Color DEFAULT_BORDER_COLOR = new Color(255, 255, 255);
    private static final Color DEFAULT_FILL_COLOR = new Color(21, 76, 121);
    private static final Color DEFAULT_TEXT_COLOR = new Color(255, 255, 255);
    private static final Color DISABLED_FILL_COLOR = new Color(90, 90, 90);
    private static final Color DISABLED_BORDER_COLOR = new Color(160, 160, 160);
    private static final Color DISABLED_TEXT_COLOR = new Color(210, 210, 210);

    private GreenfootImage image;
    private String text;
    private int width;
    private int height;
    private int fontSize;
    private String fontFile;
    private String fontName;
    private Color borderColor;
    private Color fillColor;
    private Color textColor;
    private boolean isDisabled = false;

    public Button(String text) {
        this(text, 180, DEFAULT_BORDER_COLOR, DEFAULT_FILL_COLOR, DEFAULT_TEXT_COLOR, DEFAULT_FONT_FILE, DEFAULT_FONT_SIZE);
    }

    public Button(String text, int width) {
        this(text, width, DEFAULT_BORDER_COLOR, DEFAULT_FILL_COLOR, DEFAULT_TEXT_COLOR, DEFAULT_FONT_FILE, DEFAULT_FONT_SIZE);
    }

    public Button(String text, int width, Color borderColor) {
        this(text, width, DEFAULT_HEIGHT, borderColor, DEFAULT_FILL_COLOR, DEFAULT_TEXT_COLOR, DEFAULT_FONT_FILE, DEFAULT_FONT_SIZE);
    }

    public Button(String text, int width, Color borderColor, Color fillColor) {
        this(text, width, DEFAULT_HEIGHT, borderColor, fillColor, DEFAULT_TEXT_COLOR, DEFAULT_FONT_FILE, DEFAULT_FONT_SIZE);
    }

    public Button(String text, int width, Color borderColor, Color fillColor, Color textColor) {
        this(text, width, DEFAULT_HEIGHT, borderColor, fillColor, textColor, DEFAULT_FONT_FILE, DEFAULT_FONT_SIZE);
    }

    public Button(String text, int width, Color borderColor, int fontSize) {
        this(text, width, DEFAULT_HEIGHT, borderColor, DEFAULT_FILL_COLOR, DEFAULT_TEXT_COLOR, DEFAULT_FONT_FILE, fontSize);
    }

    public Button(String text, int width, Color borderColor, String fontFile) {
        this(text, width, DEFAULT_HEIGHT, borderColor, DEFAULT_FILL_COLOR, DEFAULT_TEXT_COLOR, fontFile, DEFAULT_FONT_SIZE);
    }

    public Button(String text, int width, Color borderColor, String fontFile, int fontSize) {
        this(text, width, DEFAULT_HEIGHT, borderColor, DEFAULT_FILL_COLOR, DEFAULT_TEXT_COLOR, fontFile, fontSize);
    }

    public Button(String text, int width, Color borderColor, Color fillColor, Color textColor, String fontFile, int fontSize) {
        this(text, width, DEFAULT_HEIGHT, borderColor, fillColor, textColor, fontFile, fontSize);
    }

    public Button(String text, int width, int height, Color borderColor, Color fillColor, Color textColor, String fontFile, int fontSize) {
        this.text = text;
        this.width = width;
        this.height = height;
        this.borderColor = borderColor;
        this.fillColor = fillColor;
        this.textColor = textColor;
        this.fontFile = fontFile;
        this.fontSize = fontSize;
        redraw();
    }

    private void redraw() {
        fontName = MyWorld.loadCustomFont(fontFile);

        image = new GreenfootImage(width, height);
        image.setColor(isDisabled ? DISABLED_FILL_COLOR : fillColor);
        image.fill();

        image.setColor(isDisabled ? DISABLED_BORDER_COLOR : borderColor);
        image.drawRect(0, 0, width - 1, height - 1);

        image.setColor(isDisabled ? DISABLED_TEXT_COLOR : textColor);
        greenfoot.Font myFont = new greenfoot.Font(fontName, fontSize);
        image.setFont(myFont);
        drawCenteredText(image, text, getTextBaseline());
        setImage(image);
    }

    private int getTextBaseline() {
        return (height / 2) + (fontSize / 3);
    }

    public void setColor(Color borderColor) {
        setBorderColor(borderColor);
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        redraw();
    }

    public void setButtonColor(Color fillColor) {
        this.fillColor = fillColor;
        redraw();
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        redraw();
    }

    public void setText(String text) {
        this.text = text;
        redraw();
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        redraw();
    }

    public void setFontFile(String fontFile) {
        this.fontFile = fontFile;
        redraw();
    }

    public void setDisabled(boolean disabled) {
        this.isDisabled = disabled;
        redraw();
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public static void drawCenteredText(GreenfootImage canvas, String text, int bottomY){
        canvas.drawString(text, canvas.getWidth() / 2 - (getStringWidth(canvas.getFont(), text) / 2), bottomY);
    }

    public static int getStringWidth(Font font, String text){
        if (text == null || text.length() == 0) {
            return 0;
        }

        int maxWidth = Math.max(1, (int)(text.length() * (font.getSize()/1.20)));
        int fontSize = font.getSize();
        int marginOfError = Math.max(1, fontSize / 6);
        int checkX;

        GreenfootImage temp = new GreenfootImage(maxWidth, fontSize);
        temp.setFont(font);
        temp.drawString(text, 0, fontSize);

        boolean running = true;
        checkX = maxWidth - 1;

        while (running) {
            boolean found = false;
            for (int i = fontSize - 1; i >= 0 && !found; i -= marginOfError) {
                if (temp.getColorAt(checkX, i).getAlpha() != 0) {
                    if (temp.getColorAt(checkX + 1, i).getAlpha() != 0) {
                        checkX++;
                        if (temp.getColorAt(checkX + 1, i).getAlpha() != 0) {
                            checkX++;
                        }
                    }
                    found = true;
                }
            }

            if (found) {
                return checkX;
            }

            checkX -= 3;
            if (checkX <= marginOfError) {
                running = false;
            }
        }

        return 0;
    }
}
