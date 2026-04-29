import greenfoot.*;

public class LoadingWorld extends World {
    private int dots = 0;
    private int frame = 0;

    public LoadingWorld() {
        super(1200, 800, 1);

        setBackground(new GreenfootImage("Background.png"));

        showText("Loading... 0%", 600, 380);
        showText("Please wait", 600, 420);

        ResourceCache.startLoadingAsync();
    }

    public void act() {
        frame++;

        if (frame % 20 == 0) {
            dots = (dots + 1) % 4;
        }

        String dotText = "";
        for (int i = 0; i < dots; i++) {
            dotText += ".";
        }

        int percent = ResourceCache.getLoadingPercent();

        showText("Loading" + dotText + " " + percent + "%", 600, 380);

        if (ResourceCache.isAllResourcesLoaded()) {
            Greenfoot.setWorld(new StartingWorld());
        }
    }
}