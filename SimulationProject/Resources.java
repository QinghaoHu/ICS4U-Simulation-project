import greenfoot.*;

public class Resources extends SmoothMover {
    protected int maxAmount;
    protected int currentAmount;
    protected GreenfootImage img;

    public Resources() {
        maxAmount = 100;
        currentAmount = maxAmount;
        setupImage();
        setImage(img);
    }

    public void setupImage() {
        if ((int) (Math.random() * 2) == 0) {
            img = new GreenfootImage("Resources1.png");
        } else {
            img = new GreenfootImage("Resources2.png");
        }
        img.scale(60, 60);
    }

}
