import greenfoot.*;

public class ResourceCounter extends Actor {
    private Team team;
    private GreenfootImage img;

    public ResourceCounter(Team team) {
        this.team = team;
        updateImage(0);
    }

    public void act() {
        if (team != null) {
            updateImage(team.getMoney());
        }
    }

    private void updateImage(int amount) {
        img = new GreenfootImage(200, 40);
        img.setColor(Color.WHITE);
        img.setFont(new Font("Arial", true, false, 20));

        String text = team.getTeamId() == Team.RED
            ? "Resources: " + amount
            : "Resources: " + amount;

        img.drawString(text, 10, 25);
        setImage(img);
    }
}