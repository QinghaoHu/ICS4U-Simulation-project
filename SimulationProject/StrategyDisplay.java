import greenfoot.*;

public class StrategyDisplay extends Actor {
    private Team team;
    private GreenfootImage img;

    public StrategyDisplay(Team team) {
        this.team = team;
        updateImage();
    }

    public void act() {
        if (team != null) {
            updateImage();
        }
    }

    private void updateImage() {
        img = new GreenfootImage(200, 40);
        img.setColor(Color.WHITE);
        img.setFont(new Font("Arial", true, false, 20));

        String text = "Strategy: " + team.getStrategy();

        img.drawString(text, 10, 25);
        setImage(img);
    }
}