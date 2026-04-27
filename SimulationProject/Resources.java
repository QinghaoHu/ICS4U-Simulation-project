import greenfoot.*;

public class Resources extends SuperSmoothMover {
    protected int maxAmount;
    protected int currentAmount;
    protected GreenfootImage img;
    private int teamSide; // either team red or team blue
    public Resources(int teamSide) {
        this.teamSide = teamSide;
        maxAmount = 100;
        currentAmount = maxAmount;
        setupImage();
        setImage(img);
    }

    public void setupImage() {
        if ((int) (Math.random() * 2) == 0) {
            img = ResourceCache.getImage("Resources1.png");
        } else {
            img = ResourceCache.getImage("Resources2.png");
        }
        img.scale(80, 80);
    }

    public int getTeamSide(){
        return teamSide;
    }
    
}
