import greenfoot.GreenfootImage;

import java.util.ArrayList;

public class SupplyBot extends People{
    private static int MAX_HEALTH = 35;
    private static int SPEED = 3;

    private Supply targetSupply;

    private GreenfootImage img;

    public SupplyBot(Team team) {
        super(team, MAX_HEALTH, SPEED);

        if (team != null) {
            team.addUnit(this);
        }

        targetSupply = null;

        setupImage();
    }

    public void act (){
        collectSupply();
    }

    private void collectSupply(){
        if (targetSupply == null){
            targetSupply = findTargetSupply();
        }

        moveTowardsSupply();
    }

    private Supply findTargetSupply(){
        ArrayList<Supply> supplies = (ArrayList<Supply>)getWorld().getObjects(Supply.class);
        Supply closest = null;
        double shortestDist = speed;
        for (Supply s : supplies) {
            double distX = s.getX() - this.getX();
            double distY = s.getY() - this.getY();
            double distance = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
            if (shortestDist < distance) {
                closest = s;
            }
        }
        return closest;
    }

    private void moveTowardsSupply(){
        if (targetSupply == null || targetSupply.getWorld() == null)
        {
            return;
        }
        turnTowards(targetSupply.getX(), targetSupply.getY());
        move(speed);
    }

    private void setupImage(){
        img = new GreenfootImage ("SupplyBot.png");
        setImage(img);
    }
}